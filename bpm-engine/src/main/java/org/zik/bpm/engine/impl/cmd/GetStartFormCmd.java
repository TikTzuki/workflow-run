// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.form.handler.StartFormHandler;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.form.StartFormData;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetStartFormCmd implements Command<StartFormData>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String processDefinitionId;
    
    public GetStartFormCmd(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    @Override
    public StartFormData execute(final CommandContext commandContext) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final DeploymentCache deploymentCache = processEngineConfiguration.getDeploymentCache();
        final ProcessDefinitionEntity processDefinition = deploymentCache.findDeployedProcessDefinitionById(this.processDefinitionId);
        EnsureUtil.ensureNotNull("No process definition found for id '" + this.processDefinitionId + "'", "processDefinition", processDefinition);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadProcessDefinition(processDefinition);
        }
        final StartFormHandler startFormHandler = processDefinition.getStartFormHandler();
        EnsureUtil.ensureNotNull("No startFormHandler defined in process '" + this.processDefinitionId + "'", "startFormHandler", startFormHandler);
        return startFormHandler.createStartFormData(processDefinition);
    }
}
