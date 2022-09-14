// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.operation;

public class AtomicOperationCaseExecutionExit extends AbstractAtomicOperationCaseExecutionTerminate
{
    @Override
    public String getCanonicalName() {
        return "case-execution-exit";
    }
    
    @Override
    protected String getEventName() {
        return "exit";
    }
}
