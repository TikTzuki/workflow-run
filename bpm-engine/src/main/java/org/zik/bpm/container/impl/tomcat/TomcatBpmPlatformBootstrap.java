// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.tomcat;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.container.impl.deployment.UnregisterBpmPlatformPluginsStep;
import org.zik.bpm.container.impl.deployment.StopProcessEnginesStep;
import org.zik.bpm.container.impl.deployment.StopProcessApplicationsStep;
import org.zik.bpm.container.impl.deployment.jobexecutor.StopManagedThreadPoolStep;
import org.zik.bpm.container.impl.deployment.jobexecutor.StopJobExecutorStep;
import org.zik.bpm.container.impl.deployment.PlatformXmlStartProcessEnginesStep;
import org.zik.bpm.container.impl.deployment.jobexecutor.StartJobExecutorStep;
import org.zik.bpm.container.impl.deployment.jobexecutor.StartManagedThreadPoolStep;
import org.zik.bpm.container.impl.deployment.DiscoverBpmPlatformPluginsStep;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;
import org.zik.bpm.container.impl.tomcat.deployment.TomcatParseBpmPlatformXmlStep;
import org.apache.catalina.core.StandardServer;
import org.zik.bpm.container.RuntimeContainerDelegate;
import org.apache.catalina.LifecycleEvent;
import org.zik.bpm.container.impl.RuntimeContainerDelegateImpl;
import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;
import org.apache.catalina.LifecycleListener;

public class TomcatBpmPlatformBootstrap implements LifecycleListener
{
    private static final ContainerIntegrationLogger LOG;
    protected ProcessEngine processEngine;
    protected RuntimeContainerDelegateImpl containerDelegate;
    
    public void lifecycleEvent(final LifecycleEvent event) {
        if ("start".equals(event.getType())) {
            this.containerDelegate = (RuntimeContainerDelegateImpl)RuntimeContainerDelegate.INSTANCE.get();
            this.deployBpmPlatform(event);
        }
        else if ("stop".equals(event.getType())) {
            this.undeployBpmPlatform(event);
        }
    }
    
    protected void deployBpmPlatform(final LifecycleEvent event) {
        final StandardServer server = (StandardServer)event.getSource();
        this.containerDelegate.getServiceContainer().createDeploymentOperation("deploy BPM platform").addAttachment("server", server).addStep(new TomcatParseBpmPlatformXmlStep()).addStep(new DiscoverBpmPlatformPluginsStep()).addStep(new StartManagedThreadPoolStep()).addStep(new StartJobExecutorStep()).addStep(new PlatformXmlStartProcessEnginesStep()).execute();
        TomcatBpmPlatformBootstrap.LOG.camundaBpmPlatformSuccessfullyStarted(server.getServerInfo());
    }
    
    protected void undeployBpmPlatform(final LifecycleEvent event) {
        final StandardServer server = (StandardServer)event.getSource();
        this.containerDelegate.getServiceContainer().createUndeploymentOperation("undeploy BPM platform").addAttachment("server", server).addStep(new StopJobExecutorStep()).addStep(new StopManagedThreadPoolStep()).addStep(new StopProcessApplicationsStep()).addStep(new StopProcessEnginesStep()).addStep(new UnregisterBpmPlatformPluginsStep()).execute();
        TomcatBpmPlatformBootstrap.LOG.camundaBpmPlatformStopped(server.getServerInfo());
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
}
