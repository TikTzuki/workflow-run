// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.util.CompareUtil;
import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Date;
import org.zik.bpm.engine.history.HistoricCaseActivityInstance;
import org.zik.bpm.engine.history.HistoricCaseActivityInstanceQuery;

public class HistoricCaseActivityInstanceQueryImpl extends AbstractQuery<HistoricCaseActivityInstanceQuery, HistoricCaseActivityInstance> implements HistoricCaseActivityInstanceQuery
{
    private static final long serialVersionUID = 1L;
    protected String[] caseActivityInstanceIds;
    protected String[] caseActivityIds;
    protected String caseInstanceId;
    protected String caseDefinitionId;
    protected String caseActivityName;
    protected String caseActivityType;
    protected Date createdBefore;
    protected Date createdAfter;
    protected Date endedBefore;
    protected Date endedAfter;
    protected Boolean ended;
    protected Integer caseActivityInstanceState;
    protected Boolean required;
    protected String[] tenantIds;
    protected boolean isTenantIdSet;
    
    public HistoricCaseActivityInstanceQueryImpl() {
    }
    
    public HistoricCaseActivityInstanceQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getHistoricCaseActivityInstanceManager().findHistoricCaseActivityInstanceCountByQueryCriteria(this);
    }
    
    @Override
    public List<HistoricCaseActivityInstance> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getHistoricCaseActivityInstanceManager().findHistoricCaseActivityInstancesByQueryCriteria(this, page);
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery caseActivityInstanceId(final String caseActivityInstanceId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseActivityInstanceId", (Object)caseActivityInstanceId);
        return this.caseActivityInstanceIdIn(caseActivityInstanceId);
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery caseActivityInstanceIdIn(final String... caseActivityInstanceIds) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseActivityInstanceIds", (Object[])caseActivityInstanceIds);
        this.caseActivityInstanceIds = caseActivityInstanceIds;
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery caseInstanceId(final String caseInstanceId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseInstanceId", (Object)caseInstanceId);
        this.caseInstanceId = caseInstanceId;
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery caseDefinitionId(final String caseDefinitionId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseDefinitionId", (Object)caseDefinitionId);
        this.caseDefinitionId = caseDefinitionId;
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery caseExecutionId(final String caseExecutionId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseExecutionId", (Object)caseExecutionId);
        return this.caseActivityInstanceIdIn(caseExecutionId);
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery caseActivityId(final String caseActivityId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseActivityId", (Object)caseActivityId);
        return this.caseActivityIdIn(caseActivityId);
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery caseActivityIdIn(final String... caseActivityIds) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseActivityIds", (Object[])caseActivityIds);
        this.caseActivityIds = caseActivityIds;
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery caseActivityName(final String caseActivityName) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseActivityName", (Object)caseActivityName);
        this.caseActivityName = caseActivityName;
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery caseActivityType(final String caseActivityType) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseActivityType", (Object)caseActivityType);
        this.caseActivityType = caseActivityType;
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery createdBefore(final Date date) {
        EnsureUtil.ensureNotNull(NotValidException.class, "createdBefore", date);
        this.createdBefore = date;
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery createdAfter(final Date date) {
        EnsureUtil.ensureNotNull(NotValidException.class, "createdAfter", date);
        this.createdAfter = date;
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery endedBefore(final Date date) {
        EnsureUtil.ensureNotNull(NotValidException.class, "finishedBefore", date);
        this.endedBefore = date;
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery endedAfter(final Date date) {
        EnsureUtil.ensureNotNull(NotValidException.class, "finishedAfter", date);
        this.endedAfter = date;
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery required() {
        this.required = true;
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery ended() {
        this.ended = true;
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery notEnded() {
        this.ended = false;
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery available() {
        EnsureUtil.ensureNull(NotValidException.class, "Already querying for case activity instance state '" + this.caseActivityInstanceState + "'", "caseActivityState", this.caseActivityInstanceState);
        this.caseActivityInstanceState = CaseExecutionState.AVAILABLE.getStateCode();
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery enabled() {
        EnsureUtil.ensureNull(NotValidException.class, "Already querying for case activity instance state '" + this.caseActivityInstanceState + "'", "caseActivityState", this.caseActivityInstanceState);
        this.caseActivityInstanceState = CaseExecutionState.ENABLED.getStateCode();
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery disabled() {
        EnsureUtil.ensureNull(NotValidException.class, "Already querying for case activity instance state '" + this.caseActivityInstanceState + "'", "caseActivityState", this.caseActivityInstanceState);
        this.caseActivityInstanceState = CaseExecutionState.DISABLED.getStateCode();
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery active() {
        EnsureUtil.ensureNull(NotValidException.class, "Already querying for case activity instance state '" + this.caseActivityInstanceState + "'", "caseActivityState", this.caseActivityInstanceState);
        this.caseActivityInstanceState = CaseExecutionState.ACTIVE.getStateCode();
        return this;
    }
    
    public HistoricCaseActivityInstanceQuery suspended() {
        EnsureUtil.ensureNull(NotValidException.class, "Already querying for case activity instance state '" + this.caseActivityInstanceState + "'", "caseActivityState", this.caseActivityInstanceState);
        this.caseActivityInstanceState = CaseExecutionState.SUSPENDED.getStateCode();
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery completed() {
        EnsureUtil.ensureNull(NotValidException.class, "Already querying for case activity instance state '" + this.caseActivityInstanceState + "'", "caseActivityState", this.caseActivityInstanceState);
        this.caseActivityInstanceState = CaseExecutionState.COMPLETED.getStateCode();
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery terminated() {
        EnsureUtil.ensureNull(NotValidException.class, "Already querying for case activity instance state '" + this.caseActivityInstanceState + "'", "caseActivityState", this.caseActivityInstanceState);
        this.caseActivityInstanceState = CaseExecutionState.TERMINATED.getStateCode();
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery withoutTenantId() {
        this.tenantIds = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    protected boolean hasExcludingConditions() {
        return super.hasExcludingConditions() || CompareUtil.areNotInAscendingOrder(this.createdAfter, this.createdBefore) || CompareUtil.areNotInAscendingOrder(this.endedAfter, this.endedBefore);
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery orderByHistoricCaseActivityInstanceId() {
        this.orderBy(HistoricCaseActivityInstanceQueryProperty.HISTORIC_CASE_ACTIVITY_INSTANCE_ID);
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery orderByCaseInstanceId() {
        this.orderBy(HistoricCaseActivityInstanceQueryProperty.CASE_INSTANCE_ID);
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery orderByCaseExecutionId() {
        this.orderBy(HistoricCaseActivityInstanceQueryProperty.HISTORIC_CASE_ACTIVITY_INSTANCE_ID);
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery orderByCaseActivityId() {
        this.orderBy(HistoricCaseActivityInstanceQueryProperty.CASE_ACTIVITY_ID);
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery orderByCaseActivityName() {
        this.orderBy(HistoricCaseActivityInstanceQueryProperty.CASE_ACTIVITY_NAME);
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery orderByCaseActivityType() {
        this.orderBy(HistoricCaseActivityInstanceQueryProperty.CASE_ACTIVITY_TYPE);
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery orderByHistoricCaseActivityInstanceCreateTime() {
        this.orderBy(HistoricCaseActivityInstanceQueryProperty.CREATE);
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery orderByHistoricCaseActivityInstanceEndTime() {
        this.orderBy(HistoricCaseActivityInstanceQueryProperty.END);
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery orderByHistoricCaseActivityInstanceDuration() {
        this.orderBy(HistoricCaseActivityInstanceQueryProperty.DURATION);
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery orderByCaseDefinitionId() {
        this.orderBy(HistoricCaseActivityInstanceQueryProperty.CASE_DEFINITION_ID);
        return this;
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery orderByTenantId() {
        return ((AbstractQuery<HistoricCaseActivityInstanceQuery, U>)this).orderBy(HistoricCaseActivityInstanceQueryProperty.TENANT_ID);
    }
    
    public String[] getCaseActivityInstanceIds() {
        return this.caseActivityInstanceIds;
    }
    
    public String getCaseInstanceId() {
        return this.caseInstanceId;
    }
    
    public String getCaseDefinitionId() {
        return this.caseDefinitionId;
    }
    
    public String[] getCaseActivityIds() {
        return this.caseActivityIds;
    }
    
    public String getCaseActivityName() {
        return this.caseActivityName;
    }
    
    public String getCaseActivityType() {
        return this.caseActivityType;
    }
    
    public Date getCreatedBefore() {
        return this.createdBefore;
    }
    
    public Date getCreatedAfter() {
        return this.createdAfter;
    }
    
    public Date getEndedBefore() {
        return this.endedBefore;
    }
    
    public Date getEndedAfter() {
        return this.endedAfter;
    }
    
    public Boolean getEnded() {
        return this.ended;
    }
    
    public Integer getCaseActivityInstanceState() {
        return this.caseActivityInstanceState;
    }
    
    public Boolean isRequired() {
        return this.required;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
}
