// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment.jobexecutor;

import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.container.impl.spi.ServiceTypes;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public class StopManagedThreadPoolStep extends DeploymentOperationStep
{
    @Override
    public String getName() {
        return "Stop managed thread pool";
    }
    
    @Override
    public void performOperationStep(final DeploymentOperation operationContext) {
        final PlatformServiceContainer serviceContainer = operationContext.getServiceContainer();
        serviceContainer.stopService(ServiceTypes.BPM_PLATFORM, "executor-service");
    }
}
