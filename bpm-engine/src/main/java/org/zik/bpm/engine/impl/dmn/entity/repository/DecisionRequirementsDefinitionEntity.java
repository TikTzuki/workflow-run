// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.entity.repository;

import org.zik.bpm.engine.repository.ResourceDefinition;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.context.Context;
import java.io.Serializable;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.repository.DecisionRequirementsDefinition;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionRequirementsGraphImpl;

public class DecisionRequirementsDefinitionEntity extends DmnDecisionRequirementsGraphImpl implements DecisionRequirementsDefinition, ResourceDefinitionEntity<DecisionRequirementsDefinitionEntity>, DbEntity, HasDbRevision, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected int revision;
    protected String name;
    protected String category;
    protected String key;
    protected int version;
    protected String deploymentId;
    protected String resourceName;
    protected String diagramResourceName;
    protected String tenantId;
    protected boolean firstVersion;
    protected String previousDecisionRequirementsDefinitionId;
    
    public DecisionRequirementsDefinitionEntity() {
        this.revision = 1;
        this.firstVersion = false;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getCategory() {
        return this.category;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public int getVersion() {
        return this.version;
    }
    
    public String getResourceName() {
        return this.resourceName;
    }
    
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    public String getDiagramResourceName() {
        return this.diagramResourceName;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public Integer getHistoryTimeToLive() {
        return null;
    }
    
    public void setRevision(final int revision) {
        this.revision = revision;
    }
    
    public int getRevision() {
        return this.revision;
    }
    
    public int getRevisionNext() {
        return this.revision + 1;
    }
    
    public Object getPersistentState() {
        return DecisionRequirementsDefinitionEntity.class;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setCategory(final String category) {
        this.category = category;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setKey(final String key) {
        this.key = key;
    }
    
    public void setVersion(final int version) {
        this.version = version;
    }
    
    public void setResourceName(final String resourceName) {
        this.resourceName = resourceName;
    }
    
    public void setDeploymentId(final String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
    public void setDiagramResourceName(final String diagramResourceName) {
        this.diagramResourceName = diagramResourceName;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    public ResourceDefinitionEntity getPreviousDefinition() {
        DecisionRequirementsDefinitionEntity previousDecisionDefinition = null;
        String previousDecisionDefinitionId = this.getPreviousDecisionRequirementsDefinitionId();
        if (previousDecisionDefinitionId != null) {
            previousDecisionDefinition = this.loadDecisionRequirementsDefinition(previousDecisionDefinitionId);
            if (previousDecisionDefinition == null) {
                this.resetPreviousDecisionRequirementsDefinitionId();
                previousDecisionDefinitionId = this.getPreviousDecisionRequirementsDefinitionId();
                if (previousDecisionDefinitionId != null) {
                    previousDecisionDefinition = this.loadDecisionRequirementsDefinition(previousDecisionDefinitionId);
                }
            }
        }
        return previousDecisionDefinition;
    }
    
    public void updateModifiableFieldsFromEntity(final DecisionRequirementsDefinitionEntity updatingDefinition) {
    }
    
    protected DecisionRequirementsDefinitionEntity loadDecisionRequirementsDefinition(final String decisionRequirementsDefinitionId) {
        final ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
        final DeploymentCache deploymentCache = configuration.getDeploymentCache();
        DecisionRequirementsDefinitionEntity decisionRequirementsDefinition = deploymentCache.findDecisionRequirementsDefinitionFromCache(decisionRequirementsDefinitionId);
        if (decisionRequirementsDefinition == null) {
            final CommandContext commandContext = Context.getCommandContext();
            final DecisionRequirementsDefinitionManager manager = commandContext.getDecisionRequirementsDefinitionManager();
            decisionRequirementsDefinition = manager.findDecisionRequirementsDefinitionById(decisionRequirementsDefinitionId);
            if (decisionRequirementsDefinition != null) {
                decisionRequirementsDefinition = deploymentCache.resolveDecisionRequirementsDefinition(decisionRequirementsDefinition);
            }
        }
        return decisionRequirementsDefinition;
    }
    
    public String getPreviousDecisionRequirementsDefinitionId() {
        this.ensurePreviousDecisionRequirementsDefinitionIdInitialized();
        return this.previousDecisionRequirementsDefinitionId;
    }
    
    public void setPreviousDecisionDefinitionId(final String previousDecisionDefinitionId) {
        this.previousDecisionRequirementsDefinitionId = previousDecisionDefinitionId;
    }
    
    protected void resetPreviousDecisionRequirementsDefinitionId() {
        this.previousDecisionRequirementsDefinitionId = null;
        this.ensurePreviousDecisionRequirementsDefinitionIdInitialized();
    }
    
    protected void ensurePreviousDecisionRequirementsDefinitionIdInitialized() {
        if (this.previousDecisionRequirementsDefinitionId == null && !this.firstVersion) {
            this.previousDecisionRequirementsDefinitionId = Context.getCommandContext().getDecisionRequirementsDefinitionManager().findPreviousDecisionRequirementsDefinitionId(this.key, this.version, this.tenantId);
            if (this.previousDecisionRequirementsDefinitionId == null) {
                this.firstVersion = true;
            }
        }
    }
    
    public void setHistoryTimeToLive(final Integer historyTimeToLive) {
        throw new UnsupportedOperationException();
    }
    
    public String toString() {
        return "DecisionRequirementsDefinitionEntity [id=" + this.id + ", revision=" + this.revision + ", name=" + this.name + ", category=" + this.category + ", key=" + this.key + ", version=" + this.version + ", deploymentId=" + this.deploymentId + ", tenantId=" + this.tenantId + "]";
    }
}
