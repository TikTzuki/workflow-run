// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceHistoryListener;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import org.zik.bpm.engine.impl.tree.ReferenceWalker;
import org.zik.bpm.engine.impl.tree.ExecutionWalker;
import org.zik.bpm.engine.impl.bpmn.behavior.CancelBoundaryEventActivityBehavior;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ActivityInstanceImpl;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.bpmn.behavior.CancelEndEventActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.CompensationEventActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.ReceiveTaskActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.SequentialMultiInstanceActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.SubProcessActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.EventSubProcessActivityBehavior;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import java.util.Map;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.delegate.CompositeActivityBehavior;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.bpmn.behavior.BpmnBehaviorLogger;

public class LegacyBehavior
{
    private static final BpmnBehaviorLogger LOG;
    
    public static void pruneConcurrentScope(final PvmExecutionImpl execution) {
        ensureConcurrentScope(execution);
        LegacyBehavior.LOG.debugConcurrentScopeIsPruned(execution);
        execution.setConcurrent(false);
    }
    
    public static void cancelConcurrentScope(final PvmExecutionImpl execution, final PvmActivity cancelledScopeActivity) {
        ensureConcurrentScope(execution);
        LegacyBehavior.LOG.debugCancelConcurrentScopeExecution(execution);
        execution.interrupt("Scope " + cancelledScopeActivity + " cancelled.");
        execution.setActivity(cancelledScopeActivity);
        execution.leaveActivityInstance();
        execution.interrupt("Scope " + cancelledScopeActivity + " cancelled.");
        execution.destroy();
    }
    
    public static void destroyConcurrentScope(final PvmExecutionImpl execution) {
        ensureConcurrentScope(execution);
        LegacyBehavior.LOG.destroyConcurrentScopeExecution(execution);
        execution.destroy();
    }
    
    public static boolean eventSubprocessComplete(final ActivityExecution scopeExecution) {
        final boolean performLegacyBehavior = isLegacyBehaviorRequired(scopeExecution);
        if (performLegacyBehavior) {
            LegacyBehavior.LOG.completeNonScopeEventSubprocess();
            scopeExecution.end(false);
        }
        return performLegacyBehavior;
    }
    
    public static boolean eventSubprocessConcurrentChildExecutionEnded(final ActivityExecution scopeExecution, final ActivityExecution endedExecution) {
        final boolean performLegacyBehavior = isLegacyBehaviorRequired(endedExecution);
        if (performLegacyBehavior) {
            LegacyBehavior.LOG.endConcurrentExecutionInEventSubprocess();
            ScopeImpl flowScope = endedExecution.getActivity().getFlowScope();
            if (flowScope != null) {
                flowScope = flowScope.getFlowScope();
                if (flowScope != null) {
                    if (flowScope == endedExecution.getActivity().getProcessDefinition()) {
                        endedExecution.remove();
                        scopeExecution.tryPruneLastConcurrentChild();
                        scopeExecution.forceUpdate();
                    }
                    else {
                        final PvmActivity flowScopeActivity = (PvmActivity)flowScope;
                        final ActivityBehavior activityBehavior = flowScopeActivity.getActivityBehavior();
                        if (activityBehavior instanceof CompositeActivityBehavior) {
                            ((CompositeActivityBehavior)activityBehavior).concurrentChildExecutionEnded(scopeExecution, endedExecution);
                        }
                    }
                }
            }
        }
        return performLegacyBehavior;
    }
    
    public static boolean destroySecondNonScope(final PvmExecutionImpl execution) {
        ensureScope(execution);
        final boolean performLegacyBehavior = isLegacyBehaviorRequired(execution);
        if (performLegacyBehavior) {}
        return performLegacyBehavior;
    }
    
    protected static boolean isLegacyBehaviorRequired(final ActivityExecution scopeExecution) {
        final Map<ScopeImpl, PvmExecutionImpl> activityExecutionMapping = scopeExecution.createActivityExecutionMapping();
        PvmScope activity = scopeExecution.getActivity();
        if (!activity.isScope()) {
            activity = activity.getFlowScope();
        }
        return activityExecutionMapping.get(activity) == activityExecutionMapping.get(activity.getFlowScope());
    }
    
    public static PvmExecutionImpl getScopeExecution(final ScopeImpl scope, final Map<ScopeImpl, PvmExecutionImpl> activityExecutionMapping) {
        final ScopeImpl flowScope = scope.getFlowScope();
        return activityExecutionMapping.get(flowScope);
    }
    
