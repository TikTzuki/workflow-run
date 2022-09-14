// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.variable.serializer.AbstractTypedValueSerializer;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.HistoricDetailVariableInstanceUpdateEntity;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Date;
import org.zik.bpm.engine.impl.cmd.CommandLogger;
import org.zik.bpm.engine.history.HistoricDetail;
import org.zik.bpm.engine.history.HistoricDetailQuery;

public class HistoricDetailQueryImpl extends AbstractQuery<HistoricDetailQuery, HistoricDetail> implements HistoricDetailQuery
{
    private static final CommandLogger LOG;
    private static final long serialVersionUID = 1L;
    protected String detailId;
    protected String taskId;
    protected String processInstanceId;
    protected String caseInstanceId;
    protected String executionId;
    protected String caseExecutionId;
    protected String activityId;
    protected String activityInstanceId;
    protected String type;
    protected String variableInstanceId;
    protected String[] variableTypes;
    protected String[] tenantIds;
    protected boolean isTenantIdSet;
    protected String[] processInstanceIds;
    protected String userOperationId;
    protected Long sequenceCounter;
    protected Date occurredBefore;
    protected Date occurredAfter;
    protected boolean initial;
    protected boolean excludeTaskRelated;
    protected boolean isByteArrayFetchingEnabled;
    protected boolean isCustomObjectDeserializationEnabled;
    
    public HistoricDetailQueryImpl() {
        this.initial = false;
        this.excludeTaskRelated = false;
        this.isByteArrayFetchingEnabled = true;
        this.isCustomObjectDeserializationEnabled = true;
    }
    
