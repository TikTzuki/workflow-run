// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.repository.ResourceDefinition;
import org.zik.bpm.engine.impl.pvm.PvmProcessInstance;
import java.util.HashMap;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import java.util.Iterator;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import java.util.HashSet;
import java.util.ArrayList;
import org.zik.bpm.engine.delegate.Expression;
import java.util.Set;
import java.util.List;
import org.zik.bpm.engine.impl.task.TaskDefinition;
import java.util.Map;
import org.zik.bpm.engine.impl.form.FormDefinition;
import org.zik.bpm.engine.impl.form.handler.StartFormHandler;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;

public class ProcessDefinitionEntity extends ProcessDefinitionImpl implements ProcessDefinition, ResourceDefinitionEntity<ProcessDefinitionEntity>, DbEntity, HasDbRevision
{
    private static final long serialVersionUID = 1L;
    protected static final EnginePersistenceLogger LOG;
    protected String key;
    protected int revision;
    protected int version;
    protected String category;
    protected String deploymentId;
    protected String resourceName;
    protected Integer historyLevel;
    protected StartFormHandler startFormHandler;
    protected FormDefinition startFormDefinition;
    protected String diagramResourceName;
    protected boolean isGraphicalNotationDefined;
    protected Map<String, TaskDefinition> taskDefinitions;
    protected boolean hasStartFormKey;
    protected int suspensionState;
    protected String tenantId;
    protected String versionTag;
    protected Integer historyTimeToLive;
    protected boolean isIdentityLinksInitialized;
    protected List<IdentityLinkEntity> definitionIdentityLinkEntities;
    protected Set<Expression> candidateStarterUserIdExpressions;
    protected Set<Expression> candidateStarterGroupIdExpressions;
    protected boolean isStartableInTasklist;
    protected boolean firstVersion;
    protected String previousProcessDefinitionId;
    
    public ProcessDefinitionEntity() {
        super(null);
        this.revision = 1;
        this.suspensionState = SuspensionState.ACTIVE.getStateCode();
        this.isIdentityLinksInitialized = false;
        this.definitionIdentityLinkEntities = new ArrayList<IdentityLinkEntity>();
        this.candidateStarterUserIdExpressions = new HashSet<Expression>();
        this.candidateStarterGroupIdExpressions = new HashSet<Expression>();
        this.firstVersion = false;
    }
    
    protected void ensureNotSuspended() {
        if (this.isSuspended()) {
            throw ProcessDefinitionEntity.LOG.suspendedEntityException("Process Definition", this.id);
        }
    }
    
    @Override
    public ExecutionEntity createProcessInstance() {
        return (ExecutionEntity)super.createProcessInstance();
    }
    
    @Override
    public ExecutionEntity createProcessInstance(final String businessKey) {
        return (ExecutionEntity)super.createProcessInstance(businessKey);
    }
    
    @Override
    public ExecutionEntity createProcessInstance(final String businessKey, final String caseInstanceId) {
        return (ExecutionEntity)super.createProcessInstance(businessKey, caseInstanceId);
    }
    
    @Override
    public ExecutionEntity createProcessInstance(final String businessKey, final ActivityImpl initial) {
        return (ExecutionEntity)super.createProcessInstance(businessKey, initial);
    }
    
    @Override
    protected PvmExecutionImpl newProcessInstance() {
        final ExecutionEntity newExecution = ExecutionEntity.createNewExecution();
        if (this.tenantId != null) {
            newExecution.setTenantId(this.tenantId);
        }
        return newExecution;
    }
    
    @Override
    public ExecutionEntity createProcessInstance(final String businessKey, final String caseInstanceId, final ActivityImpl initial) {
        this.ensureNotSuspended();
        final ExecutionEntity processInstance = (ExecutionEntity)this.createProcessInstanceForInitial(initial);
        processInstance.setProcessDefinition(this.processDefinition);
        processInstance.setProcessInstance(processInstance);
        if (businessKey != null) {
            processInstance.setBusinessKey(businessKey);
        }
        if (caseInstanceId != null) {
            processInstance.setCaseInstanceId(caseInstanceId);
        }
        if (this.tenantId != null) {
            processInstance.setTenantId(this.tenantId);
        }
        return processInstance;
    }
    
