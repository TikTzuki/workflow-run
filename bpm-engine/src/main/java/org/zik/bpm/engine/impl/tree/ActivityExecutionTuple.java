// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.tree;

import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class ActivityExecutionTuple
{
    private final ActivityExecution execution;
    private final PvmScope scope;
    
    public ActivityExecutionTuple(final PvmScope scope, final ActivityExecution execution) {
        this.execution = execution;
        this.scope = scope;
    }
    
    public ActivityExecution getExecution() {
        return this.execution;
    }
    
    public PvmScope getScope() {
        return this.scope;
    }
}
