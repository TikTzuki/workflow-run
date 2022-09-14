// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.variable.serializer.AbstractTypedValueSerializer;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.CompareUtil;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.cmd.CommandLogger;
import java.io.Serializable;
import org.zik.bpm.engine.runtime.VariableInstance;
import org.zik.bpm.engine.runtime.VariableInstanceQuery;

public class VariableInstanceQueryImpl extends AbstractVariableQueryImpl<VariableInstanceQuery, VariableInstance> implements VariableInstanceQuery, Serializable
{
    private static final CommandLogger LOG;
    private static final long serialVersionUID = 1L;
    protected String variableId;
    protected String variableName;
    protected String[] variableNames;
    protected String variableNameLike;
    protected String[] executionIds;
    protected String[] processInstanceIds;
    protected String[] caseExecutionIds;
    protected String[] caseInstanceIds;
    protected String[] taskIds;
    protected String[] batchIds;
    protected String[] variableScopeIds;
    protected String[] activityInstanceIds;
    protected String[] tenantIds;
    protected boolean isByteArrayFetchingEnabled;
    protected boolean isCustomObjectDeserializationEnabled;
    
    public VariableInstanceQueryImpl() {
        this.isByteArrayFetchingEnabled = true;
        this.isCustomObjectDeserializationEnabled = true;
    }
    
    public VariableInstanceQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.isByteArrayFetchingEnabled = true;
        this.isCustomObjectDeserializationEnabled = true;
    }
    
    @Override
    public VariableInstanceQuery variableId(final String id) {
        EnsureUtil.ensureNotNull("id", (Object)id);
        this.variableId = id;
        return this;
    }
    
    @Override
    public VariableInstanceQuery variableName(final String variableName) {
        this.variableName = variableName;
        return this;
    }
    
    @Override
    public VariableInstanceQuery variableNameIn(final String... variableNames) {
        this.variableNames = variableNames;
        return this;
    }
    
    @Override
    public VariableInstanceQuery variableNameLike(final String variableNameLike) {
        this.variableNameLike = variableNameLike;
        return this;
    }
    
    @Override
    public VariableInstanceQuery executionIdIn(final String... executionIds) {
        this.executionIds = executionIds;
        return this;
    }
    
    @Override
    public VariableInstanceQuery processInstanceIdIn(final String... processInstanceIds) {
        this.processInstanceIds = processInstanceIds;
        return this;
    }
    
    @Override
    public VariableInstanceQuery caseExecutionIdIn(final String... caseExecutionIds) {
        this.caseExecutionIds = caseExecutionIds;
        return this;
    }
    
    @Override
    public VariableInstanceQuery caseInstanceIdIn(final String... caseInstanceIds) {
        this.caseInstanceIds = caseInstanceIds;
        return this;
    }
    
    @Override
    public VariableInstanceQuery taskIdIn(final String... taskIds) {
        this.taskIds = taskIds;
        return this;
    }
    
    @Override
    public VariableInstanceQuery batchIdIn(final String... batchIds) {
        this.batchIds = batchIds;
        return this;
    }
    
    @Override
    public VariableInstanceQuery variableScopeIdIn(final String... variableScopeIds) {
        this.variableScopeIds = variableScopeIds;
        return this;
    }
    
    @Override
    public VariableInstanceQuery activityInstanceIdIn(final String... activityInstanceIds) {
        this.activityInstanceIds = activityInstanceIds;
        return this;
    }
    
    @Override
    public VariableInstanceQuery disableBinaryFetching() {
        this.isByteArrayFetchingEnabled = false;
        return this;
    }
    
    @Override
    public VariableInstanceQuery disableCustomObjectDeserialization() {
        this.isCustomObjectDeserializationEnabled = false;
        return this;
    }
    
    @Override
    public VariableInstanceQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        return this;
    }
    
    @Override
    public VariableInstanceQuery orderByVariableName() {
        this.orderBy(VariableInstanceQueryProperty.VARIABLE_NAME);
        return this;
    }
    
    @Override
    public VariableInstanceQuery orderByVariableType() {
        this.orderBy(VariableInstanceQueryProperty.VARIABLE_TYPE);
        return this;
    }
    
    @Override
    public VariableInstanceQuery orderByActivityInstanceId() {
        this.orderBy(VariableInstanceQueryProperty.ACTIVITY_INSTANCE_ID);
        return this;
    }
    
    @Override
    public VariableInstanceQuery orderByTenantId() {
        this.orderBy(VariableInstanceQueryProperty.TENANT_ID);
        return this;
    }
    
    @Override
    protected boolean hasExcludingConditions() {
        return super.hasExcludingConditions() || CompareUtil.elementIsNotContainedInArray(this.variableName, this.variableNames);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        this.ensureVariablesInitialized();
        return commandContext.getVariableInstanceManager().findVariableInstanceCountByQueryCriteria(this);
    }
    
    @Override
    public List<VariableInstance> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        this.ensureVariablesInitialized();
        final List<VariableInstance> result = commandContext.getVariableInstanceManager().findVariableInstanceByQueryCriteria(this, page);
        if (result == null) {
            return result;
        }
        for (final VariableInstance variableInstance : result) {
            final VariableInstanceEntity variableInstanceEntity = (VariableInstanceEntity)variableInstance;
            if (this.shouldFetchValue(variableInstanceEntity)) {
                try {
                    variableInstanceEntity.getTypedValue(this.isCustomObjectDeserializationEnabled);
                }
                catch (Exception t) {
                    VariableInstanceQueryImpl.LOG.exceptionWhileGettingValueForVariable(t);
                }
            }
        }
        return result;
    }
    
    protected boolean shouldFetchValue(final VariableInstanceEntity entity) {
        return this.isByteArrayFetchingEnabled || !AbstractTypedValueSerializer.BINARY_VALUE_TYPES.contains(entity.getSerializer().getType().getName());
    }
    
    public String getVariableId() {
        return this.variableId;
    }
    
    public String getVariableName() {
        return this.variableName;
    }
    
    public String[] getVariableNames() {
        return this.variableNames;
    }
    
    public String getVariableNameLike() {
        return this.variableNameLike;
    }
    
    public String[] getExecutionIds() {
        return this.executionIds;
    }
    
    public String[] getProcessInstanceIds() {
        return this.processInstanceIds;
    }
    
    public String[] getCaseExecutionIds() {
        return this.caseExecutionIds;
    }
    
    public String[] getCaseInstanceIds() {
        return this.caseInstanceIds;
    }
    
    public String[] getTaskIds() {
        return this.taskIds;
    }
    
    public String[] getBatchIds() {
        return this.batchIds;
    }
    
    public String[] getVariableScopeIds() {
        return this.variableScopeIds;
    }
    
    public String[] getActivityInstanceIds() {
        return this.activityInstanceIds;
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
