// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.parser;

import javax.script.ScriptException;
import org.zik.bpm.engine.ProcessEngineException;
import java.util.Comparator;
import java.io.Serializable;

public class ErrorEventDefinition implements Serializable
{
    public static Comparator<ErrorEventDefinition> comparator;
    private static final long serialVersionUID = 1L;
    protected final String handlerActivityId;
    protected String errorCode;
    protected Integer precedence;
    protected String errorCodeVariable;
    protected String errorMessageVariable;
    
    public ErrorEventDefinition(final String handlerActivityId) {
        this.precedence = 0;
        this.handlerActivityId = handlerActivityId;
    }
    
    public String getErrorCode() {
        return this.errorCode;
    }
    
    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getHandlerActivityId() {
        return this.handlerActivityId;
    }
    
    public Integer getPrecedence() {
        return (int)this.precedence + ((this.errorCode != null) ? 1 : 0);
    }
    
    public void setPrecedence(final Integer precedence) {
        this.precedence = precedence;
    }
    
    public boolean catchesError(final String errorCode) {
        return this.errorCode == null || this.errorCode.equals(errorCode);
    }
    
    public boolean catchesException(Exception ex) {
        if (this.errorCode == null) {
            return false;
        }
        while ((ex instanceof ProcessEngineException || ex instanceof ScriptException) && ex.getCause() != null) {
            ex = (Exception)ex.getCause();
        }
        Class<?> exceptionClass = ex.getClass();
        while (!this.errorCode.equals(exceptionClass.getName())) {
            exceptionClass = exceptionClass.getSuperclass();
            if (exceptionClass == null) {
                return false;
            }
        }
        return true;
    }
    
    public void setErrorCodeVariable(final String errorCodeVariable) {
        this.errorCodeVariable = errorCodeVariable;
    }
    
    public String getErrorCodeVariable() {
        return this.errorCodeVariable;
    }
    
    public void setErrorMessageVariable(final String errorMessageVariable) {
        this.errorMessageVariable = errorMessageVariable;
    }
    
    public String getErrorMessageVariable() {
        return this.errorMessageVariable;
    }
    
    static {
        ErrorEventDefinition.comparator = new Comparator<ErrorEventDefinition>() {
            @Override
            public int compare(final ErrorEventDefinition o1, final ErrorEventDefinition o2) {
                return o2.getPrecedence().compareTo(o1.getPrecedence());
            }
        };
    }
}
