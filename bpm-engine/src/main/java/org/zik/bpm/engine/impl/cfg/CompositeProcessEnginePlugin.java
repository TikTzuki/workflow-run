// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg;

import java.util.Arrays;
import org.zik.bpm.engine.ProcessEngine;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class CompositeProcessEnginePlugin extends AbstractProcessEnginePlugin
{
    protected final List<ProcessEnginePlugin> plugins;
    
    public CompositeProcessEnginePlugin() {
        this.plugins = new ArrayList<ProcessEnginePlugin>();
    }
    
    public CompositeProcessEnginePlugin(final ProcessEnginePlugin plugin, final ProcessEnginePlugin... additionalPlugins) {
        this();
        this.addProcessEnginePlugin(plugin, additionalPlugins);
    }
    
    public CompositeProcessEnginePlugin(final List<ProcessEnginePlugin> plugins) {
        this();
        this.addProcessEnginePlugins(plugins);
    }
    
    public CompositeProcessEnginePlugin addProcessEnginePlugin(final ProcessEnginePlugin plugin, final ProcessEnginePlugin... additionalPlugins) {
        return this.addProcessEnginePlugins(toList(plugin, additionalPlugins));
    }
    
    public CompositeProcessEnginePlugin addProcessEnginePlugins(final Collection<ProcessEnginePlugin> plugins) {
        this.plugins.addAll(plugins);
        return this;
    }
    
    @Override
    public void preInit(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        for (final ProcessEnginePlugin plugin : this.plugins) {
            plugin.preInit(processEngineConfiguration);
        }
    }
    
    @Override
    public void postInit(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        for (final ProcessEnginePlugin plugin : this.plugins) {
            plugin.postInit(processEngineConfiguration);
        }
    }
    
    @Override
    public void postProcessEngineBuild(final ProcessEngine processEngine) {
        for (final ProcessEnginePlugin plugin : this.plugins) {
            plugin.postProcessEngineBuild(processEngine);
        }
    }
    
    public List<ProcessEnginePlugin> getPlugins() {
        return this.plugins;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + this.plugins;
    }
    
    private static List<ProcessEnginePlugin> toList(final ProcessEnginePlugin plugin, final ProcessEnginePlugin... additionalPlugins) {
        final List<ProcessEnginePlugin> plugins = new ArrayList<ProcessEnginePlugin>();
        plugins.add(plugin);
        if (additionalPlugins != null && additionalPlugins.length > 0) {
            plugins.addAll(Arrays.asList(additionalPlugins));
        }
        return plugins;
    }
}
