// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.util.ActivityBehaviorUtil;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.util.Iterator;
import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;
import java.util.List;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;

public class StageActivityBehavior extends StageOrTaskActivityBehavior implements CmmnCompositeActivityBehavior
{
    protected static final CmmnBehaviorLogger LOG;
    
    @Override
    protected void performStart(final CmmnActivityExecution execution) {
        final CmmnActivity activity = execution.getActivity();
        final List<CmmnActivity> childActivities = activity.getActivities();
        if (childActivities != null && !childActivities.isEmpty()) {
            final List<CmmnExecution> children = execution.createChildExecutions(childActivities);
            execution.createSentryParts();
            execution.triggerChildExecutionsLifecycle(children);
            if (execution.isActive()) {
                execution.fireIfOnlySentryParts();
                if (execution.isActive()) {
                    this.checkAndCompleteCaseExecution(execution);
                }
            }
        }
        else {
            execution.complete();
        }
    }
    
    @Override
    public void onReactivation(final CmmnActivityExecution execution) {
        final String id = execution.getId();
        if (execution.isActive()) {
            throw StageActivityBehavior.LOG.alreadyActiveException("reactivate", id);
        }
        if (execution.isCaseInstanceExecution()) {
            if (execution.isClosed()) {
                throw StageActivityBehavior.LOG.alreadyClosedCaseException("reactivate", id);
            }
        }
        else {
            this.ensureTransitionAllowed(execution, CaseExecutionState.FAILED, CaseExecutionState.ACTIVE, "reactivate");
        }
    }
    
    @Override
    public void reactivated(final CmmnActivityExecution execution) {
        if (execution.isCaseInstanceExecution()) {
            final CaseExecutionState previousState = execution.getPreviousState();
            if (CaseExecutionState.SUSPENDED.equals(previousState)) {
                this.resumed(execution);
            }
        }
    }
    
    @Override
    public void onCompletion(final CmmnActivityExecution execution) {
        this.ensureTransitionAllowed(execution, CaseExecutionState.ACTIVE, CaseExecutionState.COMPLETED, "complete");
        this.canComplete(execution, true);
        this.completing(execution);
    }
    
    @Override
    public void onManualCompletion(final CmmnActivityExecution execution) {
        this.ensureTransitionAllowed(execution, CaseExecutionState.ACTIVE, CaseExecutionState.COMPLETED, "complete");
        this.canComplete(execution, true, true);
        this.completing(execution);
    }
    
    @Override
    protected void completing(final CmmnActivityExecution execution) {
        final List<? extends CmmnExecution> children = execution.getCaseExecutions();
        for (final CmmnExecution child : children) {
            if (!child.isDisabled()) {
                child.parentComplete();
            }
            else {
                child.remove();
            }
        }
    }
    
    protected boolean canComplete(final CmmnActivityExecution execution) {
        return this.canComplete(execution, false);
    }
    
    protected boolean canComplete(final CmmnActivityExecution execution, final boolean throwException) {
        final boolean autoComplete = this.evaluateAutoComplete(execution);
        return this.canComplete(execution, throwException, autoComplete);
    }
    
    protected boolean canComplete(final CmmnActivityExecution execution, final boolean throwException, final boolean autoComplete) {
        final String id = execution.getId();
        final List<? extends CmmnExecution> children = execution.getCaseExecutions();
        if (children == null || children.isEmpty()) {
            return true;
        }
        for (final CmmnExecution child : children) {
            if (child.isNew() || child.isActive()) {
                if (throwException) {
                    throw StageActivityBehavior.LOG.remainingChildException("complete", id, child.getId(), CaseExecutionState.ACTIVE);
                }
                return false;
            }
        }
        if (autoComplete) {
            for (final CmmnExecution child : children) {
                if (child.isRequired() && !child.isDisabled() && !child.isCompleted() && !child.isTerminated()) {
                    if (throwException) {
                        throw StageActivityBehavior.LOG.remainingChildException("complete", id, child.getId(), child.getCurrentState());
                    }
                    return false;
                }
            }
        }
        else {
            for (final CmmnExecution child : children) {
                if (!child.isDisabled() && !child.isCompleted() && !child.isTerminated()) {
                    if (throwException) {
                        throw StageActivityBehavior.LOG.wrongChildStateException("complete", id, child.getId(), "[available|enabled|suspended]");
                    }
                    return false;
                }
            }
        }
        return true;
    }
    
