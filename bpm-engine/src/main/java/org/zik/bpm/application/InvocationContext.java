// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;

public class InvocationContext
{
    protected final BaseDelegateExecution execution;
    
    public InvocationContext(final BaseDelegateExecution execution) {
        this.execution = execution;
    }
    
    public BaseDelegateExecution getExecution() {
        return this.execution;
    }
    
    @Override
    public String toString() {
        return "InvocationContext [execution=" + this.execution + "]";
    }
}
