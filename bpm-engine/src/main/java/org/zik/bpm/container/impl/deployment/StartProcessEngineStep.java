// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.zik.bpm.container.impl.metadata.spi.ProcessEnginePluginXml;
import org.zik.bpm.engine.ProcessEngineConfiguration;
import org.zik.bpm.container.impl.jmx.services.JmxManagedProcessEngineController;
import org.zik.bpm.container.impl.jmx.services.JmxManagedProcessEngine;
import org.zik.bpm.engine.impl.jobexecutor.JobExecutor;
import java.util.Map;
import org.zik.bpm.container.impl.spi.PlatformService;
import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.container.impl.spi.ServiceTypes;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.container.impl.metadata.PropertyHelper;
import org.zik.bpm.engine.impl.cfg.IdGenerator;
import org.zik.bpm.engine.impl.persistence.StrongUuidGenerator;
import org.zik.bpm.engine.impl.util.ReflectUtil;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.zik.bpm.application.AbstractProcessApplication;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.container.impl.metadata.spi.ProcessEngineXml;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public class StartProcessEngineStep extends DeploymentOperationStep
{
    private static final ContainerIntegrationLogger LOG;
    protected final ProcessEngineXml processEngineXml;
    
    public StartProcessEngineStep(final ProcessEngineXml processEngineXml) {
        this.processEngineXml = processEngineXml;
    }
    
    @Override
    public String getName() {
        return "Start process engine " + this.processEngineXml.getName();
    }
    
    @Override
    public void performOperationStep(final DeploymentOperation operationContext) {
        final PlatformServiceContainer serviceContainer = operationContext.getServiceContainer();
        final AbstractProcessApplication processApplication = operationContext.getAttachment("processApplication");
        ClassLoader classLoader = null;
        if (processApplication != null) {
            classLoader = processApplication.getProcessApplicationClassloader();
        }
        String configurationClassName = this.processEngineXml.getConfigurationClass();
        if (configurationClassName == null || configurationClassName.isEmpty()) {
            configurationClassName = StandaloneProcessEngineConfiguration.class.getName();
        }
        final Class<? extends ProcessEngineConfigurationImpl> configurationClass = this.loadClass(configurationClassName, classLoader, ProcessEngineConfigurationImpl.class);
        final ProcessEngineConfigurationImpl configurationImpl;
        final ProcessEngineConfigurationImpl configuration = configurationImpl = ReflectUtil.createInstance(configurationClass);
        configurationImpl.setIdGenerator(new StrongUuidGenerator());
        final String name = this.processEngineXml.getName();
        configuration.setProcessEngineName(name);
        final String datasourceJndiName = this.processEngineXml.getDatasource();
        configuration.setDataSourceJndiName(datasourceJndiName);
        final Map<String, String> properties = this.processEngineXml.getProperties();
        this.setJobExecutorActivate(configuration, properties);
        PropertyHelper.applyProperties(configuration, properties);
        this.configurePlugins(configuration, this.processEngineXml, classLoader);
        this.addAdditionalPlugins(configuration);
        if (this.processEngineXml.getJobAcquisitionName() != null && !this.processEngineXml.getJobAcquisitionName().isEmpty()) {
            final JobExecutor jobExecutor = this.getJobExecutorService(serviceContainer);
            EnsureUtil.ensureNotNull("Cannot find referenced job executor with name '" + this.processEngineXml.getJobAcquisitionName() + "'", "jobExecutor", jobExecutor);
            configurationImpl.setJobExecutor(jobExecutor);
        }
        this.additionalConfiguration(configuration);
        final JmxManagedProcessEngine managedProcessEngineService = this.createProcessEngineControllerInstance(configuration);
        serviceContainer.startService(ServiceTypes.PROCESS_ENGINE, configuration.getProcessEngineName(), (PlatformService<Object>)managedProcessEngineService);
    }
    
    protected void setJobExecutorActivate(final ProcessEngineConfigurationImpl configuration, final Map<String, String> properties) {
        configuration.setJobExecutorActivate(true);
    }
    
    protected JmxManagedProcessEngineController createProcessEngineControllerInstance(final ProcessEngineConfigurationImpl configuration) {
        return new JmxManagedProcessEngineController(configuration);
    }
    
    protected void configurePlugins(final ProcessEngineConfigurationImpl configuration, final ProcessEngineXml processEngineXml, final ClassLoader classLoader) {
        for (final ProcessEnginePluginXml pluginXml : processEngineXml.getPlugins()) {
            final Class<? extends ProcessEnginePlugin> pluginClass = this.loadClass(pluginXml.getPluginClass(), classLoader, ProcessEnginePlugin.class);
            final ProcessEnginePlugin plugin = ReflectUtil.createInstance(pluginClass);
            final Map<String, String> properties = pluginXml.getProperties();
            PropertyHelper.applyProperties(plugin, properties);
            configuration.getProcessEnginePlugins().add(plugin);
        }
    }
    
    protected JobExecutor getJobExecutorService(final PlatformServiceContainer serviceContainer) {
        final String jobAcquisitionName = this.processEngineXml.getJobAcquisitionName();
        final JobExecutor jobExecutor = serviceContainer.getServiceValue(ServiceTypes.JOB_EXECUTOR, jobAcquisitionName);
        return jobExecutor;
    }
    
    protected <T> Class<? extends T> loadClass(final String className, final ClassLoader customClassloader, final Class<T> clazz) {
        try {
            return ReflectUtil.loadClass(className, customClassloader, clazz);
        }
        catch (ClassNotFoundException e) {
            throw StartProcessEngineStep.LOG.cannotLoadConfigurationClass(className, e);
        }
        catch (ClassCastException e2) {
            throw StartProcessEngineStep.LOG.configurationClassHasWrongType(className, clazz, e2);
        }
    }
    
    protected void addAdditionalPlugins(final ProcessEngineConfigurationImpl configuration) {
    }
    
    protected void additionalConfiguration(final ProcessEngineConfigurationImpl configuration) {
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
}
