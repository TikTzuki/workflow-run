// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

import java.util.List;

public interface DeploymentWithDefinitions extends Deployment
{
    List<ProcessDefinition> getDeployedProcessDefinitions();
    
    List<CaseDefinition> getDeployedCaseDefinitions();
    
    List<DecisionDefinition> getDeployedDecisionDefinitions();
    
    List<DecisionRequirementsDefinition> getDeployedDecisionRequirementsDefinitions();
}
