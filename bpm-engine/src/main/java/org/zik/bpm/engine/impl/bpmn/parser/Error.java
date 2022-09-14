// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.parser;

import org.zik.bpm.engine.impl.core.variable.mapping.value.ParameterValueProvider;

public class Error
{
    protected String id;
    protected String errorCode;
    private ParameterValueProvider errorMessageExpression;
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getErrorCode() {
        return this.errorCode;
    }
    
    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }
    
    public ParameterValueProvider getErrorMessageExpression() {
        return this.errorMessageExpression;
    }
    
    public void setErrorMessageExpression(final ParameterValueProvider errorMessageExpression) {
        this.errorMessageExpression = errorMessageExpression;
    }
}
