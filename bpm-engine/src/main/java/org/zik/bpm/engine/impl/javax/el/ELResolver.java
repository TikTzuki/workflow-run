// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.javax.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

public abstract class ELResolver
{
    public static final String RESOLVABLE_AT_DESIGN_TIME = "resolvableAtDesignTime";
    public static final String TYPE = "type";
    
    public abstract Class<?> getCommonPropertyType(final ELContext p0, final Object p1);
    
    public abstract Iterator<FeatureDescriptor> getFeatureDescriptors(final ELContext p0, final Object p1);
    
    public abstract Class<?> getType(final ELContext p0, final Object p1, final Object p2);
    
    public abstract Object getValue(final ELContext p0, final Object p1, final Object p2);
    
    public abstract boolean isReadOnly(final ELContext p0, final Object p1, final Object p2);
    
    public abstract void setValue(final ELContext p0, final Object p1, final Object p2, final Object p3);
    
    public Object invoke(final ELContext context, final Object base, final Object method, final Class<?>[] paramTypes, final Object[] params) {
        return null;
    }
}
