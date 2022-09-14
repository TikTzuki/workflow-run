// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;
import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;
import org.zik.bpm.engine.impl.util.ActivityBehaviorUtil;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;

public abstract class AbstractAtomicOperationCaseExecutionResume extends AbstractCmmnEventAtomicOperation
{
    @Override
    protected CmmnExecution eventNotificationsStarted(final CmmnExecution execution) {
        final CmmnActivityBehavior behavior = ActivityBehaviorUtil.getActivityBehavior(execution);
        this.triggerBehavior(behavior, execution);
        final CaseExecutionState newState = this.getPreviousState(execution);
        execution.setCurrentState(newState);
        return execution;
    }
    
    @Override
    protected void postTransitionNotification(final CmmnExecution execution) {
        final CmmnActivityBehavior behavior = ActivityBehaviorUtil.getActivityBehavior(execution);
        behavior.resumed(execution);
    }
    
    protected CaseExecutionState getPreviousState(final CmmnExecution execution) {
        return execution.getPreviousState();
    }
    
    protected abstract void triggerBehavior(final CmmnActivityBehavior p0, final CmmnExecution p1);
}