    protected static void ensureConcurrentScope(final PvmExecutionImpl execution) {
        ensureScope(execution);
        ensureConcurrent(execution);
    }
    
    protected static void ensureConcurrent(final PvmExecutionImpl execution) {
        if (!execution.isConcurrent()) {
            throw new ProcessEngineException("Execution must be concurrent.");
        }
    }
    
    protected static void ensureScope(final PvmExecutionImpl execution) {
        if (!execution.isScope()) {
            throw new ProcessEngineException("Execution must be scope.");
        }
    }
    
    public static Map<ScopeImpl, PvmExecutionImpl> createActivityExecutionMapping(final List<PvmExecutionImpl> scopeExecutions, final List<ScopeImpl> scopes) {
        final PvmExecutionImpl deepestExecution = scopeExecutions.get(0);
        if (isLegacyAsyncAtMultiInstance(deepestExecution)) {
            scopes.remove(0);
        }
        int numOfMissingExecutions = scopes.size() - scopeExecutions.size();
        Collections.reverse(scopeExecutions);
        Collections.reverse(scopes);
        final Map<ScopeImpl, PvmExecutionImpl> mapping = new HashMap<ScopeImpl, PvmExecutionImpl>();
        mapping.put(scopes.get(0), scopeExecutions.get(0));
        int executionCounter = 0;
        for (int i = 1; i < scopes.size(); ++i) {
            final ActivityImpl scope = scopes.get(i);
            PvmExecutionImpl scopeExecutionCandidate = null;
            if (executionCounter + 1 < scopeExecutions.size()) {
                scopeExecutionCandidate = scopeExecutions.get(executionCounter + 1);
            }
            if (numOfMissingExecutions > 0 && wasNoScope(scope, scopeExecutionCandidate)) {
                --numOfMissingExecutions;
            }
            else {
                ++executionCounter;
            }
            if (executionCounter >= scopeExecutions.size()) {
                throw new ProcessEngineException("Cannot construct activity-execution mapping: there are more scope executions missing than explained by the flow scope hierarchy.");
            }
            final PvmExecutionImpl execution = scopeExecutions.get(executionCounter);
            mapping.put(scope, execution);
        }
        return mapping;
    }
    
    protected static boolean wasNoScope(final ActivityImpl activity, final PvmExecutionImpl scopeExecutionCandidate) {
        return wasNoScope72(activity) || wasNoScope73(activity, scopeExecutionCandidate);
    }
    
    protected static boolean wasNoScope72(final ActivityImpl activity) {
        final ActivityBehavior activityBehavior = activity.getActivityBehavior();
        final ActivityBehavior parentActivityBehavior = (ActivityBehavior)((activity.getFlowScope() != null) ? activity.getFlowScope().getActivityBehavior() : null);
        return activityBehavior instanceof EventSubProcessActivityBehavior || (activityBehavior instanceof SubProcessActivityBehavior && parentActivityBehavior instanceof SequentialMultiInstanceActivityBehavior) || (activityBehavior instanceof ReceiveTaskActivityBehavior && parentActivityBehavior instanceof MultiInstanceActivityBehavior);
    }
    
    protected static boolean wasNoScope73(final ActivityImpl activity, final PvmExecutionImpl scopeExecutionCandidate) {
        final ActivityBehavior activityBehavior = activity.getActivityBehavior();
        return activityBehavior instanceof CompensationEventActivityBehavior || activityBehavior instanceof CancelEndEventActivityBehavior || isMultiInstanceInCompensation(activity, scopeExecutionCandidate);
    }
    
    protected static boolean isMultiInstanceInCompensation(final ActivityImpl activity, final PvmExecutionImpl scopeExecutionCandidate) {
        return activity.getActivityBehavior() instanceof MultiInstanceActivityBehavior && ((scopeExecutionCandidate != null && findCompensationThrowingAncestorExecution(scopeExecutionCandidate) != null) || scopeExecutionCandidate == null);
    }
    
    protected static boolean isLegacyAsyncAtMultiInstance(final PvmExecutionImpl execution) {
        final ActivityImpl activity = execution.getActivity();
        if (activity != null) {
            final boolean isAsync = execution.getActivityInstanceId() == null;
            final boolean isAtMultiInstance = activity.getParentFlowScopeActivity() != null && activity.getParentFlowScopeActivity().getActivityBehavior() instanceof MultiInstanceActivityBehavior;
            return isAsync && isAtMultiInstance;
        }
        return false;
    }
    
