// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.entity.runtime;

import java.util.List;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.QueryOrderingProperty;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import org.zik.bpm.engine.runtime.CaseInstance;
import org.zik.bpm.engine.runtime.CaseInstanceQuery;
import org.zik.bpm.engine.impl.AbstractVariableQueryImpl;

public class CaseInstanceQueryImpl extends AbstractVariableQueryImpl<CaseInstanceQuery, CaseInstance> implements CaseInstanceQuery
{
    private static final long serialVersionUID = 1L;
    protected String caseExecutionId;
    protected String businessKey;
    protected String caseDefinitionId;
    protected String caseDefinitionKey;
    protected String deploymentId;
    protected CaseExecutionState state;
    protected String superProcessInstanceId;
    protected String subProcessInstanceId;
    protected String superCaseInstanceId;
    protected String subCaseInstanceId;
    protected boolean isTenantIdSet;
    protected String[] tenantIds;
    protected Boolean required;
    protected Boolean repeatable;
    protected Boolean repetition;
    
    public CaseInstanceQueryImpl() {
        this.isTenantIdSet = false;
    }
    
    public CaseInstanceQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.isTenantIdSet = false;
    }
    
    @Override
    public CaseInstanceQuery caseInstanceId(final String caseInstanceId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseInstanceId", (Object)caseInstanceId);
        this.caseExecutionId = caseInstanceId;
        return this;
    }
    
    @Override
    public CaseInstanceQuery caseInstanceBusinessKey(final String caseInstanceBusinessKey) {
        EnsureUtil.ensureNotNull(NotValidException.class, "businessKey", (Object)caseInstanceBusinessKey);
        this.businessKey = caseInstanceBusinessKey;
        return this;
    }
    
    @Override
    public CaseInstanceQuery caseDefinitionKey(final String caseDefinitionKey) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseDefinitionKey", (Object)caseDefinitionKey);
        this.caseDefinitionKey = caseDefinitionKey;
        return this;
    }
    
    @Override
    public CaseInstanceQuery caseDefinitionId(final String caseDefinitionId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseDefinitionId", (Object)caseDefinitionId);
        this.caseDefinitionId = caseDefinitionId;
        return this;
    }
    
    @Override
    public CaseInstanceQuery deploymentId(final String deploymentId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "deploymentId", (Object)deploymentId);
        this.deploymentId = deploymentId;
        return this;
    }
    
    @Override
    public CaseInstanceQuery superProcessInstanceId(final String superProcessInstanceId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "superProcessInstanceId", (Object)superProcessInstanceId);
        this.superProcessInstanceId = superProcessInstanceId;
        return this;
    }
    
    @Override
    public CaseInstanceQuery subProcessInstanceId(final String subProcessInstanceId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "subProcessInstanceId", (Object)subProcessInstanceId);
        this.subProcessInstanceId = subProcessInstanceId;
        return this;
    }
    
    @Override
    public CaseInstanceQuery superCaseInstanceId(final String superCaseInstanceId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "superCaseInstanceId", (Object)superCaseInstanceId);
        this.superCaseInstanceId = superCaseInstanceId;
        return this;
    }
    
    @Override
    public CaseInstanceQuery subCaseInstanceId(final String subCaseInstanceId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "subCaseInstanceId", (Object)subCaseInstanceId);
        this.subCaseInstanceId = subCaseInstanceId;
        return this;
    }
    
    @Override
    public CaseInstanceQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public CaseInstanceQuery withoutTenantId() {
        this.tenantIds = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public CaseInstanceQuery active() {
        this.state = CaseExecutionState.ACTIVE;
        return this;
    }
    
    @Override
    public CaseInstanceQuery completed() {
        this.state = CaseExecutionState.COMPLETED;
        return this;
    }
    
    @Override
    public CaseInstanceQuery terminated() {
        this.state = CaseExecutionState.TERMINATED;
        return this;
    }
    
    @Override
    public CaseInstanceQuery orderByCaseInstanceId() {
        this.orderBy(CaseInstanceQueryProperty.CASE_INSTANCE_ID);
        return this;
    }
    
    @Override
    public CaseInstanceQuery orderByCaseDefinitionKey() {
        this.orderBy(new QueryOrderingProperty("case-definition", CaseInstanceQueryProperty.CASE_DEFINITION_KEY));
        return this;
    }
    
    @Override
    public CaseInstanceQuery orderByCaseDefinitionId() {
        this.orderBy(CaseInstanceQueryProperty.CASE_DEFINITION_ID);
        return this;
    }
    
    @Override
    public CaseInstanceQuery orderByTenantId() {
        this.orderBy(CaseInstanceQueryProperty.TENANT_ID);
        return this;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        this.ensureVariablesInitialized();
        return commandContext.getCaseExecutionManager().findCaseInstanceCountByQueryCriteria(this);
    }
    
    @Override
    public List<CaseInstance> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        this.ensureVariablesInitialized();
        return commandContext.getCaseExecutionManager().findCaseInstanceByQueryCriteria(this, page);
    }
    
    public String getCaseInstanceId() {
        return this.caseExecutionId;
    }
    
    public String getCaseExecutionId() {
        return this.caseExecutionId;
    }
    
    public String getActivityId() {
        return null;
    }
    
    public String getBusinessKey() {
        return this.businessKey;
    }
    
    public String getCaseDefinitionId() {
        return this.caseDefinitionId;
    }
    
    public String getCaseDefinitionKey() {
        return this.caseDefinitionKey;
    }
    
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    public CaseExecutionState getState() {
        return this.state;
    }
    
    public boolean isCaseInstancesOnly() {
        return true;
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
    
    public Boolean isRequired() {
        return this.required;
    }
    
    public Boolean isRepeatable() {
        return this.repeatable;
    }
    
    public Boolean isRepetition() {
        return this.repetition;
    }
}
