// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;
import org.zik.bpm.engine.impl.core.variable.scope.VariableStore;

public class TaskEntityReferencer implements VariableStore.VariableStoreObserver<VariableInstanceEntity>
{
    protected TaskEntity task;
    
    public TaskEntityReferencer(final TaskEntity task) {
        this.task = task;
    }
    
    @Override
    public void onAdd(final VariableInstanceEntity variable) {
        variable.setTask(this.task);
    }
    
    @Override
    public void onRemove(final VariableInstanceEntity variable) {
        variable.setTask(null);
    }
}
