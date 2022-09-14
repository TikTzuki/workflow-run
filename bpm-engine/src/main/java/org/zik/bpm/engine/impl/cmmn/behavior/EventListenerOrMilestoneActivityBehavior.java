// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.exception.cmmn.CaseIllegalStateTransitionException;
import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;

public abstract class EventListenerOrMilestoneActivityBehavior extends PlanItemDefinitionActivityBehavior
{
    protected static final CmmnBehaviorLogger LOG;
    
    @Override
    public void onEnable(final CmmnActivityExecution execution) {
        throw this.createIllegalStateTransitionException("enable", execution);
    }
    
    @Override
    public void onReenable(final CmmnActivityExecution execution) {
        throw this.createIllegalStateTransitionException("reenable", execution);
    }
    
    @Override
    public void onDisable(final CmmnActivityExecution execution) {
        throw this.createIllegalStateTransitionException("disable", execution);
    }
    
    @Override
    public void onStart(final CmmnActivityExecution execution) {
        throw this.createIllegalStateTransitionException("start", execution);
    }
    
    @Override
    public void onManualStart(final CmmnActivityExecution execution) {
        throw this.createIllegalStateTransitionException("manualStart", execution);
    }
    
    @Override
    public void onCompletion(final CmmnActivityExecution execution) {
        throw this.createIllegalStateTransitionException("complete", execution);
    }
    
    @Override
    public void onManualCompletion(final CmmnActivityExecution execution) {
        throw this.createIllegalStateTransitionException("complete", execution);
    }
    
    @Override
    public void onTermination(final CmmnActivityExecution execution) {
        this.ensureTransitionAllowed(execution, CaseExecutionState.AVAILABLE, CaseExecutionState.TERMINATED, "terminate");
        this.performTerminate(execution);
    }
    
    @Override
    public void onParentTermination(final CmmnActivityExecution execution) {
        if (execution.isCompleted()) {
            final String id = execution.getId();
            throw EventListenerOrMilestoneActivityBehavior.LOG.executionAlreadyCompletedException("parentTerminate", id);
        }
        this.performParentTerminate(execution);
    }
    
    @Override
    public void onExit(final CmmnActivityExecution execution) {
        throw this.createIllegalStateTransitionException("exit", execution);
    }
    
    @Override
    public void onOccur(final CmmnActivityExecution execution) {
        this.ensureTransitionAllowed(execution, CaseExecutionState.AVAILABLE, CaseExecutionState.COMPLETED, "occur");
    }
    
    @Override
    public void onSuspension(final CmmnActivityExecution execution) {
        this.ensureTransitionAllowed(execution, CaseExecutionState.AVAILABLE, CaseExecutionState.SUSPENDED, "suspend");
        this.performSuspension(execution);
    }
    
    @Override
    public void onParentSuspension(final CmmnActivityExecution execution) {
        throw this.createIllegalStateTransitionException("parentSuspend", execution);
    }
    
    @Override
    public void onResume(final CmmnActivityExecution execution) {
        this.ensureTransitionAllowed(execution, CaseExecutionState.SUSPENDED, CaseExecutionState.AVAILABLE, "resume");
        final CmmnActivityExecution parent = execution.getParent();
        if (parent != null && !parent.isActive()) {
            final String id = execution.getId();
            throw EventListenerOrMilestoneActivityBehavior.LOG.resumeInactiveCaseException("resume", id);
        }
        this.resuming(execution);
    }
    
    @Override
    public void onParentResume(final CmmnActivityExecution execution) {
        throw this.createIllegalStateTransitionException("parentResume", execution);
    }
    
    @Override
    public void onReactivation(final CmmnActivityExecution execution) {
        throw this.createIllegalStateTransitionException("reactivate", execution);
    }
    
    protected boolean isAtLeastOneExitCriterionSatisfied(final CmmnActivityExecution execution) {
        return false;
    }
    
    @Override
    public void fireExitCriteria(final CmmnActivityExecution execution) {
        throw EventListenerOrMilestoneActivityBehavior.LOG.criteriaNotAllowedForEventListenerOrMilestonesException("exit", execution.getId());
    }
    
    protected CaseIllegalStateTransitionException createIllegalStateTransitionException(final String transition, final CmmnActivityExecution execution) {
        final String id = execution.getId();
        return EventListenerOrMilestoneActivityBehavior.LOG.illegalStateTransitionException(transition, id, this.getTypeName());
    }
    
    protected abstract String getTypeName();
    
    static {
        LOG = ProcessEngineLogger.CMNN_BEHAVIOR_LOGGER;
    }
}
