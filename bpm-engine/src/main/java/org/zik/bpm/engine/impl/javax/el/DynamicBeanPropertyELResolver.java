// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.javax.el;

import org.zik.bpm.engine.impl.util.ReflectUtil;
import java.beans.FeatureDescriptor;
import java.util.Iterator;

public class DynamicBeanPropertyELResolver extends ELResolver
{
    protected Class<?> subject;
    protected String readMethodName;
    protected String writeMethodName;
    protected boolean readOnly;
    
    public DynamicBeanPropertyELResolver(final boolean readOnly, final Class<?> subject, final String readMethodName, final String writeMethodName) {
        this.readOnly = readOnly;
        this.subject = subject;
        this.readMethodName = readMethodName;
        this.writeMethodName = writeMethodName;
    }
    
    public DynamicBeanPropertyELResolver(final Class<?> subject, final String readMethodName, final String writeMethodName) {
        this(false, subject, readMethodName, writeMethodName);
    }
    
    @Override
    public Class<?> getCommonPropertyType(final ELContext context, final Object base) {
        if (this.subject.isInstance(base)) {
            return Object.class;
        }
        return null;
    }
    
    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(final ELContext context, final Object base) {
        return null;
    }
    
    @Override
    public Class<?> getType(final ELContext context, final Object base, final Object property) {
        return Object.class;
    }
    
    @Override
    public Object getValue(final ELContext context, final Object base, final Object property) {
        if (base == null || this.getCommonPropertyType(context, base) == null) {
            return null;
        }
        final String propertyName = property.toString();
        try {
            final Object value = ReflectUtil.invoke(base, this.readMethodName, new Object[] { propertyName });
            context.setPropertyResolved(true);
            return value;
        }
        catch (Exception e) {
            throw new ELException(e);
        }
    }
    
    @Override
    public boolean isReadOnly(final ELContext context, final Object base, final Object property) {
        return this.readOnly;
    }
    
    @Override
    public void setValue(final ELContext context, final Object base, final Object property, final Object value) {
        if (base == null || this.getCommonPropertyType(context, base) == null) {
            return;
        }
        final String propertyName = property.toString();
        try {
            ReflectUtil.invoke(base, this.writeMethodName, new Object[] { propertyName, value });
            context.setPropertyResolved(true);
        }
        catch (Exception e) {
            throw new ELException(e);
        }
    }
}
