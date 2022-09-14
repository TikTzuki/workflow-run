// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.validator;

import java.util.Map;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.form.handler.FormFieldHandler;

public interface FormFieldValidatorContext
{
    FormFieldHandler getFormFieldHandler();
    
    @Deprecated
    DelegateExecution getExecution();
    
    VariableScope getVariableScope();
    
    String getConfiguration();
    
    Map<String, Object> getSubmittedValues();
}
