// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.runtime;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.bpmn.helper.BpmnProperties;
import java.util.Map;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.event.EventType;
import org.zik.bpm.engine.impl.bpmn.parser.ConditionalEventDefinition;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.bpmn.parser.EventSubscriptionDeclaration;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionManager;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import java.util.ArrayList;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.cmd.CommandLogger;

public class DefaultConditionHandler implements ConditionHandler
{
    private static final CommandLogger LOG;
    
    @Override
    public List<ConditionHandlerResult> evaluateStartCondition(final CommandContext commandContext, final ConditionSet conditionSet) {
        if (conditionSet.getProcessDefinitionId() == null) {
            return this.evaluateConditionStartByEventSubscription(commandContext, conditionSet);
        }
        return this.evaluateConditionStartByProcessDefinitionId(commandContext, conditionSet, conditionSet.getProcessDefinitionId());
    }
    
    protected List<ConditionHandlerResult> evaluateConditionStartByEventSubscription(final CommandContext commandContext, final ConditionSet conditionSet) {
        final List<EventSubscriptionEntity> subscriptions = this.findConditionalStartEventSubscriptions(commandContext, conditionSet);
        if (subscriptions.isEmpty()) {
            throw DefaultConditionHandler.LOG.exceptionWhenEvaluatingConditionalStartEvent();
        }
        final List<ConditionHandlerResult> results = new ArrayList<ConditionHandlerResult>();
        for (final EventSubscriptionEntity subscription : subscriptions) {
            final ProcessDefinitionEntity processDefinition = subscription.getProcessDefinition();
            if (!processDefinition.isSuspended()) {
                final ActivityImpl activity = subscription.getActivity();
                if (!this.evaluateCondition(conditionSet, activity)) {
                    continue;
                }
                results.add(new ConditionHandlerResult(processDefinition, activity));
            }
        }
        return results;
    }
    
    protected List<EventSubscriptionEntity> findConditionalStartEventSubscriptions(final CommandContext commandContext, final ConditionSet conditionSet) {
        final EventSubscriptionManager eventSubscriptionManager = commandContext.getEventSubscriptionManager();
        if (conditionSet.isTenantIdSet) {
            return eventSubscriptionManager.findConditionalStartEventSubscriptionByTenantId(conditionSet.getTenantId());
        }
        return eventSubscriptionManager.findConditionalStartEventSubscription();
    }
    
    protected List<ConditionHandlerResult> evaluateConditionStartByProcessDefinitionId(final CommandContext commandContext, final ConditionSet conditionSet, final String processDefinitionId) {
        final DeploymentCache deploymentCache = commandContext.getProcessEngineConfiguration().getDeploymentCache();
        final ProcessDefinitionEntity processDefinition = deploymentCache.findDeployedProcessDefinitionById(processDefinitionId);
        final List<ConditionHandlerResult> results = new ArrayList<ConditionHandlerResult>();
        if (processDefinition != null && !processDefinition.isSuspended()) {
            final List<ActivityImpl> activities = this.findConditionalStartEventActivities(processDefinition);
            if (activities.isEmpty()) {
                throw DefaultConditionHandler.LOG.exceptionWhenEvaluatingConditionalStartEventByProcessDefinition(processDefinitionId);
            }
            for (final ActivityImpl activity : activities) {
                if (this.evaluateCondition(conditionSet, activity)) {
                    results.add(new ConditionHandlerResult(processDefinition, activity));
                }
            }
        }
        return results;
    }
    
    protected List<ActivityImpl> findConditionalStartEventActivities(final ProcessDefinitionEntity processDefinition) {
        final List<ActivityImpl> activities = new ArrayList<ActivityImpl>();
        for (final EventSubscriptionDeclaration declaration : EventSubscriptionDeclaration.getDeclarationsForScope(processDefinition).values()) {
            if (this.isConditionStartEvent(declaration)) {
                activities.add(((ConditionalEventDefinition)declaration).getConditionalActivity());
            }
        }
        return activities;
    }
    
    protected boolean isConditionStartEvent(final EventSubscriptionDeclaration declaration) {
        return EventType.CONDITONAL.name().equals(declaration.getEventType()) && declaration.isStartEvent();
    }
    
    protected boolean evaluateCondition(final ConditionSet conditionSet, final ActivityImpl activity) {
        final ExecutionEntity temporaryExecution = new ExecutionEntity();
        if (conditionSet.getVariables() != null) {
            temporaryExecution.initializeVariableStore((Map<String, Object>)conditionSet.getVariables());
        }
        temporaryExecution.setProcessDefinition(activity.getProcessDefinition());
        final ConditionalEventDefinition conditionalEventDefinition = activity.getProperties().get(BpmnProperties.CONDITIONAL_EVENT_DEFINITION);
        return (conditionalEventDefinition.getVariableName() == null || conditionSet.getVariables().containsKey((Object)conditionalEventDefinition.getVariableName())) && conditionalEventDefinition.tryEvaluate(temporaryExecution);
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
