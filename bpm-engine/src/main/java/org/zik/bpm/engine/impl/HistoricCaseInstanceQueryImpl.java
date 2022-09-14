// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.util.CompareUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import java.util.Collection;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.zik.bpm.engine.history.HistoricCaseInstance;
import org.zik.bpm.engine.history.HistoricCaseInstanceQuery;

public class HistoricCaseInstanceQueryImpl extends AbstractVariableQueryImpl<HistoricCaseInstanceQuery, HistoricCaseInstance> implements HistoricCaseInstanceQuery
{
    private static final long serialVersionUID = 1L;
    protected String caseInstanceId;
    protected Set<String> caseInstanceIds;
    protected String caseDefinitionId;
    protected String caseDefinitionName;
    protected String caseDefinitionNameLike;
    protected String businessKey;
    protected String businessKeyLike;
    protected Integer state;
    protected Boolean notClosed;
    protected String createdBy;
    protected String superCaseInstanceId;
    protected String subCaseInstanceId;
    protected String superProcessInstanceId;
    protected String subProcessInstanceId;
    protected List<String> caseKeyNotIn;
    protected Date createdBefore;
    protected Date createdAfter;
    protected Date closedBefore;
    protected Date closedAfter;
    protected String caseDefinitionKey;
    protected String[] caseActivityIds;
    protected boolean isTenantIdSet;
    protected String[] tenantIds;
    
    public HistoricCaseInstanceQueryImpl() {
        this.isTenantIdSet = false;
    }
    
