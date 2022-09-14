// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.el;

import java.util.Collections;
import java.beans.FeatureDescriptor;
import java.util.Iterator;
import org.zik.bpm.engine.impl.javax.el.ELContext;
import org.zik.bpm.engine.impl.javax.el.ELResolver;

public abstract class AbstractElResolverDelegate extends ELResolver
{
    protected abstract ELResolver getElResolverDelegate();
    
    @Override
    public Class<?> getCommonPropertyType(final ELContext context, final Object base) {
        final ELResolver delegate = this.getElResolverDelegate();
        if (delegate == null) {
            return null;
        }
        return delegate.getCommonPropertyType(context, base);
    }
    
    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(final ELContext context, final Object base) {
        final ELResolver delegate = this.getElResolverDelegate();
        if (delegate == null) {
            return Collections.emptySet().iterator();
        }
        return delegate.getFeatureDescriptors(context, base);
    }
    
    @Override
    public Class<?> getType(final ELContext context, final Object base, final Object property) {
        context.setPropertyResolved(false);
        final ELResolver delegate = this.getElResolverDelegate();
        if (delegate == null) {
            return null;
        }
        return delegate.getType(context, base, property);
    }
    
    @Override
    public Object getValue(final ELContext context, final Object base, final Object property) {
        context.setPropertyResolved(false);
        final ELResolver delegate = this.getElResolverDelegate();
        if (delegate == null) {
            return null;
        }
        return delegate.getValue(context, base, property);
    }
    
    @Override
    public boolean isReadOnly(final ELContext context, final Object base, final Object property) {
        context.setPropertyResolved(false);
        final ELResolver delegate = this.getElResolverDelegate();
        return delegate == null || delegate.isReadOnly(context, base, property);
    }
    
    @Override
    public void setValue(final ELContext context, final Object base, final Object property, final Object value) {
        context.setPropertyResolved(false);
        final ELResolver delegate = this.getElResolverDelegate();
        if (delegate != null) {
            delegate.setValue(context, base, property, value);
        }
    }
    
    @Override
    public Object invoke(final ELContext context, final Object base, final Object method, final Class<?>[] paramTypes, final Object[] params) {
        context.setPropertyResolved(false);
        final ELResolver delegate = this.getElResolverDelegate();
        if (delegate == null) {
            return null;
        }
        return delegate.invoke(context, base, method, paramTypes, params);
    }
}
