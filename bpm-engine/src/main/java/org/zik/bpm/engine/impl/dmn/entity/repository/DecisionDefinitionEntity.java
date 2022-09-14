// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.entity.repository;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.repository.ResourceDefinition;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.context.Context;
import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import java.io.Serializable;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.repository.DecisionDefinition;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionImpl;

public class DecisionDefinitionEntity extends DmnDecisionImpl implements DecisionDefinition, ResourceDefinitionEntity<DecisionDefinitionEntity>, DbEntity, HasDbRevision, Serializable
{
    private static final long serialVersionUID = 1L;
    protected static final EnginePersistenceLogger LOG;
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
    protected String decisionRequirementsDefinitionId;
    protected String decisionRequirementsDefinitionKey;
    protected boolean firstVersion;
    protected String previousDecisionDefinitionId;
    protected Integer historyTimeToLive;
    protected String versionTag;
    
    public DecisionDefinitionEntity() {
        this.revision = 1;
        this.firstVersion = false;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public int getRevision() {
        return this.revision;
    }
    
    public void setRevision(final int revision) {
        this.revision = revision;
    }
    
    public int getRevisionNext() {
        return this.revision + 1;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getCategory() {
        return this.category;
    }
    
    public void setCategory(final String category) {
        this.category = category;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public void setKey(final String key) {
        this.key = key;
    }
    
    public int getVersion() {
        return this.version;
    }
    
    public void setVersion(final int version) {
        this.version = version;
        this.firstVersion = (this.version == 1);
    }
    
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    public void setDeploymentId(final String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
    public String getResourceName() {
        return this.resourceName;
    }
    
    public void setResourceName(final String resourceName) {
        this.resourceName = resourceName;
    }
    
    public String getDiagramResourceName() {
        return this.diagramResourceName;
    }
    
    public void setDiagramResourceName(final String diagramResourceName) {
        this.diagramResourceName = diagramResourceName;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getDecisionRequirementsDefinitionId() {
        return this.decisionRequirementsDefinitionId;
    }
    
    public void setDecisionRequirementsDefinitionId(final String decisionRequirementsDefinitionId) {
        this.decisionRequirementsDefinitionId = decisionRequirementsDefinitionId;
    }
    
    public String getDecisionRequirementsDefinitionKey() {
        return this.decisionRequirementsDefinitionKey;
    }
    
    public void setDecisionRequirementsDefinitionKey(final String decisionRequirementsDefinitionKey) {
        this.decisionRequirementsDefinitionKey = decisionRequirementsDefinitionKey;
    }
    
    public Object getPersistentState() {
        final Map<String, Object> persistentState = new HashMap<String, Object>();
        persistentState.put("historyTimeToLive", this.historyTimeToLive);
        return persistentState;
    }
    
    public void updateModifiableFieldsFromEntity(final DecisionDefinitionEntity updatingDecisionDefinition) {
        if (this.key.equals(updatingDecisionDefinition.key) && this.deploymentId.equals(updatingDecisionDefinition.deploymentId)) {
            this.revision = updatingDecisionDefinition.revision;
            this.historyTimeToLive = updatingDecisionDefinition.historyTimeToLive;
        }
        else {
            DecisionDefinitionEntity.LOG.logUpdateUnrelatedDecisionDefinitionEntity(this.key, updatingDecisionDefinition.key, this.deploymentId, updatingDecisionDefinition.deploymentId);
        }
    }
    
    public DecisionDefinitionEntity getPreviousDefinition() {
        DecisionDefinitionEntity previousDecisionDefinition = null;
        String previousDecisionDefinitionId = this.getPreviousDecisionDefinitionId();
        if (previousDecisionDefinitionId != null) {
            previousDecisionDefinition = this.loadDecisionDefinition(previousDecisionDefinitionId);
            if (previousDecisionDefinition == null) {
                this.resetPreviousDecisionDefinitionId();
                previousDecisionDefinitionId = this.getPreviousDecisionDefinitionId();
                if (previousDecisionDefinitionId != null) {
                    previousDecisionDefinition = this.loadDecisionDefinition(previousDecisionDefinitionId);
                }
            }
        }
        return previousDecisionDefinition;
    }
    
    protected DecisionDefinitionEntity loadDecisionDefinition(final String decisionDefinitionId) {
        final ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
        final DeploymentCache deploymentCache = configuration.getDeploymentCache();
        DecisionDefinitionEntity decisionDefinition = deploymentCache.findDecisionDefinitionFromCache(decisionDefinitionId);
        if (decisionDefinition == null) {
            final CommandContext commandContext = Context.getCommandContext();
            final DecisionDefinitionManager decisionDefinitionManager = commandContext.getDecisionDefinitionManager();
            decisionDefinition = decisionDefinitionManager.findDecisionDefinitionById(decisionDefinitionId);
            if (decisionDefinition != null) {
                decisionDefinition = deploymentCache.resolveDecisionDefinition(decisionDefinition);
            }
        }
        return decisionDefinition;
    }
    
    public String getPreviousDecisionDefinitionId() {
        this.ensurePreviousDecisionDefinitionIdInitialized();
        return this.previousDecisionDefinitionId;
    }
    
    public void setPreviousDecisionDefinitionId(final String previousDecisionDefinitionId) {
        this.previousDecisionDefinitionId = previousDecisionDefinitionId;
    }
    
    protected void resetPreviousDecisionDefinitionId() {
        this.previousDecisionDefinitionId = null;
        this.ensurePreviousDecisionDefinitionIdInitialized();
    }
    
    protected void ensurePreviousDecisionDefinitionIdInitialized() {
        if (this.previousDecisionDefinitionId == null && !this.firstVersion) {
            this.previousDecisionDefinitionId = Context.getCommandContext().getDecisionDefinitionManager().findPreviousDecisionDefinitionId(this.key, this.version, this.tenantId);
            if (this.previousDecisionDefinitionId == null) {
                this.firstVersion = true;
            }
        }
    }
    
    public Integer getHistoryTimeToLive() {
        return this.historyTimeToLive;
    }
    
    public void setHistoryTimeToLive(final Integer historyTimeToLive) {
        this.historyTimeToLive = historyTimeToLive;
    }
    
    public String getVersionTag() {
        return this.versionTag;
    }
    
    public void setVersionTag(final String versionTag) {
        this.versionTag = versionTag;
    }
    
    public String toString() {
        return "DecisionDefinitionEntity{id='" + this.id + '\'' + ", name='" + this.name + '\'' + ", category='" + this.category + '\'' + ", key='" + this.key + '\'' + ", version=" + this.version + ", versionTag=" + this.versionTag + ", decisionRequirementsDefinitionId='" + this.decisionRequirementsDefinitionId + '\'' + ", decisionRequirementsDefinitionKey='" + this.decisionRequirementsDefinitionKey + '\'' + ", deploymentId='" + this.deploymentId + '\'' + ", tenantId='" + this.tenantId + '\'' + ", historyTimeToLive=" + this.historyTimeToLive + '}';
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
