// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.jmx.services;

import java.util.List;
import org.zik.bpm.container.impl.plugin.BpmPlatformPlugin;
import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.container.impl.plugin.BpmPlatformPlugins;
import org.zik.bpm.container.impl.spi.PlatformService;

public class JmxManagedBpmPlatformPlugins implements PlatformService<BpmPlatformPlugins>, JmxManagedBpmPlatformPluginsMBean
{
    protected BpmPlatformPlugins plugins;
    
    public JmxManagedBpmPlatformPlugins(final BpmPlatformPlugins plugins) {
        this.plugins = plugins;
    }
    
    @Override
    public void start(final PlatformServiceContainer mBeanServiceContainer) {
    }
    
    @Override
    public void stop(final PlatformServiceContainer mBeanServiceContainer) {
    }
    
    @Override
    public BpmPlatformPlugins getValue() {
        return this.plugins;
    }
    
    @Override
    public String[] getPluginNames() {
        final List<BpmPlatformPlugin> pluginList = this.plugins.getPlugins();
        final String[] names = new String[pluginList.size()];
        for (int i = 0; i < names.length; ++i) {
            final BpmPlatformPlugin bpmPlatformPlugin = pluginList.get(i);
            if (bpmPlatformPlugin != null) {
                names[i] = bpmPlatformPlugin.getClass().getName();
            }
        }
        return names;
    }
}
