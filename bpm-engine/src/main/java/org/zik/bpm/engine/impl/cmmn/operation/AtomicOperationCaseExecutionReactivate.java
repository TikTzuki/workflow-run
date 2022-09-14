// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;
import org.zik.bpm.engine.impl.util.ActivityBehaviorUtil;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;

public class AtomicOperationCaseExecutionReactivate extends AbstractCmmnEventAtomicOperation
{
    @Override
    public String getCanonicalName() {
        return "case-execution-re-activate";
    }
    
    @Override
    protected String getEventName() {
        return "reactivate";
    }
    
    @Override
    protected CmmnExecution eventNotificationsStarted(final CmmnExecution execution) {
        final CmmnActivityBehavior behavior = ActivityBehaviorUtil.getActivityBehavior(execution);
        behavior.onReactivation(execution);
        execution.setCurrentState(CaseExecutionState.ACTIVE);
        return execution;
    }
    
    @Override
    protected void postTransitionNotification(final CmmnExecution execution) {
        final CmmnActivityBehavior behavior = ActivityBehaviorUtil.getActivityBehavior(execution);
        behavior.reactivated(execution);
    }
}
