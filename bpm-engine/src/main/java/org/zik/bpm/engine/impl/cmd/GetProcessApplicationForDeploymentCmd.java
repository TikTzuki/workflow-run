// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetProcessApplicationForDeploymentCmd implements Command<String>
{
    protected String deploymentId;
    
    public GetProcessApplicationForDeploymentCmd(final String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
    @Override
    public String execute(final CommandContext commandContext) {
        commandContext.getAuthorizationManager().checkCamundaAdminOrPermission(CommandChecker::checkReadProcessApplicationForDeployment);
        final ProcessApplicationReference reference = Context.getProcessEngineConfiguration().getProcessApplicationManager().getProcessApplicationForDeployment(this.deploymentId);
        if (reference != null) {
            return reference.getName();
        }
        return null;
    }
}
