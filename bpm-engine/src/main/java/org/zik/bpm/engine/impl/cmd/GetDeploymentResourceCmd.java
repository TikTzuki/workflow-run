// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.ResourceEntity;
import java.util.Iterator;
import java.io.ByteArrayInputStream;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.DeploymentResourceNotFoundException;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import java.io.InputStream;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetDeploymentResourceCmd implements Command<InputStream>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String deploymentId;
    protected String resourceName;
    
    public GetDeploymentResourceCmd(final String deploymentId, final String resourceName) {
        this.deploymentId = deploymentId;
        this.resourceName = resourceName;
    }
    
    @Override
    public InputStream execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("deploymentId", (Object)this.deploymentId);
        EnsureUtil.ensureNotNull("resourceName", (Object)this.resourceName);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadDeployment(this.deploymentId);
        }
        final ResourceEntity resource = commandContext.getResourceManager().findResourceByDeploymentIdAndResourceName(this.deploymentId, this.resourceName);
        EnsureUtil.ensureNotNull(DeploymentResourceNotFoundException.class, "no resource found with name '" + this.resourceName + "' in deployment '" + this.deploymentId + "'", "resource", resource);
        return new ByteArrayInputStream(resource.getBytes());
    }
}
