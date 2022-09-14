// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Date;
import org.zik.bpm.engine.impl.variable.serializer.TypedValueSerializer;
import org.zik.bpm.engine.impl.db.entitymanager.DbEntityManager;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.repository.ResourceType;
import org.zik.bpm.engine.repository.ResourceTypes;
import org.zik.bpm.engine.impl.persistence.entity.util.ByteArrayField;
import org.zik.bpm.engine.impl.persistence.entity.util.TypedValueField;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.db.DbEntityLifecycleAware;
import org.zik.bpm.engine.history.HistoricVariableUpdate;
import org.zik.bpm.engine.impl.variable.serializer.ValueFields;
import org.zik.bpm.engine.impl.history.event.HistoricVariableUpdateEventEntity;

public class HistoricDetailVariableInstanceUpdateEntity extends HistoricVariableUpdateEventEntity implements ValueFields, HistoricVariableUpdate, DbEntityLifecycleAware
{
    private static final long serialVersionUID = 1L;
    protected static final EnginePersistenceLogger LOG;
    protected TypedValueField typedValueField;
    protected ByteArrayField byteArrayField;
    
    public HistoricDetailVariableInstanceUpdateEntity() {
        this.typedValueField = new TypedValueField(this, false);
        this.byteArrayField = new ByteArrayField(this, ResourceTypes.HISTORY);
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
    public void delete() {
        final DbEntityManager dbEntityManger = Context.getCommandContext().getDbEntityManager();
        dbEntityManger.delete(this);
        this.byteArrayField.deleteByteArrayValue();
    }
    
    public TypedValueSerializer<?> getSerializer() {
        return this.typedValueField.getSerializer();
    }
    
    @Override
    public String getErrorMessage() {
        return this.typedValueField.getErrorMessage();
    }
    
    @Override
    public void setByteArrayId(final String id) {
        this.byteArrayField.setByteArrayId(id);
    }
    
    @Override
    public String getSerializerName() {
        return this.typedValueField.getSerializerName();
    }
    
    @Override
    public void setSerializerName(final String serializerName) {
        this.typedValueField.setSerializerName(serializerName);
    }
    
    public String getByteArrayValueId() {
        return this.byteArrayField.getByteArrayId();
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
    public String getName() {
        return this.getVariableName();
    }
    
    @Override
    public void postLoad() {
        this.typedValueField.postLoad();
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
    public Date getTime() {
        return this.timestamp;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[variableName=" + this.variableName + ", variableInstanceId=" + this.variableInstanceId + ", revision=" + this.revision + ", serializerName=" + this.serializerName + ", longValue=" + this.longValue + ", doubleValue=" + this.doubleValue + ", textValue=" + this.textValue + ", textValue2=" + this.textValue2 + ", byteArrayId=" + this.byteArrayId + ", activityInstanceId=" + this.activityInstanceId + ", eventType=" + this.eventType + ", executionId=" + this.executionId + ", id=" + this.id + ", processDefinitionId=" + this.processInstanceId + ", processInstanceId=" + this.processInstanceId + ", taskId=" + this.taskId + ", timestamp=" + this.timestamp + "]";
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
