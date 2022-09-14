// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.entity.runtime;

import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.QueryOrderingProperty;
import org.zik.bpm.engine.impl.QueryOperator;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import org.zik.bpm.engine.runtime.CaseExecution;
import org.zik.bpm.engine.runtime.CaseExecutionQuery;
import org.zik.bpm.engine.impl.AbstractVariableQueryImpl;

public class CaseExecutionQueryImpl extends AbstractVariableQueryImpl<CaseExecutionQuery, CaseExecution> implements CaseExecutionQuery
{
    private static final long serialVersionUID = 1L;
    protected String caseDefinitionId;
    protected String caseDefinitionKey;
    protected String activityId;
    protected String caseExecutionId;
    protected String caseInstanceId;
    protected String businessKey;
    protected CaseExecutionState state;
    protected Boolean required;
    protected boolean isTenantIdSet;
    protected String[] tenantIds;
    protected String superProcessInstanceId;
    protected String subProcessInstanceId;
    protected String superCaseInstanceId;
    protected String subCaseInstanceId;
    protected String deploymentId;
    
    public CaseExecutionQueryImpl() {
        this.required = false;
        this.isTenantIdSet = false;
    }
    
    public CaseExecutionQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.required = false;
        this.isTenantIdSet = false;
    }
    
    @Override
    public CaseExecutionQuery caseInstanceId(final String caseInstanceId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseInstanceId", (Object)caseInstanceId);
        this.caseInstanceId = caseInstanceId;
        return this;
    }
    
    @Override
    public CaseExecutionQuery caseDefinitionId(final String caseDefinitionId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseDefinitionId", (Object)caseDefinitionId);
        this.caseDefinitionId = caseDefinitionId;
        return this;
    }
    
    @Override
    public CaseExecutionQuery caseDefinitionKey(final String caseDefinitionKey) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseDefinitionKey", (Object)caseDefinitionKey);
        this.caseDefinitionKey = caseDefinitionKey;
        return this;
    }
    
    @Override
    public CaseExecutionQuery caseInstanceBusinessKey(final String caseInstanceBusinessKey) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseInstanceBusinessKey", (Object)caseInstanceBusinessKey);
        this.businessKey = caseInstanceBusinessKey;
        return this;
    }
    
    @Override
    public CaseExecutionQuery caseExecutionId(final String caseExecutionId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseExecutionId", (Object)caseExecutionId);
        this.caseExecutionId = caseExecutionId;
        return this;
    }
    
    @Override
    public CaseExecutionQuery activityId(final String activityId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "activityId", (Object)activityId);
        this.activityId = activityId;
        return this;
    }
    
    @Override
    public CaseExecutionQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public CaseExecutionQuery withoutTenantId() {
        this.tenantIds = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public CaseExecutionQuery required() {
        this.required = true;
        return this;
    }
    
    @Override
    public CaseExecutionQuery available() {
        this.state = CaseExecutionState.AVAILABLE;
        return this;
    }
    
    @Override
    public CaseExecutionQuery enabled() {
        this.state = CaseExecutionState.ENABLED;
        return this;
    }
    
    @Override
    public CaseExecutionQuery active() {
        this.state = CaseExecutionState.ACTIVE;
        return this;
    }
    
    @Override
    public CaseExecutionQuery disabled() {
        this.state = CaseExecutionState.DISABLED;
        return this;
    }
    
    @Override
    public CaseExecutionQuery caseInstanceVariableValueEquals(final String name, final Object value) {
        this.addVariable(name, value, QueryOperator.EQUALS, false);
        return this;
    }
    
    @Override
    public CaseExecutionQuery caseInstanceVariableValueNotEquals(final String name, final Object value) {
        this.addVariable(name, value, QueryOperator.NOT_EQUALS, false);
        return this;
    }
    
    @Override
    public CaseExecutionQuery caseInstanceVariableValueGreaterThan(final String name, final Object value) {
        this.addVariable(name, value, QueryOperator.GREATER_THAN, false);
        return this;
    }
    
    @Override
    public CaseExecutionQuery caseInstanceVariableValueGreaterThanOrEqual(final String name, final Object value) {
        this.addVariable(name, value, QueryOperator.GREATER_THAN_OR_EQUAL, false);
        return this;
    }
    
    @Override
    public CaseExecutionQuery caseInstanceVariableValueLessThan(final String name, final Object value) {
        this.addVariable(name, value, QueryOperator.LESS_THAN, false);
        return this;
    }
    
    @Override
    public CaseExecutionQuery caseInstanceVariableValueLessThanOrEqual(final String name, final Object value) {
        this.addVariable(name, value, QueryOperator.LESS_THAN_OR_EQUAL, false);
        return this;
    }
    
    @Override
    public CaseExecutionQuery caseInstanceVariableValueLike(final String name, final String value) {
        this.addVariable(name, value, QueryOperator.LIKE, false);
        return this;
    }
    
    @Override
    public CaseExecutionQuery orderByCaseExecutionId() {
        this.orderBy(CaseExecutionQueryProperty.CASE_EXECUTION_ID);
        return this;
    }
    
    @Override
    public CaseExecutionQuery orderByCaseDefinitionKey() {
        this.orderBy(new QueryOrderingProperty("case-definition", CaseExecutionQueryProperty.CASE_DEFINITION_KEY));
        return this;
    }
    
    @Override
    public CaseExecutionQuery orderByCaseDefinitionId() {
        this.orderBy(CaseExecutionQueryProperty.CASE_DEFINITION_ID);
        return this;
    }
    
    @Override
    public CaseExecutionQuery orderByTenantId() {
        this.orderBy(CaseExecutionQueryProperty.TENANT_ID);
        return this;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        this.ensureVariablesInitialized();
        return commandContext.getCaseExecutionManager().findCaseExecutionCountByQueryCriteria(this);
    }
    
    @Override
    public List<CaseExecution> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        this.ensureVariablesInitialized();
        final List<CaseExecution> result = commandContext.getCaseExecutionManager().findCaseExecutionsByQueryCriteria(this, page);
        for (final CaseExecution caseExecution : result) {
            final CaseExecutionEntity caseExecutionEntity = (CaseExecutionEntity)caseExecution;
            caseExecutionEntity.getActivity();
        }
        return result;
    }
    
    public String getCaseDefinitionId() {
        return this.caseDefinitionId;
    }
    
    public String getCaseDefinitionKey() {
        return this.caseDefinitionKey;
    }
    
    public String getActivityId() {
        return this.activityId;
    }
    
    public String getCaseExecutionId() {
        return this.caseExecutionId;
    }
    
    public String getCaseInstanceId() {
        return this.caseInstanceId;
    }
    
    public String getBusinessKey() {
        return this.businessKey;
    }
    
    public CaseExecutionState getState() {
        return this.state;
    }
    
    public boolean isCaseInstancesOnly() {
        return false;
    }
    
    public String getSuperProcessInstanceId() {
        return this.superProcessInstanceId;
    }
    
    public String getSubProcessInstanceId() {
        return this.subProcessInstanceId;
    }
    
    public String getSuperCaseInstanceId() {
        return this.superCaseInstanceId;
    }
    
    public String getSubCaseInstanceId() {
        return this.subCaseInstanceId;
    }
    
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    public Boolean isRequired() {
        return this.required;
    }
}
