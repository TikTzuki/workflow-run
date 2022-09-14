// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.deploy.cache;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.persistence.entity.ResourceEntity;
import java.util.Map;
import java.util.Iterator;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import java.util.Collections;
import org.zik.bpm.engine.impl.persistence.deploy.Deployer;
import java.util.List;
import org.zik.bpm.engine.impl.cmd.CommandLogger;

public class CacheDeployer
{
    private static final CommandLogger LOG;
    protected List<Deployer> deployers;
    
    public CacheDeployer() {
        this.deployers = Collections.emptyList();
    }
    
    public void setDeployers(final List<Deployer> deployers) {
        this.deployers = deployers;
    }
    
    public void deploy(final DeploymentEntity deployment) {
        Context.getCommandContext().runWithoutAuthorization((Callable<Object>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                for (final Deployer deployer : CacheDeployer.this.deployers) {
                    deployer.deploy(deployment);
                }
                return null;
            }
        });
    }
    
    public void deployOnlyGivenResourcesOfDeployment(final DeploymentEntity deployment, final String... resourceNames) {
        this.initDeployment(deployment, resourceNames);
        Context.getCommandContext().runWithoutAuthorization((Callable<Object>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                for (final Deployer deployer : CacheDeployer.this.deployers) {
                    deployer.deploy(deployment);
                }
                return null;
            }
        });
        deployment.setResources(null);
    }
    
    protected void initDeployment(final DeploymentEntity deployment, final String... resourceNames) {
        deployment.clearResources();
        for (final String resourceName : resourceNames) {
            if (resourceName != null) {
                final ResourceEntity resource = Context.getCommandContext().getResourceManager().findResourceByDeploymentIdAndResourceName(deployment.getId(), resourceName);
                deployment.addResource(resource);
            }
        }
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
