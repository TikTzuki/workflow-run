// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.handler;

import org.zik.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.impl.form.FormDefinition;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.form.FormDataImpl;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.form.CamundaFormRef;
import org.zik.bpm.engine.impl.form.CamundaFormRefImpl;
import org.zik.bpm.engine.impl.form.StartFormDataImpl;
import org.zik.bpm.engine.form.StartFormData;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;

public class DefaultStartFormHandler extends DefaultFormHandler implements StartFormHandler
{
    @Override
    public StartFormData createStartFormData(final ProcessDefinitionEntity processDefinition) {
        final StartFormDataImpl startFormData = new StartFormDataImpl();
        final FormDefinition startFormDefinition = processDefinition.getStartFormDefinition();
        final Expression formKey = startFormDefinition.getFormKey();
        final Expression camundaFormDefinitionKey = startFormDefinition.getCamundaFormDefinitionKey();
        final String camundaFormDefinitionBinding = startFormDefinition.getCamundaFormDefinitionBinding();
        final Expression camundaFormDefinitionVersion = startFormDefinition.getCamundaFormDefinitionVersion();
        if (formKey != null) {
            startFormData.setFormKey(formKey.getExpressionText());
        }
        else if (camundaFormDefinitionKey != null && camundaFormDefinitionBinding != null) {
            final CamundaFormRefImpl ref = new CamundaFormRefImpl(camundaFormDefinitionKey.getExpressionText(), camundaFormDefinitionBinding);
            if (camundaFormDefinitionBinding.equals("version") && camundaFormDefinitionVersion != null) {
                ref.setVersion(Integer.parseInt(camundaFormDefinitionVersion.getExpressionText()));
            }
            startFormData.setCamundaFormRef(ref);
        }
        startFormData.setDeploymentId(this.deploymentId);
        startFormData.setProcessDefinition(processDefinition);
        this.initializeFormProperties(startFormData, null);
        this.initializeFormFields(startFormData, null);
        return startFormData;
    }
    
    public ExecutionEntity submitStartFormData(final ExecutionEntity processInstance, final VariableMap properties) {
        this.submitFormVariables(properties, processInstance);
        return processInstance;
    }
}
