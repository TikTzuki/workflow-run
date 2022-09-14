// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.type;

import org.camunda.bpm.engine.variable.value.TypedValue;

public abstract class SimpleFormFieldType extends AbstractFormFieldType
{
    @Override
    public TypedValue convertToFormValue(final TypedValue propertyValue) {
        return this.convertValue(propertyValue);
    }
    
    @Override
    public TypedValue convertToModelValue(final TypedValue propertyValue) {
        return this.convertValue(propertyValue);
    }
    
    protected abstract TypedValue convertValue(final TypedValue p0);
}
