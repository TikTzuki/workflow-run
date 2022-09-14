// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.jmx.services;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.container.impl.deployment.util.DeployedProcessArchive;
import java.util.Map;
import org.zik.bpm.application.impl.metadata.spi.ProcessesXml;
import java.util.List;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.application.impl.ProcessApplicationInfoImpl;
import org.zik.bpm.container.impl.spi.PlatformService;

public class JmxManagedProcessApplication implements PlatformService<JmxManagedProcessApplication>, JmxManagedProcessApplicationMBean
{
    protected final ProcessApplicationInfoImpl processApplicationInfo;
    protected final ProcessApplicationReference processApplicationReference;
    protected List<ProcessesXml> processesXmls;
    protected Map<String, DeployedProcessArchive> deploymentMap;
    
    public JmxManagedProcessApplication(final ProcessApplicationInfoImpl processApplicationInfo, final ProcessApplicationReference processApplicationReference) {
        this.processApplicationInfo = processApplicationInfo;
        this.processApplicationReference = processApplicationReference;
    }
    
    @Override
    public String getProcessApplicationName() {
        return this.processApplicationInfo.getName();
    }
    
    @Override
    public void start(final PlatformServiceContainer mBeanServiceContainer) {
    }
    
    @Override
    public void stop(final PlatformServiceContainer mBeanServiceContainer) {
    }
    
    @Override
    public JmxManagedProcessApplication getValue() {
        return this;
    }
    
    public void setProcessesXmls(final List<ProcessesXml> processesXmls) {
        this.processesXmls = processesXmls;
    }
    
    public List<ProcessesXml> getProcessesXmls() {
        return this.processesXmls;
    }
    
    public void setDeploymentMap(final Map<String, DeployedProcessArchive> processArchiveDeploymentMap) {
        this.deploymentMap = processArchiveDeploymentMap;
    }
    
    public Map<String, DeployedProcessArchive> getProcessArchiveDeploymentMap() {
        return this.deploymentMap;
    }
    
    @Override
    public List<String> getDeploymentIds() {
        final List<String> deploymentIds = new ArrayList<String>();
        for (final DeployedProcessArchive registration : this.deploymentMap.values()) {
            deploymentIds.addAll(registration.getAllDeploymentIds());
        }
        return deploymentIds;
    }
    
    @Override
    public List<String> getDeploymentNames() {
        return new ArrayList<String>(this.deploymentMap.keySet());
    }
    
    public ProcessApplicationInfoImpl getProcessApplicationInfo() {
        return this.processApplicationInfo;
    }
    
    public ProcessApplicationReference getProcessApplicationReference() {
        return this.processApplicationReference;
    }
}
