// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl;

import java.util.Map;
import org.zik.bpm.application.ProcessApplicationDeploymentInfo;
import java.util.List;
import org.zik.bpm.application.ProcessApplicationInfo;

public class ProcessApplicationInfoImpl implements ProcessApplicationInfo
{
    protected String name;
    protected List<ProcessApplicationDeploymentInfo> deploymentInfo;
    protected Map<String, String> properties;
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public List<ProcessApplicationDeploymentInfo> getDeploymentInfo() {
        return this.deploymentInfo;
    }
    
    public void setDeploymentInfo(final List<ProcessApplicationDeploymentInfo> deploymentInfo) {
        this.deploymentInfo = deploymentInfo;
    }
    
    @Override
    public Map<String, String> getProperties() {
        return this.properties;
    }
    
    public void setProperties(final Map<String, String> properties) {
        this.properties = properties;
    }
}
