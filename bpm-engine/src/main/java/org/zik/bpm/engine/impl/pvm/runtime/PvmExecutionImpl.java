// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime;

import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.bpmn.helper.BpmnProperties;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;
import org.zik.bpm.engine.impl.cmmn.model.CmmnCaseDefinition;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.variable.event.VariableEvent;
import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.zik.bpm.engine.impl.form.FormPropertyHelper;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.history.event.HistoryEventProcessor;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.impl.incident.IncidentContext;
import org.zik.bpm.engine.impl.incident.IncidentHandler;
import org.zik.bpm.engine.impl.incident.IncidentHandling;
import org.zik.bpm.engine.impl.persistence.entity.DelayedVariableEvent;
import org.zik.bpm.engine.impl.persistence.entity.IncidentEntity;
import org.zik.bpm.engine.impl.pvm.*;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.pvm.delegate.CompositeActivityBehavior;
import org.zik.bpm.engine.impl.pvm.delegate.ModificationObserverBehavior;
import org.zik.bpm.engine.impl.pvm.delegate.SignallableActivityBehavior;
import org.zik.bpm.engine.impl.pvm.process.*;
import org.zik.bpm.engine.impl.pvm.runtime.operation.PvmAtomicOperation;
import org.zik.bpm.engine.impl.tree.*;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.runtime.Incident;

import java.util.*;

public abstract class PvmExecutionImpl extends CoreExecution implements ActivityExecution, PvmProcessInstance {
    private static final long serialVersionUID = 1L;
    private static final PvmLogger LOG;
    protected transient ProcessDefinitionImpl processDefinition;
    protected transient ScopeInstantiationContext scopeInstantiationContext;
    protected transient boolean ignoreAsync;
    protected transient boolean isStarting;
    protected transient ActivityImpl activity;
    protected transient PvmActivity nextActivity;
    protected transient TransitionImpl transition;
    protected transient List<PvmTransition> transitionsToTake;
    protected String activityInstanceId;
    protected String caseInstanceId;
    protected PvmExecutionImpl replacedBy;
    protected boolean deleteRoot;
    protected String deleteReason;
    protected boolean externallyTerminated;
    protected boolean isActive;
    protected boolean isScope;
    protected boolean isConcurrent;
    protected boolean isEnded;
    protected boolean isEventScope;
    protected boolean isRemoved;
    protected boolean preserveScope;
    protected int activityInstanceState;
    protected boolean activityInstanceEndListenersFailed;
    protected long sequenceCounter;
    protected transient List<DelayedVariableEvent> delayedEvents;

    public PvmExecutionImpl() {
        this.ignoreAsync = false;
        this.isStarting = false;
        this.transitionsToTake = null;
        this.isActive = true;
        this.isScope = true;
        this.isConcurrent = false;
        this.isEnded = false;
        this.isEventScope = false;
        this.isRemoved = false;
        this.preserveScope = false;
        this.activityInstanceState = ActivityInstanceState.DEFAULT.getStateCode();
        this.activityInstanceEndListenersFailed = false;
        this.sequenceCounter = 0L;
        this.delayedEvents = new ArrayList<DelayedVariableEvent>();
    }

    @Override
    public abstract PvmExecutionImpl createExecution();

    @Override
    public PvmExecutionImpl createSubProcessInstance(final PvmProcessDefinition processDefinition) {
        return this.createSubProcessInstance(processDefinition, null);
    }

    @Override
    public PvmExecutionImpl createSubProcessInstance(final PvmProcessDefinition processDefinition, final String businessKey) {
        final PvmExecutionImpl processInstance = this.getProcessInstance();
        String caseInstanceId = null;
        if (processInstance != null) {
            caseInstanceId = processInstance.getCaseInstanceId();
        }
        return this.createSubProcessInstance(processDefinition, businessKey, caseInstanceId);
    }

    @Override
    public PvmExecutionImpl createSubProcessInstance(final PvmProcessDefinition processDefinition, final String businessKey, final String caseInstanceId) {
        final PvmExecutionImpl subProcessInstance = this.newExecution();
        subProcessInstance.setSuperExecution(this);
        this.setSubProcessInstance(subProcessInstance);
        subProcessInstance.setProcessDefinition((ProcessDefinitionImpl) processDefinition);
        subProcessInstance.setProcessInstance(subProcessInstance);
        subProcessInstance.setActivity(processDefinition.getInitial());
        if (businessKey != null) {
            subProcessInstance.setBusinessKey(businessKey);
        }
        if (caseInstanceId != null) {
            subProcessInstance.setCaseInstanceId(caseInstanceId);
        }
        return subProcessInstance;
    }

    protected abstract PvmExecutionImpl newExecution();

    @Override
    public abstract CmmnExecution createSubCaseInstance(final CmmnCaseDefinition p0);

    @Override
    public abstract CmmnExecution createSubCaseInstance(final CmmnCaseDefinition p0, final String p1);

    public abstract void initialize();

    public abstract void initializeTimerDeclarations();

    public void executeIoMapping() {
        final ScopeImpl currentScope = this.getScopeActivity();
        if (currentScope != currentScope.getProcessDefinition()) {
            final ActivityImpl currentActivity = (ActivityImpl) currentScope;
            if (currentActivity != null && currentActivity.getIoMapping() != null && !this.skipIoMapping) {
                currentActivity.getIoMapping().executeInputParameters(this);
            }
        }
    }

    @Override
    public void start() {
        this.start(null);
    }

    @Override
    public void start(final Map<String, Object> variables) {
        this.start(variables, null);
    }

    public void startWithFormProperties(final VariableMap formProperties) {
        this.start(null, formProperties);
    }

    protected void start(final Map<String, Object> variables, final VariableMap formProperties) {
        this.initialize();
        this.fireHistoricProcessStartEvent();
        if (variables != null) {
            this.setVariables(variables);
        }
        if (formProperties != null) {
            FormPropertyHelper.initFormPropertiesOnScope(formProperties, this);
        }
        this.initializeTimerDeclarations();
        this.performOperation(PvmAtomicOperation.PROCESS_START);
    }

    public void startWithoutExecuting(final Map<String, Object> variables) {
        this.initialize();
        this.fireHistoricProcessStartEvent();
        this.setActivityInstanceId(this.getId());
        this.setVariables(variables);
        this.initializeTimerDeclarations();
        this.performOperation(PvmAtomicOperation.FIRE_PROCESS_START);
        this.setActivity(null);
    }

    public abstract void fireHistoricProcessStartEvent();

    @Override
    public void destroy() {
        PvmExecutionImpl.LOG.destroying(this);
        this.setScope(false);
    }

    public void removeAllTasks() {
    }

    protected void removeEventScopes() {
        final List<PvmExecutionImpl> childExecutions = new ArrayList<PvmExecutionImpl>(this.getEventScopeExecutions());
        for (final PvmExecutionImpl childExecution : childExecutions) {
            PvmExecutionImpl.LOG.removingEventScope(childExecution);
            childExecution.destroy();
            childExecution.remove();
        }
    }

