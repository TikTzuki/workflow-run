// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.camunda.bpm.engine.variable.VariableMap;

public class ExecutionVariableSnapshotObserver implements ExecutionObserver
{
    protected VariableMap variableSnapshot;
    protected ExecutionEntity execution;
    protected boolean localVariables;
    protected boolean deserializeValues;
    
    public ExecutionVariableSnapshotObserver(final ExecutionEntity executionEntity) {
        this(executionEntity, true, false);
    }
    
    public ExecutionVariableSnapshotObserver(final ExecutionEntity executionEntity, final boolean localVariables, final boolean deserializeValues) {
        this.localVariables = true;
        this.deserializeValues = false;
        (this.execution = executionEntity).addExecutionObserver(this);
        this.localVariables = localVariables;
        this.deserializeValues = deserializeValues;
    }
    
    @Override
    public void onClear(final ExecutionEntity execution) {
        if (this.variableSnapshot == null) {
            this.variableSnapshot = this.getVariables(this.localVariables);
        }
    }
    
    public VariableMap getVariables() {
        if (this.variableSnapshot == null) {
            return this.getVariables(this.localVariables);
        }
        return this.variableSnapshot;
    }
    
    private VariableMap getVariables(final boolean localVariables) {
        return (VariableMap)(this.localVariables ? this.execution.getVariablesLocalTyped(this.deserializeValues) : this.execution.getVariablesTyped(this.deserializeValues));
    }
}
