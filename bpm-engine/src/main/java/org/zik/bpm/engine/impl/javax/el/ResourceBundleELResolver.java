// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.javax.el;

import java.util.MissingResourceException;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.beans.FeatureDescriptor;
import java.util.Iterator;

public class ResourceBundleELResolver extends ELResolver
{
    @Override
    public Class<?> getCommonPropertyType(final ELContext context, final Object base) {
        return this.isResolvable(base) ? String.class : null;
    }
    
    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(final ELContext context, final Object base) {
        if (this.isResolvable(base)) {
            final Enumeration<String> keys = ((ResourceBundle)base).getKeys();
            return new Iterator<FeatureDescriptor>() {
                @Override
                public boolean hasNext() {
                    return keys.hasMoreElements();
                }
                
                @Override
                public FeatureDescriptor next() {
                    final FeatureDescriptor feature = new FeatureDescriptor();
                    feature.setDisplayName(keys.nextElement());
                    feature.setName(feature.getDisplayName());
                    feature.setShortDescription("");
                    feature.setExpert(true);
                    feature.setHidden(false);
                    feature.setPreferred(true);
                    feature.setValue("type", String.class);
                    feature.setValue("resolvableAtDesignTime", true);
                    return feature;
                }
                
                @Override
                public void remove() {
                    throw new UnsupportedOperationException("Cannot remove");
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
        if (this.isResolvable(base)) {
            context.setPropertyResolved(true);
        }
        return null;
    }
    
    @Override
    public Object getValue(final ELContext context, final Object base, final Object property) {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        Object result = null;
        if (this.isResolvable(base)) {
            if (property != null) {
                try {
                    result = ((ResourceBundle)base).getObject(property.toString());
                }
                catch (MissingResourceException e) {
                    result = "???" + property + "???";
                }
            }
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
        return true;
    }
    
    @Override
    public void setValue(final ELContext context, final Object base, final Object property, final Object value) {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        if (this.isResolvable(base)) {
            throw new PropertyNotWritableException("resolver is read-only");
        }
    }
    
    private final boolean isResolvable(final Object base) {
        return base instanceof ResourceBundle;
    }
}
