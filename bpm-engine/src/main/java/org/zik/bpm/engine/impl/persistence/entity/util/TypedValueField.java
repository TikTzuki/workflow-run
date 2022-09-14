// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity.util;

import org.camunda.bpm.engine.variable.impl.value.UntypedValueImpl;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.value.SerializableValue;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.application.AbstractProcessApplication;
import org.zik.bpm.application.ProcessApplicationInterface;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.application.ProcessApplicationUnavailableException;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.db.DbEntityLifecycleAware;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandContextListener;
import org.zik.bpm.engine.impl.variable.serializer.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TypedValueField implements DbEntityLifecycleAware, CommandContextListener {
    protected static final EnginePersistenceLogger LOG;
    protected String serializerName;
    protected TypedValueSerializer<TypedValue> serializer;
    protected TypedValue cachedValue;
    protected String errorMessage;
    protected final ValueFields valueFields;
    protected boolean notifyOnImplicitUpdates;
    protected List<TypedValueUpdateListener> updateListeners;

    public TypedValueField(final ValueFields valueFields, final boolean notifyOnImplicitUpdates) {
        this.notifyOnImplicitUpdates = false;
        this.valueFields = valueFields;
        this.notifyOnImplicitUpdates = notifyOnImplicitUpdates;
        this.updateListeners = new ArrayList<TypedValueUpdateListener>();
    }

    public Object getValue() {
        final TypedValue typedValue = this.getTypedValue(false);
        if (typedValue != null) {
            return typedValue.getValue();
        }
        return null;
    }

    public TypedValue getTypedValue(final boolean asTransientValue) {
        return this.getTypedValue(true, asTransientValue);
    }

    public TypedValue getTypedValue(final boolean deserializeValue, final boolean asTransientValue) {
        if (Context.getCommandContext() != null) {
            if (this.cachedValue != null && this.cachedValue instanceof SerializableValue) {
                final SerializableValue serializableValue = (SerializableValue) this.cachedValue;
                if (deserializeValue && !serializableValue.isDeserialized()) {
                    this.cachedValue = null;
                }
            }
            if (this.cachedValue != null && (asTransientValue ^ this.cachedValue.isTransient())) {
                this.cachedValue = null;
            }
        }
        if (this.cachedValue == null && this.errorMessage == null) {
            try {
                this.cachedValue = (TypedValue) this.getSerializer().readValue(this.valueFields, deserializeValue, asTransientValue);
                if (this.notifyOnImplicitUpdates && this.isMutableValue(this.cachedValue)) {
                    Context.getCommandContext().registerCommandContextListener(this);
                }
            } catch (RuntimeException e) {
                this.errorMessage = e.getMessage();
                throw e;
            }
        }
        return this.cachedValue;
    }

    public TypedValue setValue(TypedValue value) {
        this.serializer = (TypedValueSerializer<TypedValue>) getSerializers().findSerializerForValue(value, Context.getProcessEngineConfiguration().getFallbackSerializerFactory());
        this.serializerName = this.serializer.getName();
        if (value instanceof UntypedValueImpl) {
            value = (TypedValue) this.serializer.convertToTypedValue((UntypedValueImpl) value);
        }
        this.writeValue(value, this.valueFields);
        this.cachedValue = value;
        if (this.notifyOnImplicitUpdates && this.isMutableValue(this.cachedValue)) {
            Context.getCommandContext().registerCommandContextListener(this);
        }
        return value;
    }

    public boolean isMutable() {
        return this.isMutableValue(this.cachedValue);
    }

    protected boolean isMutableValue(final TypedValue value) {
        return this.getSerializer().isMutableValue(value);
    }

    protected boolean isValuedImplicitlyUpdated() {
        if (this.cachedValue != null && this.isMutableValue(this.cachedValue)) {
            final byte[] byteArray = this.valueFields.getByteArrayValue();
            final ValueFieldsImpl tempValueFields = new ValueFieldsImpl();
            this.writeValue(this.cachedValue, tempValueFields);
            final byte[] byteArrayAfter = tempValueFields.getByteArrayValue();
            return !Arrays.equals(byteArray, byteArrayAfter);
        }
        return false;
    }

    protected void writeValue(final TypedValue value, final ValueFields valueFields) {
        this.getSerializer().writeValue(value, valueFields);
    }

    @Override
    public void onCommandContextClose(final CommandContext commandContext) {
        this.notifyImplicitValueUpdate();
    }

    public void notifyImplicitValueUpdate() {
        if (this.isValuedImplicitlyUpdated()) {
            for (final TypedValueUpdateListener typedValueImplicitUpdateListener : this.updateListeners) {
                typedValueImplicitUpdateListener.onImplicitValueUpdate(this.cachedValue);
            }
        }
    }

    @Override
    public void onCommandFailed(final CommandContext commandContext, final Throwable t) {
    }

    public TypedValueSerializer<TypedValue> getSerializer() {
        this.ensureSerializerInitialized();
        return this.serializer;
    }

    protected void ensureSerializerInitialized() {
        if (this.serializerName != null && this.serializer == null) {
            this.serializer = (TypedValueSerializer<TypedValue>) getSerializers().getSerializerByName(this.serializerName);
            if (this.serializer == null) {
                this.serializer = (TypedValueSerializer<TypedValue>) getFallbackSerializer(this.serializerName);
            }
            if (this.serializer == null) {
                throw TypedValueField.LOG.serializerNotDefinedException(this);
            }
        }
    }

    public static VariableSerializers getSerializers() {
        if (Context.getCommandContext() == null) {
            throw TypedValueField.LOG.serializerOutOfContextException();
        }
        final VariableSerializers variableSerializers = Context.getProcessEngineConfiguration().getVariableSerializers();
        final VariableSerializers paSerializers = getCurrentPaSerializers();
        if (paSerializers != null) {
            return variableSerializers.join(paSerializers);
        }
        return variableSerializers;
    }

    public static TypedValueSerializer<?> getFallbackSerializer(final String serializerName) {
        if (Context.getProcessEngineConfiguration() == null) {
            throw TypedValueField.LOG.serializerOutOfContextException();
        }
        final VariableSerializerFactory fallbackSerializerFactory = Context.getProcessEngineConfiguration().getFallbackSerializerFactory();
        if (fallbackSerializerFactory != null) {
            return fallbackSerializerFactory.getSerializer(serializerName);
        }
        return null;
    }

    protected static VariableSerializers getCurrentPaSerializers() {
        if (Context.getCurrentProcessApplication() != null) {
            final ProcessApplicationReference processApplicationReference = Context.getCurrentProcessApplication();
            try {
                final ProcessApplicationInterface processApplicationInterface = processApplicationReference.getProcessApplication();
                final ProcessApplicationInterface rawPa = processApplicationInterface.getRawObject();
                if (rawPa instanceof AbstractProcessApplication) {
                    return ((AbstractProcessApplication) rawPa).getVariableSerializers();
                }
                return null;
            } catch (ProcessApplicationUnavailableException e) {
                throw TypedValueField.LOG.cannotDeterminePaDataformats(e);
            }
        }
        return null;
    }

    public String getSerializerName() {
        return this.serializerName;
    }

    public void setSerializerName(final String serializerName) {
        this.serializerName = serializerName;
    }

    public void addImplicitUpdateListener(final TypedValueUpdateListener listener) {
        this.updateListeners.add(listener);
    }

    public String getTypeName() {
        if (this.serializerName == null) {
            return ValueType.NULL.getName();
        }
        return this.getSerializer().getType().getName();
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    @Override
    public void postLoad() {
    }

    public void clear() {
        this.cachedValue = null;
    }

    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
