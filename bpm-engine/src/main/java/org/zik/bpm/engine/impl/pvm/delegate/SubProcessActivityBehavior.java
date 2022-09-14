// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.delegate;

import org.zik.bpm.engine.delegate.VariableScope;

public interface SubProcessActivityBehavior extends ActivityBehavior
{
    void passOutputVariables(final ActivityExecution p0, final VariableScope p1);
    
    void completed(final ActivityExecution p0) throws Exception;
}
