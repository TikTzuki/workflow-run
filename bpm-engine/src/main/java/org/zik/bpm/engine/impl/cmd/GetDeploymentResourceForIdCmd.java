// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.ResourceEntity;
import java.util.Iterator;
import java.io.ByteArrayInputStream;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import java.io.InputStream;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetDeploymentResourceForIdCmd implements Command<InputStream>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String deploymentId;
    protected String resourceId;
    
    public GetDeploymentResourceForIdCmd(final String deploymentId, final String resourceId) {
        this.deploymentId = deploymentId;
        this.resourceId = resourceId;
    }
    
    @Override
    public InputStream execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("deploymentId", (Object)this.deploymentId);
        EnsureUtil.ensureNotNull("resourceId", (Object)this.resourceId);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadDeployment(this.deploymentId);
        }
        final ResourceEntity resource = commandContext.getResourceManager().findResourceByDeploymentIdAndResourceId(this.deploymentId, this.resourceId);
        EnsureUtil.ensureNotNull("no resource found with id '" + this.resourceId + "' in deployment '" + this.deploymentId + "'", "resource", resource);
        return new ByteArrayInputStream(resource.getBytes());
    }
}
