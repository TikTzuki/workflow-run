// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.javax.el;

import java.lang.reflect.Array;
import java.beans.FeatureDescriptor;
import java.util.Iterator;

public class ArrayELResolver extends ELResolver
{
    private final boolean readOnly;
    
    public ArrayELResolver() {
        this(false);
    }
    
    public ArrayELResolver(final boolean readOnly) {
        this.readOnly = readOnly;
    }
    
    @Override
    public Class<?> getCommonPropertyType(final ELContext context, final Object base) {
        return this.isResolvable(base) ? Integer.class : null;
    }
    
    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(final ELContext context, final Object base) {
        return null;
    }
    
    @Override
    public Class<?> getType(final ELContext context, final Object base, final Object property) {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        Class<?> result = null;
        if (this.isResolvable(base)) {
            this.toIndex(base, property);
            result = base.getClass().getComponentType();
            context.setPropertyResolved(true);
        }
        return result;
    }
    
    @Override
    public Object getValue(final ELContext context, final Object base, final Object property) {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        Object result = null;
        if (this.isResolvable(base)) {
            final int index = this.toIndex(null, property);
            result = ((index < 0 || index >= Array.getLength(base)) ? null : Array.get(base, index));
            context.setPropertyResolved(true);
        }
        return result;
    }
    
    @Override
    public boolean isReadOnly(final ELContext context, final Object base, final Object property) {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        if (this.isResolvable(base)) {
            this.toIndex(base, property);
            context.setPropertyResolved(true);
        }
        return this.readOnly;
    }
    
    @Override
    public void setValue(final ELContext context, final Object base, final Object property, final Object value) {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        if (this.isResolvable(base)) {
            if (this.readOnly) {
                throw new PropertyNotWritableException("resolver is read-only");
            }
            Array.set(base, this.toIndex(base, property), value);
            context.setPropertyResolved(true);
        }
    }
    
    private final boolean isResolvable(final Object base) {
        return base != null && base.getClass().isArray();
    }
    
    private final int toIndex(final Object base, final Object property) {
        int index = 0;
        Label_0141: {
            if (property instanceof Number) {
                index = ((Number)property).intValue();
            }
            else {
                if (property instanceof String) {
                    try {
                        index = Integer.valueOf((String)property);
                        break Label_0141;
                    }
                    catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Cannot parse array index: " + property);
                    }
                }
                if (property instanceof Character) {
                    index = (char)property;
                }
                else {
                    if (!(property instanceof Boolean)) {
                        throw new IllegalArgumentException("Cannot coerce property to array index: " + property);
                    }
                    index = (((boolean)property) ? 1 : 0);
                }
            }
        }
        if (base != null && (index < 0 || index >= Array.getLength(base))) {
            throw new PropertyNotFoundException("Array index out of bounds: " + index);
        }
        return index;
    }
}
