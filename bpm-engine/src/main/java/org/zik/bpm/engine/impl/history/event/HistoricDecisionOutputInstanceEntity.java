// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.repository.ResourceType;
import org.zik.bpm.engine.impl.persistence.entity.Nameable;
import org.zik.bpm.engine.repository.ResourceTypes;
import java.util.Date;
import org.zik.bpm.engine.impl.persistence.entity.util.TypedValueField;
import org.zik.bpm.engine.impl.persistence.entity.util.ByteArrayField;
import org.zik.bpm.engine.impl.variable.serializer.ValueFields;
import org.zik.bpm.engine.history.HistoricDecisionOutputInstance;

public class HistoricDecisionOutputInstanceEntity extends HistoryEvent implements HistoricDecisionOutputInstance, ValueFields
{
    private static final long serialVersionUID = 1L;
    protected String decisionInstanceId;
    protected String clauseId;
    protected String clauseName;
    protected String ruleId;
    protected Integer ruleOrder;
    protected String variableName;
    protected Long longValue;
    protected Double doubleValue;
    protected String textValue;
    protected String textValue2;
    protected String tenantId;
    protected ByteArrayField byteArrayField;
    protected TypedValueField typedValueField;
    protected Date createTime;
    
    public HistoricDecisionOutputInstanceEntity() {
        this.typedValueField = new TypedValueField(this, false);
        this.byteArrayField = new ByteArrayField(this, ResourceTypes.HISTORY);
    }
    
    public HistoricDecisionOutputInstanceEntity(final String rootProcessInstanceId, final Date removalTime) {
        this.typedValueField = new TypedValueField(this, false);
        this.rootProcessInstanceId = rootProcessInstanceId;
        this.removalTime = removalTime;
        this.byteArrayField = new ByteArrayField(this, ResourceTypes.HISTORY, this.getRootProcessInstanceId(), this.getRemovalTime());
    }
    
    @Override
    public String getDecisionInstanceId() {
        return this.decisionInstanceId;
    }
    
    @Override
    public String getClauseId() {
        return this.clauseId;
    }
    
    @Override
    public String getClauseName() {
        return this.clauseName;
    }
    
    @Override
    public String getRuleId() {
        return this.ruleId;
    }
    
    @Override
    public Integer getRuleOrder() {
        return this.ruleOrder;
    }
    
    public void setDecisionInstanceId(final String decisionInstanceId) {
        this.decisionInstanceId = decisionInstanceId;
    }
    
    public void setClauseId(final String clauseId) {
        this.clauseId = clauseId;
    }
    
    public void setClauseName(final String clauseName) {
        this.clauseName = clauseName;
    }
    
    public void setRuleId(final String ruleId) {
        this.ruleId = ruleId;
    }
    
    public void setRuleOrder(final Integer ruleOrder) {
        this.ruleOrder = ruleOrder;
    }
    
    @Override
    public String getVariableName() {
        return this.variableName;
    }
    
    public void setVariableName(final String variableName) {
        this.variableName = variableName;
    }
    
    @Override
    public String getTypeName() {
        return this.typedValueField.getTypeName();
    }
    
    public void setTypeName(final String typeName) {
        this.typedValueField.setSerializerName(typeName);
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
    
    @Override
    public String getErrorMessage() {
        return this.typedValueField.getErrorMessage();
    }
    
    @Override
    public String getName() {
        return this.variableName;
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
    
    public String getByteArrayValueId() {
        return this.byteArrayField.getByteArrayId();
    }
    
    public void setByteArrayValueId(final String byteArrayId) {
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
    
    public void setValue(final TypedValue typedValue) {
        this.typedValueField.setValue(typedValue);
    }
    
    public String getSerializerName() {
        return this.typedValueField.getSerializerName();
    }
    
    public void setSerializerName(final String serializerName) {
        this.typedValueField.setSerializerName(serializerName);
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
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
    
    @Override
    public void setRootProcessInstanceId(final String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }
    
    public void delete() {
        this.byteArrayField.deleteByteArrayValue();
        Context.getCommandContext().getDbEntityManager().delete(this);
    }
}
