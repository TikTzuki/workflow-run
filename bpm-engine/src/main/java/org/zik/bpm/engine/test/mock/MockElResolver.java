// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.test.mock;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import org.zik.bpm.engine.impl.javax.el.ELContext;
import org.zik.bpm.engine.impl.javax.el.ELResolver;

public class MockElResolver extends ELResolver
{
    @Override
    public Class<?> getCommonPropertyType(final ELContext context, final Object base) {
        return Object.class;
    }
    
    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(final ELContext context, final Object base) {
        return null;
    }
    
    @Override
    public Class<?> getType(final ELContext context, final Object base, final Object property) {
        return null;
    }
    
    @Override
    public Object getValue(final ELContext context, final Object base, final Object property) {
        final Object bean = Mocks.get(property);
        if (bean != null) {
            context.setPropertyResolved(true);
        }
        return bean;
    }
    
    @Override
    public boolean isReadOnly(final ELContext context, final Object base, final Object property) {
        return false;
    }
    
    @Override
    public void setValue(final ELContext context, final Object base, final Object property, final Object value) {
    }
}
