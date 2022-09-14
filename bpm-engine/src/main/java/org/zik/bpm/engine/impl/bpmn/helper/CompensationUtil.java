// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.helper;

import org.zik.bpm.engine.impl.context.Context;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.tree.ReferenceWalker;
import org.zik.bpm.engine.impl.tree.FlowScopeWalker;
import java.util.Collection;
import java.util.Set;
import org.zik.bpm.engine.impl.tree.TreeVisitor;
import java.util.HashSet;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.event.EventType;
import java.util.Map;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import java.util.Iterator;
import java.util.Collections;
import java.util.Comparator;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import java.util.List;

public class CompensationUtil
{
    public static final String SIGNAL_COMPENSATION_DONE = "compensationDone";
    
    public static void throwCompensationEvent(final List<EventSubscriptionEntity> eventSubscriptions, final ActivityExecution execution, final boolean async) {
        for (final EventSubscriptionEntity eventSubscription : eventSubscriptions) {
            ExecutionEntity compensatingExecution = getCompensatingExecution(eventSubscription);
            if (compensatingExecution != null) {
                if (compensatingExecution.getParent() != execution) {
                    compensatingExecution.setParent((PvmExecutionImpl)execution);
                }
                compensatingExecution.setEventScope(false);
            }
            else {
                compensatingExecution = (ExecutionEntity)execution.createExecution();
                eventSubscription.setConfiguration(compensatingExecution.getId());
            }
            compensatingExecution.setConcurrent(true);
        }
        Collections.sort(eventSubscriptions, new Comparator<EventSubscriptionEntity>() {
            @Override
            public int compare(final EventSubscriptionEntity o1, final EventSubscriptionEntity o2) {
                return o2.getCreated().compareTo(o1.getCreated());
            }
        });
        for (final EventSubscriptionEntity compensateEventSubscriptionEntity : eventSubscriptions) {
            compensateEventSubscriptionEntity.eventReceived(null, async);
        }
    }
    
    public static void createEventScopeExecution(final ExecutionEntity execution) {
        final ActivityImpl activity = execution.getActivity();
        final ExecutionEntity scopeExecution = (ExecutionEntity)execution.findExecutionForFlowScope(activity.getFlowScope());
        final List<EventSubscriptionEntity> eventSubscriptions = execution.getCompensateEventSubscriptions();
        if (eventSubscriptions.size() > 0 || hasCompensationEventSubprocess(activity)) {
            final ExecutionEntity eventScopeExecution = scopeExecution.createExecution();
            eventScopeExecution.setActivity(execution.getActivity());
            eventScopeExecution.activityInstanceStarting();
            eventScopeExecution.enterActivityInstance();
            eventScopeExecution.setActive(false);
            eventScopeExecution.setConcurrent(false);
            eventScopeExecution.setEventScope(true);
            final Map<String, Object> variables = (Map<String, Object>)execution.getVariablesLocal();
            for (final Map.Entry<String, Object> variable : variables.entrySet()) {
                eventScopeExecution.setVariableLocal(variable.getKey(), variable.getValue());
            }
            for (final EventSubscriptionEntity eventSubscriptionEntity : eventSubscriptions) {
                final EventSubscriptionEntity newSubscription = EventSubscriptionEntity.createAndInsert(eventScopeExecution, EventType.COMPENSATE, eventSubscriptionEntity.getActivity());
                newSubscription.setConfiguration(eventSubscriptionEntity.getConfiguration());
                newSubscription.setCreated(eventSubscriptionEntity.getCreated());
            }
            for (final PvmExecutionImpl childEventScopeExecution : execution.getEventScopeExecutions()) {
                childEventScopeExecution.setParent(eventScopeExecution);
            }
            final ActivityImpl compensationHandler = getEventScopeCompensationHandler(execution);
            final EventSubscriptionEntity eventSubscription = EventSubscriptionEntity.createAndInsert(scopeExecution, EventType.COMPENSATE, compensationHandler);
            eventSubscription.setConfiguration(eventScopeExecution.getId());
        }
    }
    
    protected static boolean hasCompensationEventSubprocess(final ActivityImpl activity) {
        final ActivityImpl compensationHandler = activity.findCompensationHandler();
        return compensationHandler != null && compensationHandler.isSubProcessScope() && compensationHandler.isTriggeredByEvent();
    }
    
    protected static ActivityImpl getEventScopeCompensationHandler(final ExecutionEntity execution) {
        final ActivityImpl activity = execution.getActivity();
        final ActivityImpl compensationHandler = activity.findCompensationHandler();
        if (compensationHandler != null && compensationHandler.isSubProcessScope()) {
            return compensationHandler;
        }
        return activity;
    }
    
    public static List<EventSubscriptionEntity> collectCompensateEventSubscriptionsForScope(final ActivityExecution execution) {
        final Map<ScopeImpl, PvmExecutionImpl> scopeExecutionMapping = execution.createActivityExecutionMapping();
        final ScopeImpl activity = (ScopeImpl)execution.getActivity();
        final Set<EventSubscriptionEntity> subscriptions = new HashSet<EventSubscriptionEntity>();
        final TreeVisitor<ScopeImpl> eventSubscriptionCollector = new TreeVisitor<ScopeImpl>() {
            @Override
            public void visit(final ScopeImpl obj) {
                final PvmExecutionImpl execution = scopeExecutionMapping.get(obj);
                subscriptions.addAll(((ExecutionEntity)execution).getCompensateEventSubscriptions());
            }
        };
        new FlowScopeWalker(activity).addPostVisitor(eventSubscriptionCollector).walkUntil(new ReferenceWalker.WalkCondition<ScopeImpl>() {
            @Override
            public boolean isFulfilled(final ScopeImpl element) {
                final Boolean consumesCompensationProperty = (Boolean)element.getProperty("consumesCompensation");
                return consumesCompensationProperty == null || consumesCompensationProperty == Boolean.TRUE;
            }
        });
        return new ArrayList<EventSubscriptionEntity>(subscriptions);
    }
    
    public static List<EventSubscriptionEntity> collectCompensateEventSubscriptionsForActivity(final ActivityExecution execution, final String activityRef) {
        final List<EventSubscriptionEntity> eventSubscriptions = collectCompensateEventSubscriptionsForScope(execution);
        final String subscriptionActivityId = getSubscriptionActivityId(execution, activityRef);
        final List<EventSubscriptionEntity> eventSubscriptionsForActivity = new ArrayList<EventSubscriptionEntity>();
        for (final EventSubscriptionEntity subscription : eventSubscriptions) {
            if (subscriptionActivityId.equals(subscription.getActivityId())) {
                eventSubscriptionsForActivity.add(subscription);
            }
        }
        return eventSubscriptionsForActivity;
    }
    
    public static ExecutionEntity getCompensatingExecution(final EventSubscriptionEntity eventSubscription) {
        final String configuration = eventSubscription.getConfiguration();
        if (configuration != null) {
            return Context.getCommandContext().getExecutionManager().findExecutionById(configuration);
        }
        return null;
    }
    
    private static String getSubscriptionActivityId(final ActivityExecution execution, final String activityRef) {
        final ActivityImpl activityToCompensate = ((ExecutionEntity)execution).getProcessDefinition().findActivity(activityRef);
        if (activityToCompensate.isMultiInstance()) {
            final ActivityImpl flowScope = (ActivityImpl)activityToCompensate.getFlowScope();
            return flowScope.getActivityId();
        }
        final ActivityImpl compensationHandler = activityToCompensate.findCompensationHandler();
        if (compensationHandler != null) {
            return compensationHandler.getActivityId();
        }
        return activityRef;
    }
}
