// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.javax.el;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class ELContext
{
    private final Map<Class<?>, Object> context;
    private Locale locale;
    private boolean resolved;
    
    public ELContext() {
        this.context = new HashMap<Class<?>, Object>();
    }
    
    public Object getContext(final Class<?> key) {
        return this.context.get(key);
    }
    
    public abstract ELResolver getELResolver();
    
    public abstract FunctionMapper getFunctionMapper();
    
    public Locale getLocale() {
        return this.locale;
    }
    
    public abstract VariableMapper getVariableMapper();
    
    public boolean isPropertyResolved() {
        return this.resolved;
    }
    
    public void putContext(final Class<?> key, final Object contextObject) {
        this.context.put(key, contextObject);
    }
    
    public void setLocale(final Locale locale) {
        this.locale = locale;
    }
    
    public void setPropertyResolved(final boolean resolved) {
        this.resolved = resolved;
    }
}
