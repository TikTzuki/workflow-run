// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;

public class AtomicOperationCaseExecutionParentComplete extends AbstractAtomicOperationCaseExecutionTerminate
{
    @Override
    public String getCanonicalName() {
        return "case-execution-parent-complete";
    }
    
    @Override
    protected String getEventName() {
        return "parentComplete";
    }
    
    @Override
    protected void eventNotificationsCompleted(final CmmnExecution execution) {
        execution.remove();
    }
}
