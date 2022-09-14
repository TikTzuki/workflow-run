// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment.jobexecutor;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Iterator;
import java.util.Set;
import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.container.impl.spi.ServiceTypes;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public class StopJobExecutorStep extends DeploymentOperationStep
{
    protected static final ContainerIntegrationLogger LOG;
    
    @Override
    public String getName() {
        return "Stop managed job acquisitions";
    }
    
    @Override
    public void performOperationStep(final DeploymentOperation operationContext) {
        final PlatformServiceContainer serviceContainer = operationContext.getServiceContainer();
        final Set<String> jobExecutorServiceNames = serviceContainer.getServiceNames(ServiceTypes.JOB_EXECUTOR);
        for (final String serviceName : jobExecutorServiceNames) {
            try {
                serviceContainer.stopService(serviceName);
            }
            catch (Exception e) {
                StopJobExecutorStep.LOG.exceptionWhileStopping("Job Executor Service", serviceName, e);
            }
        }
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
}
