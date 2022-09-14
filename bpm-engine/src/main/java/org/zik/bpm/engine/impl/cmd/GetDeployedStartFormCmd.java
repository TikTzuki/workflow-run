// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.form.FormData;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;

public class GetDeployedStartFormCmd extends AbstractGetDeployedFormCmd
{
    protected String processDefinitionId;
    
    public GetDeployedStartFormCmd(final String processDefinitionId) {
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "Process definition id cannot be null", "processDefinitionId", processDefinitionId);
        this.processDefinitionId = processDefinitionId;
    }
    
    @Override
    protected FormData getFormData() {
        return this.commandContext.runWithoutAuthorization((Command<FormData>)new GetStartFormCmd(this.processDefinitionId));
    }
    
    @Override
    protected void checkAuthorization() {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final DeploymentCache deploymentCache = processEngineConfiguration.getDeploymentCache();
        final ProcessDefinitionEntity processDefinition = deploymentCache.findDeployedProcessDefinitionById(this.processDefinitionId);
        for (final CommandChecker checker : this.commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadProcessDefinition(processDefinition);
        }
    }
}
