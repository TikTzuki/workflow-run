// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.operation;

import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionState;

public class AtomicOperationCaseExecutionSuspendingOnSuspension extends AbstractAtomicOperationCaseExecutionSuspending
{
    @Override
    public String getCanonicalName() {
        return "case-execution-suspending-on-suspension";
    }
    
    @Override
    protected CaseExecutionState getSuspendingState() {
        return CaseExecutionState.SUSPENDING_ON_SUSPENSION;
    }
    
    @Override
    protected void triggerBehavior(final CmmnActivityBehavior behavior, final CmmnExecution execution) {
        behavior.onSuspension(execution);
    }
}
