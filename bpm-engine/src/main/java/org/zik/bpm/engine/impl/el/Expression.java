// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.el;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.VariableScope;

public interface Expression extends org.zik.bpm.engine.delegate.Expression {
    Object getValue(final VariableScope p0, final BaseDelegateExecution p1);

    void setValue(final Object p0, final VariableScope p1, final BaseDelegateExecution p2);
}
