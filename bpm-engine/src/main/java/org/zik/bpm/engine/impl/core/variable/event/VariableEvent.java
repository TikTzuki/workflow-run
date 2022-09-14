// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.variable.event;

import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.zik.bpm.engine.runtime.VariableInstance;

public class VariableEvent
{
    protected VariableInstance variableInstance;
    protected String eventName;
    protected AbstractVariableScope sourceScope;
    
    public VariableEvent(final VariableInstance variableInstance, final String eventName, final AbstractVariableScope sourceScope) {
        this.variableInstance = variableInstance;
        this.eventName = eventName;
        this.sourceScope = sourceScope;
    }
    
    public VariableInstance getVariableInstance() {
        return this.variableInstance;
    }
    
    public String getEventName() {
        return this.eventName;
    }
    
    public AbstractVariableScope getSourceScope() {
        return this.sourceScope;
    }
}
