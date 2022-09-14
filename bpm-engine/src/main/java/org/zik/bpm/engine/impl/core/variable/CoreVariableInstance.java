// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.variable;

import org.camunda.bpm.engine.variable.value.TypedValue;

public interface CoreVariableInstance {
    String getName();

    TypedValue getTypedValue(final boolean p0);

    void setValue(final TypedValue p0);
}