    public IdentityLinkEntity addIdentityLink(final String userId, final String groupId) {
        final IdentityLinkEntity identityLinkEntity = IdentityLinkEntity.newIdentityLink();
        this.getIdentityLinks().add(identityLinkEntity);
        identityLinkEntity.setProcessDef(this);
        identityLinkEntity.setUserId(userId);
        identityLinkEntity.setGroupId(groupId);
        identityLinkEntity.setType("candidate");
        identityLinkEntity.setTenantId(this.getTenantId());
        identityLinkEntity.insert();
        return identityLinkEntity;
    }
    
    public void deleteIdentityLink(final String userId, final String groupId) {
        final List<IdentityLinkEntity> identityLinks = Context.getCommandContext().getIdentityLinkManager().findIdentityLinkByProcessDefinitionUserAndGroup(this.id, userId, groupId);
        for (final IdentityLinkEntity identityLink : identityLinks) {
            identityLink.delete();
        }
    }
    
    public List<IdentityLinkEntity> getIdentityLinks() {
        if (!this.isIdentityLinksInitialized) {
            this.definitionIdentityLinkEntities = Context.getCommandContext().getIdentityLinkManager().findIdentityLinksByProcessDefinitionId(this.id);
            this.isIdentityLinksInitialized = true;
        }
        return this.definitionIdentityLinkEntities;
    }
    
    @Override
    public String toString() {
        return "ProcessDefinitionEntity[" + this.id + "]";
    }
    
    @Override
    public void updateModifiableFieldsFromEntity(final ProcessDefinitionEntity updatingProcessDefinition) {
        if (this.key.equals(updatingProcessDefinition.key) && this.deploymentId.equals(updatingProcessDefinition.deploymentId)) {
            this.revision = updatingProcessDefinition.revision;
            this.suspensionState = updatingProcessDefinition.suspensionState;
            this.historyTimeToLive = updatingProcessDefinition.historyTimeToLive;
        }
        else {
            ProcessDefinitionEntity.LOG.logUpdateUnrelatedProcessDefinitionEntity(this.key, updatingProcessDefinition.key, this.deploymentId, updatingProcessDefinition.deploymentId);
        }
    }
    
    @Override
    public ProcessDefinitionEntity getPreviousDefinition() {
        ProcessDefinitionEntity previousProcessDefinition = null;
        String previousProcessDefinitionId = this.getPreviousProcessDefinitionId();
        if (previousProcessDefinitionId != null) {
            previousProcessDefinition = this.loadProcessDefinition(previousProcessDefinitionId);
            if (previousProcessDefinition == null) {
                this.resetPreviousProcessDefinitionId();
                previousProcessDefinitionId = this.getPreviousProcessDefinitionId();
                if (previousProcessDefinitionId != null) {
                    previousProcessDefinition = this.loadProcessDefinition(previousProcessDefinitionId);
                }
            }
        }
        return previousProcessDefinition;
    }
    
    protected ProcessDefinitionEntity loadProcessDefinition(final String processDefinitionId) {
        final ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
        final DeploymentCache deploymentCache = configuration.getDeploymentCache();
        ProcessDefinitionEntity processDefinition = deploymentCache.findProcessDefinitionFromCache(processDefinitionId);
        if (processDefinition == null) {
            final CommandContext commandContext = Context.getCommandContext();
            final ProcessDefinitionManager processDefinitionManager = commandContext.getProcessDefinitionManager();
            processDefinition = processDefinitionManager.findLatestProcessDefinitionById(processDefinitionId);
            if (processDefinition != null) {
                processDefinition = deploymentCache.resolveProcessDefinition(processDefinition);
            }
        }
        return processDefinition;
    }
    
    public String getPreviousProcessDefinitionId() {
        this.ensurePreviousProcessDefinitionIdInitialized();
        return this.previousProcessDefinitionId;
    }
    
    protected void resetPreviousProcessDefinitionId() {
        this.previousProcessDefinitionId = null;
        this.ensurePreviousProcessDefinitionIdInitialized();
    }
    
    protected void setPreviousProcessDefinitionId(final String previousProcessDefinitionId) {
        this.previousProcessDefinitionId = previousProcessDefinitionId;
    }
    
