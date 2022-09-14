// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.cfg.CommandChecker;
import java.util.Iterator;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.form.FormField;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Collection;

public class GetStartFormVariablesCmd extends AbstractGetFormVariablesCmd
{
    private static final long serialVersionUID = 1L;
    
    public GetStartFormVariablesCmd(final String resourceId, final Collection<String> formVariableNames, final boolean deserializeObjectValues) {
        super(resourceId, formVariableNames, deserializeObjectValues);
    }
    
    @Override
    public VariableMap execute(final CommandContext commandContext) {
        final StartFormData startFormData = commandContext.runWithoutAuthorization((Command<StartFormData>)new GetStartFormCmd(this.resourceId));
        final ProcessDefinition definition = startFormData.getProcessDefinition();
        this.checkGetStartFormVariables((ProcessDefinitionEntity)definition, commandContext);
        final VariableMap result = (VariableMap)new VariableMapImpl();
        for (final FormField formField : startFormData.getFormFields()) {
            if (this.formVariableNames == null || this.formVariableNames.contains(formField.getId())) {
                result.put((Object)formField.getId(), (Object)this.createVariable(formField, null));
            }
        }
        return result;
    }
    
    protected void checkGetStartFormVariables(final ProcessDefinitionEntity definition, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadProcessDefinition(definition);
        }
    }
}
