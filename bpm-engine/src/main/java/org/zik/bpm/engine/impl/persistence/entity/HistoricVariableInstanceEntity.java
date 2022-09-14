// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.variable.serializer.TypedValueSerializer;
import org.camunda.bpm.engine.variable.value.TypedValue;
import java.util.List;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.history.event.HistoricVariableUpdateEventEntity;
import org.zik.bpm.engine.repository.ResourceType;
import org.zik.bpm.engine.repository.ResourceTypes;
import org.zik.bpm.engine.impl.persistence.entity.util.TypedValueField;
import org.zik.bpm.engine.impl.persistence.entity.util.ByteArrayField;
import java.util.Date;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.db.DbEntityLifecycleAware;
import java.io.Serializable;
import org.zik.bpm.engine.impl.db.HistoricEntity;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.history.HistoricVariableInstance;
import org.zik.bpm.engine.impl.variable.serializer.ValueFields;

public class HistoricVariableInstanceEntity implements ValueFields, HistoricVariableInstance, DbEntity, HasDbRevision, HistoricEntity, Serializable, DbEntityLifecycleAware
{
    private static final long serialVersionUID = 1L;
    protected static final EnginePersistenceLogger LOG;
    protected String id;
    protected String processDefinitionKey;
    protected String processDefinitionId;
    protected String rootProcessInstanceId;
    protected String processInstanceId;
    protected String taskId;
    protected String executionId;
    protected String activityInstanceId;
    protected String tenantId;
    protected String caseDefinitionKey;
    protected String caseDefinitionId;
    protected String caseInstanceId;
    protected String caseExecutionId;
    protected String name;
    protected int revision;
    protected Date createTime;
    protected Long longValue;
    protected Double doubleValue;
    protected String textValue;
    protected String textValue2;
    protected String state;
    protected Date removalTime;
    protected ByteArrayField byteArrayField;
    protected TypedValueField typedValueField;
    
    public HistoricVariableInstanceEntity() {
        this.state = "CREATED";
        this.byteArrayField = new ByteArrayField(this, ResourceTypes.HISTORY);
        this.typedValueField = new TypedValueField(this, false);
    }
    
    public HistoricVariableInstanceEntity(final HistoricVariableUpdateEventEntity historyEvent) {
        this.state = "CREATED";
        this.byteArrayField = new ByteArrayField(this, ResourceTypes.HISTORY);
        this.typedValueField = new TypedValueField(this, false);
        this.updateFromEvent(historyEvent);
    }
    
    public void updateFromEvent(final HistoricVariableUpdateEventEntity historyEvent) {
        this.id = historyEvent.getVariableInstanceId();
        this.processDefinitionKey = historyEvent.getProcessDefinitionKey();
        this.processDefinitionId = historyEvent.getProcessDefinitionId();
        this.processInstanceId = historyEvent.getProcessInstanceId();
        this.taskId = historyEvent.getTaskId();
        this.executionId = historyEvent.getExecutionId();
        this.activityInstanceId = historyEvent.getScopeActivityInstanceId();
        this.tenantId = historyEvent.getTenantId();
        this.caseDefinitionKey = historyEvent.getCaseDefinitionKey();
        this.caseDefinitionId = historyEvent.getCaseDefinitionId();
        this.caseInstanceId = historyEvent.getCaseInstanceId();
        this.caseExecutionId = historyEvent.getCaseExecutionId();
        this.name = historyEvent.getVariableName();
        this.longValue = historyEvent.getLongValue();
        this.doubleValue = historyEvent.getDoubleValue();
        this.textValue = historyEvent.getTextValue();
        this.textValue2 = historyEvent.getTextValue2();
        this.createTime = historyEvent.getTimestamp();
        this.rootProcessInstanceId = historyEvent.getRootProcessInstanceId();
        this.removalTime = historyEvent.getRemovalTime();
        this.setSerializerName(historyEvent.getSerializerName());
        this.byteArrayField.deleteByteArrayValue();
        if (historyEvent.getByteValue() != null) {
            this.byteArrayField.setRootProcessInstanceId(this.rootProcessInstanceId);
            this.byteArrayField.setRemovalTime(this.removalTime);
            this.setByteArrayValue(historyEvent.getByteValue());
        }
    }
    
    public void delete() {
        this.byteArrayField.deleteByteArrayValue();
        Context.getCommandContext().getDbEntityManager().delete(this);
    }
    
    @Override
    public Object getPersistentState() {
        final List<Object> state = new ArrayList<Object>(8);
        state.add(this.getSerializerName());
        state.add(this.textValue);
        state.add(this.textValue2);
        state.add(this.state);
        state.add(this.doubleValue);
        state.add(this.longValue);
        state.add(this.processDefinitionId);
        state.add(this.processDefinitionKey);
        state.add(this.getByteArrayId());
        return state;
    }
    
    @Override
    public int getRevisionNext() {
        return this.revision + 1;
    }
    
    @Override
    public Object getValue() {
        return this.typedValueField.getValue();
    }
    
    @Override
    public TypedValue getTypedValue() {
        return this.typedValueField.getTypedValue(false);
    }
    
    public TypedValue getTypedValue(final boolean deserializeValue) {
        return this.typedValueField.getTypedValue(deserializeValue, false);
    }
    
    public TypedValueSerializer<?> getSerializer() {
        return this.typedValueField.getSerializer();
    }
    
    public String getByteArrayValueId() {
        return this.byteArrayField.getByteArrayId();
    }
    
    public String getByteArrayId() {
        return this.byteArrayField.getByteArrayId();
    }
    
