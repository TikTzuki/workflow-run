// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Iterator;
import java.util.Set;
import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.container.impl.spi.ServiceTypes;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public class StopProcessEnginesStep extends DeploymentOperationStep
{
    private static final ContainerIntegrationLogger LOG;
    
    @Override
    public String getName() {
        return "Stopping process engines";
    }
    
    @Override
    public void performOperationStep(final DeploymentOperation operationContext) {
        final PlatformServiceContainer serviceContainer = operationContext.getServiceContainer();
        final Set<String> serviceNames = serviceContainer.getServiceNames(ServiceTypes.PROCESS_ENGINE);
        for (final String serviceName : serviceNames) {
            this.stopProcessEngine(serviceName, serviceContainer);
        }
    }
    
    private void stopProcessEngine(final String serviceName, final PlatformServiceContainer serviceContainer) {
        try {
            serviceContainer.stopService(serviceName);
        }
        catch (Exception e) {
            StopProcessEnginesStep.LOG.exceptionWhileStopping("Process Engine", serviceName, e);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
}
