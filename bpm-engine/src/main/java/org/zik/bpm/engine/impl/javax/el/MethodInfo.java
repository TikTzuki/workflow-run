// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.javax.el;

public class MethodInfo
{
    private final String name;
    private final Class<?> returnType;
    private final Class<?>[] paramTypes;
    
    public MethodInfo(final String name, final Class<?> returnType, final Class<?>[] paramTypes) {
        this.name = name;
        this.returnType = returnType;
        this.paramTypes = paramTypes;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Class<?>[] getParamTypes() {
        return this.paramTypes;
    }
    
    public Class<?> getReturnType() {
        return this.returnType;
    }
}
