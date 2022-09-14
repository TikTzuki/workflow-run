// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.identity;

import java.io.Serializable;

public class IdentityOperationResult
{
    public static final String OPERATION_CREATE = "create";
    public static final String OPERATION_UPDATE = "update";
    public static final String OPERATION_DELETE = "delete";
    public static final String OPERATION_UNLOCK = "unlock";
    public static final String OPERATION_NONE = "none";
    protected Serializable value;
    protected String operation;
    
    public IdentityOperationResult(final Serializable value, final String operation) {
        this.value = value;
        this.operation = operation;
    }
    
    public Serializable getValue() {
        return this.value;
    }
    
    public void setValue(final Serializable value) {
        this.value = value;
    }
    
    public String getOperation() {
        return this.operation;
    }
    
    public void setOperation(final String operation) {
        this.operation = operation;
    }
}
