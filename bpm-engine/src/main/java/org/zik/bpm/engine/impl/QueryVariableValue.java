// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.Objects;
import java.util.List;
import org.zik.bpm.engine.impl.variable.serializer.VariableSerializers;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.TypedValue;
import java.io.Serializable;

public class QueryVariableValue implements Serializable
{
    protected static final long serialVersionUID = 1L;
    protected String name;
    protected TypedValue value;
    protected QueryOperator operator;
    protected boolean local;
    protected AbstractQueryVariableValueCondition valueCondition;
    protected boolean variableNameIgnoreCase;
    protected boolean variableValueIgnoreCase;
    
    public QueryVariableValue(final String name, final Object value, final QueryOperator operator, final boolean local) {
        this(name, value, operator, local, false, false);
    }
    
    public QueryVariableValue(final String name, final Object value, final QueryOperator operator, final boolean local, final boolean variableNameIgnoreCase, final boolean variableValueIgnoreCase) {
        this.name = name;
        this.value = Variables.untypedValue(value);
        this.operator = operator;
        this.local = local;
        this.variableNameIgnoreCase = variableNameIgnoreCase;
        this.variableValueIgnoreCase = variableValueIgnoreCase;
    }
    
    public void initialize(final VariableSerializers serializers, final String dbType) {
        if (this.value.getType() != null && this.value.getType().isAbstract()) {
            this.valueCondition = new CompositeQueryVariableValueCondition(this);
        }
        else {
            this.valueCondition = new SingleQueryVariableValueCondition(this);
        }
        this.valueCondition.initializeValue(serializers, dbType);
    }
    
    public List<SingleQueryVariableValueCondition> getValueConditions() {
        return this.valueCondition.getDisjunctiveConditions();
    }
    
    public String getName() {
        return this.name;
    }
    
    public QueryOperator getOperator() {
        if (this.operator != null) {
            return this.operator;
        }
        return QueryOperator.EQUALS;
    }
    
    public String getOperatorName() {
        return this.getOperator().toString();
    }
    
    public Object getValue() {
        return this.value.getValue();
    }
    
    public TypedValue getTypedValue() {
        return this.value;
    }
    
    public boolean isLocal() {
        return this.local;
    }
    
    public boolean isVariableNameIgnoreCase() {
        return this.variableNameIgnoreCase;
    }
    
    public void setVariableNameIgnoreCase(final boolean variableNameIgnoreCase) {
        this.variableNameIgnoreCase = variableNameIgnoreCase;
    }
    
    public boolean isVariableValueIgnoreCase() {
        return this.variableValueIgnoreCase;
    }
    
    public void setVariableValueIgnoreCase(final boolean variableValueIgnoreCase) {
        this.variableValueIgnoreCase = variableValueIgnoreCase;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final QueryVariableValue that = (QueryVariableValue)o;
        return this.local == that.local && this.variableNameIgnoreCase == that.variableNameIgnoreCase && this.variableValueIgnoreCase == that.variableValueIgnoreCase && this.name.equals(that.name) && this.value.equals(that.value) && this.operator == that.operator && Objects.equals(this.valueCondition, that.valueCondition);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.value, this.operator, this.local, this.valueCondition, this.variableNameIgnoreCase, this.variableValueIgnoreCase);
    }
}
