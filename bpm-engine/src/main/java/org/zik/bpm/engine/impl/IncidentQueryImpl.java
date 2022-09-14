// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Date;
import java.io.Serializable;
import org.zik.bpm.engine.runtime.Incident;
import org.zik.bpm.engine.runtime.IncidentQuery;

public class IncidentQueryImpl extends AbstractQuery<IncidentQuery, Incident> implements IncidentQuery, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String incidentType;
    protected String incidentMessage;
    protected String incidentMessageLike;
    protected String executionId;
    protected Date incidentTimestampBefore;
    protected Date incidentTimestampAfter;
    protected String activityId;
    protected String failedActivityId;
    protected String processInstanceId;
    protected String processDefinitionId;
    protected String[] processDefinitionKeys;
    protected String causeIncidentId;
    protected String rootCauseIncidentId;
    protected String configuration;
    protected String[] tenantIds;
    protected String[] jobDefinitionIds;
    
    public IncidentQueryImpl() {
    }
    
    public IncidentQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public IncidentQuery incidentId(final String incidentId) {
        this.id = incidentId;
        return this;
    }
    
    @Override
    public IncidentQuery incidentType(final String incidentType) {
        this.incidentType = incidentType;
        return this;
    }
    
    @Override
    public IncidentQuery incidentMessage(final String incidentMessage) {
        this.incidentMessage = incidentMessage;
        return this;
    }
    
    @Override
    public IncidentQuery incidentMessageLike(final String incidentMessageLike) {
        this.incidentMessageLike = incidentMessageLike;
        return this;
    }
    
    @Override
    public IncidentQuery executionId(final String executionId) {
        this.executionId = executionId;
        return this;
    }
    
    @Override
    public IncidentQuery incidentTimestampBefore(final Date incidentTimestampBefore) {
        this.incidentTimestampBefore = incidentTimestampBefore;
        return this;
    }
    
    @Override
    public IncidentQuery incidentTimestampAfter(final Date incidentTimestampAfter) {
        this.incidentTimestampAfter = incidentTimestampAfter;
        return this;
    }
    
    @Override
    public IncidentQuery activityId(final String activityId) {
        this.activityId = activityId;
        return this;
    }
    
    @Override
    public IncidentQuery failedActivityId(final String activityId) {
        this.failedActivityId = activityId;
        return this;
    }
    
    @Override
    public IncidentQuery processInstanceId(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }
    
    @Override
    public IncidentQuery processDefinitionId(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public IncidentQuery processDefinitionKeyIn(final String... processDefinitionKeys) {
        EnsureUtil.ensureNotNull("processDefinitionKeys", (Object[])processDefinitionKeys);
        this.processDefinitionKeys = processDefinitionKeys;
        return this;
    }
    
    @Override
    public IncidentQuery causeIncidentId(final String causeIncidentId) {
        this.causeIncidentId = causeIncidentId;
        return this;
    }
    
    @Override
    public IncidentQuery rootCauseIncidentId(final String rootCauseIncidentId) {
        this.rootCauseIncidentId = rootCauseIncidentId;
        return this;
    }
    
    @Override
    public IncidentQuery configuration(final String configuration) {
        this.configuration = configuration;
        return this;
    }
    
    @Override
    public IncidentQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        return this;
    }
    
    @Override
    public IncidentQuery jobDefinitionIdIn(final String... jobDefinitionIds) {
        EnsureUtil.ensureNotNull("jobDefinitionIds", (Object[])jobDefinitionIds);
        this.jobDefinitionIds = jobDefinitionIds;
        return this;
    }
    
    @Override
    public IncidentQuery orderByIncidentId() {
        this.orderBy(IncidentQueryProperty.INCIDENT_ID);
        return this;
    }
    
    @Override
    public IncidentQuery orderByIncidentTimestamp() {
        this.orderBy(IncidentQueryProperty.INCIDENT_TIMESTAMP);
        return this;
    }
    
    @Override
    public IncidentQuery orderByIncidentType() {
        this.orderBy(IncidentQueryProperty.INCIDENT_TYPE);
        return this;
    }
    
    @Override
    public IncidentQuery orderByExecutionId() {
        this.orderBy(IncidentQueryProperty.EXECUTION_ID);
        return this;
    }
    
    @Override
    public IncidentQuery orderByActivityId() {
        this.orderBy(IncidentQueryProperty.ACTIVITY_ID);
        return this;
    }
    
    @Override
    public IncidentQuery orderByProcessInstanceId() {
        this.orderBy(IncidentQueryProperty.PROCESS_INSTANCE_ID);
        return this;
    }
    
    @Override
    public IncidentQuery orderByProcessDefinitionId() {
        this.orderBy(IncidentQueryProperty.PROCESS_DEFINITION_ID);
        return this;
    }
    
    @Override
    public IncidentQuery orderByCauseIncidentId() {
        this.orderBy(IncidentQueryProperty.CAUSE_INCIDENT_ID);
        return this;
    }
    
    @Override
    public IncidentQuery orderByRootCauseIncidentId() {
        this.orderBy(IncidentQueryProperty.ROOT_CAUSE_INCIDENT_ID);
        return this;
    }
    
    @Override
    public IncidentQuery orderByConfiguration() {
        this.orderBy(IncidentQueryProperty.CONFIGURATION);
        return this;
    }
    
    @Override
    public IncidentQuery orderByTenantId() {
        return ((AbstractQuery<IncidentQuery, U>)this).orderBy(IncidentQueryProperty.TENANT_ID);
    }
    
    @Override
    public IncidentQuery orderByIncidentMessage() {
        return ((AbstractQuery<IncidentQuery, U>)this).orderBy(IncidentQueryProperty.INCIDENT_MESSAGE);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getIncidentManager().findIncidentCountByQueryCriteria(this);
    }
    
    @Override
    public List<Incident> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getIncidentManager().findIncidentByQueryCriteria(this, page);
    }
    
    public String[] getProcessDefinitionKeys() {
        return this.processDefinitionKeys;
    }
}
