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

public class AtomicOperationCaseExecutionEnable extends AbstractCmmnEventAtomicOperation
{
    @Override
    public String getCanonicalName() {
        return "case-execution-enable";
    }
    
    @Override
    protected String getEventName() {
        return "enable";
    }
    
    @Override
    protected CmmnExecution eventNotificationsStarted(final CmmnExecution execution) {
        final CmmnActivityBehavior behavior = ActivityBehaviorUtil.getActivityBehavior(execution);
        behavior.onEnable(execution);
        execution.setCurrentState(CaseExecutionState.ENABLED);
        return execution;
    }
}
