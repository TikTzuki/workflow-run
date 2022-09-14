// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.variable.Variables;
import java.util.Map;
import org.camunda.bpm.engine.variable.VariableMap;
import java.io.Serializable;
import org.zik.bpm.engine.runtime.ProcessInstance;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SubmitStartFormCmd implements Command<ProcessInstance>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected final String processDefinitionId;
    protected final String businessKey;
    protected VariableMap variables;
    
    public SubmitStartFormCmd(final String processDefinitionId, final String businessKey, final Map<String, Object> properties) {
        this.processDefinitionId = processDefinitionId;
        this.businessKey = businessKey;
        this.variables = Variables.fromMap((Map)properties);
    }
    
    @Override
    public ProcessInstance execute(final CommandContext commandContext) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final DeploymentCache deploymentCache = processEngineConfiguration.getDeploymentCache();
        final ProcessDefinitionEntity processDefinition = deploymentCache.findDeployedProcessDefinitionById(this.processDefinitionId);
        EnsureUtil.ensureNotNull("No process definition found for id = '" + this.processDefinitionId + "'", "processDefinition", processDefinition);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkCreateProcessInstance(processDefinition);
        }
        ExecutionEntity processInstance = null;
        if (this.businessKey != null) {
            processInstance = processDefinition.createProcessInstance(this.businessKey);
        }
        else {
            processInstance = processDefinition.createProcessInstance();
        }
        processInstance.startWithFormProperties(this.variables);
        return processInstance;
    }
}
