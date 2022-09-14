// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.BeanELResolver;
import org.zik.bpm.engine.impl.javax.el.ResourceBundleELResolver;
import org.zik.bpm.engine.impl.javax.el.MapELResolver;
import org.zik.bpm.engine.impl.javax.el.ListELResolver;
import org.zik.bpm.engine.impl.javax.el.ArrayELResolver;
import java.beans.FeatureDescriptor;
import java.util.Iterator;
import org.zik.bpm.engine.impl.javax.el.ELContext;
import org.zik.bpm.engine.impl.javax.el.CompositeELResolver;
import org.zik.bpm.engine.impl.javax.el.ELResolver;

public class SimpleResolver extends ELResolver
{
    private static final ELResolver DEFAULT_RESOLVER_READ_ONLY;
    private static final ELResolver DEFAULT_RESOLVER_READ_WRITE;
    private final RootPropertyResolver root;
    private final CompositeELResolver delegate;
    
    public SimpleResolver(final ELResolver resolver, final boolean readOnly) {
        (this.delegate = new CompositeELResolver()).add(this.root = new RootPropertyResolver(readOnly));
        this.delegate.add(resolver);
    }
    
    public SimpleResolver(final ELResolver resolver) {
        this(resolver, false);
    }
    
    public SimpleResolver(final boolean readOnly) {
        this(readOnly ? SimpleResolver.DEFAULT_RESOLVER_READ_ONLY : SimpleResolver.DEFAULT_RESOLVER_READ_WRITE, readOnly);
    }
    
    public SimpleResolver() {
        this(SimpleResolver.DEFAULT_RESOLVER_READ_WRITE, false);
    }
    
    public RootPropertyResolver getRootPropertyResolver() {
        return this.root;
    }
    
    @Override
    public Class<?> getCommonPropertyType(final ELContext context, final Object base) {
        return this.delegate.getCommonPropertyType(context, base);
    }
    
    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(final ELContext context, final Object base) {
        return this.delegate.getFeatureDescriptors(context, base);
    }
    
    @Override
    public Class<?> getType(final ELContext context, final Object base, final Object property) {
        return this.delegate.getType(context, base, property);
    }
    
    @Override
    public Object getValue(final ELContext context, final Object base, final Object property) {
        return this.delegate.getValue(context, base, property);
    }
    
    @Override
    public boolean isReadOnly(final ELContext context, final Object base, final Object property) {
        return this.delegate.isReadOnly(context, base, property);
    }
    
    @Override
    public void setValue(final ELContext context, final Object base, final Object property, final Object value) {
        this.delegate.setValue(context, base, property, value);
    }
    
    @Override
    public Object invoke(final ELContext context, final Object base, final Object method, final Class<?>[] paramTypes, final Object[] params) {
        return this.delegate.invoke(context, base, method, paramTypes, params);
    }
    
    static {
        DEFAULT_RESOLVER_READ_ONLY = new CompositeELResolver() {
            {
                this.add(new ArrayELResolver(true));
                this.add(new ListELResolver(true));
                this.add(new MapELResolver(true));
                this.add(new ResourceBundleELResolver());
                this.add(new BeanELResolver(true));
            }
        };
        DEFAULT_RESOLVER_READ_WRITE = new CompositeELResolver() {
            {
                this.add(new ArrayELResolver(false));
                this.add(new ListELResolver(false));
                this.add(new MapELResolver(false));
                this.add(new ResourceBundleELResolver());
                this.add(new BeanELResolver(false));
            }
        };
    }
}
