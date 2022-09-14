// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.delegate;

import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.ProcessEngineException;

public class BpmnError extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    private String errorCode;
    private String errorMessage;
    
    public BpmnError(final String errorCode) {
        super(exceptionMessage(errorCode, null));
        this.setErrorCode(errorCode);
    }
    
    public BpmnError(final String errorCode, final String message) {
        super(exceptionMessage(errorCode, message));
        this.setErrorCode(errorCode);
        this.setMessage(message);
    }
    
    public BpmnError(final String errorCode, final String message, final Throwable cause) {
        super(exceptionMessage(errorCode, message), cause);
        this.setErrorCode(errorCode);
        this.setMessage(message);
    }
    
    public BpmnError(final String errorCode, final Throwable cause) {
        super(exceptionMessage(errorCode, null), cause);
        this.setErrorCode(errorCode);
    }
    
    private static String exceptionMessage(final String errorCode, final String message) {
        if (message == null) {
            return "";
        }
        return message + " (errorCode='" + errorCode + "')";
    }
    
    protected void setErrorCode(final String errorCode) {
        EnsureUtil.ensureNotEmpty("Error Code", errorCode);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return this.errorCode;
    }
    
    @Override
    public String toString() {
        return super.toString() + " (errorCode='" + this.errorCode + "')";
    }
    
    protected void setMessage(final String errorMessage) {
        EnsureUtil.ensureNotEmpty("Error Message", errorMessage);
        this.errorMessage = errorMessage;
    }
    
    @Override
    public String getMessage() {
        return this.errorMessage;
    }
}
