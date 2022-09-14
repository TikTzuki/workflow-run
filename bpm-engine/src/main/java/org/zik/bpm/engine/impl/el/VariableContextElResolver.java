// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.engine.variable.context.VariableContext;
import org.zik.bpm.engine.impl.javax.el.ELContext;
import org.zik.bpm.engine.impl.javax.el.ELResolver;

public class VariableContextElResolver extends ELResolver
{
    public static final String VAR_CTX_KEY = "variableContext";
    
    @Override
    public Object getValue(final ELContext context, final Object base, final Object property) {
        if (base == null) {
            final VariableContext variableContext = (VariableContext)context.getContext(VariableContext.class);
            if (variableContext != null) {
                if ("variableContext".equals(property)) {
                    context.setPropertyResolved(true);
                    return variableContext;
                }
                final TypedValue typedValue = variableContext.resolve((String)property);
                if (typedValue != null) {
                    context.setPropertyResolved(true);
                    return this.unpack(typedValue);
                }
            }
        }
        return null;
    }
    
    @Override
    public void setValue(final ELContext context, final Object base, final Object property, final Object value) {
    }
    
    @Override
    public boolean isReadOnly(final ELContext context, final Object base, final Object property) {
        return true;
    }
    
    @Override
    public Class<?> getCommonPropertyType(final ELContext arg0, final Object arg1) {
        return Object.class;
    }
    
    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(final ELContext arg0, final Object arg1) {
        return null;
    }
    
    @Override
    public Class<?> getType(final ELContext arg0, final Object arg1, final Object arg2) {
        return Object.class;
    }
    
    protected Object unpack(final TypedValue typedValue) {
        if (typedValue != null) {
            return typedValue.getValue();
        }
        return null;
    }
}
