// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.variable;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.List;
import java.util.stream.Collector;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import java.util.Iterator;
import org.camunda.bpm.engine.variable.VariableMap;
import java.util.Map;
import org.zik.bpm.engine.impl.variable.serializer.TypedValueSerializer;
import org.zik.bpm.engine.impl.variable.serializer.VariableSerializerFactory;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.variable.Variables;
import org.zik.bpm.engine.impl.persistence.entity.util.TypedValueField;
import org.camunda.bpm.engine.variable.value.SerializableValue;
import org.zik.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.impl.core.CoreLogger;
import org.zik.bpm.engine.impl.cmd.CommandLogger;

public class VariableUtil
{
    public static final CommandLogger CMD_LOGGER;
    public static final CoreLogger CORE_LOGGER;
    public static final String ERROR_MSG = "Cannot set variable with name {0}. Java serialization format is prohibited";
    
    public static boolean isJavaSerializationProhibited(final TypedValue value) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        if (value instanceof SerializableValue && !processEngineConfiguration.isJavaSerializationFormatEnabled()) {
            final SerializableValue serializableValue = (SerializableValue)value;
            if (!serializableValue.isDeserialized()) {
                String requestedDataFormat = serializableValue.getSerializationDataFormat();
                if (requestedDataFormat == null) {
                    final VariableSerializerFactory fallbackSerializerFactory = processEngineConfiguration.getFallbackSerializerFactory();
                    final TypedValueSerializer serializerForValue = TypedValueField.getSerializers().findSerializerForValue((TypedValue)serializableValue, fallbackSerializerFactory);
                    if (serializerForValue != null) {
                        requestedDataFormat = serializerForValue.getSerializationDataformat();
                    }
                }
                return Variables.SerializationDataFormats.JAVA.getName().equals(requestedDataFormat);
            }
        }
        return false;
    }
    
    public static void checkJavaSerialization(final String variableName, final TypedValue value) {
        if (isJavaSerializationProhibited(value)) {
            throw VariableUtil.CORE_LOGGER.javaSerializationProhibitedException(variableName);
        }
    }
    
    public static void setVariables(final Map<String, ?> variables, final SetVariableFunction setVariableFunction) {
        if (variables != null) {
            for (final String variableName : variables.keySet()) {
                Object value = null;
                if (variables instanceof VariableMap) {
                    value = ((VariableMap)variables).getValueTyped(variableName);
                }
                else {
                    value = variables.get(variableName);
                }
                setVariableFunction.apply(variableName, value);
            }
        }
    }
    
    public static void setVariablesByBatchId(final Map<String, ?> variables, final String batchId) {
        setVariables(variables, (name, value) -> setVariableByBatchId(batchId, name, value));
    }
    
    public static void setVariableByBatchId(final String batchId, final String variableName, final Object variableValue) {
        final TypedValue variableTypedValue = Variables.untypedValue(variableValue);
        final boolean isTransient = variableTypedValue.isTransient();
        if (isTransient) {
            throw VariableUtil.CMD_LOGGER.exceptionSettingTransientVariablesAsyncNotSupported(variableName);
        }
        checkJavaSerialization(variableName, variableTypedValue);
        final VariableInstanceEntity variableInstance = VariableInstanceEntity.createAndInsert(variableName, variableTypedValue);
        variableInstance.setVariableScopeId(batchId);
        variableInstance.setBatchId(batchId);
    }
    
    public static Map<String, ?> findBatchVariablesSerialized(final String batchId, final CommandContext commandContext) {
        final List<VariableInstanceEntity> variableInstances = commandContext.getVariableInstanceManager().findVariableInstancesByBatchId(batchId);
        return variableInstances.stream().collect((Collector<? super Object, ?, Map<String, TypedValue>>)variablesCollector());
    }
    
    protected static Collector<VariableInstanceEntity, ?, Map<String, TypedValue>> variablesCollector() {
        return Collectors.toMap((Function<? super VariableInstanceEntity, ? extends String>)VariableInstanceEntity::getName, (Function<? super VariableInstanceEntity, ? extends TypedValue>)VariableUtil::getSerializedValue);
    }
    
    protected static TypedValue getSerializedValue(final VariableInstanceEntity variableInstanceEntity) {
        return variableInstanceEntity.getTypedValue(false);
    }
    
    static {
        CMD_LOGGER = ProcessEngineLogger.CMD_LOGGER;
        CORE_LOGGER = ProcessEngineLogger.CORE_LOGGER;
    }
    
    @FunctionalInterface
    public interface SetVariableFunction
    {
        void apply(final String p0, final Object p1);
    }
}
