// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.repository;

import org.zik.bpm.engine.query.Query;
import java.util.Arrays;
import org.zik.bpm.engine.repository.Deployment;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.repository.ProcessDefinition;
import java.util.HashSet;
import java.util.Set;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.repository.CandidateDeployment;
import org.zik.bpm.engine.repository.Resource;
import org.zik.bpm.engine.RepositoryService;
import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.engine.repository.DeploymentHandler;

public class DefaultDeploymentHandler implements DeploymentHandler
{
    protected ProcessEngine processEngine;
    protected RepositoryService repositoryService;
    
    public DefaultDeploymentHandler(final ProcessEngine processEngine) {
        this.processEngine = processEngine;
        this.repositoryService = processEngine.getRepositoryService();
    }
    
    @Override
    public boolean shouldDeployResource(final Resource newResource, final Resource existingResource) {
        return this.resourcesDiffer(newResource, existingResource);
    }
    
    @Override
    public String determineDuplicateDeployment(final CandidateDeployment candidateDeployment) {
        return Context.getCommandContext().getDeploymentManager().findLatestDeploymentByName(candidateDeployment.getName()).getId();
    }
    
    @Override
    public Set<String> determineDeploymentsToResumeByProcessDefinitionKey(final String[] processDefinitionKeys) {
        final Set<String> deploymentIds = new HashSet<String>();
        final List<ProcessDefinition> processDefinitions = Context.getCommandContext().getProcessDefinitionManager().findProcessDefinitionsByKeyIn(processDefinitionKeys);
        for (final ProcessDefinition processDefinition : processDefinitions) {
            deploymentIds.add(processDefinition.getDeploymentId());
        }
        return deploymentIds;
    }
    
    @Override
    public Set<String> determineDeploymentsToResumeByDeploymentName(final CandidateDeployment candidateDeployment) {
        final List<Deployment> previousDeployments = ((Query<T, Deployment>)this.processEngine.getRepositoryService().createDeploymentQuery().deploymentName(candidateDeployment.getName())).list();
        final Set<String> deploymentIds = new HashSet<String>();
        for (final Deployment deployment : previousDeployments) {
            deploymentIds.add(deployment.getId());
        }
        return deploymentIds;
    }
    
    protected boolean resourcesDiffer(final Resource resource, final Resource existing) {
        final byte[] bytes = resource.getBytes();
        final byte[] savedBytes = existing.getBytes();
        return !Arrays.equals(bytes, savedBytes);
    }
}
