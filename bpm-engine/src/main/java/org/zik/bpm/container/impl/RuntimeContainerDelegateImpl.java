// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.application.ProcessApplicationInfo;
import org.zik.bpm.container.impl.jmx.services.JmxManagedProcessApplication;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import org.zik.bpm.container.ExecutorService;
import org.zik.bpm.container.impl.deployment.NotifyPostProcessApplicationUndeployedStep;
import org.zik.bpm.container.impl.deployment.StopProcessApplicationServiceStep;
import org.zik.bpm.container.impl.deployment.ProcessesXmlStopProcessEnginesStep;
import org.zik.bpm.container.impl.deployment.UndeployProcessArchivesStep;
import org.zik.bpm.container.impl.deployment.PreUndeployInvocationStep;
import java.util.Arrays;
import org.zik.bpm.container.impl.deployment.PostDeployInvocationStep;
import org.zik.bpm.container.impl.deployment.StartProcessApplicationServiceStep;
import org.zik.bpm.container.impl.deployment.DeployProcessArchivesStep;
import org.zik.bpm.container.impl.deployment.ProcessesXmlStartProcessEnginesStep;
import org.zik.bpm.container.impl.deployment.ParseProcessesXmlStep;
import java.util.List;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;
import java.util.Collection;
import org.zik.bpm.application.AbstractProcessApplication;
import org.zik.bpm.container.impl.spi.PlatformService;
import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.container.impl.spi.ServiceTypes;
import org.zik.bpm.container.impl.jmx.services.JmxManagedProcessEngine;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.container.impl.jmx.MBeanServiceContainer;
import org.zik.bpm.ProcessApplicationService;
import org.zik.bpm.ProcessEngineService;
import org.zik.bpm.container.RuntimeContainerDelegate;

public class RuntimeContainerDelegateImpl implements RuntimeContainerDelegate, ProcessEngineService, ProcessApplicationService
{
    protected static final ContainerIntegrationLogger LOG;
    protected MBeanServiceContainer serviceContainer;
    public static final String SERVICE_NAME_EXECUTOR = "executor-service";
    public static final String SERVICE_NAME_PLATFORM_PLUGINS = "bpm-platform-plugins";
    
    public RuntimeContainerDelegateImpl() {
        this.serviceContainer = new MBeanServiceContainer();
    }
    
    @Override
    public void registerProcessEngine(final ProcessEngine processEngine) {
        EnsureUtil.ensureNotNull("Cannot register process engine in Jmx Runtime Container", "process engine", processEngine);
        final String processEngineName = processEngine.getName();
        final JmxManagedProcessEngine managedProcessEngine = new JmxManagedProcessEngine(processEngine);
        this.serviceContainer.startService(ServiceTypes.PROCESS_ENGINE, processEngineName, (PlatformService<Object>)managedProcessEngine);
    }
    
    @Override
    public void unregisterProcessEngine(final ProcessEngine processEngine) {
        EnsureUtil.ensureNotNull("Cannot unregister process engine in Jmx Runtime Container", "process engine", processEngine);
        this.serviceContainer.stopService(ServiceTypes.PROCESS_ENGINE, processEngine.getName());
    }
    
    @Override
    public void deployProcessApplication(final AbstractProcessApplication processApplication) {
        EnsureUtil.ensureNotNull("Process application", processApplication);
        final String operationName = "Deployment of Process Application " + processApplication.getName();
        this.serviceContainer.createDeploymentOperation(operationName).addAttachment("processApplication", processApplication).addSteps(this.getDeploymentSteps()).execute();
        RuntimeContainerDelegateImpl.LOG.paDeployed(processApplication.getName());
    }
    
    @Override
    public void undeployProcessApplication(final AbstractProcessApplication processApplication) {
        EnsureUtil.ensureNotNull("Process application", processApplication);
        final String processAppName = processApplication.getName();
        if (this.serviceContainer.getService(ServiceTypes.PROCESS_APPLICATION, processAppName) == null) {
            return;
        }
        final String operationName = "Undeployment of Process Application " + processAppName;
        this.serviceContainer.createUndeploymentOperation(operationName).addAttachment("processApplication", processApplication).addSteps(this.getUndeploymentSteps()).execute();
        RuntimeContainerDelegateImpl.LOG.paUndeployed(processApplication.getName());
    }
    
    protected List<DeploymentOperationStep> getDeploymentSteps() {
        return Arrays.asList(new ParseProcessesXmlStep(), new ProcessesXmlStartProcessEnginesStep(), new DeployProcessArchivesStep(), new StartProcessApplicationServiceStep(), new PostDeployInvocationStep());
    }
    
    protected List<DeploymentOperationStep> getUndeploymentSteps() {
        return Arrays.asList(new PreUndeployInvocationStep(), new UndeployProcessArchivesStep(), new ProcessesXmlStopProcessEnginesStep(), new StopProcessApplicationServiceStep(), new NotifyPostProcessApplicationUndeployedStep());
    }
    
    @Override
    public ProcessEngineService getProcessEngineService() {
        return this;
    }
    
    @Override
    public ProcessApplicationService getProcessApplicationService() {
        return this;
    }
    
    @Override
    public ExecutorService getExecutorService() {
        return this.serviceContainer.getServiceValue(ServiceTypes.BPM_PLATFORM, "executor-service");
    }
    
    @Override
    public ProcessEngine getDefaultProcessEngine() {
        return this.serviceContainer.getServiceValue(ServiceTypes.PROCESS_ENGINE, "default");
    }
    
    @Override
    public ProcessEngine getProcessEngine(final String name) {
        return this.serviceContainer.getServiceValue(ServiceTypes.PROCESS_ENGINE, name);
    }
    
    @Override
    public List<ProcessEngine> getProcessEngines() {
        return this.serviceContainer.getServiceValuesByType(ServiceTypes.PROCESS_ENGINE);
    }
    
    @Override
    public Set<String> getProcessEngineNames() {
        final Set<String> processEngineNames = new HashSet<String>();
        final List<ProcessEngine> processEngines = this.getProcessEngines();
        for (final ProcessEngine processEngine : processEngines) {
            processEngineNames.add(processEngine.getName());
        }
        return processEngineNames;
    }
    
    @Override
    public Set<String> getProcessApplicationNames() {
        final List<JmxManagedProcessApplication> processApplications = this.serviceContainer.getServiceValuesByType(ServiceTypes.PROCESS_APPLICATION);
        final Set<String> processApplicationNames = new HashSet<String>();
        for (final JmxManagedProcessApplication jmxManagedProcessApplication : processApplications) {
            processApplicationNames.add(jmxManagedProcessApplication.getProcessApplicationName());
        }
        return processApplicationNames;
    }
    
    @Override
    public ProcessApplicationInfo getProcessApplicationInfo(final String processApplicationName) {
        final JmxManagedProcessApplication processApplicationService = this.serviceContainer.getServiceValue(ServiceTypes.PROCESS_APPLICATION, processApplicationName);
        if (processApplicationService == null) {
            return null;
        }
        return processApplicationService.getProcessApplicationInfo();
    }
    
    @Override
    public ProcessApplicationReference getDeployedProcessApplication(final String processApplicationName) {
        final JmxManagedProcessApplication processApplicationService = this.serviceContainer.getServiceValue(ServiceTypes.PROCESS_APPLICATION, processApplicationName);
        if (processApplicationService == null) {
            return null;
        }
        return processApplicationService.getProcessApplicationReference();
    }
    
    public PlatformServiceContainer getServiceContainer() {
        return this.serviceContainer;
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
}
