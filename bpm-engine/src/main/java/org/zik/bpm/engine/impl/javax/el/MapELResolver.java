// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.javax.el;

import java.util.Map;
import java.beans.FeatureDescriptor;
import java.util.Iterator;

public class MapELResolver extends ELResolver
{
    private final boolean readOnly;
    
    public MapELResolver() {
        this(false);
    }
    
    public MapELResolver(final boolean readOnly) {
        this.readOnly = readOnly;
    }
    
    @Override
    public Class<?> getCommonPropertyType(final ELContext context, final Object base) {
        return this.isResolvable(base) ? Object.class : null;
    }
    
    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(final ELContext context, final Object base) {
        if (this.isResolvable(base)) {
            final Map<?, ?> map = (Map<?, ?>)base;
            final Iterator<?> keys = map.keySet().iterator();
            return new Iterator<FeatureDescriptor>() {
                @Override
                public boolean hasNext() {
                    return keys.hasNext();
                }
                
                @Override
                public FeatureDescriptor next() {
                    final Object key = keys.next();
                    final FeatureDescriptor feature = new FeatureDescriptor();
                    feature.setDisplayName((key == null) ? "null" : key.toString());
                    feature.setName(feature.getDisplayName());
                    feature.setShortDescription("");
                    feature.setExpert(true);
                    feature.setHidden(false);
                    feature.setPreferred(true);
                    feature.setValue("type", (key == null) ? null : key.getClass());
                    feature.setValue("resolvableAtDesignTime", true);
                    return feature;
                }
                
                @Override
                public void remove() {
                    throw new UnsupportedOperationException("cannot remove");
                }
            };
        }
        return null;
    }
    
    @Override
    public Class<?> getType(final ELContext context, final Object base, final Object property) {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        Class<?> result = null;
        if (this.isResolvable(base)) {
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
        if (this.isResolvable(base)) {
            result = ((Map)base).get(property);
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
            ((Map)base).put(property, value);
            context.setPropertyResolved(true);
        }
    }
    
    private final boolean isResolvable(final Object base) {
        return base instanceof Map;
    }
}
