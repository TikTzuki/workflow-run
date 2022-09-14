// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.jmx.services;

import org.zik.bpm.engine.ManagementService;
import java.util.Set;
import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.container.impl.spi.PlatformService;

public class JmxManagedProcessEngine implements PlatformService<ProcessEngine>, JmxManagedProcessEngineMBean
{
    protected ProcessEngine processEngine;
    
    protected JmxManagedProcessEngine() {
    }
    
    public JmxManagedProcessEngine(final ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }
    
    @Override
    public void start(final PlatformServiceContainer contanier) {
    }
    
    @Override
    public void stop(final PlatformServiceContainer container) {
    }
    
    @Override
    public String getName() {
        return this.processEngine.getName();
    }
    
    public ProcessEngine getProcessEngine() {
        return this.processEngine;
    }
    
    @Override
    public ProcessEngine getValue() {
        return this.processEngine;
    }
    
    @Override
    public Set<String> getRegisteredDeployments() {
        final ManagementService managementService = this.processEngine.getManagementService();
        return managementService.getRegisteredDeployments();
    }
    
    @Override
    public void registerDeployment(final String deploymentId) {
        final ManagementService managementService = this.processEngine.getManagementService();
        managementService.registerDeploymentForJobExecutor(deploymentId);
    }
    
    @Override
    public void unregisterDeployment(final String deploymentId) {
        final ManagementService managementService = this.processEngine.getManagementService();
        managementService.unregisterDeploymentForJobExecutor(deploymentId);
    }
    
    @Override
    public void reportDbMetrics() {
        final ManagementService managementService = this.processEngine.getManagementService();
        managementService.reportDbMetricsNow();
    }
}
