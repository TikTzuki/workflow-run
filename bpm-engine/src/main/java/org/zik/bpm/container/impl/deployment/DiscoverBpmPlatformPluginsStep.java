// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment;

import org.zik.bpm.BpmPlatform;
import org.zik.bpm.engine.impl.util.ClassLoaderUtil;
import org.zik.bpm.container.impl.spi.PlatformService;
import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.container.impl.spi.ServiceTypes;
import org.zik.bpm.container.impl.jmx.services.JmxManagedBpmPlatformPlugins;
import org.zik.bpm.container.impl.plugin.BpmPlatformPlugins;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public class DiscoverBpmPlatformPluginsStep extends DeploymentOperationStep
{
    @Override
    public String getName() {
        return "Discover BPM Platform Plugins";
    }
    
    @Override
    public void performOperationStep(final DeploymentOperation operationContext) {
        final PlatformServiceContainer serviceContainer = operationContext.getServiceContainer();
        final BpmPlatformPlugins plugins = BpmPlatformPlugins.load(this.getPluginsClassloader());
        final JmxManagedBpmPlatformPlugins jmxManagedPlugins = new JmxManagedBpmPlatformPlugins(plugins);
        serviceContainer.startService(ServiceTypes.BPM_PLATFORM, "bpm-platform-plugins", (PlatformService<Object>)jmxManagedPlugins);
    }
    
    protected ClassLoader getPluginsClassloader() {
        ClassLoader pluginsClassLoader = ClassLoaderUtil.getContextClassloader();
        if (pluginsClassLoader == null) {
            pluginsClassLoader = BpmPlatform.class.getClassLoader();
        }
        return pluginsClassLoader;
    }
}
