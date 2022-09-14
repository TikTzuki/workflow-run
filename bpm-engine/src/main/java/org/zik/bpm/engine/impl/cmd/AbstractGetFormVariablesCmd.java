// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.form.FormField;
import java.util.Collection;
import java.io.Serializable;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.interceptor.Command;

public abstract class AbstractGetFormVariablesCmd implements Command<VariableMap>, Serializable
{
    private static final long serialVersionUID = 1L;
    public String resourceId;
    public Collection<String> formVariableNames;
    protected boolean deserializeObjectValues;
    
    public AbstractGetFormVariablesCmd(final String resourceId, final Collection<String> formVariableNames, final boolean deserializeObjectValues) {
        this.resourceId = resourceId;
        this.formVariableNames = formVariableNames;
        this.deserializeObjectValues = deserializeObjectValues;
    }
    
    protected TypedValue createVariable(final FormField formField, final VariableScope variableScope) {
        final TypedValue value = formField.getValue();
        if (value != null) {
            return value;
        }
        return null;
    }
}