    public static PvmExecutionImpl determinePropagatingExecutionOnEnd(final PvmExecutionImpl propagatingExecution, final Map<ScopeImpl, PvmExecutionImpl> activityExecutionMapping) {
        if (!propagatingExecution.isScope()) {
            return propagatingExecution;
        }
        if (activityExecutionMapping.values().contains(propagatingExecution)) {
            return propagatingExecution;
        }
        propagatingExecution.remove();
        final PvmExecutionImpl parent = propagatingExecution.getParent();
        parent.setActivity(propagatingExecution.getActivity());
        return propagatingExecution.getParent();
    }
    
    public static boolean isConcurrentScope(final PvmExecutionImpl propagatingExecution) {
        return propagatingExecution.isConcurrent() && propagatingExecution.isScope();
    }
    
    public static void removeLegacySubscriptionOnParent(final ExecutionEntity execution, final EventSubscriptionEntity eventSubscription) {
        final ActivityImpl activity = execution.getActivity();
        if (activity == null) {
            return;
        }
        final ActivityBehavior behavior = activity.getActivityBehavior();
        final ActivityBehavior parentBehavior = (ActivityBehavior)((activity.getFlowScope() != null) ? activity.getFlowScope().getActivityBehavior() : null);
        if (behavior instanceof ReceiveTaskActivityBehavior && parentBehavior instanceof MultiInstanceActivityBehavior) {
            final List<EventSubscriptionEntity> parentSubscriptions = execution.getParent().getEventSubscriptions();
            for (final EventSubscriptionEntity subscription : parentSubscriptions) {
                if (areEqualEventSubscriptions(subscription, eventSubscription)) {
                    subscription.delete();
                }
            }
        }
    }
    
    protected static boolean areEqualEventSubscriptions(final EventSubscriptionEntity subscription1, final EventSubscriptionEntity subscription2) {
        return valuesEqual(subscription1.getEventType(), subscription2.getEventType()) && valuesEqual(subscription1.getEventName(), subscription2.getEventName()) && valuesEqual(subscription1.getActivityId(), subscription2.getActivityId());
    }
    
    protected static <T> boolean valuesEqual(final T value1, final T value2) {
        return (value1 == null && value2 == null) || (value1 != null && value1.equals(value2));
    }
    
    public static void removeLegacyNonScopesFromMapping(final Map<ScopeImpl, PvmExecutionImpl> mapping) {
        final Map<PvmExecutionImpl, List<ScopeImpl>> scopesForExecutions = new HashMap<PvmExecutionImpl, List<ScopeImpl>>();
        for (final Map.Entry<ScopeImpl, PvmExecutionImpl> mappingEntry : mapping.entrySet()) {
            List<ScopeImpl> scopesForExecution = scopesForExecutions.get(mappingEntry.getValue());
            if (scopesForExecution == null) {
                scopesForExecution = new ArrayList<ScopeImpl>();
                scopesForExecutions.put(mappingEntry.getValue(), scopesForExecution);
            }
            scopesForExecution.add(mappingEntry.getKey());
        }
        for (final Map.Entry<PvmExecutionImpl, List<ScopeImpl>> scopesForExecution2 : scopesForExecutions.entrySet()) {
            final List<ScopeImpl> scopes = scopesForExecution2.getValue();
            if (scopes.size() > 1) {
                final ScopeImpl topMostScope = getTopMostScope(scopes);
                for (final ScopeImpl scope : scopes) {
                    if (scope != scope.getProcessDefinition() && scope != topMostScope) {
                        mapping.remove(scope);
                    }
                }
            }
        }
    }
    
    protected static ScopeImpl getTopMostScope(final List<ScopeImpl> scopes) {
        ScopeImpl topMostScope = null;
        for (final ScopeImpl candidateScope : scopes) {
            if (topMostScope == null || candidateScope.isAncestorFlowScopeOf(topMostScope)) {
                topMostScope = candidateScope;
            }
        }
        return topMostScope;
    }
    
    public static void repairParentRelationships(final Collection<ActivityInstanceImpl> values, final String processInstanceId) {
        for (final ActivityInstanceImpl activityInstance : values) {
            if (valuesEqual(activityInstance.getId(), activityInstance.getParentActivityInstanceId())) {
                activityInstance.setParentActivityInstanceId(processInstanceId);
            }
        }
    }
    
