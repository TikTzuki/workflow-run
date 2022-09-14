// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.delegate;

public interface VariableListener<T extends DelegateVariableInstance<?>>
{
    public static final String CREATE = "create";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";
    
    void notify(final T p0) throws Exception;
}
