// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.util.ImmutablePair;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Date;
import org.zik.bpm.engine.history.HistoricDecisionInstance;
import org.zik.bpm.engine.history.HistoricDecisionInstanceQuery;

public class HistoricDecisionInstanceQueryImpl extends AbstractQuery<HistoricDecisionInstanceQuery, HistoricDecisionInstance> implements HistoricDecisionInstanceQuery
{
    private static final long serialVersionUID = 1L;
    protected String decisionInstanceId;
    protected String[] decisionInstanceIdIn;
    protected String decisionDefinitionId;
    protected String[] decisionDefinitionIdIn;
    protected String decisionDefinitionKey;
    protected String[] decisionDefinitionKeyIn;
    protected String decisionDefinitionName;
    protected String decisionDefinitionNameLike;
    protected String processDefinitionKey;
    protected String processDefinitionId;
    protected String processInstanceId;
    protected String caseDefinitionKey;
    protected String caseDefinitionId;
    protected String caseInstanceId;
    protected String[] activityInstanceIds;
    protected String[] activityIds;
    protected Date evaluatedBefore;
    protected Date evaluatedAfter;
    protected String userId;
    protected boolean includeInput;
    protected boolean includeOutputs;
    protected boolean isByteArrayFetchingEnabled;
    protected boolean isCustomObjectDeserializationEnabled;
    protected String rootDecisionInstanceId;
    protected boolean rootDecisionInstancesOnly;
    protected String decisionRequirementsDefinitionId;
    protected String decisionRequirementsDefinitionKey;
    protected String[] tenantIds;
    protected boolean isTenantIdSet;
    
    public HistoricDecisionInstanceQueryImpl() {
        this.includeInput = false;
        this.includeOutputs = false;
        this.isByteArrayFetchingEnabled = true;
        this.isCustomObjectDeserializationEnabled = true;
        this.rootDecisionInstancesOnly = false;
    }
    
    public HistoricDecisionInstanceQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.includeInput = false;
        this.includeOutputs = false;
        this.isByteArrayFetchingEnabled = true;
        this.isCustomObjectDeserializationEnabled = true;
        this.rootDecisionInstancesOnly = false;
    }
    
    @Override
    public HistoricDecisionInstanceQuery decisionInstanceId(final String decisionInstanceId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "decisionInstanceId", (Object)decisionInstanceId);
        this.decisionInstanceId = decisionInstanceId;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery decisionInstanceIdIn(final String... decisionInstanceIdIn) {
        EnsureUtil.ensureNotNull("decisionInstanceIdIn", (Object[])decisionInstanceIdIn);
        this.decisionInstanceIdIn = decisionInstanceIdIn;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery decisionDefinitionId(final String decisionDefinitionId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "decisionDefinitionId", (Object)decisionDefinitionId);
        this.decisionDefinitionId = decisionDefinitionId;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery decisionDefinitionIdIn(final String... decisionDefinitionIdIn) {
        EnsureUtil.ensureNotNull(NotValidException.class, "decisionDefinitionIdIn", (Object[])decisionDefinitionIdIn);
        this.decisionDefinitionIdIn = decisionDefinitionIdIn;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery decisionDefinitionKey(final String decisionDefinitionKey) {
        EnsureUtil.ensureNotNull(NotValidException.class, "decisionDefinitionKey", (Object)decisionDefinitionKey);
        this.decisionDefinitionKey = decisionDefinitionKey;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery decisionDefinitionKeyIn(final String... decisionDefinitionKeyIn) {
        EnsureUtil.ensureNotNull(NotValidException.class, "decisionDefinitionKeyIn", (Object[])decisionDefinitionKeyIn);
        this.decisionDefinitionKeyIn = decisionDefinitionKeyIn;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery decisionDefinitionName(final String decisionDefinitionName) {
        EnsureUtil.ensureNotNull(NotValidException.class, "decisionDefinitionName", (Object)decisionDefinitionName);
        this.decisionDefinitionName = decisionDefinitionName;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery decisionDefinitionNameLike(final String decisionDefinitionNameLike) {
        EnsureUtil.ensureNotNull(NotValidException.class, "decisionDefinitionNameLike", (Object)decisionDefinitionNameLike);
        this.decisionDefinitionNameLike = decisionDefinitionNameLike;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery processDefinitionKey(final String processDefinitionKey) {
        EnsureUtil.ensureNotNull(NotValidException.class, "processDefinitionKey", (Object)processDefinitionKey);
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery processDefinitionId(final String processDefinitionId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "processDefinitionId", (Object)processDefinitionId);
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery processInstanceId(final String processInstanceId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "processInstanceId", (Object)processInstanceId);
        this.processInstanceId = processInstanceId;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery caseDefinitionKey(final String caseDefinitionKey) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseDefinitionKey", (Object)caseDefinitionKey);
        this.caseDefinitionKey = caseDefinitionKey;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery caseDefinitionId(final String caseDefinitionId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseDefinitionId", (Object)caseDefinitionId);
        this.caseDefinitionId = caseDefinitionId;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery caseInstanceId(final String caseInstanceId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "caseInstanceId", (Object)caseInstanceId);
        this.caseInstanceId = caseInstanceId;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery activityIdIn(final String... activityIds) {
        EnsureUtil.ensureNotNull("activityIds", (Object[])activityIds);
        this.activityIds = activityIds;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery activityInstanceIdIn(final String... activityInstanceIds) {
        EnsureUtil.ensureNotNull("activityInstanceIds", (Object[])activityInstanceIds);
        this.activityInstanceIds = activityInstanceIds;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery evaluatedBefore(final Date evaluatedBefore) {
        EnsureUtil.ensureNotNull(NotValidException.class, "evaluatedBefore", evaluatedBefore);
        this.evaluatedBefore = evaluatedBefore;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery evaluatedAfter(final Date evaluatedAfter) {
        EnsureUtil.ensureNotNull(NotValidException.class, "evaluatedAfter", evaluatedAfter);
        this.evaluatedAfter = evaluatedAfter;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery withoutTenantId() {
        this.tenantIds = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery orderByTenantId() {
        return ((AbstractQuery<HistoricDecisionInstanceQuery, U>)this).orderBy(HistoricDecisionInstanceQueryProperty.TENANT_ID);
    }
    
    @Override
    public HistoricDecisionInstanceQuery userId(final String userId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "userId", (Object)userId);
        this.userId = userId;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery orderByEvaluationTime() {
        this.orderBy(HistoricDecisionInstanceQueryProperty.EVALUATION_TIME);
        return this;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getHistoricDecisionInstanceManager().findHistoricDecisionInstanceCountByQueryCriteria(this);
    }
    
    @Override
    public List<HistoricDecisionInstance> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getHistoricDecisionInstanceManager().findHistoricDecisionInstancesByQueryCriteria(this, page);
    }
    
    @Override
    public List<ImmutablePair<String, String>> executeDeploymentIdMappingsList(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getHistoricDecisionInstanceManager().findHistoricDecisionInstanceDeploymentIdMappingsByQueryCriteria(this);
    }
    
    public String getDecisionDefinitionId() {
        return this.decisionDefinitionId;
    }
    
    public String getDecisionDefinitionKey() {
        return this.decisionDefinitionKey;
    }
    
    public String getDecisionDefinitionName() {
        return this.decisionDefinitionName;
    }
    
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public String getCaseDefinitionKey() {
        return this.caseDefinitionKey;
    }
    
    public String getCaseDefinitionId() {
        return this.caseDefinitionId;
    }
    
    public String getCaseInstanceId() {
        return this.caseInstanceId;
    }
    
    public String[] getActivityInstanceIds() {
        return this.activityInstanceIds;
    }
    
    public String[] getActivityIds() {
        return this.activityIds;
    }
    
    public String[] getTenantIds() {
        return this.tenantIds;
    }
    
    @Override
    public HistoricDecisionInstanceQuery includeInputs() {
        this.includeInput = true;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery includeOutputs() {
        this.includeOutputs = true;
        return this;
    }
    
    public boolean isIncludeInput() {
        return this.includeInput;
    }
    
    public boolean isIncludeOutputs() {
        return this.includeOutputs;
    }
    
    @Override
    public HistoricDecisionInstanceQuery disableBinaryFetching() {
        this.isByteArrayFetchingEnabled = false;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery disableCustomObjectDeserialization() {
        this.isCustomObjectDeserializationEnabled = false;
        return this;
    }
    
    public boolean isByteArrayFetchingEnabled() {
        return this.isByteArrayFetchingEnabled;
    }
    
    public boolean isCustomObjectDeserializationEnabled() {
        return this.isCustomObjectDeserializationEnabled;
    }
    
    public String getRootDecisionInstanceId() {
        return this.rootDecisionInstanceId;
    }
    
    @Override
    public HistoricDecisionInstanceQuery rootDecisionInstanceId(final String rootDecisionInstanceId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "rootDecisionInstanceId", (Object)rootDecisionInstanceId);
        this.rootDecisionInstanceId = rootDecisionInstanceId;
        return this;
    }
    
    public boolean isRootDecisionInstancesOnly() {
        return this.rootDecisionInstancesOnly;
    }
    
    @Override
    public HistoricDecisionInstanceQuery rootDecisionInstancesOnly() {
        this.rootDecisionInstancesOnly = true;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery decisionRequirementsDefinitionId(final String decisionRequirementsDefinitionId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "decisionRequirementsDefinitionId", (Object)decisionRequirementsDefinitionId);
        this.decisionRequirementsDefinitionId = decisionRequirementsDefinitionId;
        return this;
    }
    
    @Override
    public HistoricDecisionInstanceQuery decisionRequirementsDefinitionKey(final String decisionRequirementsDefinitionKey) {
        EnsureUtil.ensureNotNull(NotValidException.class, "decisionRequirementsDefinitionKey", (Object)decisionRequirementsDefinitionKey);
        this.decisionRequirementsDefinitionKey = decisionRequirementsDefinitionKey;
        return this;
    }
    
    public String getDecisionRequirementsDefinitionId() {
        return this.decisionRequirementsDefinitionId;
    }
    
    public String getDecisionRequirementsDefinitionKey() {
        return this.decisionRequirementsDefinitionKey;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
}
