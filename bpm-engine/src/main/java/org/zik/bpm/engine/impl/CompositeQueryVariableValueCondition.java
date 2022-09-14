// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.Iterator;
import java.util.Collection;
import org.camunda.bpm.engine.variable.type.ValueTypeResolver;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.variable.serializer.VariableSerializers;
import java.util.ArrayList;
import java.util.List;

public class CompositeQueryVariableValueCondition extends AbstractQueryVariableValueCondition
{
    protected List<SingleQueryVariableValueCondition> aggregatedValues;
    
    public CompositeQueryVariableValueCondition(final QueryVariableValue variableValue) {
        super(variableValue);
        this.aggregatedValues = new ArrayList<SingleQueryVariableValueCondition>();
    }
    
    @Override
    public void initializeValue(final VariableSerializers serializers, final String dbType) {
        final TypedValue typedValue = this.wrappedQueryValue.getTypedValue();
        final ValueTypeResolver resolver = Context.getProcessEngineConfiguration().getValueTypeResolver();
        final Collection<ValueType> concreteTypes = (Collection<ValueType>)resolver.getSubTypes(typedValue.getType());
        for (final ValueType type : concreteTypes) {
            if (type.canConvertFromTypedValue(typedValue)) {
                final TypedValue convertedValue = type.convertFromTypedValue(typedValue);
                final SingleQueryVariableValueCondition aggregatedValue = new SingleQueryVariableValueCondition(this.wrappedQueryValue);
                aggregatedValue.initializeValue(serializers, convertedValue, dbType);
                this.aggregatedValues.add(aggregatedValue);
            }
        }
    }
    
    @Override
    public List<SingleQueryVariableValueCondition> getDisjunctiveConditions() {
        return this.aggregatedValues;
    }
}
