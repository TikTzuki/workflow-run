// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.PropertyNotWritableException;
import org.zik.bpm.engine.impl.javax.el.PropertyNotFoundException;
import java.beans.FeatureDescriptor;
import java.util.Iterator;
import org.zik.bpm.engine.impl.javax.el.ELContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.zik.bpm.engine.impl.javax.el.ELResolver;

public class RootPropertyResolver extends ELResolver
{
    private final Map<String, Object> map;
    private final boolean readOnly;
    
    public RootPropertyResolver() {
        this(false);
    }
    
    public RootPropertyResolver(final boolean readOnly) {
        this.map = Collections.synchronizedMap(new HashMap<String, Object>());
        this.readOnly = readOnly;
    }
    
    private boolean isResolvable(final Object base) {
        return base == null;
    }
    
    private boolean resolve(final ELContext context, final Object base, final Object property) {
        context.setPropertyResolved(this.isResolvable(base) && property instanceof String);
        return context.isPropertyResolved();
    }
    
    @Override
    public Class<?> getCommonPropertyType(final ELContext context, final Object base) {
        return this.isResolvable(context) ? String.class : null;
    }
    
    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(final ELContext context, final Object base) {
        return null;
    }
    
    @Override
    public Class<?> getType(final ELContext context, final Object base, final Object property) {
        return this.resolve(context, base, property) ? Object.class : null;
    }
    
    @Override
    public Object getValue(final ELContext context, final Object base, final Object property) {
        if (!this.resolve(context, base, property)) {
            return null;
        }
        if (!this.isProperty((String)property)) {
            throw new PropertyNotFoundException("Cannot find property " + property);
        }
        return this.getProperty((String)property);
    }
    
    @Override
    public boolean isReadOnly(final ELContext context, final Object base, final Object property) {
        return this.resolve(context, base, property) && this.readOnly;
    }
    
    @Override
    public void setValue(final ELContext context, final Object base, final Object property, final Object value) throws PropertyNotWritableException {
        if (this.resolve(context, base, property)) {
            if (this.readOnly) {
                throw new PropertyNotWritableException("Resolver is read only!");
            }
            this.setProperty((String)property, value);
        }
    }
    
    @Override
    public Object invoke(final ELContext context, final Object base, final Object method, final Class<?>[] paramTypes, final Object[] params) {
        if (this.resolve(context, base, method)) {
            throw new NullPointerException("Cannot invoke method " + method + " on null");
        }
        return null;
    }
    
    public Object getProperty(final String property) {
        return this.map.get(property);
    }
    
    public void setProperty(final String property, final Object value) {
        this.map.put(property, value);
    }
    
    public boolean isProperty(final String property) {
        return this.map.containsKey(property);
    }
    
    public Iterable<String> properties() {
        return this.map.keySet();
    }
}
