// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.model;

import java.io.Serializable;

public class CmmnVariableOnPartDeclaration implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected String variableEvent;
    protected String variableName;
    
    public String getVariableEvent() {
        return this.variableEvent;
    }
    
    public void setVariableEvent(final String variableEvent) {
        this.variableEvent = variableEvent;
    }
    
    public String getVariableName() {
        return this.variableName;
    }
    
    public void setVariableName(final String variableName) {
        this.variableName = variableName;
    }
}
