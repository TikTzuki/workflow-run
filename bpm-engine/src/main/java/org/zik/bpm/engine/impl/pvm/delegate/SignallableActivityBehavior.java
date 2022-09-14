// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.delegate;

public interface SignallableActivityBehavior extends ActivityBehavior
{
    void signal(final ActivityExecution p0, final String p1, final Object p2) throws Exception;
}
