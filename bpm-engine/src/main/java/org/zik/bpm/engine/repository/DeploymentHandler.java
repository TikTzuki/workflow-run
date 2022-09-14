// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

import java.util.Set;

public interface DeploymentHandler
{
    boolean shouldDeployResource(final Resource p0, final Resource p1);
    
    String determineDuplicateDeployment(final CandidateDeployment p0);
    
    Set<String> determineDeploymentsToResumeByProcessDefinitionKey(final String[] p0);
    
    Set<String> determineDeploymentsToResumeByDeploymentName(final CandidateDeployment p0);
}