    public HistoricDetailQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.initial = false;
        this.excludeTaskRelated = false;
        this.isByteArrayFetchingEnabled = true;
        this.isCustomObjectDeserializationEnabled = true;
    }
    
    @Override
    public HistoricDetailQuery detailId(final String id) {
        EnsureUtil.ensureNotNull("detailId", (Object)id);
        this.detailId = id;
        return this;
    }
    
    @Override
    public HistoricDetailQuery variableInstanceId(final String variableInstanceId) {
        EnsureUtil.ensureNotNull("variableInstanceId", (Object)variableInstanceId);
        this.variableInstanceId = variableInstanceId;
        return this;
    }
    
    @Override
    public HistoricDetailQuery variableTypeIn(final String... variableTypes) {
        EnsureUtil.ensureNotNull("Variable types", (Object[])variableTypes);
        this.variableTypes = this.lowerCase(variableTypes);
        return this;
    }
    
    private String[] lowerCase(final String... variableTypes) {
        for (int i = 0; i < variableTypes.length; ++i) {
            variableTypes[i] = variableTypes[i].toLowerCase();
        }
        return variableTypes;
    }
    
    @Override
    public HistoricDetailQuery processInstanceId(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }
    
    @Override
    public HistoricDetailQuery caseInstanceId(final String caseInstanceId) {
        EnsureUtil.ensureNotNull("Case instance id", (Object)caseInstanceId);
        this.caseInstanceId = caseInstanceId;
        return this;
    }
    
    @Override
    public HistoricDetailQuery executionId(final String executionId) {
        this.executionId = executionId;
        return this;
    }
    
    @Override
    public HistoricDetailQuery caseExecutionId(final String caseExecutionId) {
        EnsureUtil.ensureNotNull("Case execution id", (Object)caseExecutionId);
        this.caseExecutionId = caseExecutionId;
        return this;
    }
    
    @Override
    public HistoricDetailQuery activityId(final String activityId) {
        this.activityId = activityId;
        return this;
    }
    
    @Override
    public HistoricDetailQuery activityInstanceId(final String activityInstanceId) {
        this.activityInstanceId = activityInstanceId;
        return this;
    }
    
    @Override
    public HistoricDetailQuery taskId(final String taskId) {
        this.taskId = taskId;
        return this;
    }
    
    @Override
    public HistoricDetailQuery formProperties() {
        this.type = "FormProperty";
        return this;
    }
    
    @Override
    public HistoricDetailQuery formFields() {
        this.type = "FormProperty";
        return this;
    }
    
    @Override
    public HistoricDetailQuery variableUpdates() {
        this.type = "VariableUpdate";
        return this;
    }
    
    @Override
    public HistoricDetailQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public HistoricDetailQuery withoutTenantId() {
        this.tenantIds = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public HistoricDetailQuery processInstanceIdIn(final String... processInstanceIds) {
        EnsureUtil.ensureNotNull("Process Instance Ids", (Object[])processInstanceIds);
        this.processInstanceIds = processInstanceIds;
        return this;
    }
    
    @Override
    public HistoricDetailQuery userOperationId(final String userOperationId) {
        EnsureUtil.ensureNotNull("userOperationId", (Object)userOperationId);
        this.userOperationId = userOperationId;
        return this;
    }
    
    public HistoricDetailQueryImpl sequenceCounter(final long sequenceCounter) {
        this.sequenceCounter = sequenceCounter;
        return this;
    }
    
    @Override
    public HistoricDetailQuery excludeTaskDetails() {
        this.excludeTaskRelated = true;
        return this;
    }
    
    @Override
    public HistoricDetailQuery occurredBefore(final Date date) {
        EnsureUtil.ensureNotNull("occurred before", date);
        this.occurredBefore = date;
        return this;
    }
    
    @Override
    public HistoricDetailQuery occurredAfter(final Date date) {
        EnsureUtil.ensureNotNull("occurred after", date);
        this.occurredAfter = date;
        return this;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getHistoricDetailManager().findHistoricDetailCountByQueryCriteria(this);
    }
    
    @Override
    public HistoricDetailQuery disableBinaryFetching() {
        this.isByteArrayFetchingEnabled = false;
        return this;
    }
    
    @Override
    public HistoricDetailQuery disableCustomObjectDeserialization() {
        this.isCustomObjectDeserializationEnabled = false;
        return this;
    }
    
    @Override
    public HistoricDetailQuery initial() {
        this.initial = true;
        return this;
    }
    
    @Override
    public List<HistoricDetail> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        final List<HistoricDetail> historicDetails = commandContext.getHistoricDetailManager().findHistoricDetailsByQueryCriteria(this, page);
        if (historicDetails != null) {
            for (final HistoricDetail historicDetail : historicDetails) {
                if (historicDetail instanceof HistoricDetailVariableInstanceUpdateEntity) {
                    final HistoricDetailVariableInstanceUpdateEntity entity = (HistoricDetailVariableInstanceUpdateEntity)historicDetail;
                    if (!this.shouldFetchValue(entity)) {
                        continue;
                    }
                    try {
                        entity.getTypedValue(this.isCustomObjectDeserializationEnabled);
                    }
                    catch (Exception t) {
                        HistoricDetailQueryImpl.LOG.exceptionWhileGettingValueForVariable(t);
                    }
                }
            }
        }
        return historicDetails;
    }
    
    protected boolean shouldFetchValue(final HistoricDetailVariableInstanceUpdateEntity entity) {
        return this.isByteArrayFetchingEnabled || !AbstractTypedValueSerializer.BINARY_VALUE_TYPES.contains(entity.getSerializer().getType().getName());
    }
    
    @Override
    public HistoricDetailQuery orderByProcessInstanceId() {
        this.orderBy(HistoricDetailQueryProperty.PROCESS_INSTANCE_ID);
        return this;
    }
    
    @Override
    public HistoricDetailQuery orderByTime() {
        this.orderBy(HistoricDetailQueryProperty.TIME);
        return this;
    }
    
    @Override
    public HistoricDetailQuery orderByVariableName() {
        this.orderBy(HistoricDetailQueryProperty.VARIABLE_NAME);
        return this;
    }
    
    @Override
    public HistoricDetailQuery orderByFormPropertyId() {
        this.orderBy(HistoricDetailQueryProperty.VARIABLE_NAME);
        return this;
    }
    
    @Override
    public HistoricDetailQuery orderByVariableRevision() {
        this.orderBy(HistoricDetailQueryProperty.VARIABLE_REVISION);
        return this;
    }
    
    @Override
    public HistoricDetailQuery orderByVariableType() {
        this.orderBy(HistoricDetailQueryProperty.VARIABLE_TYPE);
        return this;
    }
    
    @Override
    public HistoricDetailQuery orderPartiallyByOccurrence() {
        this.orderBy(HistoricDetailQueryProperty.SEQUENCE_COUNTER);
        return this;
    }
    
    @Override
    public HistoricDetailQuery orderByTenantId() {
        return ((AbstractQuery<HistoricDetailQuery, U>)this).orderBy(HistoricDetailQueryProperty.TENANT_ID);
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public String getCaseInstanceId() {
        return this.caseInstanceId;
    }
    
    public String getExecutionId() {
        return this.executionId;
    }
    
    public String getCaseExecutionId() {
        return this.caseExecutionId;
    }
    
    public String getTaskId() {
        return this.taskId;
    }
    
    public String getActivityId() {
        return this.activityId;
    }
    
    public String getType() {
        return this.type;
    }
    
    public boolean getExcludeTaskRelated() {
        return this.excludeTaskRelated;
    }
    
    public String getDetailId() {
        return this.detailId;
    }
    
    public String[] getProcessInstanceIds() {
        return this.processInstanceIds;
    }
    
    public Date getOccurredBefore() {
        return this.occurredBefore;
    }
    
    public Date getOccurredAfter() {
        return this.occurredAfter;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
    
    public boolean isInitial() {
        return this.initial;
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
