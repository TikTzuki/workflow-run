// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.history.IncidentState;
import java.util.Date;
import org.zik.bpm.engine.history.HistoricIncident;
import org.zik.bpm.engine.history.HistoricIncidentQuery;

public class HistoricIncidentQueryImpl extends AbstractVariableQueryImpl<HistoricIncidentQuery, HistoricIncident> implements HistoricIncidentQuery
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String incidentType;
    protected String incidentMessage;
    protected String incidentMessageLike;
    protected String executionId;
    protected String activityId;
    protected Date createTimeBefore;
    protected Date createTimeAfter;
    protected Date endTimeBefore;
    protected Date endTimeAfter;
    protected String failedActivityId;
    protected String processInstanceId;
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected String[] processDefinitionKeys;
    protected String causeIncidentId;
    protected String rootCauseIncidentId;
    protected String configuration;
    protected String historyConfiguration;
    protected IncidentState incidentState;
    protected String[] tenantIds;
    protected boolean isTenantIdSet;
    protected String[] jobDefinitionIds;
    
    public HistoricIncidentQueryImpl() {
    }
    
    public HistoricIncidentQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public HistoricIncidentQuery incidentId(final String incidentId) {
        EnsureUtil.ensureNotNull("incidentId", (Object)incidentId);
        this.id = incidentId;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery incidentType(final String incidentType) {
        EnsureUtil.ensureNotNull("incidentType", (Object)incidentType);
        this.incidentType = incidentType;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery incidentMessage(final String incidentMessage) {
        EnsureUtil.ensureNotNull("incidentMessage", (Object)incidentMessage);
        this.incidentMessage = incidentMessage;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery incidentMessageLike(final String incidentMessageLike) {
        EnsureUtil.ensureNotNull("incidentMessageLike", (Object)incidentMessageLike);
        this.incidentMessageLike = incidentMessageLike;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery executionId(final String executionId) {
        EnsureUtil.ensureNotNull("executionId", (Object)executionId);
        this.executionId = executionId;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery createTimeBefore(final Date createTimeBefore) {
        EnsureUtil.ensureNotNull("createTimeBefore", createTimeBefore);
        this.createTimeBefore = createTimeBefore;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery createTimeAfter(final Date createTimeAfter) {
        EnsureUtil.ensureNotNull("createTimeAfter", createTimeAfter);
        this.createTimeAfter = createTimeAfter;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery endTimeBefore(final Date endTimeBefore) {
        EnsureUtil.ensureNotNull("endTimeBefore", endTimeBefore);
        this.endTimeBefore = endTimeBefore;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery endTimeAfter(final Date endTimeAfter) {
        EnsureUtil.ensureNotNull("endTimeAfter", endTimeAfter);
        this.endTimeAfter = endTimeAfter;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery activityId(final String activityId) {
        EnsureUtil.ensureNotNull("activityId", (Object)activityId);
        this.activityId = activityId;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery failedActivityId(final String activityId) {
        EnsureUtil.ensureNotNull("failedActivityId", (Object)activityId);
        this.failedActivityId = activityId;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery processInstanceId(final String processInstanceId) {
        EnsureUtil.ensureNotNull("processInstanceId", (Object)processInstanceId);
        this.processInstanceId = processInstanceId;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery processDefinitionId(final String processDefinitionId) {
        EnsureUtil.ensureNotNull("processDefinitionId", (Object)processDefinitionId);
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery processDefinitionKey(final String processDefinitionKey) {
        EnsureUtil.ensureNotNull("processDefinitionKey", (Object)processDefinitionKey);
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery processDefinitionKeyIn(final String... processDefinitionKeys) {
        EnsureUtil.ensureNotNull("processDefinitionKeys", (Object[])processDefinitionKeys);
        this.processDefinitionKeys = processDefinitionKeys;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery causeIncidentId(final String causeIncidentId) {
        EnsureUtil.ensureNotNull("causeIncidentId", (Object)causeIncidentId);
        this.causeIncidentId = causeIncidentId;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery rootCauseIncidentId(final String rootCauseIncidentId) {
        EnsureUtil.ensureNotNull("rootCauseIncidentId", (Object)rootCauseIncidentId);
        this.rootCauseIncidentId = rootCauseIncidentId;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery withoutTenantId() {
        this.tenantIds = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery configuration(final String configuration) {
        EnsureUtil.ensureNotNull("configuration", (Object)configuration);
        this.configuration = configuration;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery historyConfiguration(final String historyConfiguration) {
        EnsureUtil.ensureNotNull("historyConfiguration", (Object)historyConfiguration);
        this.historyConfiguration = historyConfiguration;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery jobDefinitionIdIn(final String... jobDefinitionIds) {
        EnsureUtil.ensureNotNull("jobDefinitionIds", (Object[])jobDefinitionIds);
        this.jobDefinitionIds = jobDefinitionIds;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery open() {
        if (this.incidentState != null) {
            throw new ProcessEngineException("Already querying for incident state <" + this.incidentState + ">");
        }
        this.incidentState = IncidentState.DEFAULT;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery resolved() {
        if (this.incidentState != null) {
            throw new ProcessEngineException("Already querying for incident state <" + this.incidentState + ">");
        }
        this.incidentState = IncidentState.RESOLVED;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery deleted() {
        if (this.incidentState != null) {
            throw new ProcessEngineException("Already querying for incident state <" + this.incidentState + ">");
        }
        this.incidentState = IncidentState.DELETED;
        return this;
    }
    
    @Override
    public HistoricIncidentQuery orderByIncidentId() {
        this.orderBy(HistoricIncidentQueryProperty.INCIDENT_ID);
        return this;
    }
    
    @Override
    public HistoricIncidentQuery orderByIncidentMessage() {
        this.orderBy(HistoricIncidentQueryProperty.INCIDENT_MESSAGE);
        return this;
    }
    
    @Override
    public HistoricIncidentQuery orderByCreateTime() {
        this.orderBy(HistoricIncidentQueryProperty.INCIDENT_CREATE_TIME);
        return this;
    }
    
    @Override
    public HistoricIncidentQuery orderByEndTime() {
        this.orderBy(HistoricIncidentQueryProperty.INCIDENT_END_TIME);
        return this;
    }
    
    @Override
    public HistoricIncidentQuery orderByIncidentType() {
        this.orderBy(HistoricIncidentQueryProperty.INCIDENT_TYPE);
        return this;
    }
    
    @Override
    public HistoricIncidentQuery orderByExecutionId() {
        this.orderBy(HistoricIncidentQueryProperty.EXECUTION_ID);
        return this;
    }
    
    @Override
    public HistoricIncidentQuery orderByActivityId() {
        this.orderBy(HistoricIncidentQueryProperty.ACTIVITY_ID);
        return this;
    }
    
    @Override
    public HistoricIncidentQuery orderByProcessInstanceId() {
        this.orderBy(HistoricIncidentQueryProperty.PROCESS_INSTANCE_ID);
        return this;
    }
    
    @Override
    public HistoricIncidentQuery orderByProcessDefinitionKey() {
        this.orderBy(HistoricIncidentQueryProperty.PROCESS_DEFINITION_KEY);
        return this;
    }
    
    @Override
    public HistoricIncidentQuery orderByProcessDefinitionId() {
        this.orderBy(HistoricIncidentQueryProperty.PROCESS_DEFINITION_ID);
        return this;
    }
    
    @Override
    public HistoricIncidentQuery orderByCauseIncidentId() {
        this.orderBy(HistoricIncidentQueryProperty.CAUSE_INCIDENT_ID);
        return this;
    }
    
    @Override
    public HistoricIncidentQuery orderByRootCauseIncidentId() {
        this.orderBy(HistoricIncidentQueryProperty.ROOT_CAUSE_INCIDENT_ID);
        return this;
    }
    
    @Override
    public HistoricIncidentQuery orderByConfiguration() {
        this.orderBy(HistoricIncidentQueryProperty.CONFIGURATION);
        return this;
    }
    
    @Override
    public HistoricIncidentQuery orderByHistoryConfiguration() {
        this.orderBy(HistoricIncidentQueryProperty.HISTORY_CONFIGURATION);
        return this;
    }
    
    @Override
    public HistoricIncidentQuery orderByIncidentState() {
        this.orderBy(HistoricIncidentQueryProperty.INCIDENT_STATE);
        return this;
    }
    
    @Override
    public HistoricIncidentQuery orderByTenantId() {
        return ((AbstractQuery<HistoricIncidentQuery, U>)this).orderBy(HistoricIncidentQueryProperty.TENANT_ID);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getHistoricIncidentManager().findHistoricIncidentCountByQueryCriteria(this);
    }
    
    @Override
    public List<HistoricIncident> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getHistoricIncidentManager().findHistoricIncidentByQueryCriteria(this, page);
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getIncidentType() {
        return this.incidentType;
    }
    
    public String getIncidentMessage() {
        return this.incidentMessage;
    }
    
    public String getExecutionId() {
        return this.executionId;
    }
    
    public String getActivityId() {
        return this.activityId;
    }
    
    public String getFailedActivityId() {
        return this.failedActivityId;
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public String[] getProcessDefinitionKeys() {
        return this.processDefinitionKeys;
    }
    
    public String getCauseIncidentId() {
        return this.causeIncidentId;
    }
    
    public String getRootCauseIncidentId() {
        return this.rootCauseIncidentId;
    }
    
    public String getConfiguration() {
        return this.configuration;
    }
    
    public String getHistoryConfiguration() {
        return this.historyConfiguration;
    }
    
    public IncidentState getIncidentState() {
        return this.incidentState;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
}
