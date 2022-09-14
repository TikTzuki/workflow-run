// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Collections;
import java.util.Set;
import org.zik.bpm.engine.impl.interceptor.Command;

public class UnregisterProcessApplicationCmd implements Command<Void>
{
    protected boolean removeProcessesFromCache;
    protected Set<String> deploymentIds;
    
    public UnregisterProcessApplicationCmd(final String deploymentId, final boolean removeProcessesFromCache) {
        this(Collections.singleton(deploymentId), removeProcessesFromCache);
    }
    
    public UnregisterProcessApplicationCmd(final Set<String> deploymentIds, final boolean removeProcessesFromCache) {
        this.deploymentIds = deploymentIds;
        this.removeProcessesFromCache = removeProcessesFromCache;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        if (this.deploymentIds == null) {
            throw new ProcessEngineException("Deployment Ids cannot be null.");
        }
        commandContext.getAuthorizationManager().checkCamundaAdminOrPermission(CommandChecker::checkUnregisterProcessApplication);
        Context.getProcessEngineConfiguration().getProcessApplicationManager().unregisterProcessApplicationForDeployments(this.deploymentIds, this.removeProcessesFromCache);
        return null;
    }
}
