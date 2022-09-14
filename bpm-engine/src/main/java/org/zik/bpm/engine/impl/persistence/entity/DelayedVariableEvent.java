// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.core.variable.event.VariableEvent;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class DelayedVariableEvent
{
    protected PvmExecutionImpl targetScope;
    protected VariableEvent event;
    
    public DelayedVariableEvent(final PvmExecutionImpl targetScope, final VariableEvent event) {
        this.targetScope = targetScope;
        this.event = event;
    }
    
    public PvmExecutionImpl getTargetScope() {
        return this.targetScope;
    }
    
    public VariableEvent getEvent() {
        return this.event;
    }
}
