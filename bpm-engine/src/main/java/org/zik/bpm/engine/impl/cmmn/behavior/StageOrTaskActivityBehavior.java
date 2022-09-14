// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.zik.bpm.engine.impl.cmmn.CaseControlRule;
import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;

public abstract class StageOrTaskActivityBehavior extends PlanItemDefinitionActivityBehavior
{
    protected static final CmmnBehaviorLogger LOG;
    
    @Override
    protected void creating(final CmmnActivityExecution execution) {
        this.evaluateRequiredRule(execution);
    }
    
    @Override
    public void created(final CmmnActivityExecution execution) {
        if (execution.isAvailable() && this.isAtLeastOneEntryCriterionSatisfied(execution)) {
            this.fireEntryCriteria(execution);
        }
    }
    
    @Override
    public void onEnable(final CmmnActivityExecution execution) {
        this.ensureNotCaseInstance(execution, "enable");
        this.ensureTransitionAllowed(execution, CaseExecutionState.AVAILABLE, CaseExecutionState.ENABLED, "enable");
    }
    
    @Override
    public void onReenable(final CmmnActivityExecution execution) {
        this.ensureNotCaseInstance(execution, "re-enable");
        this.ensureTransitionAllowed(execution, CaseExecutionState.DISABLED, CaseExecutionState.ENABLED, "re-enable");
    }
    
    @Override
    public void onDisable(final CmmnActivityExecution execution) {
        this.ensureNotCaseInstance(execution, "disable");
        this.ensureTransitionAllowed(execution, CaseExecutionState.ENABLED, CaseExecutionState.DISABLED, "disable");
    }
    
    @Override
    public void onStart(final CmmnActivityExecution execution) {
        this.ensureNotCaseInstance(execution, "start");
        this.ensureTransitionAllowed(execution, CaseExecutionState.AVAILABLE, CaseExecutionState.ACTIVE, "start");
    }
    
    @Override
    public void onManualStart(final CmmnActivityExecution execution) {
        this.ensureNotCaseInstance(execution, "manualStart");
        this.ensureTransitionAllowed(execution, CaseExecutionState.ENABLED, CaseExecutionState.ACTIVE, "start");
    }
    
    @Override
    public void started(final CmmnActivityExecution execution) {
        if (execution.isActive()) {
            this.performStart(execution);
        }
    }
    
    protected abstract void performStart(final CmmnActivityExecution p0);
    
    @Override
    public void onCompletion(final CmmnActivityExecution execution) {
        this.ensureTransitionAllowed(execution, CaseExecutionState.ACTIVE, CaseExecutionState.COMPLETED, "complete");
        this.completing(execution);
    }
    
    @Override
    public void onManualCompletion(final CmmnActivityExecution execution) {
        this.ensureTransitionAllowed(execution, CaseExecutionState.ACTIVE, CaseExecutionState.COMPLETED, "complete");
        this.manualCompleting(execution);
    }
    
    @Override
    public void onTermination(final CmmnActivityExecution execution) {
        this.ensureTransitionAllowed(execution, CaseExecutionState.ACTIVE, CaseExecutionState.TERMINATED, "terminate");
        this.performTerminate(execution);
    }
    
    @Override
    public void onParentTermination(final CmmnActivityExecution execution) {
        final String id = execution.getId();
        throw StageOrTaskActivityBehavior.LOG.illegalStateTransitionException("parentTerminate", id, this.getTypeName());
    }
    
    @Override
    public void onExit(final CmmnActivityExecution execution) {
        final String id = execution.getId();
        if (execution.isTerminated()) {
            throw StageOrTaskActivityBehavior.LOG.alreadyTerminatedException("exit", id);
        }
        if (execution.isCompleted()) {
            throw StageOrTaskActivityBehavior.LOG.wrongCaseStateException("exit", id, "[available|enabled|disabled|active|failed|suspended]", "completed");
        }
        this.performExit(execution);
    }
    
    @Override
    public void onSuspension(final CmmnActivityExecution execution) {
        this.ensureTransitionAllowed(execution, CaseExecutionState.ACTIVE, CaseExecutionState.SUSPENDED, "suspend");
        this.performSuspension(execution);
    }
    
    @Override
    public void onParentSuspension(final CmmnActivityExecution execution) {
        this.ensureNotCaseInstance(execution, "parentSuspension");
        final String id = execution.getId();
        if (execution.isSuspended()) {
            throw StageOrTaskActivityBehavior.LOG.alreadySuspendedException("parentSuspend", id);
        }
        if (execution.isCompleted() || execution.isTerminated()) {
            throw StageOrTaskActivityBehavior.LOG.wrongCaseStateException("parentSuspend", id, "suspend", "[available|enabled|disabled|active]", execution.getCurrentState().toString());
        }
        this.performParentSuspension(execution);
    }
    
    @Override
    public void onResume(final CmmnActivityExecution execution) {
        this.ensureNotCaseInstance(execution, "resume");
        this.ensureTransitionAllowed(execution, CaseExecutionState.SUSPENDED, CaseExecutionState.ACTIVE, "resume");
        final CmmnActivityExecution parent = execution.getParent();
        if (parent != null && !parent.isActive()) {
            final String id = execution.getId();
            throw StageOrTaskActivityBehavior.LOG.resumeInactiveCaseException("resume", id);
        }
        this.resuming(execution);
    }
    
    @Override
    public void onParentResume(final CmmnActivityExecution execution) {
        this.ensureNotCaseInstance(execution, "parentResume");
        final String id = execution.getId();
        if (!execution.isSuspended()) {
            throw StageOrTaskActivityBehavior.LOG.wrongCaseStateException("parentResume", id, "resume", "suspended", execution.getCurrentState().toString());
        }
        final CmmnActivityExecution parent = execution.getParent();
        if (parent != null && !parent.isActive()) {
            throw StageOrTaskActivityBehavior.LOG.resumeInactiveCaseException("parentResume", id);
        }
        this.resuming(execution);
    }
    
    @Override
    public void onOccur(final CmmnActivityExecution execution) {
        final String id = execution.getId();
        throw StageOrTaskActivityBehavior.LOG.illegalStateTransitionException("occur", id, this.getTypeName());
    }
    
    @Override
    public void fireEntryCriteria(final CmmnActivityExecution execution) {
        final boolean manualActivation = this.evaluateManualActivationRule(execution);
        if (manualActivation) {
            execution.enable();
        }
        else {
            execution.start();
        }
    }
    
    protected boolean evaluateManualActivationRule(final CmmnActivityExecution execution) {
        boolean manualActivation = false;
        final CmmnActivity activity = execution.getActivity();
        final Object manualActivationRule = activity.getProperty("manualActivationRule");
        if (manualActivationRule != null) {
            final CaseControlRule rule = (CaseControlRule)manualActivationRule;
            manualActivation = rule.evaluate(execution);
        }
        return manualActivation;
    }
    
    protected abstract String getTypeName();
    
    static {
        LOG = ProcessEngineLogger.CMNN_BEHAVIOR_LOGGER;
    }
}