    public void clearScope(final String reason, final boolean skipCustomListeners, final boolean skipIoMappings, final boolean externallyTerminated) {
        this.skipCustomListeners = skipCustomListeners;
        this.skipIoMapping = skipIoMappings;
        if (this.getSubProcessInstance() != null) {
            this.getSubProcessInstance().deleteCascade(reason, skipCustomListeners, skipIoMappings, externallyTerminated, false);
        }
        final List<PvmExecutionImpl> executions = new ArrayList<PvmExecutionImpl>(this.getNonEventScopeExecutions());
        for (final PvmExecutionImpl childExecution : executions) {
            if (childExecution.getSubProcessInstance() != null) {
                childExecution.getSubProcessInstance().deleteCascade(reason, skipCustomListeners, skipIoMappings, externallyTerminated, false);
            }
            childExecution.deleteCascade(reason, skipCustomListeners, skipIoMappings, externallyTerminated, false);
        }
        final PvmActivity activity = this.getActivity();
        if (this.isActive && activity != null) {
            if (this.activityInstanceState != ActivityInstanceState.ENDING.getStateCode()) {
                this.setCanceled(true);
                this.performOperation(PvmAtomicOperation.FIRE_ACTIVITY_END);
            }
            this.activityInstanceState = ActivityInstanceState.DEFAULT.getStateCode();
        }
    }

    @Override
    public void interrupt(final String reason) {
        this.interrupt(reason, false, false, false);
    }

    public void interrupt(final String reason, final boolean skipCustomListeners, final boolean skipIoMappings, final boolean externallyTerminated) {
        PvmExecutionImpl.LOG.interruptingExecution(reason, skipCustomListeners);
        this.clearScope(reason, skipCustomListeners, skipIoMappings, externallyTerminated);
    }

    @Override
    public void end(final boolean completeScope) {
        this.setCompleteScope(completeScope);
        this.isActive = false;
        this.isEnded = true;
        if (this.hasReplacedParent()) {
            this.getParent().replacedBy = null;
        }
        this.performOperation(PvmAtomicOperation.ACTIVITY_NOTIFY_LISTENER_END);
    }

    @Override
    public void endCompensation() {
        this.performOperation(PvmAtomicOperation.FIRE_ACTIVITY_END);
        this.remove();
        final PvmExecutionImpl parent = this.getParent();
        if (parent.getActivity() == null) {
            parent.setActivity((PvmActivity) this.getActivity().getFlowScope());
        }
        parent.signal("compensationDone", null);
    }

    public void propagateEnd() {
        if (!this.isEnded()) {
            throw new ProcessEngineException(this.toString() + " must have ended before ending can be propagated");
        }
        if (this.isProcessInstanceExecution()) {
            this.performOperation(PvmAtomicOperation.PROCESS_END);
        }
    }

    @Override
    public void remove() {
        final PvmExecutionImpl parent = this.getParent();
        if (parent != null) {
            parent.getExecutions().remove(this);
            final long parentSequenceCounter = parent.getSequenceCounter();
            final long mySequenceCounter = this.getSequenceCounter();
            if (mySequenceCounter > parentSequenceCounter) {
                parent.setSequenceCounter(mySequenceCounter);
            }
            final PvmExecutionImpl pvmExecutionImpl = parent;
            pvmExecutionImpl.skipCustomListeners |= this.skipCustomListeners;
            final PvmExecutionImpl pvmExecutionImpl2 = parent;
            pvmExecutionImpl2.skipIoMapping |= this.skipIoMapping;
        }
        this.isActive = false;
        this.isEnded = true;
        this.isRemoved = true;
        if (this.hasReplacedParent()) {
            this.getParent().replacedBy = null;
        }
        this.removeEventScopes();
    }

    public boolean isRemoved() {
        return this.isRemoved;
    }

    public PvmExecutionImpl createConcurrentExecution() {
        if (!this.isScope()) {
            throw new ProcessEngineException("Cannot create concurrent execution for " + this);
        }
        final List<? extends PvmExecutionImpl> children = this.getNonEventScopeExecutions();
        this.forceUpdate();
        if (children.isEmpty()) {
            final PvmExecutionImpl replacingExecution = this.createExecution();
            replacingExecution.setConcurrent(true);
            replacingExecution.setScope(false);
            replacingExecution.replace(this);
            this.inactivate();
            this.setActivity(null);
        } else if (children.size() == 1) {
            final PvmExecutionImpl child = (PvmExecutionImpl) children.get(0);
            final PvmExecutionImpl concurrentReplacingExecution = this.createExecution();
            concurrentReplacingExecution.setConcurrent(true);
            concurrentReplacingExecution.setScope(false);
            concurrentReplacingExecution.setActive(false);
            concurrentReplacingExecution.onConcurrentExpand(this);
            child.setParent(concurrentReplacingExecution);
            this.leaveActivityInstance();
            this.setActivity(null);
        }
        final PvmExecutionImpl concurrentExecution = this.createExecution();
        concurrentExecution.setConcurrent(true);
        concurrentExecution.setScope(false);
        return concurrentExecution;
    }