    protected boolean evaluateAutoComplete(final CmmnActivityExecution execution) {
        final CmmnActivity activity = this.getActivity(execution);
        final Object autoCompleteProperty = activity.getProperty("autoComplete");
        if (autoCompleteProperty != null) {
            final String message = "Property autoComplete expression returns non-Boolean: " + autoCompleteProperty + " (" + autoCompleteProperty.getClass().getName() + ")";
            EnsureUtil.ensureInstanceOf(message, "autoComplete", autoCompleteProperty, Boolean.class);
            return (boolean)autoCompleteProperty;
        }
        return false;
    }
    
    protected boolean isAbleToTerminate(final CmmnActivityExecution execution) {
        final List<? extends CmmnExecution> children = execution.getCaseExecutions();
        if (children != null && !children.isEmpty()) {
            for (final CmmnExecution child : children) {
                if (!child.isTerminated() && !child.isCompleted()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    protected void performTerminate(final CmmnActivityExecution execution) {
        if (!this.isAbleToTerminate(execution)) {
            this.terminateChildren(execution);
        }
        else {
            super.performTerminate(execution);
        }
    }
    
    @Override
    protected void performExit(final CmmnActivityExecution execution) {
        if (!this.isAbleToTerminate(execution)) {
            this.terminateChildren(execution);
        }
        else {
            super.performExit(execution);
        }
    }
    
    protected void terminateChildren(final CmmnActivityExecution execution) {
        final List<? extends CmmnExecution> children = execution.getCaseExecutions();
        for (final CmmnExecution child : children) {
            this.terminateChild(child);
        }
    }
    
    protected void terminateChild(final CmmnExecution child) {
        final CmmnActivityBehavior behavior = ActivityBehaviorUtil.getActivityBehavior(child);
        if (!child.isTerminated() && !child.isCompleted()) {
            if (behavior instanceof StageOrTaskActivityBehavior) {
                child.exit();
            }
            else {
                child.parentTerminate();
            }
        }
    }
    
    @Override
    protected void performSuspension(final CmmnActivityExecution execution) {
        if (!this.isAbleToSuspend(execution)) {
            this.suspendChildren(execution);
        }
        else {
            super.performSuspension(execution);
        }
    }
    
    @Override
    protected void performParentSuspension(final CmmnActivityExecution execution) {
        if (!this.isAbleToSuspend(execution)) {
            this.suspendChildren(execution);
        }
        else {
            super.performParentSuspension(execution);
        }
    }
    
    protected void suspendChildren(final CmmnActivityExecution execution) {
        final List<? extends CmmnExecution> children = execution.getCaseExecutions();
        if (children != null && !children.isEmpty()) {
            for (final CmmnExecution child : children) {
                final CmmnActivityBehavior behavior = ActivityBehaviorUtil.getActivityBehavior(child);
                if (!child.isTerminated() && !child.isSuspended()) {
                    if (behavior instanceof StageOrTaskActivityBehavior) {
                        child.parentSuspend();
                    }
                    else {
                        child.suspend();
                    }
                }
            }
        }
    }
    
    protected boolean isAbleToSuspend(final CmmnActivityExecution execution) {
        final List<? extends CmmnExecution> children = execution.getCaseExecutions();
        if (children != null && !children.isEmpty()) {
            for (final CmmnExecution child : children) {
                if (!child.isSuspended()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public void resumed(final CmmnActivityExecution execution) {
        if (execution.isAvailable()) {
            this.created(execution);
        }
        else if (execution.isActive()) {
            this.resumeChildren(execution);
        }
    }
    
    protected void resumeChildren(final CmmnActivityExecution execution) {
        final List<? extends CmmnExecution> children = execution.getCaseExecutions();
        if (children != null && !children.isEmpty()) {
            for (final CmmnExecution child : children) {
                final CmmnActivityBehavior behavior = ActivityBehaviorUtil.getActivityBehavior(child);
                if (!child.isTerminated()) {
                    if (behavior instanceof StageOrTaskActivityBehavior) {
                        child.parentResume();
                    }
                    else {
                        child.resume();
                    }
                }
            }
        }
    }
    
    @Override
    protected boolean isAtLeastOneEntryCriterionSatisfied(final CmmnActivityExecution execution) {
        return !execution.isCaseInstanceExecution() && super.isAtLeastOneEntryCriterionSatisfied(execution);
    }
    
    @Override
    public void fireExitCriteria(final CmmnActivityExecution execution) {
        if (!execution.isCaseInstanceExecution()) {
            execution.exit();
        }
        else {
            execution.terminate();
        }
    }
    
    @Override
    public void fireEntryCriteria(final CmmnActivityExecution execution) {
        if (!execution.isCaseInstanceExecution()) {
            super.fireEntryCriteria(execution);
            return;
        }
        throw StageActivityBehavior.LOG.criteriaNotAllowedForCaseInstanceException("entry", execution.getId());
    }
    
    @Override
    public void handleChildCompletion(final CmmnActivityExecution execution, final CmmnActivityExecution child) {
        this.fireForceUpdate(execution);
        if (execution.isActive()) {
            this.checkAndCompleteCaseExecution(execution);
        }
    }
    
    @Override
    public void handleChildDisabled(final CmmnActivityExecution execution, final CmmnActivityExecution child) {
        this.fireForceUpdate(execution);
        if (execution.isActive()) {
            this.checkAndCompleteCaseExecution(execution);
        }
    }
    
    @Override
    public void handleChildSuspension(final CmmnActivityExecution execution, final CmmnActivityExecution child) {
        if (execution.isSuspending() && this.isAbleToSuspend(execution)) {
            final String id = execution.getId();
            final CaseExecutionState currentState = execution.getCurrentState();
            if (CaseExecutionState.SUSPENDING_ON_SUSPENSION.equals(currentState)) {
                execution.performSuspension();
            }
            else {
                if (!CaseExecutionState.SUSPENDING_ON_PARENT_SUSPENSION.equals(currentState)) {
                    throw StageActivityBehavior.LOG.suspendCaseException(id, currentState);
                }
                execution.performParentSuspension();
            }
        }
    }
    
    @Override
    public void handleChildTermination(final CmmnActivityExecution execution, final CmmnActivityExecution child) {
        this.fireForceUpdate(execution);
        if (execution.isActive()) {
            this.checkAndCompleteCaseExecution(execution);
        }
        else if (execution.isTerminating() && this.isAbleToTerminate(execution)) {
            final String id = execution.getId();
            final CaseExecutionState currentState = execution.getCurrentState();
            if (CaseExecutionState.TERMINATING_ON_TERMINATION.equals(currentState)) {
                execution.performTerminate();
            }
            else if (CaseExecutionState.TERMINATING_ON_EXIT.equals(currentState)) {
                execution.performExit();
            }
            else {
                if (CaseExecutionState.TERMINATING_ON_PARENT_TERMINATION.equals(currentState)) {
                    throw StageActivityBehavior.LOG.illegalStateTransitionException("parentTerminate", id, this.getTypeName());
                }
                throw StageActivityBehavior.LOG.terminateCaseException(id, currentState);
            }
        }
    }
    
    protected void checkAndCompleteCaseExecution(final CmmnActivityExecution execution) {
        if (this.canComplete(execution)) {
            execution.complete();
        }
    }
    
    protected void fireForceUpdate(final CmmnActivityExecution execution) {
        if (execution instanceof CaseExecutionEntity) {
            final CaseExecutionEntity entity = (CaseExecutionEntity)execution;
            entity.forceUpdate();
        }
    }
    
    @Override
    protected String getTypeName() {
        return "stage";
    }
    
    static {
        LOG = ProcessEngineLogger.CMNN_BEHAVIOR_LOGGER;
    }
}