    public void setByteArrayId(final String byteArrayId) {
        this.byteArrayField.setByteArrayId(byteArrayId);
    }
    
    @Override
    public byte[] getByteArrayValue() {
        return this.byteArrayField.getByteArrayValue();
    }
    
    @Override
    public void setByteArrayValue(final byte[] bytes) {
        this.byteArrayField.setByteArrayValue(bytes);
    }
    
    @Override
    public void postLoad() {
        this.typedValueField.postLoad();
    }
    
    public String getSerializerName() {
        return this.typedValueField.getSerializerName();
    }
    
    public void setSerializerName(final String serializerName) {
        this.typedValueField.setSerializerName(serializerName);
    }
    
    @Override
    public String getTypeName() {
        return this.typedValueField.getTypeName();
    }
    
    @Override
    public String getVariableTypeName() {
        return this.getTypeName();
    }
    
    @Override
    public String getVariableName() {
        return this.name;
    }
    
    @Override
    public int getRevision() {
        return this.revision;
    }
    
    @Override
    public void setRevision(final int revision) {
        this.revision = revision;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public Long getLongValue() {
        return this.longValue;
    }
    
    @Override
    public void setLongValue(final Long longValue) {
        this.longValue = longValue;
    }
    
    @Override
    public Double getDoubleValue() {
        return this.doubleValue;
    }
    
    @Override
    public void setDoubleValue(final Double doubleValue) {
        this.doubleValue = doubleValue;
    }
    
    @Override
    public String getTextValue() {
        return this.textValue;
    }
    
    @Override
    public void setTextValue(final String textValue) {
        this.textValue = textValue;
    }
    
    @Override
    public String getTextValue2() {
        return this.textValue2;
    }
    
    @Override
    public void setTextValue2(final String textValue2) {
        this.textValue2 = textValue2;
    }
    
    public void setByteArrayValue(final ByteArrayEntity byteArrayValue) {
        this.byteArrayField.setByteArrayValue(byteArrayValue);
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public void setId(final String id) {
        this.id = id;
    }
    
    @Override
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public void setProcessDefinitionKey(final String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }
    
    @Override
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public void setProcessDefinitionId(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    @Override
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public void setProcessInstanceId(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    @Override
    public String getTaskId() {
        return this.taskId;
    }
    
    public void setTaskId(final String taskId) {
        this.taskId = taskId;
    }
    
    @Override
    public String getExecutionId() {
        return this.executionId;
    }
    
    public void setExecutionId(final String executionId) {
        this.executionId = executionId;
    }
    
    @Deprecated
    @Override
    public String getActivtyInstanceId() {
        return this.activityInstanceId;
    }
    
    @Override
    public String getActivityInstanceId() {
        return this.activityInstanceId;
    }
    
    public void setActivityInstanceId(final String activityInstanceId) {
        this.activityInstanceId = activityInstanceId;
    }
    
    @Override
    public String getCaseDefinitionKey() {
        return this.caseDefinitionKey;
    }
    
    public void setCaseDefinitionKey(final String caseDefinitionKey) {
        this.caseDefinitionKey = caseDefinitionKey;
    }
    
    @Override
    public String getCaseDefinitionId() {
        return this.caseDefinitionId;
    }
    
    public void setCaseDefinitionId(final String caseDefinitionId) {
        this.caseDefinitionId = caseDefinitionId;
    }
    
    @Override
    public String getCaseInstanceId() {
        return this.caseInstanceId;
    }
    
    public void setCaseInstanceId(final String caseInstanceId) {
        this.caseInstanceId = caseInstanceId;
    }
    
    @Override
    public String getCaseExecutionId() {
        return this.caseExecutionId;
    }
    
    public void setCaseExecutionId(final String caseExecutionId) {
        this.caseExecutionId = caseExecutionId;
    }
    
    @Override
    public String getErrorMessage() {
        return this.typedValueField.getErrorMessage();
    }
    
    @Override
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public String getState() {
        return this.state;
    }
    
    public void setState(final String state) {
        this.state = state;
    }
    
    @Override
    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(final Date createTime) {
        this.createTime = createTime;
    }
    
    @Override
    public String getRootProcessInstanceId() {
        return this.rootProcessInstanceId;
    }
    
    public void setRootProcessInstanceId(final String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }
    
    @Override
    public Date getRemovalTime() {
        return this.removalTime;
    }
    
    public void setRemovalTime(final Date removalTime) {
        this.removalTime = removalTime;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.id + ", processDefinitionKey=" + this.processDefinitionKey + ", processDefinitionId=" + this.processDefinitionId + ", rootProcessInstanceId=" + this.rootProcessInstanceId + ", removalTime=" + this.removalTime + ", processInstanceId=" + this.processInstanceId + ", taskId=" + this.taskId + ", executionId=" + this.executionId + ", tenantId=" + this.tenantId + ", activityInstanceId=" + this.activityInstanceId + ", caseDefinitionKey=" + this.caseDefinitionKey + ", caseDefinitionId=" + this.caseDefinitionId + ", caseInstanceId=" + this.caseInstanceId + ", caseExecutionId=" + this.caseExecutionId + ", name=" + this.name + ", createTime=" + this.createTime + ", revision=" + this.revision + ", serializerName=" + this.getSerializerName() + ", longValue=" + this.longValue + ", doubleValue=" + this.doubleValue + ", textValue=" + this.textValue + ", textValue2=" + this.textValue2 + ", state=" + this.state + ", byteArrayId=" + this.getByteArrayId() + "]";
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
