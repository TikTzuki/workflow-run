// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.variable.serializer.AbstractTypedValueSerializer;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.variable.serializer.VariableSerializers;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.context.Context;
import java.util.Arrays;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.List;
import org.zik.bpm.engine.impl.cmd.CommandLogger;
import org.zik.bpm.engine.history.HistoricVariableInstance;
import org.zik.bpm.engine.history.HistoricVariableInstanceQuery;

public class HistoricVariableInstanceQueryImpl extends AbstractQuery<HistoricVariableInstanceQuery, HistoricVariableInstance> implements HistoricVariableInstanceQuery
{
    private static final CommandLogger LOG;
    private static final long serialVersionUID = 1L;
    protected List<String> variableNameIn;
    protected String variableId;
    protected String processInstanceId;
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected String caseInstanceId;
    protected String variableName;
    protected String variableNameLike;
    protected QueryVariableValue queryVariableValue;
    protected Boolean variableNamesIgnoreCase;
    protected Boolean variableValuesIgnoreCase;
    protected String[] variableTypes;
    protected String[] taskIds;
    protected String[] executionIds;
    protected String[] caseExecutionIds;
    protected String[] caseActivityIds;
    protected String[] activityInstanceIds;
    protected String[] tenantIds;
    protected boolean isTenantIdSet;
    protected String[] processInstanceIds;
    protected boolean includeDeleted;
    protected boolean isByteArrayFetchingEnabled;
    protected boolean isCustomObjectDeserializationEnabled;
    
    public HistoricVariableInstanceQueryImpl() {
        this.includeDeleted = false;
        this.isByteArrayFetchingEnabled = true;
        this.isCustomObjectDeserializationEnabled = true;
    }
    
