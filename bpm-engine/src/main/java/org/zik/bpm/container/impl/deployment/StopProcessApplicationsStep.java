// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.application.ProcessApplicationInterface;
import org.zik.bpm.application.ProcessApplicationReference;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.container.impl.jmx.services.JmxManagedProcessApplication;
import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.container.impl.spi.ServiceTypes;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public class StopProcessApplicationsStep extends DeploymentOperationStep
{
    private static final ContainerIntegrationLogger LOG;
    
    @Override
    public String getName() {
        return "Stopping process applications";
    }
    
    @Override
    public void performOperationStep(final DeploymentOperation operationContext) {
        final PlatformServiceContainer serviceContainer = operationContext.getServiceContainer();
        final List<JmxManagedProcessApplication> processApplicationsReferences = serviceContainer.getServiceValuesByType(ServiceTypes.PROCESS_APPLICATION);
        for (final JmxManagedProcessApplication processApplication : processApplicationsReferences) {
            this.stopProcessApplication(processApplication.getProcessApplicationReference());
        }
    }
    
    protected void stopProcessApplication(final ProcessApplicationReference processApplicationReference) {
        try {
            final ProcessApplicationInterface processApplication = processApplicationReference.getProcessApplication();
            processApplication.undeploy();
        }
        catch (Throwable t) {
            StopProcessApplicationsStep.LOG.exceptionWhileStopping("Process Application", processApplicationReference.getName(), t);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
}
