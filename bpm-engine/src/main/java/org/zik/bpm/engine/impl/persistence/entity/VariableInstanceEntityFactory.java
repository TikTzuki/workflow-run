// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.impl.core.variable.scope.VariableInstanceFactory;

public class VariableInstanceEntityFactory implements VariableInstanceFactory<VariableInstanceEntity>
{
    public static final VariableInstanceEntityFactory INSTANCE;
    
    @Override
    public VariableInstanceEntity build(final String name, final TypedValue value, final boolean isTransient) {
        return VariableInstanceEntity.create(name, value, isTransient);
    }
    
    static {
        INSTANCE = new VariableInstanceEntityFactory();
    }
}
