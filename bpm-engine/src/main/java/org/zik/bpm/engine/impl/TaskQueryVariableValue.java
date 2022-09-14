// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

public class TaskQueryVariableValue extends QueryVariableValue
{
    private static final long serialVersionUID = 1L;
    protected boolean isProcessInstanceVariable;
    
    public TaskQueryVariableValue(final String name, final Object value, final QueryOperator operator, final boolean isTaskVariable, final boolean isProcessInstanceVariable) {
        this(name, value, operator, isTaskVariable, isProcessInstanceVariable, false, false);
    }
    
    public TaskQueryVariableValue(final String name, final Object value, final QueryOperator operator, final boolean isTaskVariable, final boolean isProcessInstanceVariable, final boolean variableNameIgnoreCase, final boolean variableValueIgnoreCase) {
        super(name, value, operator, isTaskVariable, variableNameIgnoreCase, variableValueIgnoreCase);
        this.isProcessInstanceVariable = isProcessInstanceVariable;
    }
    
    public boolean isProcessInstanceVariable() {
        return this.isProcessInstanceVariable;
    }
}
