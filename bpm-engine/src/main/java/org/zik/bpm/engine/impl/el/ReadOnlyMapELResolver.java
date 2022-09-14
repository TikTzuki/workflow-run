// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.javax.el.ELContext;
import java.util.Map;
import org.zik.bpm.engine.impl.javax.el.ELResolver;

public class ReadOnlyMapELResolver extends ELResolver
{
    protected Map<Object, Object> wrappedMap;
    
    public ReadOnlyMapELResolver(final Map<Object, Object> map) {
        this.wrappedMap = map;
    }
    
    @Override
    public Object getValue(final ELContext context, final Object base, final Object property) {
        if (base == null && this.wrappedMap.containsKey(property)) {
            context.setPropertyResolved(true);
            return this.wrappedMap.get(property);
        }
        return null;
    }
    
    @Override
    public boolean isReadOnly(final ELContext context, final Object base, final Object property) {
        return true;
    }
    
    @Override
    public void setValue(final ELContext context, final Object base, final Object property, final Object value) {
        if (base == null && this.wrappedMap.containsKey(property)) {
            throw new ProcessEngineException("Cannot set value of '" + property + "', it's readonly!");
        }
    }
    
    @Override
    public Class<?> getCommonPropertyType(final ELContext context, final Object arg) {
        return Object.class;
    }
    
    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(final ELContext context, final Object arg) {
        return null;
    }
    
    @Override
    public Class<?> getType(final ELContext context, final Object arg1, final Object arg2) {
        return Object.class;
    }
}
