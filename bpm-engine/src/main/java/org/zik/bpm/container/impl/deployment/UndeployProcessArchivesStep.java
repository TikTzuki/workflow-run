// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.zik.bpm.container.impl.deployment.util.DeployedProcessArchive;
import org.zik.bpm.application.impl.metadata.spi.ProcessArchiveXml;
import org.zik.bpm.application.impl.metadata.spi.ProcessesXml;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.container.impl.spi.ServiceTypes;
import org.zik.bpm.container.impl.jmx.services.JmxManagedProcessApplication;
import org.zik.bpm.application.AbstractProcessApplication;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public class UndeployProcessArchivesStep extends DeploymentOperationStep
{
    @Override
    public String getName() {
        return "Stopping process engines";
    }
    
    @Override
    public void performOperationStep(final DeploymentOperation operationContext) {
        final PlatformServiceContainer serviceContainer = operationContext.getServiceContainer();
        final AbstractProcessApplication processApplication = operationContext.getAttachment("processApplication");
        final JmxManagedProcessApplication deployedProcessApplication = serviceContainer.getService(ServiceTypes.PROCESS_APPLICATION, processApplication.getName());
        EnsureUtil.ensureNotNull("Cannot find process application with name " + processApplication.getName(), "deployedProcessApplication", deployedProcessApplication);
        final Map<String, DeployedProcessArchive> deploymentMap = deployedProcessApplication.getProcessArchiveDeploymentMap();
        if (deploymentMap != null) {
            final List<ProcessesXml> processesXmls = deployedProcessApplication.getProcessesXmls();
            for (final ProcessesXml processesXml : processesXmls) {
                for (final ProcessArchiveXml parsedProcessArchive : processesXml.getProcessArchives()) {
                    final DeployedProcessArchive deployedProcessArchive = deploymentMap.get(parsedProcessArchive.getName());
                    if (deployedProcessArchive != null) {
                        operationContext.addStep(new UndeployProcessArchiveStep(deployedProcessApplication, parsedProcessArchive, deployedProcessArchive.getProcessEngineName()));
                    }
                }
            }
        }
    }
}
