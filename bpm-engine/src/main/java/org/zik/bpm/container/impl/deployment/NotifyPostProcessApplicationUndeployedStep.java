// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment;

import java.util.Iterator;
import org.zik.bpm.application.ProcessApplicationInterface;
import org.zik.bpm.container.impl.plugin.BpmPlatformPlugin;
import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.container.impl.spi.ServiceTypes;
import org.zik.bpm.container.impl.jmx.services.JmxManagedBpmPlatformPlugins;
import org.zik.bpm.application.AbstractProcessApplication;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public class NotifyPostProcessApplicationUndeployedStep extends DeploymentOperationStep
{
    @Override
    public String getName() {
        return "NotifyPostProcessApplicationUndeployedStep";
    }
    
    @Override
    public void performOperationStep(final DeploymentOperation operationContext) {
        final AbstractProcessApplication processApplication = operationContext.getAttachment("processApplication");
        final PlatformServiceContainer serviceContainer = operationContext.getServiceContainer();
        final JmxManagedBpmPlatformPlugins plugins = serviceContainer.getService(ServiceTypes.BPM_PLATFORM, "bpm-platform-plugins");
        if (plugins != null) {
            for (final BpmPlatformPlugin plugin : plugins.getValue().getPlugins()) {
                plugin.postProcessApplicationUndeploy(processApplication);
            }
        }
    }
}