    public HistoricVariableInstanceQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.includeDeleted = false;
        this.isByteArrayFetchingEnabled = true;
        this.isCustomObjectDeserializationEnabled = true;
    }
    
    @Override
    public HistoricVariableInstanceQuery variableNameIn(final String... names) {
        EnsureUtil.ensureNotNull("Variable names", (Object[])names);
        this.variableNameIn = Arrays.asList(names);
        return this;
    }
    
    @Override
    public HistoricVariableInstanceQuery variableId(final String id) {
        EnsureUtil.ensureNotNull("variableId", (Object)id);
        this.variableId = id;
        return this;
    }
    
    @Override
    public HistoricVariableInstanceQueryImpl processInstanceId(final String processInstanceId) {
        EnsureUtil.ensureNotNull("processInstanceId", (Object)processInstanceId);
        this.processInstanceId = processInstanceId;
        return this;
    }
    
    @Override
    public HistoricVariableInstanceQuery processDefinitionId(final String processDefinitionId) {
        EnsureUtil.ensureNotNull("processDefinitionId", (Object)processDefinitionId);
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public HistoricVariableInstanceQuery processDefinitionKey(final String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }
    
    @Override
    public HistoricVariableInstanceQuery caseInstanceId(final String caseInstanceId) {
        EnsureUtil.ensureNotNull("caseInstanceId", (Object)caseInstanceId);
        this.caseInstanceId = caseInstanceId;
        return this;
    }
    
    @Override
    public HistoricVariableInstanceQuery variableTypeIn(final String... variableTypes) {
        EnsureUtil.ensureNotNull("Variable types", (Object[])variableTypes);
        this.variableTypes = this.lowerCase(variableTypes);
        return this;
    }
    
    @Override
    public HistoricVariableInstanceQuery matchVariableNamesIgnoreCase() {
        this.variableNamesIgnoreCase = true;
        if (this.queryVariableValue != null) {
            this.queryVariableValue.variableNameIgnoreCase = true;
        }
        return this;
    }
    
    @Override
    public HistoricVariableInstanceQuery matchVariableValuesIgnoreCase() {
        this.variableValuesIgnoreCase = true;
        if (this.queryVariableValue != null) {
            this.queryVariableValue.variableValueIgnoreCase = true;
        }
        return this;
    }
    
    private String[] lowerCase(final String... variableTypes) {
        for (int i = 0; i < variableTypes.length; ++i) {
            variableTypes[i] = variableTypes[i].toLowerCase();
        }
        return variableTypes;
    }
    
    @Override
    public HistoricVariableInstanceQuery processInstanceIdIn(final String... processInstanceIds) {
        EnsureUtil.ensureNotNull("Process Instance Ids", (Object[])processInstanceIds);
        this.processInstanceIds = processInstanceIds;
        return this;
    }
    
    @Override
    public HistoricVariableInstanceQuery taskIdIn(final String... taskIds) {
        EnsureUtil.ensureNotNull("Task Ids", (Object[])taskIds);
        this.taskIds = taskIds;
        return this;
    }
    
    @Override
    public HistoricVariableInstanceQuery executionIdIn(final String... executionIds) {
        EnsureUtil.ensureNotNull("Execution Ids", (Object[])executionIds);
        this.executionIds = executionIds;
        return this;
    }
    
    @Override
    public HistoricVariableInstanceQuery caseExecutionIdIn(final String... caseExecutionIds) {
        EnsureUtil.ensureNotNull("Case execution ids", (Object[])caseExecutionIds);
        this.caseExecutionIds = caseExecutionIds;
        return this;
    }
    
    @Override
    public HistoricVariableInstanceQuery caseActivityIdIn(final String... caseActivityIds) {
        EnsureUtil.ensureNotNull("Case activity ids", (Object[])caseActivityIds);
        this.caseActivityIds = caseActivityIds;
        return this;
    }
    
    @Override
    public HistoricVariableInstanceQuery activityInstanceIdIn(final String... activityInstanceIds) {
        EnsureUtil.ensureNotNull("Activity Instance Ids", (Object[])activityInstanceIds);
        this.activityInstanceIds = activityInstanceIds;
        return this;
    }
    
    @Override
    public HistoricVariableInstanceQuery variableName(final String variableName) {
        EnsureUtil.ensureNotNull("variableName", (Object)variableName);
        this.variableName = variableName;
        return this;
    }
    
    @Override
    public HistoricVariableInstanceQuery variableValueEquals(final String variableName, final Object variableValue) {
        EnsureUtil.ensureNotNull("variableName", (Object)variableName);
        EnsureUtil.ensureNotNull("variableValue", variableValue);
        this.variableName = variableName;
        this.queryVariableValue = new QueryVariableValue(variableName, variableValue, QueryOperator.EQUALS, true, Boolean.TRUE.equals(this.variableNamesIgnoreCase), Boolean.TRUE.equals(this.variableValuesIgnoreCase));
        return this;
    }
    
    @Override
    public HistoricVariableInstanceQuery variableNameLike(final String variableNameLike) {
        EnsureUtil.ensureNotNull("variableNameLike", (Object)variableNameLike);
        this.variableNameLike = variableNameLike;
        return this;
    }
    
    protected void ensureVariablesInitialized() {
        if (this.queryVariableValue != null) {
            final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
            final VariableSerializers variableSerializers = processEngineConfiguration.getVariableSerializers();
            final String dbType = processEngineConfiguration.getDatabaseType();
            this.queryVariableValue.initialize(variableSerializers, dbType);
        }
    }
    
    @Override
    public HistoricVariableInstanceQuery disableBinaryFetching() {
        this.isByteArrayFetchingEnabled = false;
        return this;
    }
    
    @Override
    public HistoricVariableInstanceQuery disableCustomObjectDeserialization() {
        this.isCustomObjectDeserializationEnabled = false;
        return this;
    }
    
    @Override
    public HistoricVariableInstanceQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public HistoricVariableInstanceQuery withoutTenantId() {
        this.tenantIds = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        this.ensureVariablesInitialized();
        return commandContext.getHistoricVariableInstanceManager().findHistoricVariableInstanceCountByQueryCriteria(this);
    }
    
    @Override
    public List<HistoricVariableInstance> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        this.ensureVariablesInitialized();
        final List<HistoricVariableInstance> historicVariableInstances = commandContext.getHistoricVariableInstanceManager().findHistoricVariableInstancesByQueryCriteria(this, page);
        if (historicVariableInstances != null) {
            for (final HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
                final HistoricVariableInstanceEntity variableInstanceEntity = (HistoricVariableInstanceEntity)historicVariableInstance;
                if (this.shouldFetchValue(variableInstanceEntity)) {
                    try {
                        variableInstanceEntity.getTypedValue(this.isCustomObjectDeserializationEnabled);
                    }
                    catch (Exception t) {
                        HistoricVariableInstanceQueryImpl.LOG.exceptionWhileGettingValueForVariable(t);
                    }
                }
            }
        }
        return historicVariableInstances;
    }
    
    protected boolean shouldFetchValue(final HistoricVariableInstanceEntity entity) {
        return this.isByteArrayFetchingEnabled || !AbstractTypedValueSerializer.BINARY_VALUE_TYPES.contains(entity.getSerializer().getType().getName());
    }
    
    @Override
    public HistoricVariableInstanceQuery orderByProcessInstanceId() {
        this.orderBy(HistoricVariableInstanceQueryProperty.PROCESS_INSTANCE_ID);
        return this;
    }
    
    @Override
    public HistoricVariableInstanceQuery orderByVariableName() {
        this.orderBy(HistoricVariableInstanceQueryProperty.VARIABLE_NAME);
        return this;
    }
    
    @Override
    public HistoricVariableInstanceQuery orderByTenantId() {
        this.orderBy(HistoricVariableInstanceQueryProperty.TENANT_ID);
        return this;
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public String getCaseInstanceId() {
        return this.caseInstanceId;
    }
    
    public String[] getActivityInstanceIds() {
        return this.activityInstanceIds;
    }
    
    public String[] getProcessInstanceIds() {
        return this.processInstanceIds;
    }
    
    public String[] getTaskIds() {
        return this.taskIds;
    }
    
    public String[] getExecutionIds() {
        return this.executionIds;
    }
    
    public String[] getCaseExecutionIds() {
        return this.caseExecutionIds;
    }
    
    public String[] getCaseActivityIds() {
        return this.caseActivityIds;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
    
    public String getVariableName() {
        return this.variableName;
    }
    
    public String getVariableNameLike() {
        return this.variableNameLike;
    }
    
    public QueryVariableValue getQueryVariableValue() {
        return this.queryVariableValue;
    }
    
    public Boolean getVariableNamesIgnoreCase() {
        return this.variableNamesIgnoreCase;
    }
    
    public Boolean getVariableValuesIgnoreCase() {
        return this.variableValuesIgnoreCase;
    }
    
    @Override
    public HistoricVariableInstanceQuery includeDeleted() {
        this.includeDeleted = true;
        return this;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public List<String> getVariableNameIn() {
        return this.variableNameIn;
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
