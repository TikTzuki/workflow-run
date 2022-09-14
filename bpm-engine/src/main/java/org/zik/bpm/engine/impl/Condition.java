// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.delegate.DelegateExecution;

public interface Condition
{
    boolean evaluate(final DelegateExecution p0);
    
    boolean evaluate(final VariableScope p0, final DelegateExecution p1);
    
    boolean tryEvaluate(final VariableScope p0, final DelegateExecution p1);
}
