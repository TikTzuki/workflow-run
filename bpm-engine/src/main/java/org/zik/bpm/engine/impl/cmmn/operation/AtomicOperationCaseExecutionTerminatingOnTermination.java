// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.operation;

import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;

public class AtomicOperationCaseExecutionTerminatingOnTermination extends AbstractAtomicOperationCaseExecutionTerminating
{
    @Override
    public String getCanonicalName() {
        return "case-execution-terminating-on-termination";
    }
    
    @Override
    protected void triggerBehavior(final CmmnActivityBehavior behavior, final CmmnExecution execution) {
        behavior.onTermination(execution);
    }
    
    @Override
    protected CaseExecutionState getTerminatingState() {
        return CaseExecutionState.TERMINATING_ON_TERMINATION;
    }
}
