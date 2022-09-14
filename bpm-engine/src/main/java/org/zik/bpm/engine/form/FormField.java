// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.form;

import java.util.Map;
import java.util.List;
import org.camunda.bpm.engine.variable.value.TypedValue;

public interface FormField
{
    String getId();
    
    String getLabel();
    
    FormType getType();
    
    String getTypeName();
    
    @Deprecated
    Object getDefaultValue();
    
    TypedValue getValue();
    
    List<FormFieldValidationConstraint> getValidationConstraints();
    
    Map<String, String> getProperties();
    
    boolean isBusinessKey();
}
