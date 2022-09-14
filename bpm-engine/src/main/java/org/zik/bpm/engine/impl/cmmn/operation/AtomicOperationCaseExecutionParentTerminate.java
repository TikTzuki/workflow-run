// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.operation;

public class AtomicOperationCaseExecutionParentTerminate extends AbstractAtomicOperationCaseExecutionTerminate
{
    @Override
    public String getCanonicalName() {
        return "case-execution-parent-terminate";
    }
    
    @Override
    protected String getEventName() {
        return "parentTerminate";
    }
}
