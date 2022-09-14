// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.form.FormDefinition;
import org.zik.bpm.engine.delegate.Expression;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.task.TaskDefinition;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetFormKeyCmd implements Command<String>
{
    protected String taskDefinitionKey;
    protected String processDefinitionId;
    
    public GetFormKeyCmd(final String processDefinitionId) {
        this.setProcessDefinitionId(processDefinitionId);
    }
    
    public GetFormKeyCmd(final String processDefinitionId, final String taskDefinitionKey) {
        this.setProcessDefinitionId(processDefinitionId);
        if (taskDefinitionKey == null || taskDefinitionKey.length() < 1) {
            throw new ProcessEngineException("The task definition key is mandatory, but '" + taskDefinitionKey + "' has been provided.");
        }
        this.taskDefinitionKey = taskDefinitionKey;
    }
    
    protected void setProcessDefinitionId(final String processDefinitionId) {
        if (processDefinitionId == null || processDefinitionId.length() < 1) {
            throw new ProcessEngineException("The process definition id is mandatory, but '" + processDefinitionId + "' has been provided.");
        }
        this.processDefinitionId = processDefinitionId;
    }
    
    @Override
    public String execute(final CommandContext commandContext) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final DeploymentCache deploymentCache = processEngineConfiguration.getDeploymentCache();
        final ProcessDefinitionEntity processDefinition = deploymentCache.findDeployedProcessDefinitionById(this.processDefinitionId);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadProcessDefinition(processDefinition);
        }
        Expression formKeyExpression = null;
        if (this.taskDefinitionKey == null) {
            final FormDefinition formDefinition = processDefinition.getStartFormDefinition();
            formKeyExpression = formDefinition.getFormKey();
        }
        else {
            final TaskDefinition taskDefinition = processDefinition.getTaskDefinitions().get(this.taskDefinitionKey);
            formKeyExpression = taskDefinition.getFormKey();
        }
        String formKey = null;
        if (formKeyExpression != null) {
            formKey = formKeyExpression.getExpressionText();
        }
        return formKey;
    }
}