    public HistoricCaseInstanceQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.isTenantIdSet = false;
    }
    
    @Override
    public HistoricCaseInstanceQueryImpl caseInstanceId(final String caseInstanceId) {
        this.caseInstanceId = caseInstanceId;
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery caseInstanceIds(final Set<String> caseInstanceIds) {
        EnsureUtil.ensureNotEmpty("Set of case instance ids", caseInstanceIds);
        this.caseInstanceIds = caseInstanceIds;
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQueryImpl caseDefinitionId(final String caseDefinitionId) {
        this.caseDefinitionId = caseDefinitionId;
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery caseDefinitionKey(final String caseDefinitionKey) {
        this.caseDefinitionKey = caseDefinitionKey;
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery caseDefinitionName(final String caseDefinitionName) {
        this.caseDefinitionName = caseDefinitionName;
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery caseDefinitionNameLike(final String nameLike) {
        this.caseDefinitionNameLike = nameLike;
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery caseInstanceBusinessKey(final String businessKey) {
        this.businessKey = businessKey;
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery caseInstanceBusinessKeyLike(final String businessKeyLike) {
        this.businessKeyLike = businessKeyLike;
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery createdBy(final String userId) {
        this.createdBy = userId;
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery caseDefinitionKeyNotIn(final List<String> caseDefinitionKeys) {
        EnsureUtil.ensureNotContainsNull("caseDefinitionKeys", caseDefinitionKeys);
        EnsureUtil.ensureNotContainsEmptyString("caseDefinitionKeys", caseDefinitionKeys);
        this.caseKeyNotIn = caseDefinitionKeys;
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery caseActivityIdIn(final String... caseActivityIds) {
        EnsureUtil.ensureNotNull("caseActivityIds", (Object[])caseActivityIds);
        this.caseActivityIds = caseActivityIds;
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery createdAfter(final Date date) {
        this.createdAfter = date;
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery createdBefore(final Date date) {
        this.createdBefore = date;
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery closedAfter(final Date date) {
        if (this.state != null && !this.state.equals(CaseExecutionState.CLOSED.getStateCode())) {
            throw new NotValidException("Already querying for case instance state '" + this.state + "'");
        }
        this.closedAfter = date;
        this.state = CaseExecutionState.CLOSED.getStateCode();
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery closedBefore(final Date date) {
        if (this.state != null && !this.state.equals(CaseExecutionState.CLOSED.getStateCode())) {
            throw new NotValidException("Already querying for case instance state '" + this.state + "'");
        }
        this.closedBefore = date;
        this.state = CaseExecutionState.CLOSED.getStateCode();
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery superCaseInstanceId(final String superCaseInstanceId) {
        this.superCaseInstanceId = superCaseInstanceId;
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery subCaseInstanceId(final String subCaseInstanceId) {
        this.subCaseInstanceId = subCaseInstanceId;
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery superProcessInstanceId(final String superProcessInstanceId) {
        this.superProcessInstanceId = superProcessInstanceId;
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery subProcessInstanceId(final String subProcessInstanceId) {
        this.subProcessInstanceId = subProcessInstanceId;
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery withoutTenantId() {
        this.tenantIds = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery active() {
        EnsureUtil.ensureNull(NotValidException.class, "Already querying for case instance state '" + this.state + "'", "state", this.state);
        this.state = CaseExecutionState.ACTIVE.getStateCode();
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery completed() {
        EnsureUtil.ensureNull(NotValidException.class, "Already querying for case instance state '" + this.state + "'", "state", this.state);
        this.state = CaseExecutionState.COMPLETED.getStateCode();
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery terminated() {
        EnsureUtil.ensureNull(NotValidException.class, "Already querying for case instance state '" + this.state + "'", "state", this.state);
        this.state = CaseExecutionState.TERMINATED.getStateCode();
        return this;
    }
    
    public HistoricCaseInstanceQuery failed() {
        EnsureUtil.ensureNull(NotValidException.class, "Already querying for case instance state '" + this.state + "'", "state", this.state);
        this.state = CaseExecutionState.FAILED.getStateCode();
        return this;
    }
    
    public HistoricCaseInstanceQuery suspended() {
        EnsureUtil.ensureNull(NotValidException.class, "Already querying for case instance state '" + this.state + "'", "state", this.state);
        this.state = CaseExecutionState.SUSPENDED.getStateCode();
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery closed() {
        EnsureUtil.ensureNull(NotValidException.class, "Already querying for case instance state '" + this.state + "'", "state", this.state);
        this.state = CaseExecutionState.CLOSED.getStateCode();
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery notClosed() {
        this.notClosed = true;
        return this;
    }
    
    @Override
    public HistoricCaseInstanceQuery orderByCaseInstanceBusinessKey() {
        return ((AbstractQuery<HistoricCaseInstanceQuery, U>)this).orderBy(HistoricCaseInstanceQueryProperty.BUSINESS_KEY);
    }
    
    @Override
    public HistoricCaseInstanceQuery orderByCaseInstanceDuration() {
        return ((AbstractQuery<HistoricCaseInstanceQuery, U>)this).orderBy(HistoricCaseInstanceQueryProperty.DURATION);
    }
    
    @Override
    public HistoricCaseInstanceQuery orderByCaseInstanceCreateTime() {
        return ((AbstractQuery<HistoricCaseInstanceQuery, U>)this).orderBy(HistoricCaseInstanceQueryProperty.CREATE_TIME);
    }
    
    @Override
    public HistoricCaseInstanceQuery orderByCaseInstanceCloseTime() {
        return ((AbstractQuery<HistoricCaseInstanceQuery, U>)this).orderBy(HistoricCaseInstanceQueryProperty.CLOSE_TIME);
    }
    
    @Override
    public HistoricCaseInstanceQuery orderByCaseDefinitionId() {
        return ((AbstractQuery<HistoricCaseInstanceQuery, U>)this).orderBy(HistoricCaseInstanceQueryProperty.PROCESS_DEFINITION_ID);
    }
    
    @Override
    public HistoricCaseInstanceQuery orderByCaseInstanceId() {
        return ((AbstractQuery<HistoricCaseInstanceQuery, U>)this).orderBy(HistoricCaseInstanceQueryProperty.PROCESS_INSTANCE_ID_);
    }
    
    @Override
    public HistoricCaseInstanceQuery orderByTenantId() {
        return ((AbstractQuery<HistoricCaseInstanceQuery, U>)this).orderBy(HistoricCaseInstanceQueryProperty.TENANT_ID);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        this.ensureVariablesInitialized();
        return commandContext.getHistoricCaseInstanceManager().findHistoricCaseInstanceCountByQueryCriteria(this);
    }
    
    @Override
    public List<HistoricCaseInstance> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        this.ensureVariablesInitialized();
        return commandContext.getHistoricCaseInstanceManager().findHistoricCaseInstancesByQueryCriteria(this, page);
    }
    
    @Override
    protected boolean hasExcludingConditions() {
        return super.hasExcludingConditions() || CompareUtil.areNotInAscendingOrder(this.createdAfter, this.createdBefore) || CompareUtil.areNotInAscendingOrder(this.closedAfter, this.closedBefore) || CompareUtil.elementIsNotContainedInList(this.caseInstanceId, this.caseInstanceIds) || CompareUtil.elementIsContainedInList(this.caseDefinitionKey, this.caseKeyNotIn);
    }
    
    public String getBusinessKey() {
        return this.businessKey;
    }
    
    public String getBusinessKeyLike() {
        return this.businessKeyLike;
    }
    
    public String getCaseDefinitionId() {
        return this.caseDefinitionId;
    }
    
    public String getCaseDefinitionKey() {
        return this.caseDefinitionKey;
    }
    
    public String getCaseDefinitionIdLike() {
        return this.caseDefinitionKey + ":%:%";
    }
    
    public String getCaseDefinitionName() {
        return this.caseDefinitionName;
    }
    
    public String getCaseDefinitionNameLike() {
        return this.caseDefinitionNameLike;
    }
    
    public String getCaseInstanceId() {
        return this.caseInstanceId;
    }
    
    public Set<String> getCaseInstanceIds() {
        return this.caseInstanceIds;
    }
    
    public String getStartedBy() {
        return this.createdBy;
    }
    
    public String getSuperCaseInstanceId() {
        return this.superCaseInstanceId;
    }
    
    public void setSuperCaseInstanceId(final String superCaseInstanceId) {
        this.superCaseInstanceId = superCaseInstanceId;
    }
    
    public List<String> getCaseKeyNotIn() {
        return this.caseKeyNotIn;
    }
    
    public Date getCreatedAfter() {
        return this.createdAfter;
    }
    
    public Date getCreatedBefore() {
        return this.createdBefore;
    }
    
    public Date getClosedAfter() {
        return this.closedAfter;
    }
    
    public Date getClosedBefore() {
        return this.closedBefore;
    }
    
    public String getSubCaseInstanceId() {
        return this.subCaseInstanceId;
    }
    
    public String getSuperProcessInstanceId() {
        return this.superProcessInstanceId;
    }
    
    public String getSubProcessInstanceId() {
        return this.subProcessInstanceId;
    }
}