    protected void ensurePreviousProcessDefinitionIdInitialized() {
        if (this.previousProcessDefinitionId == null && !this.firstVersion) {
            this.previousProcessDefinitionId = Context.getCommandContext().getProcessDefinitionManager().findPreviousProcessDefinitionId(this.key, this.version, this.tenantId);
            if (this.previousProcessDefinitionId == null) {
                this.firstVersion = true;
            }
        }
    }
    
    @Override
    public Object getPersistentState() {
        final Map<String, Object> persistentState = new HashMap<String, Object>();
        persistentState.put("suspensionState", this.suspensionState);
        persistentState.put("historyTimeToLive", this.historyTimeToLive);
        return persistentState;
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
    public String getDescription() {
        return (String)this.getProperty("documentation");
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
    public int getVersion() {
        return this.version;
    }
    
    @Override
    public void setVersion(final int version) {
        this.version = version;
        this.firstVersion = (this.version == 1);
    }
    
    @Override
    public void setId(final String id) {
        this.id = id;
    }
    
    @Override
    public String getResourceName() {
        return this.resourceName;
    }
    
    @Override
    public void setResourceName(final String resourceName) {
        this.resourceName = resourceName;
    }
    
    public Integer getHistoryLevel() {
        return this.historyLevel;
    }
    
    public void setHistoryLevel(final Integer historyLevel) {
        this.historyLevel = historyLevel;
    }
    
    public StartFormHandler getStartFormHandler() {
        return this.startFormHandler;
    }
    
    public void setStartFormHandler(final StartFormHandler startFormHandler) {
        this.startFormHandler = startFormHandler;
    }
    
    public FormDefinition getStartFormDefinition() {
        return this.startFormDefinition;
    }
    
    public void setStartFormDefinition(final FormDefinition startFormDefinition) {
        this.startFormDefinition = startFormDefinition;
    }
    
    public Map<String, TaskDefinition> getTaskDefinitions() {
        return this.taskDefinitions;
    }
    
    public void setTaskDefinitions(final Map<String, TaskDefinition> taskDefinitions) {
        this.taskDefinitions = taskDefinitions;
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
    public String getDiagramResourceName() {
        return this.diagramResourceName;
    }
    
    @Override
    public void setDiagramResourceName(final String diagramResourceName) {
        this.diagramResourceName = diagramResourceName;
    }
    
    @Override
    public boolean hasStartFormKey() {
        return this.hasStartFormKey;
    }
    
    public boolean getHasStartFormKey() {
        return this.hasStartFormKey;
    }
    
    public void setStartFormKey(final boolean hasStartFormKey) {
        this.hasStartFormKey = hasStartFormKey;
    }
    
    public void setHasStartFormKey(final boolean hasStartFormKey) {
        this.hasStartFormKey = hasStartFormKey;
    }
    
    public boolean isGraphicalNotationDefined() {
        return this.isGraphicalNotationDefined;
    }
    
    public void setGraphicalNotationDefined(final boolean isGraphicalNotationDefined) {
        this.isGraphicalNotationDefined = isGraphicalNotationDefined;
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
    
    public int getSuspensionState() {
        return this.suspensionState;
    }
    
    public void setSuspensionState(final int suspensionState) {
        this.suspensionState = suspensionState;
    }
    
    @Override
    public boolean isSuspended() {
        return this.suspensionState == SuspensionState.SUSPENDED.getStateCode();
    }
    
    public Set<Expression> getCandidateStarterUserIdExpressions() {
        return this.candidateStarterUserIdExpressions;
    }
    
    public void addCandidateStarterUserIdExpression(final Expression userId) {
        this.candidateStarterUserIdExpressions.add(userId);
    }
    
    public Set<Expression> getCandidateStarterGroupIdExpressions() {
        return this.candidateStarterGroupIdExpressions;
    }
    
    public void addCandidateStarterGroupIdExpression(final Expression groupId) {
        this.candidateStarterGroupIdExpressions.add(groupId);
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
    public String getVersionTag() {
        return this.versionTag;
    }
    
    public void setVersionTag(final String versionTag) {
        this.versionTag = versionTag;
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
    public boolean isStartableInTasklist() {
        return this.isStartableInTasklist;
    }
    
    public void setStartableInTasklist(final boolean isStartableInTasklist) {
        this.isStartableInTasklist = isStartableInTasklist;
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