    public static void migrateMultiInstanceJobDefinitions(final ProcessDefinitionEntity processDefinition, final List<JobDefinitionEntity> jobDefinitions) {
        for (final JobDefinitionEntity jobDefinition : jobDefinitions) {
            final String activityId = jobDefinition.getActivityId();
            if (activityId != null) {
                final ActivityImpl activity = processDefinition.findActivity(jobDefinition.getActivityId());
                if (isAsync(activity) || !isActivityWrappedInMultiInstanceBody(activity) || !isAsyncJobDefinition(jobDefinition)) {
                    continue;
                }
                jobDefinition.setActivityId(activity.getFlowScope().getId());
            }
        }
    }
    
    protected static boolean isAsync(final ActivityImpl activity) {
        return activity.isAsyncBefore() || activity.isAsyncAfter();
    }
    
    protected static boolean isAsyncJobDefinition(final JobDefinitionEntity jobDefinition) {
        return "async-continuation".equals(jobDefinition.getJobType());
    }
    
    protected static boolean isActivityWrappedInMultiInstanceBody(final ActivityImpl activity) {
        final ScopeImpl flowScope = activity.getFlowScope();
        if (flowScope != activity.getProcessDefinition()) {
            final ActivityImpl flowScopeActivity = (ActivityImpl)flowScope;
            return flowScopeActivity.getActivityBehavior() instanceof MultiInstanceActivityBehavior;
        }
        return false;
    }
    
    public static void repairMultiInstanceAsyncJob(final ExecutionEntity execution) {
        final ActivityImpl activity = execution.getActivity();
        if (!isAsync(activity) && isActivityWrappedInMultiInstanceBody(activity)) {
            execution.setActivity((PvmActivity)activity.getFlowScope());
        }
    }
    
    public static boolean signalCancelBoundaryEvent(final String signalName) {
        return "compensationDone".equals(signalName);
    }
    
    public static void parseCancelBoundaryEvent(final ActivityImpl activity) {
        activity.setProperty("throwsCompensation", true);
    }
    
    public static boolean hasInvalidIntermediaryActivityId(final PvmExecutionImpl execution) {
        return !execution.getNonEventScopeExecutions().isEmpty() && !CompensationBehavior.isCompensationThrowing(execution);
    }
    
    public static boolean isCompensationThrowing(final PvmExecutionImpl execution, final Map<ScopeImpl, PvmExecutionImpl> activityExecutionMapping) {
        if (!CompensationBehavior.isCompensationThrowing(execution)) {
            return false;
        }
        final ScopeImpl compensationThrowingActivity = execution.getActivity();
        if (compensationThrowingActivity.isScope()) {
            return activityExecutionMapping.get(compensationThrowingActivity) == activityExecutionMapping.get(compensationThrowingActivity.getFlowScope());
        }
        return compensationThrowingActivity.getActivityBehavior() instanceof CancelBoundaryEventActivityBehavior;
    }
    
    public static boolean isCompensationThrowing(final PvmExecutionImpl execution) {
        return isCompensationThrowing(execution, execution.createActivityExecutionMapping());
    }
    
    protected static PvmExecutionImpl findCompensationThrowingAncestorExecution(final PvmExecutionImpl execution) {
        final ExecutionWalker walker = new ExecutionWalker(execution);
        walker.walkUntil(new ReferenceWalker.WalkCondition<PvmExecutionImpl>() {
            @Override
            public boolean isFulfilled(final PvmExecutionImpl element) {
                return element == null || CompensationBehavior.isCompensationThrowing(element);
            }
        });
        return walker.getCurrentElement();
    }
    
    public static void createMissingHistoricVariables(final PvmExecutionImpl execution) {
        final Collection<VariableInstanceEntity> variables = ((ExecutionEntity)execution).getVariablesInternal();
        if (variables != null && variables.size() > 0) {
            for (final VariableInstanceEntity variable : variables) {
                if (variable.wasCreatedBefore713()) {
                    VariableInstanceHistoryListener.INSTANCE.onCreate(variable, (AbstractVariableScope)variable.getExecution());
                }
            }
        }
    }
    
    static {
        LOG = ProcessEngineLogger.BPMN_BEHAVIOR_LOGGER;
    }
}
