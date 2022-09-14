// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnCompositeActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;
import org.zik.bpm.engine.impl.util.ActivityBehaviorUtil;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;

public class AtomicOperationCaseExecutionDisable extends AbstractCmmnEventAtomicOperation
{
    @Override
    public String getCanonicalName() {
        return "case-execution-disable";
    }
    
    @Override
    protected String getEventName() {
        return "disable";
    }
    
    @Override
    protected CmmnExecution eventNotificationsStarted(final CmmnExecution execution) {
        final CmmnActivityBehavior behavior = ActivityBehaviorUtil.getActivityBehavior(execution);
        behavior.onDisable(execution);
        execution.setCurrentState(CaseExecutionState.DISABLED);
        return execution;
    }
    
    @Override
    protected void preTransitionNotification(final CmmnExecution execution) {
        final CmmnExecution parent = execution.getParent();
        if (parent != null) {
            final CmmnActivityBehavior behavior = ActivityBehaviorUtil.getActivityBehavior(parent);
            if (behavior instanceof CmmnCompositeActivityBehavior) {
                final CmmnCompositeActivityBehavior compositeBehavior = (CmmnCompositeActivityBehavior)behavior;
                compositeBehavior.handleChildDisabled(parent, execution);
            }
        }
    }
}
