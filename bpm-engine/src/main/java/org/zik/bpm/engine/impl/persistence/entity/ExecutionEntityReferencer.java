// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;
import org.zik.bpm.engine.impl.core.variable.scope.VariableStore;

public class ExecutionEntityReferencer implements VariableStore.VariableStoreObserver<VariableInstanceEntity>
{
    protected ExecutionEntity execution;
    
    public ExecutionEntityReferencer(final ExecutionEntity execution) {
        this.execution = execution;
    }
    
    @Override
    public void onAdd(final VariableInstanceEntity variable) {
        variable.setExecution(this.execution);
    }
    
    @Override
    public void onRemove(final VariableInstanceEntity variable) {
        variable.setExecution(null);
    }
}
