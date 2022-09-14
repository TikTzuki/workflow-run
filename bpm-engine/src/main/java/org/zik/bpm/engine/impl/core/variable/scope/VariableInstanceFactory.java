// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.variable.scope;

import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;

public interface VariableInstanceFactory<T extends CoreVariableInstance>
{
    T build(final String p0, final TypedValue p1, final boolean p2);
}
