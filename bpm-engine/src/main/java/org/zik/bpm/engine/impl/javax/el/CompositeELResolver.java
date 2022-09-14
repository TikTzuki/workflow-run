// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.javax.el;

import java.util.Collections;
import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class CompositeELResolver extends ELResolver
{
    private final List<ELResolver> resolvers;
    
    public CompositeELResolver() {
        this.resolvers = new ArrayList<ELResolver>();
    }
    
    public void add(final ELResolver elResolver) {
        if (elResolver == null) {
            throw new NullPointerException("resolver must not be null");
        }
        this.resolvers.add(elResolver);
    }
    
    @Override
    public Class<?> getCommonPropertyType(final ELContext context, final Object base) {
        Class<?> result = null;
        for (final ELResolver resolver : this.resolvers) {
            final Class<?> type = resolver.getCommonPropertyType(context, base);
            if (type != null) {
                if (result == null || type.isAssignableFrom(result)) {
                    result = type;
                }
                else {
                    if (result.isAssignableFrom(type)) {
                        continue;
                    }
                    result = Object.class;
                }
            }
        }
        return result;
    }
    
    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(final ELContext context, final Object base) {
        return new Iterator<FeatureDescriptor>() {
            Iterator<FeatureDescriptor> empty = Collections.emptyList().iterator();
            Iterator<ELResolver> resolvers = CompositeELResolver.this.resolvers.iterator();
            Iterator<FeatureDescriptor> features = this.empty;
            
            Iterator<FeatureDescriptor> features() {
                while (!this.features.hasNext() && this.resolvers.hasNext()) {
                    this.features = this.resolvers.next().getFeatureDescriptors(context, base);
                    if (this.features == null) {
                        this.features = this.empty;
                    }
                }
                return this.features;
            }
            
            @Override
            public boolean hasNext() {
                return this.features().hasNext();
            }
            
            @Override
            public FeatureDescriptor next() {
                return this.features().next();
            }
            
            @Override
            public void remove() {
                this.features().remove();
            }
        };
    }
    
    @Override
    public Class<?> getType(final ELContext context, final Object base, final Object property) {
        context.setPropertyResolved(false);
        for (final ELResolver resolver : this.resolvers) {
            final Class<?> type = resolver.getType(context, base, property);
            if (context.isPropertyResolved()) {
                return type;
            }
        }
        return null;
    }
    
    @Override
    public Object getValue(final ELContext context, final Object base, final Object property) {
        context.setPropertyResolved(false);
        for (final ELResolver resolver : this.resolvers) {
            final Object value = resolver.getValue(context, base, property);
            if (context.isPropertyResolved()) {
                return value;
            }
        }
        return null;
    }
    
    @Override
    public boolean isReadOnly(final ELContext context, final Object base, final Object property) {
        context.setPropertyResolved(false);
        for (final ELResolver resolver : this.resolvers) {
            final boolean readOnly = resolver.isReadOnly(context, base, property);
            if (context.isPropertyResolved()) {
                return readOnly;
            }
        }
        return false;
    }
    
    @Override
    public void setValue(final ELContext context, final Object base, final Object property, final Object value) {
        context.setPropertyResolved(false);
        for (final ELResolver resolver : this.resolvers) {
            resolver.setValue(context, base, property, value);
            if (context.isPropertyResolved()) {
                return;
            }
        }
    }
    
    @Override
    public Object invoke(final ELContext context, final Object base, final Object method, final Class<?>[] paramTypes, final Object[] params) {
        context.setPropertyResolved(false);
        for (final ELResolver resolver : this.resolvers) {
            final Object result = resolver.invoke(context, base, method, paramTypes, params);
            if (context.isPropertyResolved()) {
                return result;
            }
        }
        return null;
    }
}
