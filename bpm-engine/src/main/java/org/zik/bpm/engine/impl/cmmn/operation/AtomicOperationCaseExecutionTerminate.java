// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.operation;

public class AtomicOperationCaseExecutionTerminate extends AbstractAtomicOperationCaseExecutionTerminate
{
    @Override
    public String getCanonicalName() {
        return "case-execution-terminate";
    }
    
    @Override
    protected String getEventName() {
        return "terminate";
    }
}
