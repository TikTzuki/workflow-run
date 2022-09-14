// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.delegate;

public interface CaseVariableListener extends VariableListener<DelegateCaseVariableInstance>
{
    public static final String CREATE = "create";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";
    
    void notify(final DelegateCaseVariableInstance p0) throws Exception;
}
