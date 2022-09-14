// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer.jpa;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.variable.serializer.ValueFields;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.value.UntypedValueImpl;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.zik.bpm.engine.impl.variable.serializer.AbstractTypedValueSerializer;

public class JPAVariableSerializer extends AbstractTypedValueSerializer<ObjectValue>
{
    public static final String NAME = "jpa";
    private JPAEntityMappings mappings;
    
    public JPAVariableSerializer() {
        super((ValueType)ValueType.OBJECT);
        this.mappings = new JPAEntityMappings();
    }
    
    @Override
    public String getName() {
        return "jpa";
    }
    
    @Override
    protected boolean canWriteValue(final TypedValue value) {
        return (this.isDeserializedObjectValue(value) || value instanceof UntypedValueImpl) && (value.getValue() == null || this.mappings.isJPAEntity(value.getValue()));
    }
    
    protected boolean isDeserializedObjectValue(final TypedValue value) {
        return value instanceof ObjectValue && ((ObjectValue)value).isDeserialized();
    }
    
    @Override
    public ObjectValue convertToTypedValue(final UntypedValueImpl untypedValue) {
        return (ObjectValue)Variables.objectValue(untypedValue.getValue(), untypedValue.isTransient()).create();
    }
    
    @Override
    public void writeValue(final ObjectValue objectValue, final ValueFields valueFields) {
        final EntityManagerSession entityManagerSession = Context.getCommandContext().getSession(EntityManagerSession.class);
        if (entityManagerSession == null) {
            throw new ProcessEngineException("Cannot set JPA variable: " + EntityManagerSession.class + " not configured");
        }
        entityManagerSession.flush();
        final Object value = objectValue.getValue();
        if (value != null) {
            final String className = this.mappings.getJPAClassString(value);
            final String idString = this.mappings.getJPAIdString(value);
            valueFields.setTextValue(className);
            valueFields.setTextValue2(idString);
        }
        else {
            valueFields.setTextValue(null);
            valueFields.setTextValue2(null);
        }
    }
    
    @Override
    public ObjectValue readValue(final ValueFields valueFields, final boolean deserializeObjectValue, final boolean asTransientValue) {
        if (valueFields.getTextValue() != null && valueFields.getTextValue2() != null) {
            final Object jpaEntity = this.mappings.getJPAEntity(valueFields.getTextValue(), valueFields.getTextValue2());
            return (ObjectValue)Variables.objectValue(jpaEntity).setTransient(asTransientValue).create();
        }
        return (ObjectValue)Variables.objectValue((Object)null).setTransient(asTransientValue).create();
    }
}
