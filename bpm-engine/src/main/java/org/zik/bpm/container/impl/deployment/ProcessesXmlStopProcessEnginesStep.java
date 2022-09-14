// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.container.impl.metadata.spi.ProcessEngineXml;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.application.impl.metadata.spi.ProcessesXml;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.container.impl.spi.ServiceTypes;
import org.zik.bpm.container.impl.jmx.services.JmxManagedProcessApplication;
import org.zik.bpm.application.AbstractProcessApplication;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public class ProcessesXmlStopProcessEnginesStep extends DeploymentOperationStep
{
    private static final ContainerIntegrationLogger LOG;
    
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
        final List<ProcessesXml> processesXmls = deployedProcessApplication.getProcessesXmls();
        for (final ProcessesXml processesXml : processesXmls) {
            this.stopProcessEngines(processesXml.getProcessEngines(), operationContext);
        }
    }
    
    protected void stopProcessEngines(final List<ProcessEngineXml> processEngine, final DeploymentOperation operationContext) {
        for (final ProcessEngineXml parsedProcessEngine : processEngine) {
            this.stopProcessEngine(parsedProcessEngine.getName(), operationContext);
        }
    }
    
    protected void stopProcessEngine(final String processEngineName, final DeploymentOperation operationContext) {
        final PlatformServiceContainer serviceContainer = operationContext.getServiceContainer();
        try {
            serviceContainer.stopService(ServiceTypes.PROCESS_ENGINE, processEngineName);
        }
        catch (Exception e) {
            ProcessesXmlStopProcessEnginesStep.LOG.exceptionWhileStopping("Process Engine", processEngineName, e);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
}
