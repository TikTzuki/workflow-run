// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.repository.DecisionRequirementsDefinition;
import org.zik.bpm.engine.repository.DecisionDefinition;
import org.zik.bpm.engine.repository.CaseDefinition;
import org.zik.bpm.engine.repository.ProcessDefinition;
import java.util.List;
import java.util.Date;
import org.zik.bpm.application.ProcessApplicationRegistration;
import org.zik.bpm.engine.repository.DeploymentWithDefinitions;
import org.zik.bpm.engine.repository.ProcessApplicationDeployment;

public class ProcessApplicationDeploymentImpl implements ProcessApplicationDeployment
{
    protected DeploymentWithDefinitions deployment;
    protected ProcessApplicationRegistration registration;
    
    public ProcessApplicationDeploymentImpl(final DeploymentWithDefinitions deployment, final ProcessApplicationRegistration registration) {
        this.deployment = deployment;
        this.registration = registration;
    }
    
    @Override
    public String getId() {
        return this.deployment.getId();
    }
    
    @Override
    public String getName() {
        return this.deployment.getName();
    }
    
    @Override
    public Date getDeploymentTime() {
        return this.deployment.getDeploymentTime();
    }
    
    @Override
    public String getSource() {
        return this.deployment.getSource();
    }
    
    @Override
    public String getTenantId() {
        return this.deployment.getTenantId();
    }
    
    @Override
    public ProcessApplicationRegistration getProcessApplicationRegistration() {
        return this.registration;
    }
    
    @Override
    public List<ProcessDefinition> getDeployedProcessDefinitions() {
        return this.deployment.getDeployedProcessDefinitions();
    }
    
    @Override
    public List<CaseDefinition> getDeployedCaseDefinitions() {
        return this.deployment.getDeployedCaseDefinitions();
    }
    
    @Override
    public List<DecisionDefinition> getDeployedDecisionDefinitions() {
        return this.deployment.getDeployedDecisionDefinitions();
    }
    
    @Override
    public List<DecisionRequirementsDefinition> getDeployedDecisionRequirementsDefinitions() {
        return this.deployment.getDeployedDecisionRequirementsDefinitions();
    }
}
