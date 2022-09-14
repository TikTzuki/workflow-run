// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Collection;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Collections;
import java.util.Set;
import org.zik.bpm.engine.impl.interceptor.Command;

public class UnregisterDeploymentCmd implements Command<Void>
{
    protected Set<String> deploymentIds;
    
    public UnregisterDeploymentCmd(final Set<String> deploymentIds) {
        this.deploymentIds = deploymentIds;
    }
    
    public UnregisterDeploymentCmd(final String deploymentId) {
        this(Collections.singleton(deploymentId));
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        commandContext.getAuthorizationManager().checkCamundaAdminOrPermission(CommandChecker::checkUnregisterDeployment);
        Context.getProcessEngineConfiguration().getRegisteredDeployments().removeAll(this.deploymentIds);
        return null;
    }
}
