// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.repository.Deployment;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class RegisterDeploymentCmd implements Command<Void>
{
    protected String deploymentId;
    
    public RegisterDeploymentCmd(final String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final Deployment deployment = commandContext.getDeploymentManager().findDeploymentById(this.deploymentId);
        EnsureUtil.ensureNotNull("Deployment " + this.deploymentId + " does not exist", "deployment", deployment);
        commandContext.getAuthorizationManager().checkCamundaAdminOrPermission(CommandChecker::checkRegisterDeployment);
        Context.getProcessEngineConfiguration().getRegisteredDeployments().add(this.deploymentId);
        return null;
    }
}
