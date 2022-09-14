// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.plugin;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.ArrayList;
import java.util.List;

public class BpmPlatformPlugins
{
    protected List<BpmPlatformPlugin> plugins;
    
    public BpmPlatformPlugins() {
        this.plugins = new ArrayList<BpmPlatformPlugin>();
    }
    
    public void add(final BpmPlatformPlugin plugin) {
        this.plugins.add(plugin);
    }
    
    public List<BpmPlatformPlugin> getPlugins() {
        return this.plugins;
    }
    
    public static BpmPlatformPlugins load(final ClassLoader classLoader) {
        final BpmPlatformPlugins plugins = new BpmPlatformPlugins();
        final Iterator<BpmPlatformPlugin> it = ServiceLoader.load(BpmPlatformPlugin.class, classLoader).iterator();
        while (it.hasNext()) {
            plugins.add(it.next());
        }
        return plugins;
    }
}
