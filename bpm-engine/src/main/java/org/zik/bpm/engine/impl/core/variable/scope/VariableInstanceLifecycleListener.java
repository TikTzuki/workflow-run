// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.variable.scope;

import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;

public interface VariableInstanceLifecycleListener<T extends CoreVariableInstance>
{
    void onCreate(final T p0, final AbstractVariableScope p1);
    
    void onDelete(final T p0, final AbstractVariableScope p1);
    
    void onUpdate(final T p0, final AbstractVariableScope p1);
}
