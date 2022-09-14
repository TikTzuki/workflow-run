// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment;

import org.zik.bpm.application.ProcessApplicationInterface;
import org.zik.bpm.container.impl.plugin.BpmPlatformPlugin;
import org.zik.bpm.container.impl.jmx.services.JmxManagedBpmPlatformPlugins;
import java.util.Iterator;
import org.zik.bpm.application.impl.ProcessApplicationDeploymentInfoImpl;
import org.zik.bpm.application.ProcessApplicationDeploymentInfo;
import org.zik.bpm.application.impl.ProcessApplicationInfoImpl;
import org.zik.bpm.container.impl.spi.PlatformService;
import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.container.impl.spi.ServiceTypes;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import org.zik.bpm.container.impl.jmx.services.JmxManagedProcessApplication;
import org.zik.bpm.container.impl.deployment.util.DeployedProcessArchive;
import org.zik.bpm.application.impl.metadata.spi.ProcessesXml;
import java.net.URL;
import java.util.Map;
import org.zik.bpm.application.AbstractProcessApplication;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public class StartProcessApplicationServiceStep extends DeploymentOperationStep
{
    @Override
    public String getName() {
        return "Start Process Application Service";
    }
    
    @Override
    public void performOperationStep(final DeploymentOperation operationContext) {
        final AbstractProcessApplication processApplication = operationContext.getAttachment("processApplication");
        final Map<URL, ProcessesXml> processesXmls = operationContext.getAttachment("processesXmlList");
        final Map<String, DeployedProcessArchive> processArchiveDeploymentMap = operationContext.getAttachment("processArchiveDeploymentMap");
        final PlatformServiceContainer serviceContainer = operationContext.getServiceContainer();
        final ProcessApplicationInfoImpl processApplicationInfo = this.createProcessApplicationInfo(processApplication, processArchiveDeploymentMap);
        final JmxManagedProcessApplication mbean = new JmxManagedProcessApplication(processApplicationInfo, processApplication.getReference());
        mbean.setProcessesXmls(new ArrayList<ProcessesXml>(processesXmls.values()));
        mbean.setDeploymentMap(processArchiveDeploymentMap);
        serviceContainer.startService(ServiceTypes.PROCESS_APPLICATION, processApplication.getName(), (PlatformService<Object>)mbean);
        this.notifyBpmPlatformPlugins(serviceContainer, processApplication);
    }
    
    protected ProcessApplicationInfoImpl createProcessApplicationInfo(final AbstractProcessApplication processApplication, final Map<String, DeployedProcessArchive> processArchiveDeploymentMap) {
        final ProcessApplicationInfoImpl processApplicationInfo = new ProcessApplicationInfoImpl();
        processApplicationInfo.setName(processApplication.getName());
        processApplicationInfo.setProperties(processApplication.getProperties());
        final List<ProcessApplicationDeploymentInfo> deploymentInfoList = new ArrayList<ProcessApplicationDeploymentInfo>();
        if (processArchiveDeploymentMap != null) {
            for (final Map.Entry<String, DeployedProcessArchive> deployment : processArchiveDeploymentMap.entrySet()) {
                final DeployedProcessArchive deployedProcessArchive = deployment.getValue();
                for (final String deploymentId : deployedProcessArchive.getAllDeploymentIds()) {
                    final ProcessApplicationDeploymentInfoImpl deploymentInfo = new ProcessApplicationDeploymentInfoImpl();
                    deploymentInfo.setDeploymentId(deploymentId);
                    deploymentInfo.setProcessEngineName(deployedProcessArchive.getProcessEngineName());
                    deploymentInfoList.add(deploymentInfo);
                }
            }
        }
        processApplicationInfo.setDeploymentInfo(deploymentInfoList);
        return processApplicationInfo;
    }
    
    protected void notifyBpmPlatformPlugins(final PlatformServiceContainer serviceContainer, final AbstractProcessApplication processApplication) {
        final JmxManagedBpmPlatformPlugins plugins = serviceContainer.getService(ServiceTypes.BPM_PLATFORM, "bpm-platform-plugins");
        if (plugins != null) {
            for (final BpmPlatformPlugin plugin : plugins.getValue().getPlugins()) {
                plugin.postProcessApplicationDeploy(processApplication);
            }
        }
    }
}
