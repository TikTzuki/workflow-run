// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.application;

import org.zik.bpm.application.ProcessApplicationReference;
import java.util.Set;
import org.zik.bpm.application.ProcessApplicationRegistration;

public class DefaultProcessApplicationRegistration implements ProcessApplicationRegistration
{
    protected Set<String> deploymentIds;
    protected String processEngineName;
    protected ProcessApplicationReference reference;
    
    public DefaultProcessApplicationRegistration(final ProcessApplicationReference reference, final Set<String> deploymentIds, final String processEnginenName) {
        this.reference = reference;
        this.deploymentIds = deploymentIds;
        this.processEngineName = processEnginenName;
    }
    
    @Override
    public Set<String> getDeploymentIds() {
        return this.deploymentIds;
    }
    
    @Override
    public String getProcessEngineName() {
        return this.processEngineName;
    }
    
    public ProcessApplicationReference getReference() {
        return this.reference;
    }
}
