// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.delegate;

public interface CompositeActivityBehavior extends ActivityBehavior
{
    void concurrentChildExecutionEnded(final ActivityExecution p0, final ActivityExecution p1);
    
    void complete(final ActivityExecution p0);
}
