// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;
import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.zik.bpm.engine.impl.core.variable.scope.VariableInstanceLifecycleListener;

public class VariableInstanceEntityPersistenceListener implements VariableInstanceLifecycleListener<VariableInstanceEntity>
{
    public static final VariableInstanceEntityPersistenceListener INSTANCE;
    
    @Override
    public void onCreate(final VariableInstanceEntity variable, final AbstractVariableScope sourceScope) {
        VariableInstanceEntity.insert(variable);
    }
    
    @Override
    public void onDelete(final VariableInstanceEntity variable, final AbstractVariableScope sourceScope) {
        variable.delete();
    }
    
    @Override
    public void onUpdate(final VariableInstanceEntity variable, final AbstractVariableScope sourceScope) {
    }
    
    static {
        INSTANCE = new VariableInstanceEntityPersistenceListener();
    }
}
