// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.query.QueryProperty;
import java.util.ArrayList;
import org.camunda.bpm.engine.variable.type.ValueType;

public class VariableOrderProperty extends QueryOrderingProperty
{
    private static final long serialVersionUID = 1L;
    
    public VariableOrderProperty(final String name, final ValueType valueType) {
        super("variable", typeToQueryProperty(valueType));
        (this.relationConditions = new ArrayList<QueryEntityRelationCondition>()).add(new QueryEntityRelationCondition(VariableInstanceQueryProperty.VARIABLE_NAME, name));
        this.relationConditions.add(new QueryEntityRelationCondition(VariableInstanceQueryProperty.VARIABLE_TYPE, valueType.getName()));
    }
    
    public VariableOrderProperty() {
    }
    
    public static VariableOrderProperty forProcessInstanceVariable(final String variableName, final ValueType valueType) {
        final VariableOrderProperty orderingProperty = new VariableOrderProperty(variableName, valueType);
        orderingProperty.relationConditions.add(new QueryEntityRelationCondition(VariableInstanceQueryProperty.EXECUTION_ID, TaskQueryProperty.PROCESS_INSTANCE_ID));
        return orderingProperty;
    }
    
    public static VariableOrderProperty forExecutionVariable(final String variableName, final ValueType valueType) {
        final VariableOrderProperty orderingProperty = new VariableOrderProperty(variableName, valueType);
        orderingProperty.relationConditions.add(new QueryEntityRelationCondition(VariableInstanceQueryProperty.EXECUTION_ID, TaskQueryProperty.EXECUTION_ID));
        return orderingProperty;
    }
    
    public static VariableOrderProperty forTaskVariable(final String variableName, final ValueType valueType) {
        final VariableOrderProperty orderingProperty = new VariableOrderProperty(variableName, valueType);
        orderingProperty.relationConditions.add(new QueryEntityRelationCondition(VariableInstanceQueryProperty.TASK_ID, TaskQueryProperty.TASK_ID));
        return orderingProperty;
    }
    
    public static VariableOrderProperty forCaseInstanceVariable(final String variableName, final ValueType valueType) {
        final VariableOrderProperty orderingProperty = new VariableOrderProperty(variableName, valueType);
        orderingProperty.relationConditions.add(new QueryEntityRelationCondition(VariableInstanceQueryProperty.CASE_EXECUTION_ID, TaskQueryProperty.CASE_INSTANCE_ID));
        return orderingProperty;
    }
    
    public static VariableOrderProperty forCaseExecutionVariable(final String variableName, final ValueType valueType) {
        final VariableOrderProperty orderingProperty = new VariableOrderProperty(variableName, valueType);
        orderingProperty.relationConditions.add(new QueryEntityRelationCondition(VariableInstanceQueryProperty.CASE_EXECUTION_ID, TaskQueryProperty.CASE_EXECUTION_ID));
        return orderingProperty;
    }
    
    public static QueryProperty typeToQueryProperty(final ValueType type) {
        if (ValueType.STRING.equals(type)) {
            return VariableInstanceQueryProperty.TEXT_AS_LOWER;
        }
        if (ValueType.INTEGER.equals(type)) {
            return VariableInstanceQueryProperty.LONG;
        }
        if (ValueType.SHORT.equals(type)) {
            return VariableInstanceQueryProperty.LONG;
        }
        if (ValueType.DATE.equals(type)) {
            return VariableInstanceQueryProperty.LONG;
        }
        if (ValueType.BOOLEAN.equals(type)) {
            return VariableInstanceQueryProperty.LONG;
        }
        if (ValueType.LONG.equals(type)) {
            return VariableInstanceQueryProperty.LONG;
        }
        if (ValueType.DOUBLE.equals(type)) {
            return VariableInstanceQueryProperty.DOUBLE;
        }
        throw new ProcessEngineException("Cannot order by variables of type " + type.getName());
    }
}
