// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetDeploymentResourcesCmd implements Command<List>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String deploymentId;
    
    public GetDeploymentResourcesCmd(final String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
    @Override
    public List execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("deploymentId", (Object)this.deploymentId);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadDeployment(this.deploymentId);
        }
        return Context.getCommandContext().getResourceManager().findResourcesByDeploymentId(this.deploymentId);
    }
}
