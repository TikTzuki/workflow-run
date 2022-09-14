// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionEntity;
import org.zik.bpm.engine.repository.DecisionRequirementsDefinition;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionEntity;
import org.zik.bpm.engine.repository.DecisionDefinition;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.zik.bpm.engine.repository.CaseDefinition;
import org.zik.bpm.engine.repository.ProcessDefinition;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import java.util.Iterator;
import org.zik.bpm.engine.impl.context.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Map;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.repository.DeploymentWithDefinitions;
import java.io.Serializable;

public class DeploymentEntity implements Serializable, DeploymentWithDefinitions, DbEntity
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String name;
    protected Map<String, ResourceEntity> resources;
    protected Date deploymentTime;
    protected boolean validatingSchema;
    protected boolean isNew;
    protected String source;
    protected String tenantId;
    protected Map<Class<?>, List> deployedArtifacts;
    
    public DeploymentEntity() {
        this.validatingSchema = true;
    }
    
    public ResourceEntity getResource(final String resourceName) {
        return this.getResources().get(resourceName);
    }
    
    public void addResource(final ResourceEntity resource) {
        if (this.resources == null) {
            this.resources = new HashMap<String, ResourceEntity>();
        }
        this.resources.put(resource.getName(), resource);
    }
    
    public void clearResources() {
        if (this.resources != null) {
            this.resources.clear();
        }
    }
    
    public Map<String, ResourceEntity> getResources() {
        if (this.resources == null && this.id != null) {
            final List<ResourceEntity> resourcesList = Context.getCommandContext().getResourceManager().findResourcesByDeploymentId(this.id);
            this.resources = new HashMap<String, ResourceEntity>();
            for (final ResourceEntity resource : resourcesList) {
                this.resources.put(resource.getName(), resource);
            }
        }
        return this.resources;
    }
    
    @Override
    public Object getPersistentState() {
        return DeploymentEntity.class;
    }
    
    public void addDeployedArtifact(final ResourceDefinitionEntity deployedArtifact) {
        if (this.deployedArtifacts == null) {
            this.deployedArtifacts = new HashMap<Class<?>, List>();
        }
        final Class<?> clazz = deployedArtifact.getClass();
        List artifacts = this.deployedArtifacts.get(clazz);
        if (artifacts == null) {
            artifacts = new ArrayList();
            this.deployedArtifacts.put(clazz, artifacts);
        }
        artifacts.add(deployedArtifact);
    }
    
    public Map<Class<?>, List> getDeployedArtifacts() {
        return this.deployedArtifacts;
    }
    
    public <T> List<T> getDeployedArtifacts(final Class<T> clazz) {
        if (this.deployedArtifacts == null) {
            return null;
        }
        return this.deployedArtifacts.get(clazz);
    }
    
    public void removeArtifact(final ResourceDefinitionEntity notDeployedArtifact) {
        if (this.deployedArtifacts != null) {
            final List artifacts = this.deployedArtifacts.get(notDeployedArtifact.getClass());
            if (artifacts != null) {
                artifacts.remove(notDeployedArtifact);
                if (artifacts.isEmpty()) {
                    this.deployedArtifacts.remove(notDeployedArtifact.getClass());
                }
            }
        }
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public void setId(final String id) {
        this.id = id;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setResources(final Map<String, ResourceEntity> resources) {
        this.resources = resources;
    }
    
    @Override
    public Date getDeploymentTime() {
        return this.deploymentTime;
    }
    
    public void setDeploymentTime(final Date deploymentTime) {
        this.deploymentTime = deploymentTime;
    }
    
    public boolean isValidatingSchema() {
        return this.validatingSchema;
    }
    
    public void setValidatingSchema(final boolean validatingSchema) {
        this.validatingSchema = validatingSchema;
    }
    
    public boolean isNew() {
        return this.isNew;
    }
    
    public void setNew(final boolean isNew) {
        this.isNew = isNew;
    }
    
    @Override
    public String getSource() {
        return this.source;
    }
    
    public void setSource(final String source) {
        this.source = source;
    }
    
    @Override
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public List<ProcessDefinition> getDeployedProcessDefinitions() {
        return (this.deployedArtifacts == null) ? null : this.deployedArtifacts.get(ProcessDefinitionEntity.class);
    }
    
    @Override
    public List<CaseDefinition> getDeployedCaseDefinitions() {
        return (this.deployedArtifacts == null) ? null : this.deployedArtifacts.get(CaseDefinitionEntity.class);
    }
    
    @Override
    public List<DecisionDefinition> getDeployedDecisionDefinitions() {
        return (this.deployedArtifacts == null) ? null : this.deployedArtifacts.get(DecisionDefinitionEntity.class);
    }
    
    @Override
    public List<DecisionRequirementsDefinition> getDeployedDecisionRequirementsDefinitions() {
        return (this.deployedArtifacts == null) ? null : this.deployedArtifacts.get(DecisionRequirementsDefinitionEntity.class);
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.id + ", name=" + this.name + ", resources=" + this.resources + ", deploymentTime=" + this.deploymentTime + ", validatingSchema=" + this.validatingSchema + ", isNew=" + this.isNew + ", source=" + this.source + ", tenantId=" + this.tenantId + "]";
    }
}
