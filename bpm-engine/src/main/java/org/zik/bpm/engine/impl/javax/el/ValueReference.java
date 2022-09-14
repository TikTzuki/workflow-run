// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.javax.el;

import java.io.Serializable;

public class ValueReference implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Object base;
    private Object property;
    
    public ValueReference(final Object base, final Object property) {
        this.base = base;
        this.property = property;
    }
    
    public Object getBase() {
        return this.base;
    }
    
    public Object getProperty() {
        return this.property;
    }
}