    @Override
    public boolean tryPruneLastConcurrentChild() {
        if (this.getNonEventScopeExecutions().size() == 1) {
            final PvmExecutionImpl lastConcurrent = (PvmExecutionImpl) this.getNonEventScopeExecutions().get(0);
            if (lastConcurrent.isConcurrent()) {
                if (!lastConcurrent.isScope()) {
                    this.setActivity(lastConcurrent.getActivity());
                    this.setTransition(lastConcurrent.getTransition());
                    this.replace(lastConcurrent);
                    if (lastConcurrent.hasChildren()) {
                        for (final PvmExecutionImpl childExecution : lastConcurrent.getExecutionsAsCopy()) {
                            childExecution.setParent(this);
                        }
                    }
                    if (!this.isActive() && lastConcurrent.isActive()) {
                        this.setActive(true);
                    }
                    lastConcurrent.remove();
                } else {
                    LegacyBehavior.pruneConcurrentScope(lastConcurrent);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteCascade(final String deleteReason) {
        this.deleteCascade(deleteReason, false, false);
    }

    public void deleteCascade(final String deleteReason, final boolean skipCustomListeners, final boolean skipIoMappings) {
        this.deleteCascade(deleteReason, skipCustomListeners, skipIoMappings, false, false);
    }

    public void deleteCascade(final String deleteReason, final boolean skipCustomListeners, final boolean skipIoMappings, final boolean externallyTerminated, final boolean skipSubprocesses) {
        this.deleteReason = deleteReason;
        this.setDeleteRoot(true);
        this.isEnded = true;
        this.skipCustomListeners = skipCustomListeners;
        this.skipIoMapping = skipIoMappings;
        this.externallyTerminated = externallyTerminated;
        this.skipSubprocesses = skipSubprocesses;
        this.performOperation(PvmAtomicOperation.DELETE_CASCADE);
    }

    public void executeEventHandlerActivity(final ActivityImpl eventHandlerActivity) {
        final ScopeImpl flowScope = eventHandlerActivity.getFlowScope();
        final ScopeImpl eventScope = eventHandlerActivity.getEventScope();
        if (eventHandlerActivity.getActivityStartBehavior() == ActivityStartBehavior.CONCURRENT_IN_FLOW_SCOPE && flowScope != eventScope) {
            this.findExecutionForScope(eventScope, flowScope).executeActivity(eventHandlerActivity);
        } else {
            this.executeActivity(eventHandlerActivity);
        }
    }

    public abstract PvmExecutionImpl getReplacedBy();

    public PvmExecutionImpl resolveReplacedBy() {
        PvmExecutionImpl replacingExecution = this.getReplacedBy();
        if (replacingExecution != null) {
            final PvmExecutionImpl secondHopReplacingExecution = replacingExecution.getReplacedBy();
            if (secondHopReplacingExecution != null) {
                replacingExecution = secondHopReplacingExecution;
            }
        }
        return replacingExecution;
    }

    public boolean hasReplacedParent() {
        return this.getParent() != null && this.getParent().getReplacedBy() == this;
    }

    public boolean isReplacedByParent() {
        return this.getReplacedBy() != null && this.getReplacedBy() == this.getParent();
    }

    public void replace(final PvmExecutionImpl execution) {
        this.activityInstanceId = execution.getActivityInstanceId();
        this.isActive = execution.isActive;
        this.replacedBy = null;
        execution.replacedBy = this;
        this.transitionsToTake = execution.transitionsToTake;
        execution.leaveActivityInstance();
    }

    public void onConcurrentExpand(final PvmExecutionImpl scopeExecution) {
    }

    @Override
    public void signal(final String signalName, final Object signalData) {
        if (this.getActivity() == null) {
            throw new PvmException("cannot signal execution " + this.id + ": it has no current activity");
        }
        final SignallableActivityBehavior activityBehavior = (SignallableActivityBehavior) this.activity.getActivityBehavior();
        try {
            activityBehavior.signal(this, signalName, signalData);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw new PvmException("couldn't process signal '" + signalName + "' on activity '" + this.activity.getId() + "': " + e2.getMessage(), e2);
        }
    }

    public void take() {
        if (this.transition == null) {
            throw new PvmException(this.toString() + ": no transition to take specified");
        }
        final TransitionImpl transitionImpl = this.transition;
        this.setActivity(transitionImpl.getSource());
        this.setActivityInstanceId(null);
        this.setActive(true);
        this.performOperation(PvmAtomicOperation.TRANSITION_NOTIFY_LISTENER_TAKE);
    }

    @Override
    public void executeActivity(final PvmActivity activity) {
        if (!activity.getIncomingTransitions().isEmpty()) {
            throw new ProcessEngineException("Activity is contained in normal flow and cannot be executed using executeActivity().");
        }
        final ActivityStartBehavior activityStartBehavior = activity.getActivityStartBehavior();
        if (!this.isScope() && ActivityStartBehavior.DEFAULT != activityStartBehavior) {
            throw new ProcessEngineException("Activity '" + activity + "' with start behavior '" + activityStartBehavior + "'cannot be executed by non-scope execution.");
        }
        final PvmActivity activityImpl = activity;
        this.isEnded = false;
        this.isActive = true;
        switch (activityStartBehavior) {
            case CONCURRENT_IN_FLOW_SCOPE: {
                this.nextActivity = activityImpl;
                this.performOperation(PvmAtomicOperation.ACTIVITY_START_CONCURRENT);
                break;
            }
            case CANCEL_EVENT_SCOPE: {
                this.nextActivity = activityImpl;
                this.performOperation(PvmAtomicOperation.ACTIVITY_START_CANCEL_SCOPE);
                break;
            }
            case INTERRUPT_EVENT_SCOPE: {
                this.nextActivity = activityImpl;
                this.performOperation(PvmAtomicOperation.ACTIVITY_START_INTERRUPT_SCOPE);
                break;
            }
            default: {
                this.setActivity(activityImpl);
                this.setActivityInstanceId(null);
                this.performOperation(PvmAtomicOperation.ACTIVITY_START_CREATE_SCOPE);
                break;
            }
        }
    }

    public void executeActivitiesConcurrent(final List<PvmActivity> activityStack, final PvmActivity targetActivity, final PvmTransition targetTransition, final Map<String, Object> variables, final Map<String, Object> localVariables, final boolean skipCustomListeners, final boolean skipIoMappings) {
        ScopeImpl flowScope = null;
        if (!activityStack.isEmpty()) {
            flowScope = activityStack.get(0).getFlowScope();
        } else if (targetActivity != null) {
            flowScope = targetActivity.getFlowScope();
        } else if (targetTransition != null) {
            flowScope = targetTransition.getSource().getFlowScope();
        }
        PvmExecutionImpl propagatingExecution = null;
        if (flowScope.getActivityBehavior() instanceof ModificationObserverBehavior) {
            final ModificationObserverBehavior flowScopeBehavior = (ModificationObserverBehavior) flowScope.getActivityBehavior();
            propagatingExecution = (PvmExecutionImpl) flowScopeBehavior.createInnerInstance(this);
        } else {
            propagatingExecution = this.createConcurrentExecution();
        }
        propagatingExecution.executeActivities(activityStack, targetActivity, targetTransition, variables, localVariables, skipCustomListeners, skipIoMappings);
    }

    public Map<PvmActivity, PvmExecutionImpl> instantiateScopes(final List<PvmActivity> activityStack, final boolean skipCustomListeners, final boolean skipIoMappings) {
        if (activityStack.isEmpty()) {
            return Collections.emptyMap();
        }
        this.skipCustomListeners = skipCustomListeners;
        this.skipIoMapping = skipIoMappings;
        final ScopeInstantiationContext executionStartContext = new ScopeInstantiationContext();
        final InstantiationStack instantiationStack = new InstantiationStack(new LinkedList<PvmActivity>(activityStack));
        executionStartContext.setInstantiationStack(instantiationStack);
        this.setStartContext(executionStartContext);
        this.performOperation(PvmAtomicOperation.ACTIVITY_INIT_STACK_AND_RETURN);
        final Map<PvmActivity, PvmExecutionImpl> createdExecutions = new HashMap<PvmActivity, PvmExecutionImpl>();
        PvmExecutionImpl currentExecution = this;
        for (final PvmActivity instantiatedActivity : activityStack) {
            currentExecution = (PvmExecutionImpl) currentExecution.getNonEventScopeExecutions().get(0);
            if (currentExecution.isConcurrent()) {
                currentExecution = (PvmExecutionImpl) currentExecution.getNonEventScopeExecutions().get(0);
            }
            createdExecutions.put(instantiatedActivity, currentExecution);
        }
        return createdExecutions;
    }

    public void executeActivities(final List<PvmActivity> activityStack, final PvmActivity targetActivity, final PvmTransition targetTransition, final Map<String, Object> variables, final Map<String, Object> localVariables, final boolean skipCustomListeners, final boolean skipIoMappings) {
        this.skipCustomListeners = skipCustomListeners;
        this.skipIoMapping = skipIoMappings;
        this.activityInstanceId = null;
        this.isEnded = false;
        if (!activityStack.isEmpty()) {
            final ScopeInstantiationContext executionStartContext = new ScopeInstantiationContext();
            final InstantiationStack instantiationStack = new InstantiationStack(activityStack, targetActivity, targetTransition);
            executionStartContext.setInstantiationStack(instantiationStack);
            executionStartContext.setVariables(variables);
            executionStartContext.setVariablesLocal(localVariables);
            this.setStartContext(executionStartContext);
            this.performOperation(PvmAtomicOperation.ACTIVITY_INIT_STACK);
        } else if (targetActivity != null) {
            this.setVariables(variables);
            this.setVariablesLocal(localVariables);
            this.setActivity(targetActivity);
            this.performOperation(PvmAtomicOperation.ACTIVITY_START_CREATE_SCOPE);
        } else if (targetTransition != null) {
            this.setVariables(variables);
            this.setVariablesLocal(localVariables);
            this.setActivity(targetTransition.getSource());
            this.setTransition(targetTransition);
            this.performOperation(PvmAtomicOperation.TRANSITION_START_NOTIFY_LISTENER_TAKE);
        }
    }

    @Override
    public List<ActivityExecution> findInactiveConcurrentExecutions(final PvmActivity activity) {
        List<ActivityExecution> inactiveConcurrentExecutionsInActivity = new ArrayList();
        if (this.isConcurrent()) {
            return this.getParent().findInactiveChildExecutions(activity);
        } else {
            if (!this.isActive()) {
                inactiveConcurrentExecutionsInActivity.add(this);
            }

            return inactiveConcurrentExecutionsInActivity;
        }
    }

    @Override
    public List<ActivityExecution> findInactiveChildExecutions(final PvmActivity activity) {
        final List<ActivityExecution> inactiveConcurrentExecutionsInActivity = new ArrayList<>();
        final List<? extends PvmExecutionImpl> concurrentExecutions = this.getAllChildExecutions();
        for (final PvmExecutionImpl concurrentExecution : concurrentExecutions) {
            if (concurrentExecution.getActivity() == activity && !concurrentExecution.isActive()) {
                inactiveConcurrentExecutionsInActivity.add(concurrentExecution);
            }
        }
        return inactiveConcurrentExecutionsInActivity;
    }

    protected List<PvmExecutionImpl> getAllChildExecutions() {
        final List<PvmExecutionImpl> childExecutions = new ArrayList<PvmExecutionImpl>();
        for (final PvmExecutionImpl childExecution : this.getExecutions()) {
            childExecutions.add(childExecution);
            childExecutions.addAll(childExecution.getAllChildExecutions());
        }
        return childExecutions;
    }

    @Override
    public void leaveActivityViaTransition(final PvmTransition outgoingTransition) {
        this.leaveActivityViaTransitions(Arrays.asList(outgoingTransition), Collections.emptyList());
    }

    @Override
    public void leaveActivityViaTransitions(final List<PvmTransition> _transitions, final List<? extends ActivityExecution> _recyclableExecutions) {
        List<? extends ActivityExecution> recyclableExecutions = Collections.emptyList();
        if (_recyclableExecutions != null) {
            recyclableExecutions = new ArrayList<ActivityExecution>(_recyclableExecutions);
        }
        if (recyclableExecutions.size() > 1) {
            this.removeVariablesLocalInternal();
        }
        for (final ActivityExecution execution : recyclableExecutions) {
            execution.setEnded(true);
        }
        recyclableExecutions.remove(this);
        for (final ActivityExecution execution : recyclableExecutions) {
            execution.setIgnoreAsync(true);
            execution.end(_transitions.isEmpty());
        }
        PvmExecutionImpl propagatingExecution = this;
        if (this.getReplacedBy() != null) {
            propagatingExecution = this.getReplacedBy();
        }
        propagatingExecution.isActive = true;
        propagatingExecution.isEnded = false;
        if (_transitions.isEmpty()) {
            propagatingExecution.end(!propagatingExecution.isConcurrent());
        } else {
            propagatingExecution.setTransitionsToTake(_transitions);
            propagatingExecution.performOperation(PvmAtomicOperation.TRANSITION_NOTIFY_LISTENER_END);
        }
    }

    protected abstract void removeVariablesLocalInternal();

    public boolean isActive(final String activityId) {
        return this.findExecution(activityId) != null;
    }

    @Override
    public void inactivate() {
        this.isActive = false;
    }

    @Override
    public abstract List<PvmExecutionImpl> getExecutions();

    public abstract List<PvmExecutionImpl> getExecutionsAsCopy();

    @Override
    public List<? extends PvmExecutionImpl> getNonEventScopeExecutions() {
        final List<? extends PvmExecutionImpl> children = this.getExecutions();
        final List<PvmExecutionImpl> result = new ArrayList<PvmExecutionImpl>();
        for (final PvmExecutionImpl child : children) {
            if (!child.isEventScope()) {
                result.add(child);
            }
        }
        return result;
    }

    public List<? extends PvmExecutionImpl> getEventScopeExecutions() {
        final List<? extends PvmExecutionImpl> children = this.getExecutions();
        final List<PvmExecutionImpl> result = new ArrayList<PvmExecutionImpl>();
        for (final PvmExecutionImpl child : children) {
            if (child.isEventScope()) {
                result.add(child);
            }
        }
        return result;
    }

    @Override
    public PvmExecutionImpl findExecution(final String activityId) {
        if (this.getActivity() != null && this.getActivity().getId().equals(activityId)) {
            return this;
        }
        for (final PvmExecutionImpl nestedExecution : this.getExecutions()) {
            final PvmExecutionImpl result = nestedExecution.findExecution(activityId);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public List<PvmExecution> findExecutions(final String activityId) {
        final List<PvmExecution> matchingExecutions = new ArrayList<PvmExecution>();
        this.collectExecutions(activityId, matchingExecutions);
        return matchingExecutions;
    }

    protected void collectExecutions(final String activityId, final List<PvmExecution> executions) {
        if (this.getActivity() != null && this.getActivity().getId().equals(activityId)) {
            executions.add(this);
        }
        for (final PvmExecutionImpl nestedExecution : this.getExecutions()) {
            nestedExecution.collectExecutions(activityId, executions);
        }
    }

    @Override
    public List<String> findActiveActivityIds() {
        final List<String> activeActivityIds = new ArrayList<String>();
        this.collectActiveActivityIds(activeActivityIds);
        return activeActivityIds;
    }

    protected void collectActiveActivityIds(final List<String> activeActivityIds) {
        final ActivityImpl activity = this.getActivity();
        if (this.isActive && activity != null) {
            activeActivityIds.add(activity.getId());
        }
        for (final PvmExecutionImpl execution : this.getExecutions()) {
            execution.collectActiveActivityIds(activeActivityIds);
        }
    }

    @Override
    public String getProcessBusinessKey() {
        return this.getProcessInstance().getBusinessKey();
    }

    @Override
    public void setProcessBusinessKey(final String businessKey) {
        final PvmExecutionImpl processInstance = this.getProcessInstance();
        processInstance.setBusinessKey(businessKey);
        final HistoryLevel historyLevel = Context.getCommandContext().getProcessEngineConfiguration().getHistoryLevel();
        if (historyLevel.isHistoryEventProduced(HistoryEventTypes.PROCESS_INSTANCE_UPDATE, processInstance)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    return producer.createProcessInstanceUpdateEvt(processInstance);
                }
            });
        }
    }

    @Override
    public String getBusinessKey() {
        if (this.isProcessInstanceExecution()) {
            return this.businessKey;
        }
        return this.getProcessBusinessKey();
    }

    public void setProcessDefinition(final ProcessDefinitionImpl processDefinition) {
        this.processDefinition = processDefinition;
    }

    public ProcessDefinitionImpl getProcessDefinition() {
        return this.processDefinition;
    }

    @Override
    public abstract PvmExecutionImpl getProcessInstance();

    public abstract void setProcessInstance(final PvmExecutionImpl p0);

    public String getCaseInstanceId() {
        return this.caseInstanceId;
    }

    public void setCaseInstanceId(final String caseInstanceId) {
        this.caseInstanceId = caseInstanceId;
    }

    @Override
    public ActivityImpl getActivity() {
        return this.activity;
    }

    public String getActivityId() {
        final ActivityImpl activity = this.getActivity();
        if (activity != null) {
            return activity.getId();
        }
        return null;
    }

    @Override
    public String getCurrentActivityName() {
        final ActivityImpl activity = this.getActivity();
        if (activity != null) {
            return activity.getName();
        }
        return null;
    }

    @Override
    public String getCurrentActivityId() {
        return this.getActivityId();
    }

    @Override
    public void setActivity(final PvmActivity activity) {
        this.activity = (ActivityImpl) activity;
    }

    @Override
    public void enterActivityInstance() {
        final ActivityImpl activity = this.getActivity();
        this.activityInstanceId = this.generateActivityInstanceId(activity.getId());
        PvmExecutionImpl.LOG.debugEnterActivityInstance(this, this.getParentActivityInstanceId());
        this.executeIoMapping();
        if (activity.isScope()) {
            this.initializeTimerDeclarations();
        }
        this.activityInstanceEndListenersFailed = false;
    }

    public void activityInstanceStarting() {
        this.activityInstanceState = ActivityInstanceState.STARTING.getStateCode();
    }

    public void activityInstanceStarted() {
        this.activityInstanceState = ActivityInstanceState.DEFAULT.getStateCode();
    }

    public void activityInstanceDone() {
        this.activityInstanceState = ActivityInstanceState.ENDING.getStateCode();
    }

    public void activityInstanceEndListenerFailure() {
        this.activityInstanceEndListenersFailed = true;
    }

    protected abstract String generateActivityInstanceId(final String p0);

    @Override
    public void leaveActivityInstance() {
        if (this.activityInstanceId != null) {
            PvmExecutionImpl.LOG.debugLeavesActivityInstance(this, this.activityInstanceId);
        }
        this.activityInstanceId = this.getParentActivityInstanceId();
        this.activityInstanceState = ActivityInstanceState.DEFAULT.getStateCode();
        this.activityInstanceEndListenersFailed = false;
    }

    @Override
    public String getParentActivityInstanceId() {
        if (this.isProcessInstanceExecution()) {
            return this.getId();
        }
        return this.getParent().getActivityInstanceId();
    }

    @Override
    public void setActivityInstanceId(final String activityInstanceId) {
        this.activityInstanceId = activityInstanceId;
    }

    @Override
    public String getActivityInstanceId() {
        return this.activityInstanceId;
    }

    @Override
    public abstract PvmExecutionImpl getParent();

    @Override
    public String getParentId() {
        final PvmExecutionImpl parent = this.getParent();
        if (parent != null) {
            return parent.getId();
        }
        return null;
    }

    @Override
    public boolean hasChildren() {
        return !this.getExecutions().isEmpty();
    }

    public void setParent(final PvmExecutionImpl parent) {
        PvmExecutionImpl currentParent = this.getParent();
        this.setParentExecution(parent);
        if (currentParent != null) {
            currentParent.getExecutions().remove(this);
        }

        if (parent != null) {
            parent.getExecutions().add(this);
        }
    }

    public abstract void setParentExecution(final PvmExecutionImpl p0);

    @Override
    public abstract PvmExecutionImpl getSuperExecution();

    public abstract void setSuperExecution(final PvmExecutionImpl p0);

    public abstract PvmExecutionImpl getSubProcessInstance();

    public abstract void setSubProcessInstance(final PvmExecutionImpl p0);

    public abstract CmmnExecution getSuperCaseExecution();

    public abstract void setSuperCaseExecution(final CmmnExecution p0);

    public abstract CmmnExecution getSubCaseInstance();

    public abstract void setSubCaseInstance(final CmmnExecution p0);

    protected ScopeImpl getScopeActivity() {
        ScopeImpl scope = null;
        if (this.isProcessInstanceExecution()) {
            scope = this.getProcessDefinition();
        } else {
            scope = this.getActivity();
        }
        return scope;
    }

    @Override
    public boolean isScope() {
        return this.isScope;
    }

    @Override
    public void setScope(final boolean isScope) {
        this.isScope = isScope;
    }

    @Override
    public PvmExecutionImpl findExecutionForFlowScope(final PvmScope targetFlowScope) {
        final PvmExecutionImpl scopeExecution = this.isScope() ? this : this.getParent();
        ScopeImpl currentActivity = this.getActivity();
        EnsureUtil.ensureNotNull("activity of current execution", currentActivity);
        currentActivity = (currentActivity.isScope() ? currentActivity : currentActivity.getFlowScope());
        return scopeExecution.findExecutionForScope(currentActivity, (ScopeImpl) targetFlowScope);
    }

    public PvmExecutionImpl findExecutionForScope(final ScopeImpl currentScope, final ScopeImpl targetScope) {
        if (!targetScope.isScope()) {
            throw new ProcessEngineException("Target scope must be a scope.");
        }
        final Map<ScopeImpl, PvmExecutionImpl> activityExecutionMapping = this.createActivityExecutionMapping(currentScope);
        PvmExecutionImpl scopeExecution = activityExecutionMapping.get(targetScope);
        if (scopeExecution == null) {
            scopeExecution = LegacyBehavior.getScopeExecution(targetScope, activityExecutionMapping);
        }
        return scopeExecution;
    }

    public Map<ScopeImpl, PvmExecutionImpl> createActivityExecutionMapping(final ScopeImpl currentScope) {
        if (!this.isScope()) {
            throw new ProcessEngineException("Execution must be a scope execution");
        }
        if (!currentScope.isScope()) {
            throw new ProcessEngineException("Current scope must be a scope.");
        }
        final LeafActivityInstanceExecutionCollector leafCollector = new LeafActivityInstanceExecutionCollector();
        new ExecutionWalker(this).addPreVisitor(leafCollector).walkUntil();
        final List<PvmExecutionImpl> leaves = leafCollector.getLeaves();
        leaves.remove(this);
        Collections.reverse(leaves);
        Map<ScopeImpl, PvmExecutionImpl> mapping = new HashMap<ScopeImpl, PvmExecutionImpl>();
        for (final PvmExecutionImpl leaf : leaves) {
            final ScopeImpl leafFlowScope = leaf.getFlowScope();
            final PvmExecutionImpl leafFlowScopeExecution = leaf.getFlowScopeExecution();
            mapping = leafFlowScopeExecution.createActivityExecutionMapping(leafFlowScope, mapping);
        }
        mapping = this.createActivityExecutionMapping(currentScope, mapping);
        return mapping;
    }

    @Override
    public Map<ScopeImpl, PvmExecutionImpl> createActivityExecutionMapping() {
        final ScopeImpl currentActivity = this.getActivity();
        EnsureUtil.ensureNotNull("activity of current execution", currentActivity);
        final ScopeImpl flowScope = this.getFlowScope();
        final PvmExecutionImpl flowScopeExecution = this.getFlowScopeExecution();
        return flowScopeExecution.createActivityExecutionMapping(flowScope);
    }

    protected PvmExecutionImpl getFlowScopeExecution() {
        if (!this.isScope || CompensationBehavior.executesNonScopeCompensationHandler(this) || this.isAsyncAfterScopeWithoutTransition()) {
            return this.getParent().getFlowScopeExecution();
        }
        return this;
    }

    protected ScopeImpl getFlowScope() {
        final ActivityImpl activity = this.getActivity();
        if (!activity.isScope() || this.activityInstanceId == null || (activity.isScope() && !this.isScope() && activity.getActivityBehavior() instanceof CompositeActivityBehavior)) {
            return activity.getFlowScope();
        }
        return activity;
    }

    protected Map<ScopeImpl, PvmExecutionImpl> createActivityExecutionMapping(final ScopeImpl currentScope, final Map<ScopeImpl, PvmExecutionImpl> mapping) {
        if (!this.isScope()) {
            throw new ProcessEngineException("Execution must be a scope execution");
        }
        if (!currentScope.isScope()) {
            throw new ProcessEngineException("Current scope must be a scope.");
        }
        final ScopeExecutionCollector scopeExecutionCollector = new ScopeExecutionCollector();
        new ExecutionWalker(this).addPreVisitor(scopeExecutionCollector).walkWhile(new ReferenceWalker.WalkCondition<PvmExecutionImpl>() {
            @Override
            public boolean isFulfilled(final PvmExecutionImpl element) {
                return element == null || mapping.containsValue(element);
            }
        });
        final List<PvmExecutionImpl> scopeExecutions = scopeExecutionCollector.getScopeExecutions();
        final ScopeCollector scopeCollector = new ScopeCollector();
        new FlowScopeWalker(currentScope).addPreVisitor(scopeCollector).walkWhile(new ReferenceWalker.WalkCondition<ScopeImpl>() {
            @Override
            public boolean isFulfilled(final ScopeImpl element) {
                return element == null || mapping.containsKey(element);
            }
        });
        final List<ScopeImpl> scopes = scopeCollector.getScopes();
        final ScopeImpl topMostScope = scopes.get(scopes.size() - 1);
        new FlowScopeWalker(topMostScope.getFlowScope()).addPreVisitor(new TreeVisitor<ScopeImpl>() {
            @Override
            public void visit(final ScopeImpl obj) {
                scopes.add(obj);
                final PvmExecutionImpl priorMappingExecution = mapping.get(obj);
                if (priorMappingExecution != null && !scopeExecutions.contains(priorMappingExecution)) {
                    scopeExecutions.add(priorMappingExecution);
                }
            }
        }).walkWhile();
        if (scopes.size() == scopeExecutions.size()) {
            final Map<ScopeImpl, PvmExecutionImpl> result = new HashMap<ScopeImpl, PvmExecutionImpl>();
            for (int i = 0; i < scopes.size(); ++i) {
                result.put(scopes.get(i), scopeExecutions.get(i));
            }
            return result;
        }
        return LegacyBehavior.createActivityExecutionMapping(scopeExecutions, scopes);
    }

    @Override
    public String toString() {
        if (this.isProcessInstanceExecution()) {
            return "ProcessInstance[" + this.getToStringIdentity() + "]";
        }
        return (this.isConcurrent ? "Concurrent" : "") + (this.isScope ? "Scope" : "") + "Execution[" + this.getToStringIdentity() + "]";
    }

    protected String getToStringIdentity() {
        return this.id;
    }

    @Override
    public String getVariableScopeKey() {
        return "execution";
    }

    @Override
    public AbstractVariableScope getParentVariableScope() {
        return this.getParent();
    }

    @Override
    public void setVariable(final String variableName, final Object value, final String targetActivityId) {
        final String activityId = this.getActivityId();
        if (activityId != null && activityId.equals(targetActivityId)) {
            this.setVariableLocal(variableName, value);
        } else {
            final PvmExecutionImpl executionForFlowScope = this.findExecutionForFlowScope(targetActivityId);
            if (executionForFlowScope != null) {
                executionForFlowScope.setVariableLocal(variableName, value);
            }
        }
    }

    protected PvmExecutionImpl findExecutionForFlowScope(final String targetScopeId) {
        EnsureUtil.ensureNotNull("target scope id", (Object) targetScopeId);
        final ScopeImpl currentActivity = this.getActivity();
        EnsureUtil.ensureNotNull("activity of current execution", currentActivity);
        final FlowScopeWalker walker = new FlowScopeWalker(currentActivity);
        final ScopeImpl targetFlowScope = walker.walkUntil(new ReferenceWalker.WalkCondition<ScopeImpl>() {
            @Override
            public boolean isFulfilled(final ScopeImpl scope) {
                return scope == null || scope.getId().equals(targetScopeId);
            }
        });
        if (targetFlowScope == null) {
            throw PvmExecutionImpl.LOG.scopeNotFoundException(targetScopeId, this.getId());
        }
        return this.findExecutionForFlowScope(targetFlowScope);
    }

    public long getSequenceCounter() {
        return this.sequenceCounter;
    }

    public void setSequenceCounter(final long sequenceCounter) {
        this.sequenceCounter = sequenceCounter;
    }

    public void incrementSequenceCounter() {
        ++this.sequenceCounter;
    }

    public boolean isExternallyTerminated() {
        return this.externallyTerminated;
    }

    public void setExternallyTerminated(final boolean externallyTerminated) {
        this.externallyTerminated = externallyTerminated;
    }

    public String getDeleteReason() {
        return this.deleteReason;
    }

    public void setDeleteReason(final String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public boolean isDeleteRoot() {
        return this.deleteRoot;
    }

    public void setDeleteRoot(final boolean deleteRoot) {
        this.deleteRoot = deleteRoot;
    }

    @Override
    public TransitionImpl getTransition() {
        return this.transition;
    }

    public List<PvmTransition> getTransitionsToTake() {
        return this.transitionsToTake;
    }

    public void setTransitionsToTake(final List<PvmTransition> transitionsToTake) {
        this.transitionsToTake = transitionsToTake;
    }

    @Override
    public String getCurrentTransitionId() {
        final TransitionImpl transition = this.getTransition();
        if (transition != null) {
            return transition.getId();
        }
        return null;
    }

    public void setTransition(final PvmTransition transition) {
        this.transition = (TransitionImpl) transition;
    }

    @Override
    public boolean isConcurrent() {
        return this.isConcurrent;
    }

    @Override
    public void setConcurrent(final boolean isConcurrent) {
        this.isConcurrent = isConcurrent;
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }

    @Override
    public void setActive(final boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public void setEnded(final boolean isEnded) {
        this.isEnded = isEnded;
    }

    @Override
    public boolean isEnded() {
        return this.isEnded;
    }

    @Override
    public boolean isCanceled() {
        return ActivityInstanceState.CANCELED.getStateCode() == this.activityInstanceState;
    }

    public void setCanceled(final boolean canceled) {
        if (canceled) {
            this.activityInstanceState = ActivityInstanceState.CANCELED.getStateCode();
        }
    }

    @Override
    public boolean isCompleteScope() {
        return ActivityInstanceState.SCOPE_COMPLETE.getStateCode() == this.activityInstanceState;
    }

    public void setCompleteScope(final boolean completeScope) {
        if (completeScope && !this.isCanceled()) {
            this.activityInstanceState = ActivityInstanceState.SCOPE_COMPLETE.getStateCode();
        }
    }

    public void setPreserveScope(final boolean preserveScope) {
        this.preserveScope = preserveScope;
    }

    public boolean isPreserveScope() {
        return this.preserveScope;
    }

    public int getActivityInstanceState() {
        return this.activityInstanceState;
    }

    public boolean isInState(final ActivityInstanceState state) {
        return this.activityInstanceState == state.getStateCode();
    }

    public boolean hasFailedOnEndListeners() {
        return this.activityInstanceEndListenersFailed;
    }

    public boolean isEventScope() {
        return this.isEventScope;
    }

    public void setEventScope(final boolean isEventScope) {
        this.isEventScope = isEventScope;
    }

    public ScopeInstantiationContext getScopeInstantiationContext() {
        return this.scopeInstantiationContext;
    }

    public void disposeScopeInstantiationContext() {
        this.scopeInstantiationContext = null;
        PvmExecutionImpl parent = this;
        while ((parent = parent.getParent()) != null && parent.scopeInstantiationContext != null) {
            parent.scopeInstantiationContext = null;
        }
    }

    @Override
    public PvmActivity getNextActivity() {
        return this.nextActivity;
    }

    @Override
    public boolean isProcessInstanceExecution() {
        return this.getParent() == null;
    }

    public void setStartContext(final ScopeInstantiationContext startContext) {
        this.scopeInstantiationContext = startContext;
    }

    @Override
    public void setIgnoreAsync(final boolean ignoreAsync) {
        this.ignoreAsync = ignoreAsync;
    }

    public boolean isIgnoreAsync() {
        return this.ignoreAsync;
    }

    public void setStarting(final boolean isStarting) {
        this.isStarting = isStarting;
    }

    public boolean isStarting() {
        return this.isStarting;
    }

    public boolean isProcessInstanceStarting() {
        return this.getProcessInstance().isStarting();
    }

    public void setProcessInstanceStarting(final boolean starting) {
        this.getProcessInstance().setStarting(starting);
    }

    public void setNextActivity(final PvmActivity nextActivity) {
        this.nextActivity = nextActivity;
    }

    public PvmExecutionImpl getParentScopeExecution(final boolean considerSuperExecution) {
        if (this.isProcessInstanceExecution()) {
            if (!considerSuperExecution || this.getSuperExecution() == null) {
                return null;
            }
            final PvmExecutionImpl superExecution = this.getSuperExecution();
            if (superExecution.isScope()) {
                return superExecution;
            }
            return superExecution.getParent();
        } else {
            final PvmExecutionImpl parent = this.getParent();
            if (parent.isScope()) {
                return parent;
            }
            return parent.getParent();
        }
    }

    public void delayEvent(final PvmExecutionImpl targetScope, final VariableEvent variableEvent) {
        final DelayedVariableEvent delayedVariableEvent = new DelayedVariableEvent(targetScope, variableEvent);
        this.delayEvent(delayedVariableEvent);
    }

    public void delayEvent(final DelayedVariableEvent delayedVariableEvent) {
        final Boolean hasConditionalEvents = this.getProcessDefinition().getProperties().get(BpmnProperties.HAS_CONDITIONAL_EVENTS);
        if (hasConditionalEvents == null || !hasConditionalEvents.equals(Boolean.TRUE)) {
            return;
        }
        if (this.isProcessInstanceExecution()) {
            this.delayedEvents.add(delayedVariableEvent);
        } else {
            this.getProcessInstance().delayEvent(delayedVariableEvent);
        }
    }

    public List<DelayedVariableEvent> getDelayedEvents() {
        if (this.isProcessInstanceExecution()) {
            return this.delayedEvents;
        }
        return this.getProcessInstance().getDelayedEvents();
    }

    public void clearDelayedEvents() {
        if (this.isProcessInstanceExecution()) {
            this.delayedEvents.clear();
        } else {
            this.getProcessInstance().clearDelayedEvents();
        }
    }

    public void dispatchDelayedEventsAndPerformOperation(final PvmAtomicOperation atomicOperation) {
        this.dispatchDelayedEventsAndPerformOperation(new Callback<PvmExecutionImpl, Void>() {
            @Override
            public Void callback(final PvmExecutionImpl param) {
                param.performOperation(atomicOperation);
                return null;
            }
        });
    }

    public void dispatchDelayedEventsAndPerformOperation(final Callback<PvmExecutionImpl, Void> continuation) {
        final PvmExecutionImpl execution = this;
        if (execution.getDelayedEvents().isEmpty()) {
            this.continueExecutionIfNotCanceled(continuation, execution);
            return;
        }
        this.continueIfExecutionDoesNotAffectNextOperation(new Callback<PvmExecutionImpl, Void>() {
            @Override
            public Void callback(final PvmExecutionImpl execution) {
                PvmExecutionImpl.this.dispatchScopeEvents(execution);
                return null;
            }
        }, new Callback<PvmExecutionImpl, Void>() {
            @Override
            public Void callback(final PvmExecutionImpl execution) {
                PvmExecutionImpl.this.continueExecutionIfNotCanceled(continuation, execution);
                return null;
            }
        }, execution);
    }

    public void continueIfExecutionDoesNotAffectNextOperation(final Callback<PvmExecutionImpl, Void> dispatching, final Callback<PvmExecutionImpl, Void> continuation, PvmExecutionImpl execution) {
        final String lastActivityId = execution.getActivityId();
        final String lastActivityInstanceId = this.getActivityInstanceId(execution);
        dispatching.callback(execution);
        execution = ((execution.getReplacedBy() != null) ? execution.getReplacedBy() : execution);
        final String currentActivityInstanceId = this.getActivityInstanceId(execution);
        final String currentActivityId = execution.getActivityId();
        if (!execution.isCanceled() && this.isOnSameActivity(lastActivityInstanceId, lastActivityId, currentActivityInstanceId, currentActivityId)) {
            continuation.callback(execution);
        }
    }

    protected void continueExecutionIfNotCanceled(final Callback<PvmExecutionImpl, Void> continuation, final PvmExecutionImpl execution) {
        if (continuation != null && !execution.isCanceled()) {
            continuation.callback(execution);
        }
    }

    protected void dispatchScopeEvents(final PvmExecutionImpl execution) {
        final PvmExecutionImpl scopeExecution = execution.isScope() ? execution : execution.getParent();
        final List<DelayedVariableEvent> delayedEvents = new ArrayList<DelayedVariableEvent>(scopeExecution.getDelayedEvents());
        scopeExecution.clearDelayedEvents();
        final Map<PvmExecutionImpl, String> activityInstanceIds = new HashMap<PvmExecutionImpl, String>();
        final Map<PvmExecutionImpl, String> activityIds = new HashMap<PvmExecutionImpl, String>();
        this.initActivityIds(delayedEvents, activityInstanceIds, activityIds);
        for (final DelayedVariableEvent event : delayedEvents) {
            final PvmExecutionImpl targetScope = event.getTargetScope();
            final PvmExecutionImpl replaced = (targetScope.getReplacedBy() != null) ? targetScope.getReplacedBy() : targetScope;
            this.dispatchOnSameActivity(targetScope, replaced, activityIds, activityInstanceIds, event);
        }
    }

    protected void initActivityIds(final List<DelayedVariableEvent> delayedEvents, final Map<PvmExecutionImpl, String> activityInstanceIds, final Map<PvmExecutionImpl, String> activityIds) {
        for (final DelayedVariableEvent event : delayedEvents) {
            final PvmExecutionImpl targetScope = event.getTargetScope();
            final String targetScopeActivityInstanceId = this.getActivityInstanceId(targetScope);
            activityInstanceIds.put(targetScope, targetScopeActivityInstanceId);
            activityIds.put(targetScope, targetScope.getActivityId());
        }
    }

    private void dispatchOnSameActivity(final PvmExecutionImpl targetScope, final PvmExecutionImpl replacedBy, final Map<PvmExecutionImpl, String> activityIds, final Map<PvmExecutionImpl, String> activityInstanceIds, final DelayedVariableEvent delayedVariableEvent) {
        String currentActivityInstanceId = this.getActivityInstanceId(targetScope);
        String currentActivityId = targetScope.getActivityId();
        final String lastActivityInstanceId = activityInstanceIds.get(targetScope);
        final String lastActivityId = activityIds.get(targetScope);
        boolean onSameAct = this.isOnSameActivity(lastActivityInstanceId, lastActivityId, currentActivityInstanceId, currentActivityId);
        if (targetScope != replacedBy && !onSameAct) {
            currentActivityInstanceId = this.getActivityInstanceId(replacedBy);
            currentActivityId = replacedBy.getActivityId();
            onSameAct = this.isOnSameActivity(lastActivityInstanceId, lastActivityId, currentActivityInstanceId, currentActivityId);
        }
        if (onSameAct && this.isOnDispatchableState(targetScope)) {
            targetScope.dispatchEvent(delayedVariableEvent.getEvent());
        }
    }

    private boolean isOnDispatchableState(final PvmExecutionImpl targetScope) {
        final ActivityImpl targetActivity = targetScope.getActivity();
        return targetScope.getActivityId() == null || !targetActivity.isScope() || targetScope.isInState(ActivityInstanceState.DEFAULT);
    }

    private boolean isOnSameActivity(final String lastActivityInstanceId, final String lastActivityId, final String currentActivityInstanceId, final String currentActivityId) {
        return (lastActivityInstanceId == null && lastActivityInstanceId == currentActivityInstanceId && lastActivityId.equals(currentActivityId)) || (lastActivityInstanceId != null && lastActivityInstanceId.equals(currentActivityInstanceId) && (lastActivityId == null || lastActivityId.equals(currentActivityId)));
    }

    private String getActivityInstanceId(final PvmExecutionImpl targetScope) {
        if (targetScope.isConcurrent()) {
            return targetScope.getActivityInstanceId();
        }
        final ActivityImpl targetActivity = targetScope.getActivity();
        if (targetActivity != null && targetActivity.getActivities().isEmpty()) {
            return targetScope.getActivityInstanceId();
        }
        return targetScope.getParentActivityInstanceId();
    }

    @Override
    public Incident createIncident(final String incidentType, final String configuration) {
        return this.createIncident(incidentType, configuration, null);
    }

    @Override
    public Incident createIncident(final String incidentType, final String configuration, final String message) {
        final IncidentContext incidentContext = this.createIncidentContext(configuration);
        return IncidentHandling.createIncident(incidentType, incidentContext, message);
    }

    protected IncidentContext createIncidentContext(final String configuration) {
        final IncidentContext incidentContext = new IncidentContext();
        incidentContext.setTenantId(this.getTenantId());
        incidentContext.setProcessDefinitionId(this.getProcessDefinitionId());
        incidentContext.setExecutionId(this.getId());
        incidentContext.setActivityId(this.getActivityId());
        incidentContext.setConfiguration(configuration);
        return incidentContext;
    }

    @Override
    public void resolveIncident(final String incidentId) {
        final IncidentEntity incident = (IncidentEntity) Context.getCommandContext().getIncidentManager().findIncidentById(incidentId);
        final IncidentContext incidentContext = new IncidentContext(incident);
        IncidentHandling.removeIncidents(incident.getIncidentType(), incidentContext, true);
    }

    public IncidentHandler findIncidentHandler(final String incidentType) {
        final Map<String, IncidentHandler> incidentHandlers = Context.getProcessEngineConfiguration().getIncidentHandlers();
        return incidentHandlers.get(incidentType);
    }

    public boolean isExecutingScopeLeafActivity() {
        return this.isActive && this.getActivity() != null && this.getActivity().isScope() && this.activityInstanceId != null && !(this.getActivity().getActivityBehavior() instanceof CompositeActivityBehavior);
    }

    public boolean isAsyncAfterScopeWithoutTransition() {
        return this.activityInstanceId == null && this.activity.isScope() && !this.isActive;
    }

    static {
        LOG = ProcessEngineLogger.PVM_LOGGER;
    }
}
