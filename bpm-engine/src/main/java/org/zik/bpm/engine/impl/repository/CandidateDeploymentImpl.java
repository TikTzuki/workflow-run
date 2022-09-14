// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.repository;

import java.util.HashMap;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.zik.bpm.engine.repository.Resource;
import java.util.Map;
import org.zik.bpm.engine.repository.CandidateDeployment;

public class CandidateDeploymentImpl implements CandidateDeployment
{
    protected String name;
    protected Map<String, Resource> resources;
    
    public CandidateDeploymentImpl(final String name, final Map<String, Resource> resources) {
        this.name = name;
        this.resources = resources;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public Map<String, Resource> getResources() {
        return this.resources;
    }
    
    public void setResources(final Map<String, Resource> resources) {
        this.resources = resources;
    }
    
    public static CandidateDeploymentImpl fromDeploymentEntity(final DeploymentEntity deploymentEntity) {
        final Map<String, Resource> resources = new HashMap<String, Resource>(deploymentEntity.getResources());
        return new CandidateDeploymentImpl(deploymentEntity.getName(), resources);
    }
}
