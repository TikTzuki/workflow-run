// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.handler;

import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.impl.form.FormDefinition;
import org.zik.bpm.engine.impl.task.TaskDefinition;
import org.zik.bpm.engine.impl.form.FormDataImpl;
import org.zik.bpm.engine.task.Task;
import org.zik.bpm.engine.form.CamundaFormRef;
import org.zik.bpm.engine.impl.form.CamundaFormRefImpl;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.form.TaskFormDataImpl;
import org.zik.bpm.engine.form.TaskFormData;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;

public class DefaultTaskFormHandler extends DefaultFormHandler implements TaskFormHandler
{
    @Override
    public TaskFormData createTaskForm(final TaskEntity task) {
        final TaskFormDataImpl taskFormData = new TaskFormDataImpl();
        final TaskDefinition taskDefinition = task.getTaskDefinition();
        final FormDefinition formDefinition = taskDefinition.getFormDefinition();
        final Expression formKey = formDefinition.getFormKey();
        final Expression camundaFormDefinitionKey = formDefinition.getCamundaFormDefinitionKey();
        final String camundaFormDefinitionBinding = formDefinition.getCamundaFormDefinitionBinding();
        final Expression camundaFormDefinitionVersion = formDefinition.getCamundaFormDefinitionVersion();
        if (formKey != null) {
            final Object formValue = formKey.getValue(task);
            if (formValue != null) {
                taskFormData.setFormKey(formValue.toString());
            }
        }
        else if (camundaFormDefinitionKey != null && camundaFormDefinitionBinding != null) {
            final Object formRefKeyValue = camundaFormDefinitionKey.getValue(task);
            if (formRefKeyValue != null) {
                final CamundaFormRefImpl ref = new CamundaFormRefImpl(formRefKeyValue.toString(), camundaFormDefinitionBinding);
                if (camundaFormDefinitionBinding.equals("version") && camundaFormDefinitionVersion != null) {
                    final Object formRefVersionValue = camundaFormDefinitionVersion.getValue(task);
                    if (formRefVersionValue != null) {
                        ref.setVersion(Integer.parseInt((String)formRefVersionValue));
                    }
                }
                taskFormData.setCamundaFormRef(ref);
            }
        }
        taskFormData.setDeploymentId(this.deploymentId);
        taskFormData.setTask(task);
        this.initializeFormProperties(taskFormData, task.getExecution());
        this.initializeFormFields(taskFormData, task.getExecution());
        return taskFormData;
    }
}
