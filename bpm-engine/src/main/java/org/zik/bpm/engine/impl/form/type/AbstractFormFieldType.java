// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.type;

import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.form.FormType;

public abstract class AbstractFormFieldType implements FormType
{
    @Override
    public abstract String getName();
    
    public abstract TypedValue convertToFormValue(final TypedValue p0);
    
    public abstract TypedValue convertToModelValue(final TypedValue p0);
    
    @Deprecated
    public abstract Object convertFormValueToModelValue(final Object p0);
    
    @Deprecated
    public abstract String convertModelValueToFormValue(final Object p0);
    
    @Override
    public Object getInformation(final String key) {
        return null;
    }
}
