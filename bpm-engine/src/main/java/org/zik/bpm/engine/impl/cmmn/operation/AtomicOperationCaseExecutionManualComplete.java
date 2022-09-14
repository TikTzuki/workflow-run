// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.operation;

import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;

public class AtomicOperationCaseExecutionManualComplete extends AbstractAtomicOperationCaseExecutionComplete
{
    @Override
    public String getCanonicalName() {
        return "case-execution-manual-complete";
    }
    
    @Override
    protected void triggerBehavior(final CmmnActivityBehavior behavior, final CmmnExecution execution) {
        behavior.onManualCompletion(execution);
    }
}
