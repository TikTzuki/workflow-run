// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl;

import org.zik.bpm.application.ProcessApplicationDeploymentInfo;

public class ProcessApplicationDeploymentInfoImpl implements ProcessApplicationDeploymentInfo
{
    protected String processEngineName;
    protected String deploymentId;
    
    @Override
    public String getProcessEngineName() {
        return this.processEngineName;
    }
    
    public void setProcessEngineName(final String processEngineName) {
        this.processEngineName = processEngineName;
    }
    
    @Override
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    public void setDeploymentId(final String deploymentId) {
        this.deploymentId = deploymentId;
    }
}
