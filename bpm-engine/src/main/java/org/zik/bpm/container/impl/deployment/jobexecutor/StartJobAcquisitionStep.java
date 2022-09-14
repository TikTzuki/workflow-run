// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment.jobexecutor;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Map;
import org.zik.bpm.engine.impl.jobexecutor.JobExecutor;
import org.zik.bpm.container.impl.spi.PlatformService;
import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.container.impl.spi.ServiceTypes;
import org.zik.bpm.container.impl.jmx.services.JmxManagedJobExecutor;
import org.zik.bpm.container.impl.metadata.PropertyHelper;
import org.zik.bpm.engine.impl.jobexecutor.RuntimeContainerJobExecutor;
import org.zik.bpm.engine.ProcessEngineConfiguration;
import org.zik.bpm.application.AbstractProcessApplication;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.container.impl.metadata.spi.JobAcquisitionXml;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public class StartJobAcquisitionStep extends DeploymentOperationStep
{
    protected static final ContainerIntegrationLogger LOG;
    protected final JobAcquisitionXml jobAcquisitionXml;
    
    public StartJobAcquisitionStep(final JobAcquisitionXml jobAcquisitionXml) {
        this.jobAcquisitionXml = jobAcquisitionXml;
    }
    
    @Override
    public String getName() {
        return "Start job acquisition '" + this.jobAcquisitionXml.getName() + "'";
    }
    
    @Override
    public void performOperationStep(final DeploymentOperation operationContext) {
        final PlatformServiceContainer serviceContainer = operationContext.getServiceContainer();
        final AbstractProcessApplication processApplication = operationContext.getAttachment("processApplication");
        ClassLoader configurationClassloader = null;
        if (processApplication != null) {
            configurationClassloader = processApplication.getProcessApplicationClassloader();
        }
        else {
            configurationClassloader = ProcessEngineConfiguration.class.getClassLoader();
        }
        String configurationClassName = this.jobAcquisitionXml.getJobExecutorClassName();
        if (configurationClassName == null || configurationClassName.isEmpty()) {
            configurationClassName = RuntimeContainerJobExecutor.class.getName();
        }
        final Class<? extends JobExecutor> jobExecutorClass = this.loadJobExecutorClass(configurationClassloader, configurationClassName);
        final JobExecutor jobExecutor = this.instantiateJobExecutor(jobExecutorClass);
        final Map<String, String> properties = this.jobAcquisitionXml.getProperties();
        PropertyHelper.applyProperties(jobExecutor, properties);
        final JmxManagedJobExecutor jmxManagedJobExecutor = new JmxManagedJobExecutor(jobExecutor);
        serviceContainer.startService(ServiceTypes.JOB_EXECUTOR, this.jobAcquisitionXml.getName(), jmxManagedJobExecutor);
    }
    
    protected JobExecutor instantiateJobExecutor(final Class<? extends JobExecutor> configurationClass) {
        try {
            return (JobExecutor)configurationClass.newInstance();
        }
        catch (Exception e) {
            throw StartJobAcquisitionStep.LOG.couldNotInstantiateJobExecutorClass(e);
        }
    }
    
    protected Class<? extends JobExecutor> loadJobExecutorClass(final ClassLoader processApplicationClassloader, final String jobExecutorClassname) {
        try {
            return (Class<? extends JobExecutor>)processApplicationClassloader.loadClass(jobExecutorClassname);
        }
        catch (ClassNotFoundException e) {
            throw StartJobAcquisitionStep.LOG.couldNotLoadJobExecutorClass(e);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
}
