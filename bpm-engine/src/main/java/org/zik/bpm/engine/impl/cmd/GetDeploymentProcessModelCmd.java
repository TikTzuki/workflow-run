// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.ProcessEngineException;
import java.io.Serializable;
import java.io.InputStream;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetDeploymentProcessModelCmd implements Command<InputStream>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String processDefinitionId;
    
    public GetDeploymentProcessModelCmd(final String processDefinitionId) {
        if (processDefinitionId == null || processDefinitionId.length() < 1) {
            throw new ProcessEngineException("The process definition id is mandatory, but '" + processDefinitionId + "' has been provided.");
        }
        this.processDefinitionId = processDefinitionId;
    }
    
    @Override
    public InputStream execute(final CommandContext commandContext) {
        final ProcessDefinitionEntity processDefinition = Context.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(this.processDefinitionId);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadProcessDefinition(processDefinition);
        }
        final String deploymentId = processDefinition.getDeploymentId();
        final String resourceName = processDefinition.getResourceName();
        final InputStream processModelStream = commandContext.runWithoutAuthorization((Command<InputStream>)new GetDeploymentResourceCmd(deploymentId, resourceName));
        return processModelStream;
    }
}
