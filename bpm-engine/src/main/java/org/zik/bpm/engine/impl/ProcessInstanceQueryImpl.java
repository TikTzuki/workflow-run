// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.Iterator;
import org.zik.bpm.engine.impl.variable.serializer.VariableSerializers;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.ImmutablePair;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import java.util.Set;
import java.io.Serializable;
import org.zik.bpm.engine.runtime.ProcessInstance;
import org.zik.bpm.engine.runtime.ProcessInstanceQuery;

public class ProcessInstanceQueryImpl extends AbstractVariableQueryImpl<ProcessInstanceQuery, ProcessInstance> implements ProcessInstanceQuery, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String processInstanceId;
    protected String businessKey;
    protected String businessKeyLike;
    protected String processDefinitionId;
    protected Set<String> processInstanceIds;
    protected String processDefinitionKey;
    protected String[] processDefinitionKeys;
    protected String[] processDefinitionKeyNotIn;
    protected String deploymentId;
    protected String superProcessInstanceId;
    protected String subProcessInstanceId;
    protected SuspensionState suspensionState;
    protected boolean withIncident;
    protected String incidentType;
    protected String incidentId;
    protected String incidentMessage;
    protected String incidentMessageLike;
    protected String caseInstanceId;
    protected String superCaseInstanceId;
    protected String subCaseInstanceId;
    protected String[] activityIds;
    protected boolean isRootProcessInstances;
    protected boolean isLeafProcessInstances;
    protected boolean isTenantIdSet;
    protected String[] tenantIds;
    protected boolean isProcessDefinitionWithoutTenantId;
    protected List<ProcessInstanceQueryImpl> queries;
    protected boolean isOrQueryActive;
    
    public ProcessInstanceQueryImpl() {
        this.isTenantIdSet = false;
        this.isProcessDefinitionWithoutTenantId = false;
        this.queries = new ArrayList<ProcessInstanceQueryImpl>(Arrays.asList(this));
        this.isOrQueryActive = false;
    }
    
    public ProcessInstanceQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.isTenantIdSet = false;
        this.isProcessDefinitionWithoutTenantId = false;
        this.queries = new ArrayList<ProcessInstanceQueryImpl>(Arrays.asList(this));
        this.isOrQueryActive = false;
    }
    
    @Override
    public ProcessInstanceQueryImpl processInstanceId(final String processInstanceId) {
        EnsureUtil.ensureNotNull("Process instance id", (Object)processInstanceId);
        this.processInstanceId = processInstanceId;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery processInstanceIds(final Set<String> processInstanceIds) {
        EnsureUtil.ensureNotEmpty("Set of process instance ids", processInstanceIds);
        this.processInstanceIds = processInstanceIds;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery processInstanceBusinessKey(final String businessKey) {
        EnsureUtil.ensureNotNull("Business key", (Object)businessKey);
        this.businessKey = businessKey;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery processInstanceBusinessKey(final String businessKey, final String processDefinitionKey) {
        EnsureUtil.ensureNotNull("Business key", (Object)businessKey);
        this.businessKey = businessKey;
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery processInstanceBusinessKeyLike(final String businessKeyLike) {
        this.businessKeyLike = businessKeyLike;
        return this;
    }
    
    @Override
    public ProcessInstanceQueryImpl processDefinitionId(final String processDefinitionId) {
        EnsureUtil.ensureNotNull("Process definition id", (Object)processDefinitionId);
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public ProcessInstanceQueryImpl processDefinitionKey(final String processDefinitionKey) {
        EnsureUtil.ensureNotNull("Process definition key", (Object)processDefinitionKey);
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery processDefinitionKeyIn(final String... processDefinitionKeys) {
        EnsureUtil.ensureNotNull("processDefinitionKeys", (Object[])processDefinitionKeys);
        this.processDefinitionKeys = processDefinitionKeys;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery processDefinitionKeyNotIn(final String... processDefinitionKeys) {
        EnsureUtil.ensureNotNull("processDefinitionKeyNotIn", (Object[])processDefinitionKeys);
        this.processDefinitionKeyNotIn = processDefinitionKeys;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery deploymentId(final String deploymentId) {
        EnsureUtil.ensureNotNull("Deployment id", (Object)deploymentId);
        this.deploymentId = deploymentId;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery superProcessInstanceId(final String superProcessInstanceId) {
        if (this.isRootProcessInstances) {
            throw new ProcessEngineException("Invalid query usage: cannot set both rootProcessInstances and superProcessInstanceId");
        }
        this.superProcessInstanceId = superProcessInstanceId;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery subProcessInstanceId(final String subProcessInstanceId) {
        this.subProcessInstanceId = subProcessInstanceId;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery caseInstanceId(final String caseInstanceId) {
        EnsureUtil.ensureNotNull("caseInstanceId", (Object)caseInstanceId);
        this.caseInstanceId = caseInstanceId;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery superCaseInstanceId(final String superCaseInstanceId) {
        EnsureUtil.ensureNotNull("superCaseInstanceId", (Object)superCaseInstanceId);
        this.superCaseInstanceId = superCaseInstanceId;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery subCaseInstanceId(final String subCaseInstanceId) {
        EnsureUtil.ensureNotNull("subCaseInstanceId", (Object)subCaseInstanceId);
        this.subCaseInstanceId = subCaseInstanceId;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery orderByProcessInstanceId() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByProcessInstanceId() within 'or' query");
        }
        this.orderBy(ProcessInstanceQueryProperty.PROCESS_INSTANCE_ID);
        return this;
    }
    
    @Override
    public ProcessInstanceQuery orderByProcessDefinitionId() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByProcessDefinitionId() within 'or' query");
        }
        this.orderBy(new QueryOrderingProperty("process-definition", ProcessInstanceQueryProperty.PROCESS_DEFINITION_ID));
        return this;
    }
    
    @Override
    public ProcessInstanceQuery orderByProcessDefinitionKey() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByProcessDefinitionKey() within 'or' query");
        }
        this.orderBy(new QueryOrderingProperty("process-definition", ProcessInstanceQueryProperty.PROCESS_DEFINITION_KEY));
        return this;
    }
    
    @Override
    public ProcessInstanceQuery orderByTenantId() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByTenantId() within 'or' query");
        }
        this.orderBy(ProcessInstanceQueryProperty.TENANT_ID);
        return this;
    }
    
    @Override
    public ProcessInstanceQuery orderByBusinessKey() {
        if (this.isOrQueryActive) {
            throw new ProcessEngineException("Invalid query usage: cannot set orderByBusinessKey() within 'or' query");
        }
        this.orderBy(ProcessInstanceQueryProperty.BUSINESS_KEY);
        return this;
    }
    
    @Override
    public ProcessInstanceQuery active() {
        this.suspensionState = SuspensionState.ACTIVE;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery suspended() {
        this.suspensionState = SuspensionState.SUSPENDED;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery withIncident() {
        this.withIncident = true;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery incidentType(final String incidentType) {
        EnsureUtil.ensureNotNull("incident type", (Object)incidentType);
        this.incidentType = incidentType;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery incidentId(final String incidentId) {
        EnsureUtil.ensureNotNull("incident id", (Object)incidentId);
        this.incidentId = incidentId;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery incidentMessage(final String incidentMessage) {
        EnsureUtil.ensureNotNull("incident message", (Object)incidentMessage);
        this.incidentMessage = incidentMessage;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery incidentMessageLike(final String incidentMessageLike) {
        EnsureUtil.ensureNotNull("incident messageLike", (Object)incidentMessageLike);
        this.incidentMessageLike = incidentMessageLike;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery withoutTenantId() {
        this.tenantIds = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery activityIdIn(final String... activityIds) {
        EnsureUtil.ensureNotNull("activity ids", (Object[])activityIds);
        this.activityIds = activityIds;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery rootProcessInstances() {
        if (this.superProcessInstanceId != null) {
            throw new ProcessEngineException("Invalid query usage: cannot set both rootProcessInstances and superProcessInstanceId");
        }
        this.isRootProcessInstances = true;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery leafProcessInstances() {
        if (this.subProcessInstanceId != null) {
            throw new ProcessEngineException("Invalid query usage: cannot set both leafProcessInstances and subProcessInstanceId");
        }
        this.isLeafProcessInstances = true;
        return this;
    }
    
    @Override
    public ProcessInstanceQuery processDefinitionWithoutTenantId() {
        this.isProcessDefinitionWithoutTenantId = true;
        return this;
    }
    
    @Override
    protected void checkQueryOk() {
        this.ensureVariablesInitialized();
        super.checkQueryOk();
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getExecutionManager().findProcessInstanceCountByQueryCriteria(this);
    }
    
    @Override
    public List<ProcessInstance> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getExecutionManager().findProcessInstancesByQueryCriteria(this, page);
    }
    
    @Override
    public List<String> executeIdsList(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getExecutionManager().findProcessInstancesIdsByQueryCriteria(this);
    }
    
    @Override
    public List<ImmutablePair<String, String>> executeDeploymentIdMappingsList(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getExecutionManager().findDeploymentIdMappingsByQueryCriteria(this);
    }
    
    @Override
    protected void ensureVariablesInitialized() {
        super.ensureVariablesInitialized();
        if (!this.queries.isEmpty()) {
            final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
            final VariableSerializers variableSerializers = processEngineConfiguration.getVariableSerializers();
            final String dbType = processEngineConfiguration.getDatabaseType();
            for (final ProcessInstanceQueryImpl orQuery : this.queries) {
                for (final QueryVariableValue var : orQuery.getQueryVariableValues()) {
                    var.initialize(variableSerializers, dbType);
                }
            }
        }
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public Set<String> getProcessInstanceIds() {
        return this.processInstanceIds;
    }
    
    public List<ProcessInstanceQueryImpl> getQueries() {
        return this.queries;
    }
    
    public void addOrQuery(final ProcessInstanceQueryImpl orQuery) {
        orQuery.isOrQueryActive = true;
        this.queries.add(orQuery);
    }
    
    public void setOrQueryActive() {
        this.isOrQueryActive = true;
    }
    
    public boolean isOrQueryActive() {
        return this.isOrQueryActive;
    }
    
    public String[] getActivityIds() {
        return this.activityIds;
    }
    
    public String getBusinessKey() {
        return this.businessKey;
    }
    
    public String getBusinessKeyLike() {
        return this.businessKeyLike;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public String[] getProcessDefinitionKeys() {
        return this.processDefinitionKeys;
    }
    
    public String[] getProcessDefinitionKeyNotIn() {
        return this.processDefinitionKeyNotIn;
    }
    
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    public String getSuperProcessInstanceId() {
        return this.superProcessInstanceId;
    }
    
    public String getSubProcessInstanceId() {
        return this.subProcessInstanceId;
    }
    
    public SuspensionState getSuspensionState() {
        return this.suspensionState;
    }
    
    public void setSuspensionState(final SuspensionState suspensionState) {
        this.suspensionState = suspensionState;
    }
    
    public boolean isWithIncident() {
        return this.withIncident;
    }
    
    public String getIncidentId() {
        return this.incidentId;
    }
    
    public String getIncidentType() {
        return this.incidentType;
    }
    
    public String getIncidentMessage() {
        return this.incidentMessage;
    }
    
    public String getIncidentMessageLike() {
        return this.incidentMessageLike;
    }
    
    public String getCaseInstanceId() {
        return this.caseInstanceId;
    }
    
    public String getSuperCaseInstanceId() {
        return this.superCaseInstanceId;
    }
    
    public String getSubCaseInstanceId() {
        return this.subCaseInstanceId;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
    
    public boolean isRootProcessInstances() {
        return this.isRootProcessInstances;
    }
    
    public boolean isProcessDefinitionWithoutTenantId() {
        return this.isProcessDefinitionWithoutTenantId;
    }
    
    public boolean isLeafProcessInstances() {
        return this.isLeafProcessInstances;
    }
    
    public String[] getTenantIds() {
        return this.tenantIds;
    }
    
    @Override
    public ProcessInstanceQuery or() {
        if (this != this.queries.get(0)) {
            throw new ProcessEngineException("Invalid query usage: cannot set or() within 'or' query");
        }
        final ProcessInstanceQueryImpl orQuery = new ProcessInstanceQueryImpl();
        orQuery.isOrQueryActive = true;
        orQuery.queries = this.queries;
        this.queries.add(orQuery);
        return orQuery;
    }
    
    @Override
    public ProcessInstanceQuery endOr() {
        if (!this.queries.isEmpty() && this != this.queries.get(this.queries.size() - 1)) {
            throw new ProcessEngineException("Invalid query usage: cannot set endOr() before or()");
        }
        return this.queries.get(0);
    }
}
