// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment.util;

import org.zik.bpm.application.ProcessApplicationRegistration;
import org.zik.bpm.engine.repository.ProcessApplicationDeployment;
import java.util.Set;

public class DeployedProcessArchive
{
    protected String primaryDeploymentId;
    protected Set<String> allDeploymentIds;
    protected String processEngineName;
    
    public DeployedProcessArchive(final ProcessApplicationDeployment deployment) {
        this.primaryDeploymentId = deployment.getId();
        final ProcessApplicationRegistration registration = deployment.getProcessApplicationRegistration();
        this.allDeploymentIds = registration.getDeploymentIds();
        this.processEngineName = registration.getProcessEngineName();
    }
    
    public String getPrimaryDeploymentId() {
        return this.primaryDeploymentId;
    }
    
    public void setPrimaryDeploymentId(final String primaryDeploymentId) {
        this.primaryDeploymentId = primaryDeploymentId;
    }
    
    public Set<String> getAllDeploymentIds() {
        return this.allDeploymentIds;
    }
    
    public void setAllDeploymentIds(final Set<String> allDeploymentIds) {
        this.allDeploymentIds = allDeploymentIds;
    }
    
    public String getProcessEngineName() {
        return this.processEngineName;
    }
    
    public void setProcessEngineName(final String processEngineName) {
        this.processEngineName = processEngineName;
    }
}
