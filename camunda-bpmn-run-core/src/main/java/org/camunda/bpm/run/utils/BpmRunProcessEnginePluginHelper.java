package org.camunda.bpm.run.utils;

import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.impl.util.ReflectUtil;
import org.camunda.bpm.run.property.BpmRunProcessEnginePluginProperty;
import org.camunda.bpm.spring.boot.starter.util.SpringBootStarterException;
import org.camunda.bpm.spring.boot.starter.util.SpringBootStarterPropertyHelper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BpmRunProcessEnginePluginHelper {
    protected static final BpmRunLogger LOG = BpmRunLogger.LOG;

    public static void registerYamlPlugins(List<ProcessEnginePlugin> processEnginePlugins, List<BpmRunProcessEnginePluginProperty> pluginsInfo) {
        for (BpmRunProcessEnginePluginProperty pluginInfo : pluginsInfo) {
            String className = pluginInfo.getPluginClass();
            ProcessEnginePlugin plugin = getOrCreatePluginInstance(processEnginePlugins, className);
            Map<String, Object> pluginParameters = pluginInfo.getPluginParameters();
            populatePluginInstance(plugin, pluginParameters);
            LOG.processEnginePluginRegistered(className);
        }
    }

    protected static ProcessEnginePlugin getOrCreatePluginInstance(List<ProcessEnginePlugin> processEnginePlugins, String className) {
        try {
            Class<? extends ProcessEnginePlugin> pluginClass = ReflectUtil.loadClass(className, null, ProcessEnginePlugin.class);
            Optional<ProcessEnginePlugin> plugin = processEnginePlugins.stream().filter(p -> pluginClass.isInstance(p)).findFirst();
            return plugin.orElseGet(() -> {
                ProcessEnginePlugin newPlugin = (ProcessEnginePlugin) ReflectUtil.createInstance(pluginClass);
                processEnginePlugins.add(newPlugin);
                return newPlugin;
            });
        } catch (ClassNotFoundException | ClassCastException | org.camunda.bpm.engine.ProcessEngineException e) {
            throw LOG.failedProcessEnginePluginInstantiation(className, e);
        }
    }

    protected static void populatePluginInstance(ProcessEnginePlugin plugin, Map<String, Object> properties) {
        try {
            SpringBootStarterPropertyHelper.applyProperties(plugin, properties, false);
        } catch (SpringBootStarterException e) {
            throw LOG.pluginPropertyNotFound(plugin.getClass().getCanonicalName(), "", e);
        }
    }
}
