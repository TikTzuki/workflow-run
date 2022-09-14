// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment;

import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.container.impl.spi.ServiceTypes;
import org.zik.bpm.application.AbstractProcessApplication;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public class StopProcessApplicationServiceStep extends DeploymentOperationStep
{
    @Override
    public String getName() {
        return "Removing process application";
    }
    
    @Override
    public void performOperationStep(final DeploymentOperation operationContext) {
        final PlatformServiceContainer serviceContainer = operationContext.getServiceContainer();
        final AbstractProcessApplication processApplication = operationContext.getAttachment("processApplication");
        serviceContainer.stopService(ServiceTypes.PROCESS_APPLICATION, processApplication.getName());
    }
}
