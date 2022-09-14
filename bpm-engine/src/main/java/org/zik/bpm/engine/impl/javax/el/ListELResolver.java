// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.javax.el;

import java.util.List;
import java.beans.FeatureDescriptor;
import java.util.Iterator;

public class ListELResolver extends ELResolver
{
    private final boolean readOnly;
    
    public ListELResolver() {
        this(false);
    }
    
    public ListELResolver(final boolean readOnly) {
        this.readOnly = readOnly;
    }
    
    @Override
    public Class<?> getCommonPropertyType(final ELContext context, final Object base) {
        return isResolvable(base) ? Integer.class : null;
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
        if (isResolvable(base)) {
            toIndex((List<?>)base, property);
            result = Object.class;
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
        if (isResolvable(base)) {
            final int index = toIndex(null, property);
            final List<?> list = (List<?>)base;
            result = ((index < 0 || index >= list.size()) ? null : list.get(index));
            context.setPropertyResolved(true);
        }
        return result;
    }
    
    @Override
    public boolean isReadOnly(final ELContext context, final Object base, final Object property) {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        if (isResolvable(base)) {
            toIndex((List<?>)base, property);
            context.setPropertyResolved(true);
        }
        return this.readOnly;
    }
    
    @Override
    public void setValue(final ELContext context, final Object base, final Object property, final Object value) {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        if (isResolvable(base)) {
            if (this.readOnly) {
                throw new PropertyNotWritableException("resolver is read-only");
            }
            final List list = (List)base;
            final int index = toIndex(list, property);
            try {
                list.set(index, value);
            }
            catch (UnsupportedOperationException e) {
                throw new PropertyNotWritableException(e);
            }
            catch (ArrayStoreException e2) {
                throw new IllegalArgumentException(e2);
            }
            context.setPropertyResolved(true);
        }
    }
    
    private static final boolean isResolvable(final Object base) {
        return base instanceof List;
    }
    
    private static final int toIndex(final List<?> base, final Object property) {
        int index = 0;
        Label_0140: {
            if (property instanceof Number) {
                index = ((Number)property).intValue();
            }
            else {
                if (property instanceof String) {
                    try {
                        index = Integer.valueOf((String)property);
                        break Label_0140;
                    }
                    catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Cannot parse list index: " + property);
                    }
                }
                if (property instanceof Character) {
                    index = (char)property;
                }
                else {
                    if (!(property instanceof Boolean)) {
                        throw new IllegalArgumentException("Cannot coerce property to list index: " + property);
                    }
                    index = (((boolean)property) ? 1 : 0);
                }
            }
        }
        if (base != null && (index < 0 || index >= base.size())) {
            throw new PropertyNotFoundException("List index out of bounds: " + index);
        }
        return index;
    }
}
