// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer;

import org.camunda.bpm.engine.variable.impl.value.UntypedValueImpl;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.impl.util.StringUtil;
import org.zik.bpm.engine.impl.digest._apacheCommonsCodec.Base64;
import org.zik.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.type.SerializableValueType;
import org.camunda.bpm.engine.variable.value.SerializableValue;

public abstract class AbstractSerializableValueSerializer<T extends SerializableValue> extends AbstractTypedValueSerializer<T>
{
    protected String serializationDataFormat;
    
    public AbstractSerializableValueSerializer(final SerializableValueType type, final String serializationDataFormat) {
        super((ValueType)type);
        this.serializationDataFormat = serializationDataFormat;
    }
    
    @Override
    public String getSerializationDataformat() {
        return this.serializationDataFormat;
    }
    
    @Override
    public void writeValue(final T value, final ValueFields valueFields) {
        String serializedStringValue = value.getValueSerialized();
        byte[] serializedByteValue = null;
        if (value.isDeserialized()) {
            final Object objectToSerialize = value.getValue();
            if (objectToSerialize != null) {
                try {
                    serializedByteValue = this.serializeToByteArray(objectToSerialize);
                    serializedStringValue = this.getSerializedStringValue(serializedByteValue);
                }
                catch (Exception e) {
                    throw new ProcessEngineException("Cannot serialize object in variable '" + valueFields.getName() + "': " + e.getMessage(), e);
                }
            }
        }
        else if (serializedStringValue != null) {
            serializedByteValue = this.getSerializedBytesValue(serializedStringValue);
        }
        this.writeToValueFields(value, valueFields, serializedByteValue);
        this.updateTypedValue(value, serializedStringValue);
    }
    
    @Override
    public T readValue(final ValueFields valueFields, final boolean deserializeObjectValue, final boolean asTransientValue) {
        final byte[] serializedByteValue = this.readSerializedValueFromFields(valueFields);
        final String serializedStringValue = this.getSerializedStringValue(serializedByteValue);
        if (deserializeObjectValue) {
            Object deserializedObject = null;
            if (serializedByteValue != null) {
                try {
                    deserializedObject = this.deserializeFromByteArray(serializedByteValue, valueFields);
                }
                catch (Exception e) {
                    throw new ProcessEngineException("Cannot deserialize object in variable '" + valueFields.getName() + "': " + e.getMessage(), e);
                }
            }
            final T value = this.createDeserializedValue(deserializedObject, serializedStringValue, valueFields, asTransientValue);
            return value;
        }
        return this.createSerializedValue(serializedStringValue, valueFields, asTransientValue);
    }
    
    protected abstract T createDeserializedValue(final Object p0, final String p1, final ValueFields p2, final boolean p3);
    
    protected abstract T createSerializedValue(final String p0, final ValueFields p1, final boolean p2);
    
    protected abstract void writeToValueFields(final T p0, final ValueFields p1, final byte[] p2);
    
    protected abstract void updateTypedValue(final T p0, final String p1);
    
    protected byte[] readSerializedValueFromFields(final ValueFields valueFields) {
        return valueFields.getByteArrayValue();
    }
    
    protected String getSerializedStringValue(byte[] serializedByteValue) {
        if (serializedByteValue != null) {
            if (!this.isSerializationTextBased()) {
                serializedByteValue = Base64.encodeBase64(serializedByteValue);
            }
            return StringUtil.fromBytes(serializedByteValue);
        }
        return null;
    }
    
    protected byte[] getSerializedBytesValue(final String serializedStringValue) {
        if (serializedStringValue != null) {
            byte[] serializedByteValue = StringUtil.toByteArray(serializedStringValue);
            if (!this.isSerializationTextBased()) {
                serializedByteValue = Base64.decodeBase64(serializedByteValue);
            }
            return serializedByteValue;
        }
        return null;
    }
    
    @Override
    protected boolean canWriteValue(final TypedValue typedValue) {
        if (!(typedValue instanceof SerializableValue) && !(typedValue instanceof UntypedValueImpl)) {
            return false;
        }
        if (!(typedValue instanceof SerializableValue)) {
            return typedValue.getValue() == null || this.canSerializeValue(typedValue.getValue());
        }
        final SerializableValue serializableValue = (SerializableValue)typedValue;
        final String requestedDataFormat = serializableValue.getSerializationDataFormat();
        if (!serializableValue.isDeserialized()) {
            return this.serializationDataFormat.equals(requestedDataFormat);
        }
        final boolean canSerialize = typedValue.getValue() == null || this.canSerializeValue(typedValue.getValue());
        return canSerialize && (requestedDataFormat == null || this.serializationDataFormat.equals(requestedDataFormat));
    }
    
    protected abstract boolean canSerializeValue(final Object p0);
    
    protected abstract byte[] serializeToByteArray(final Object p0) throws Exception;
    
    protected abstract Object deserializeFromByteArray(final byte[] p0, final ValueFields p1) throws Exception;
    
    protected abstract boolean isSerializationTextBased();
}
