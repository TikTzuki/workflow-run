// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;
import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.zik.bpm.engine.impl.core.variable.scope.VariableInstanceLifecycleListener;

public class VariableInstanceSequenceCounterListener implements VariableInstanceLifecycleListener<VariableInstanceEntity>
{
    public static final VariableInstanceSequenceCounterListener INSTANCE;
    
    @Override
    public void onCreate(final VariableInstanceEntity variableInstance, final AbstractVariableScope sourceScope) {
    }
    
    @Override
    public void onDelete(final VariableInstanceEntity variableInstance, final AbstractVariableScope sourceScope) {
        variableInstance.incrementSequenceCounter();
    }
    
    @Override
    public void onUpdate(final VariableInstanceEntity variableInstance, final AbstractVariableScope sourceScope) {
        variableInstance.incrementSequenceCounter();
    }
    
    static {
        INSTANCE = new VariableInstanceSequenceCounterListener();
    }
}
