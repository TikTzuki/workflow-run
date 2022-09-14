// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnCompositeActivityBehavior;
import org.zik.bpm.engine.impl.util.ActivityBehaviorUtil;
import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;

public abstract class AbstractAtomicOperationCaseExecutionTerminate extends AbstractCmmnEventAtomicOperation
{
    @Override
    protected CmmnExecution eventNotificationsStarted(final CmmnExecution execution) {
        execution.setCurrentState(CaseExecutionState.TERMINATED);
        return execution;
    }
    
    @Override
    protected void postTransitionNotification(final CmmnExecution execution) {
        if (!execution.isCaseInstanceExecution()) {
            execution.remove();
        }
        final CmmnExecution parent = execution.getParent();
        if (parent != null) {
            this.notifyParent(parent, execution);
        }
    }
    
    protected void notifyParent(final CmmnExecution parent, final CmmnExecution execution) {
        final CmmnActivityBehavior behavior = ActivityBehaviorUtil.getActivityBehavior(parent);
        if (behavior instanceof CmmnCompositeActivityBehavior) {
            final CmmnCompositeActivityBehavior compositeBehavior = (CmmnCompositeActivityBehavior)behavior;
            compositeBehavior.handleChildTermination(parent, execution);
        }
    }
}
