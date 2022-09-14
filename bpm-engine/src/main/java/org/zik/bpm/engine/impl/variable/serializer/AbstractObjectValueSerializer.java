// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer;

import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.engine.variable.value.SerializableValue;
import org.camunda.bpm.engine.variable.impl.value.ObjectValueImpl;
import org.zik.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.value.UntypedValueImpl;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.value.ObjectValue;

public abstract class AbstractObjectValueSerializer extends AbstractSerializableValueSerializer<ObjectValue>
{
    public AbstractObjectValueSerializer(final String serializationDataFormat) {
        super(ValueType.OBJECT, serializationDataFormat);
    }
    
    @Override
    public ObjectValue convertToTypedValue(final UntypedValueImpl untypedValue) {
        return (ObjectValue)Variables.objectValue(untypedValue.getValue(), untypedValue.isTransient()).create();
    }
    
    @Override
    protected void writeToValueFields(final ObjectValue value, final ValueFields valueFields, final byte[] serializedValue) {
        final String objectTypeName = this.getObjectTypeName(value, valueFields);
        valueFields.setByteArrayValue(serializedValue);
        valueFields.setTextValue2(objectTypeName);
    }
    
    protected String getObjectTypeName(final ObjectValue value, final ValueFields valueFields) {
        String objectTypeName = value.getObjectTypeName();
        if (objectTypeName == null && !value.isDeserialized() && value.getValueSerialized() != null) {
            throw new ProcessEngineException("Cannot write serialized value for variable '" + valueFields.getName() + "': no 'objectTypeName' provided for non-null value.");
        }
        if (value.isDeserialized() && value.getValue() != null) {
            objectTypeName = this.getTypeNameForDeserialized(value.getValue());
        }
        return objectTypeName;
    }
    
    @Override
    protected void updateTypedValue(final ObjectValue value, final String serializedStringValue) {
        final String objectTypeName = this.getObjectTypeName(value, null);
        final ObjectValueImpl objectValue = (ObjectValueImpl)value;
        objectValue.setObjectTypeName(objectTypeName);
        objectValue.setSerializedValue(serializedStringValue);
        objectValue.setSerializationDataFormat(this.serializationDataFormat);
    }
    
    @Override
    protected ObjectValue createDeserializedValue(final Object deserializedObject, final String serializedStringValue, final ValueFields valueFields, final boolean asTransientValue) {
        final String objectTypeName = this.readObjectNameFromFields(valueFields);
        final ObjectValueImpl objectValue = new ObjectValueImpl(deserializedObject, serializedStringValue, this.serializationDataFormat, objectTypeName, true);
        objectValue.setTransient(asTransientValue);
        return (ObjectValue)objectValue;
    }
    
    @Override
    protected ObjectValue createSerializedValue(final String serializedStringValue, final ValueFields valueFields, final boolean asTransientValue) {
        final String objectTypeName = this.readObjectNameFromFields(valueFields);
        final ObjectValueImpl objectValue = new ObjectValueImpl((Object)null, serializedStringValue, this.serializationDataFormat, objectTypeName, false);
        objectValue.setTransient(asTransientValue);
        return (ObjectValue)objectValue;
    }
    
    protected String readObjectNameFromFields(final ValueFields valueFields) {
        return valueFields.getTextValue2();
    }
    
    @Override
    public boolean isMutableValue(final ObjectValue typedValue) {
        return typedValue.isDeserialized();
    }
    
    protected abstract String getTypeNameForDeserialized(final Object p0);
    
    @Override
    protected abstract byte[] serializeToByteArray(final Object p0) throws Exception;
    
    @Override
    protected Object deserializeFromByteArray(final byte[] object, final ValueFields valueFields) throws Exception {
        final String objectTypeName = this.readObjectNameFromFields(valueFields);
        return this.deserializeFromByteArray(object, objectTypeName);
    }
    
    protected abstract Object deserializeFromByteArray(final byte[] p0, final String p1) throws Exception;
    
    @Override
    protected abstract boolean isSerializationTextBased();
}
