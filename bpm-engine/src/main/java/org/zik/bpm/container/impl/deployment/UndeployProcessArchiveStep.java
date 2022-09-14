// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment;

import org.zik.bpm.engine.RepositoryService;
import java.util.Map;
import org.zik.bpm.container.impl.metadata.PropertyHelper;
import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.container.impl.spi.ServiceTypes;
import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.container.impl.deployment.util.DeployedProcessArchive;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.application.impl.metadata.spi.ProcessArchiveXml;
import org.zik.bpm.container.impl.jmx.services.JmxManagedProcessApplication;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public class UndeployProcessArchiveStep extends DeploymentOperationStep
{
    protected String processArchvieName;
    protected JmxManagedProcessApplication deployedProcessApplication;
    protected ProcessArchiveXml processArchive;
    protected String processEngineName;
    
    public UndeployProcessArchiveStep(final JmxManagedProcessApplication deployedProcessApplication, final ProcessArchiveXml processArchive, final String processEngineName) {
        this.deployedProcessApplication = deployedProcessApplication;
        this.processArchive = processArchive;
        this.processEngineName = processEngineName;
    }
    
    @Override
    public String getName() {
        return "Undeploying process archvie " + this.processArchvieName;
    }
    
    @Override
    public void performOperationStep(final DeploymentOperation operationContext) {
        final PlatformServiceContainer serviceContainer = operationContext.getServiceContainer();
        final Map<String, DeployedProcessArchive> processArchiveDeploymentMap = this.deployedProcessApplication.getProcessArchiveDeploymentMap();
        final DeployedProcessArchive deployedProcessArchive = processArchiveDeploymentMap.get(this.processArchive.getName());
        final ProcessEngine processEngine = serviceContainer.getServiceValue(ServiceTypes.PROCESS_ENGINE, this.processEngineName);
        processEngine.getManagementService().unregisterProcessApplication(deployedProcessArchive.getAllDeploymentIds(), true);
        if (PropertyHelper.getBooleanProperty(this.processArchive.getProperties(), "isDeleteUponUndeploy", false) && processEngine != null) {
            this.deleteDeployment(deployedProcessArchive.getPrimaryDeploymentId(), processEngine.getRepositoryService());
        }
    }
    
    protected void deleteDeployment(final String deploymentId, final RepositoryService repositoryService) {
        repositoryService.deleteDeployment(deploymentId, true, true);
    }
}
