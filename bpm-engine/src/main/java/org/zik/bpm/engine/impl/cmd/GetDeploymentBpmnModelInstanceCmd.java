// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.ProcessEngineException;
import java.io.Serializable;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetDeploymentBpmnModelInstanceCmd implements Command<BpmnModelInstance>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String processDefinitionId;
    
    public GetDeploymentBpmnModelInstanceCmd(final String processDefinitionId) {
        if (processDefinitionId == null || processDefinitionId.length() < 1) {
            throw new ProcessEngineException("The process definition id is mandatory, but '" + processDefinitionId + "' has been provided.");
        }
        this.processDefinitionId = processDefinitionId;
    }
    
    @Override
    public BpmnModelInstance execute(final CommandContext commandContext) {
        final ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
        final DeploymentCache deploymentCache = configuration.getDeploymentCache();
        final ProcessDefinitionEntity processDefinition = deploymentCache.findDeployedProcessDefinitionById(this.processDefinitionId);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadProcessDefinition(processDefinition);
        }
        final BpmnModelInstance modelInstance = deploymentCache.findBpmnModelInstanceForProcessDefinition(this.processDefinitionId);
        EnsureUtil.ensureNotNull("no BPMN model instance found for process definition id " + this.processDefinitionId, "modelInstance", modelInstance);
        return modelInstance;
    }
}
