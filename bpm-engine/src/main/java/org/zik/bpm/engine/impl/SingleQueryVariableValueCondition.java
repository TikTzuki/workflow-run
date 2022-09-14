// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.Collections;
import java.util.List;
import org.zik.bpm.engine.impl.variable.serializer.jpa.JPAVariableSerializer;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.variable.serializer.TypedValueSerializer;
import java.util.Arrays;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.impl.value.UntypedValueImpl;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.impl.variable.serializer.VariableSerializers;
import org.zik.bpm.engine.impl.variable.serializer.ValueFields;

public class SingleQueryVariableValueCondition extends AbstractQueryVariableValueCondition implements ValueFields
{
    protected String textValue;
    protected String textValue2;
    protected Long longValue;
    protected Double doubleValue;
    protected String type;
    protected boolean findNulledEmptyStrings;
    
    public SingleQueryVariableValueCondition(final QueryVariableValue variableValue) {
        super(variableValue);
    }
    
    @Override
    public void initializeValue(final VariableSerializers serializers, final String dbType) {
        final TypedValue typedValue = this.wrappedQueryValue.getTypedValue();
        this.initializeValue(serializers, typedValue, dbType);
    }
    
    public void initializeValue(final VariableSerializers serializers, TypedValue typedValue, final String dbType) {
        final TypedValueSerializer serializer = this.determineSerializer(serializers, typedValue);
        if (typedValue instanceof UntypedValueImpl) {
            typedValue = serializer.convertToTypedValue((UntypedValueImpl)typedValue);
        }
        serializer.writeValue(typedValue, this);
        this.type = serializer.getName();
        if (ValueType.STRING.getName().equals(this.type) && "oracle".equals(dbType) && "".equals(this.textValue) && Arrays.asList(QueryOperator.EQUALS, QueryOperator.NOT_EQUALS).contains(this.wrappedQueryValue.getOperator())) {
            this.findNulledEmptyStrings = true;
        }
    }
    
    protected TypedValueSerializer determineSerializer(final VariableSerializers serializers, final TypedValue value) {
        final TypedValueSerializer serializer = serializers.findSerializerForValue(value);
        if (serializer.getType() == ValueType.BYTES) {
            throw new ProcessEngineException("Variables of type ByteArray cannot be used to query");
        }
        if (serializer.getType() == ValueType.FILE) {
            throw new ProcessEngineException("Variables of type File cannot be used to query");
        }
        if (serializer instanceof JPAVariableSerializer) {
            if (this.wrappedQueryValue.getOperator() != QueryOperator.EQUALS) {
                throw new ProcessEngineException("JPA entity variables can only be used in 'variableValueEquals'");
            }
        }
        else if (!serializer.getType().isPrimitiveValueType()) {
            throw new ProcessEngineException("Object values cannot be used to query");
        }
        return serializer;
    }
    
    @Override
    public List<SingleQueryVariableValueCondition> getDisjunctiveConditions() {
        return Collections.singletonList(this);
    }
    
    @Override
    public String getName() {
        return this.wrappedQueryValue.getName();
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
    
    @Override
    public byte[] getByteArrayValue() {
        return null;
    }
    
    @Override
    public void setByteArrayValue(final byte[] bytes) {
    }
    
    public String getType() {
        return this.type;
    }
    
    public boolean getFindNulledEmptyStrings() {
        return this.findNulledEmptyStrings;
    }
    
    public void setFindNulledEmptyStrings(final boolean findNulledEmptyStrings) {
        this.findNulledEmptyStrings = findNulledEmptyStrings;
    }
}
