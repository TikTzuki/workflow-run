// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.variable.scope;

import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;
import org.zik.bpm.engine.runtime.VariableInstance;
import org.zik.bpm.engine.impl.core.variable.event.VariableEvent;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;

public class CmmnVariableInvocationListener implements VariableInstanceLifecycleListener<VariableInstanceEntity>
{
    public static final CmmnVariableInvocationListener INSTANCE;
    
    @Override
    public void onCreate(final VariableInstanceEntity variable, final AbstractVariableScope sourceScope) {
        sourceScope.dispatchEvent(new VariableEvent(variable, "create", sourceScope));
    }
    
    @Override
    public void onUpdate(final VariableInstanceEntity variable, final AbstractVariableScope sourceScope) {
        sourceScope.dispatchEvent(new VariableEvent(variable, "update", sourceScope));
    }
    
    @Override
    public void onDelete(final VariableInstanceEntity variable, final AbstractVariableScope sourceScope) {
        sourceScope.dispatchEvent(new VariableEvent(variable, "delete", sourceScope));
    }
    
    static {
        INSTANCE = new CmmnVariableInvocationListener();
    }
}
