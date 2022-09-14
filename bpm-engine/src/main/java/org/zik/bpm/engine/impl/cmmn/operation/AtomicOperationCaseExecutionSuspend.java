// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.operation;

public class AtomicOperationCaseExecutionSuspend extends AbstractAtomicOperationCaseExecutionSuspend
{
    @Override
    public String getCanonicalName() {
        return "case-execution-suspend";
    }
    
    @Override
    protected String getEventName() {
        return "suspend";
    }
}
