// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;
import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.zik.bpm.engine.impl.core.variable.scope.VariableInstanceLifecycleListener;

public class VariableInstanceConcurrentLocalInitializer implements VariableInstanceLifecycleListener<VariableInstanceEntity>
{
    protected ExecutionEntity execution;
    
    public VariableInstanceConcurrentLocalInitializer(final ExecutionEntity execution) {
        this.execution = execution;
    }
    
    @Override
    public void onCreate(final VariableInstanceEntity variableInstance, final AbstractVariableScope sourceScope) {
        variableInstance.setConcurrentLocal(!this.execution.isScope() || this.execution.isExecutingScopeLeafActivity());
    }
    
    @Override
    public void onDelete(final VariableInstanceEntity variableInstance, final AbstractVariableScope sourceScope) {
    }
    
    @Override
    public void onUpdate(final VariableInstanceEntity variableInstance, final AbstractVariableScope sourceScope) {
    }
}
