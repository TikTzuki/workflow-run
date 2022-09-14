// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer;

import java.util.Iterator;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.variable.value.TypedValue;
import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.io.Serializable;

public class DefaultVariableSerializers implements Serializable, VariableSerializers
{
    private static final long serialVersionUID = 1L;
    protected List<TypedValueSerializer<?>> serializerList;
    protected Map<String, TypedValueSerializer<?>> serializerMap;
    
    public DefaultVariableSerializers() {
        this.serializerList = new ArrayList<TypedValueSerializer<?>>();
        this.serializerMap = new HashMap<String, TypedValueSerializer<?>>();
    }
    
    public DefaultVariableSerializers(final DefaultVariableSerializers serializers) {
        this.serializerList = new ArrayList<TypedValueSerializer<?>>();
        this.serializerMap = new HashMap<String, TypedValueSerializer<?>>();
        this.serializerList.addAll(serializers.serializerList);
        this.serializerMap.putAll(serializers.serializerMap);
    }
    
    @Override
    public TypedValueSerializer<?> getSerializerByName(final String serializerName) {
        return this.serializerMap.get(serializerName);
    }
    
    @Override
    public TypedValueSerializer<?> findSerializerForValue(final TypedValue value, final VariableSerializerFactory fallBackSerializerFactory) {
        final String defaultSerializationFormat = Context.getProcessEngineConfiguration().getDefaultSerializationFormat();
        final List<TypedValueSerializer<?>> matchedSerializers = new ArrayList<TypedValueSerializer<?>>();
        final ValueType type = value.getType();
        if (type != null && type.isAbstract()) {
            throw new ProcessEngineException("Cannot serialize value of abstract type " + type.getName());
        }
        for (final TypedValueSerializer<?> serializer : this.serializerList) {
            if ((type == null || serializer.getType().equals(type)) && serializer.canHandle(value)) {
                matchedSerializers.add(serializer);
                if (serializer.getType().isPrimitiveValueType()) {
                    break;
                }
                continue;
            }
        }
        if (matchedSerializers.size() == 0) {
            if (fallBackSerializerFactory != null) {
                final TypedValueSerializer<?> serializer2 = fallBackSerializerFactory.getSerializer(value);
                if (serializer2 != null) {
                    return serializer2;
                }
            }
            throw new ProcessEngineException("Cannot find serializer for value '" + value + "'.");
        }
        if (matchedSerializers.size() == 1) {
            return matchedSerializers.get(0);
        }
        if (defaultSerializationFormat != null) {
            for (final TypedValueSerializer<?> typedValueSerializer : matchedSerializers) {
                if (defaultSerializationFormat.equals(typedValueSerializer.getSerializationDataformat())) {
                    return typedValueSerializer;
                }
            }
        }
        return matchedSerializers.get(0);
    }
    
    @Override
    public TypedValueSerializer<?> findSerializerForValue(final TypedValue value) {
        return this.findSerializerForValue(value, null);
    }
    
    @Override
    public DefaultVariableSerializers addSerializer(final TypedValueSerializer<?> serializer) {
        return this.addSerializer(serializer, this.serializerList.size());
    }
    
    @Override
    public DefaultVariableSerializers addSerializer(final TypedValueSerializer<?> serializer, final int index) {
        this.serializerList.add(index, serializer);
        this.serializerMap.put(serializer.getName(), serializer);
        return this;
    }
    
    public void setSerializerList(final List<TypedValueSerializer<?>> serializerList) {
        this.serializerList.clear();
        this.serializerList.addAll(serializerList);
        this.serializerMap.clear();
        for (final TypedValueSerializer<?> serializer : serializerList) {
            this.serializerMap.put(serializer.getName(), serializer);
        }
    }
    
    @Override
    public int getSerializerIndex(final TypedValueSerializer<?> serializer) {
        return this.serializerList.indexOf(serializer);
    }
    
    @Override
    public int getSerializerIndexByName(final String serializerName) {
        final TypedValueSerializer<?> serializer = this.serializerMap.get(serializerName);
        if (serializer != null) {
            return this.getSerializerIndex(serializer);
        }
        return -1;
    }
    
    @Override
    public VariableSerializers removeSerializer(final TypedValueSerializer<?> serializer) {
        this.serializerList.remove(serializer);
        this.serializerMap.remove(serializer.getName());
        return this;
    }
    
    @Override
    public VariableSerializers join(final VariableSerializers other) {
        final DefaultVariableSerializers copy = new DefaultVariableSerializers();
        for (final TypedValueSerializer<?> thisSerializer : this.serializerList) {
            TypedValueSerializer<?> serializer = other.getSerializerByName(thisSerializer.getName());
            if (serializer == null) {
                serializer = thisSerializer;
            }
            copy.addSerializer(serializer);
        }
        for (final TypedValueSerializer<?> otherSerializer : other.getSerializers()) {
            if (!copy.serializerMap.containsKey(otherSerializer.getName())) {
                copy.addSerializer(otherSerializer);
            }
        }
        return copy;
    }
    
    @Override
    public List<TypedValueSerializer<?>> getSerializers() {
        return new ArrayList<TypedValueSerializer<?>>(this.serializerList);
    }
}
