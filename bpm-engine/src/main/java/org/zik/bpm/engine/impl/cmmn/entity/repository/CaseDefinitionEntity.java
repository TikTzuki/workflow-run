// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.entity.repository;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.repository.ResourceDefinition;
import java.util.HashMap;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.task.TaskDefinition;
import java.util.Map;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.repository.CaseDefinition;
import org.zik.bpm.engine.impl.cmmn.model.CmmnCaseDefinition;

public class CaseDefinitionEntity extends CmmnCaseDefinition implements CaseDefinition, ResourceDefinitionEntity<CaseDefinitionEntity>, DbEntity, HasDbRevision
{
    private static final long serialVersionUID = 1L;
    protected static final EnginePersistenceLogger LOG;
    protected int revision;
    protected String category;
    protected String key;
    protected int version;
    protected String deploymentId;
    protected String resourceName;
    protected String diagramResourceName;
    protected String tenantId;
    protected Integer historyTimeToLive;
    protected Map<String, TaskDefinition> taskDefinitions;
    protected boolean firstVersion;
    protected String previousCaseDefinitionId;
    
    public CaseDefinitionEntity() {
        super(null);
        this.revision = 1;
        this.firstVersion = false;
    }
    
    @Override
    public int getRevision() {
        return this.revision;
    }
    
    @Override
    public void setRevision(final int revision) {
        this.revision = revision;
    }
    
    @Override
    public int getRevisionNext() {
        return this.revision + 1;
    }
    
    @Override
    public String getCategory() {
        return this.category;
    }
    
    @Override
    public void setCategory(final String category) {
        this.category = category;
    }
    
    @Override
    public String getKey() {
        return this.key;
    }
    
    @Override
    public void setKey(final String key) {
        this.key = key;
    }
    
    @Override
    public int getVersion() {
        return this.version;
    }
    
    @Override
    public void setVersion(final int version) {
        this.version = version;
        this.firstVersion = (this.version == 1);
    }
    
    @Override
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    @Override
    public void setDeploymentId(final String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
    @Override
    public String getResourceName() {
        return this.resourceName;
    }
    
    @Override
    public void setResourceName(final String resourceName) {
        this.resourceName = resourceName;
    }
    
    @Override
    public String getDiagramResourceName() {
        return this.diagramResourceName;
    }
    
    @Override
    public void setDiagramResourceName(final String diagramResourceName) {
        this.diagramResourceName = diagramResourceName;
    }
    
    public Map<String, TaskDefinition> getTaskDefinitions() {
        return this.taskDefinitions;
    }
    
    public void setTaskDefinitions(final Map<String, TaskDefinition> taskDefinitions) {
        this.taskDefinitions = taskDefinitions;
    }
    
    @Override
    public String getTenantId() {
        return this.tenantId;
    }
    
    @Override
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public Integer getHistoryTimeToLive() {
        return this.historyTimeToLive;
    }
    
    @Override
    public void setHistoryTimeToLive(final Integer historyTimeToLive) {
        this.historyTimeToLive = historyTimeToLive;
    }
    
    @Override
    public CaseDefinitionEntity getPreviousDefinition() {
        CaseDefinitionEntity previousCaseDefinition = null;
        String previousCaseDefinitionId = this.getPreviousCaseDefinitionId();
        if (previousCaseDefinitionId != null) {
            previousCaseDefinition = this.loadCaseDefinition(previousCaseDefinitionId);
            if (previousCaseDefinition == null) {
                this.resetPreviousCaseDefinitionId();
                previousCaseDefinitionId = this.getPreviousCaseDefinitionId();
                if (previousCaseDefinitionId != null) {
                    previousCaseDefinition = this.loadCaseDefinition(previousCaseDefinitionId);
                }
            }
        }
        return previousCaseDefinition;
    }
    
    protected CaseDefinitionEntity loadCaseDefinition(final String caseDefinitionId) {
        final ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
        final DeploymentCache deploymentCache = configuration.getDeploymentCache();
        CaseDefinitionEntity caseDefinition = deploymentCache.findCaseDefinitionFromCache(caseDefinitionId);
        if (caseDefinition == null) {
            final CommandContext commandContext = Context.getCommandContext();
            final CaseDefinitionManager caseDefinitionManager = commandContext.getCaseDefinitionManager();
            caseDefinition = caseDefinitionManager.findCaseDefinitionById(caseDefinitionId);
            if (caseDefinition != null) {
                caseDefinition = deploymentCache.resolveCaseDefinition(caseDefinition);
            }
        }
        return caseDefinition;
    }
    
    protected String getPreviousCaseDefinitionId() {
        this.ensurePreviousCaseDefinitionIdInitialized();
        return this.previousCaseDefinitionId;
    }
    
    protected void setPreviousCaseDefinitionId(final String previousCaseDefinitionId) {
        this.previousCaseDefinitionId = previousCaseDefinitionId;
    }
    
    protected void resetPreviousCaseDefinitionId() {
        this.previousCaseDefinitionId = null;
        this.ensurePreviousCaseDefinitionIdInitialized();
    }
    
    protected void ensurePreviousCaseDefinitionIdInitialized() {
        if (this.previousCaseDefinitionId == null && !this.firstVersion) {
            this.previousCaseDefinitionId = Context.getCommandContext().getCaseDefinitionManager().findPreviousCaseDefinitionId(this.key, this.version, this.tenantId);
            if (this.previousCaseDefinitionId == null) {
                this.firstVersion = true;
            }
        }
    }
    
    @Override
    protected CmmnExecution newCaseInstance() {
        final CaseExecutionEntity caseInstance = new CaseExecutionEntity();
        if (this.tenantId != null) {
            caseInstance.setTenantId(this.tenantId);
        }
        Context.getCommandContext().getCaseExecutionManager().insertCaseExecution(caseInstance);
        return caseInstance;
    }
    
    @Override
    public Object getPersistentState() {
        final Map<String, Object> persistentState = new HashMap<String, Object>();
        persistentState.put("historyTimeToLive", this.historyTimeToLive);
        return persistentState;
    }
    
    @Override
    public String toString() {
        return "CaseDefinitionEntity[" + this.id + "]";
    }
    
    @Override
    public void updateModifiableFieldsFromEntity(final CaseDefinitionEntity updatingCaseDefinition) {
        if (this.key.equals(updatingCaseDefinition.key) && this.deploymentId.equals(updatingCaseDefinition.deploymentId)) {
            this.revision = updatingCaseDefinition.revision;
            this.historyTimeToLive = updatingCaseDefinition.historyTimeToLive;
        }
        else {
            CaseDefinitionEntity.LOG.logUpdateUnrelatedCaseDefinitionEntity(this.key, updatingCaseDefinition.key, this.deploymentId, updatingCaseDefinition.deploymentId);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
