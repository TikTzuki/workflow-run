// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.delegate;

public interface DelegateListener<T extends BaseDelegateExecution>
{
    void notify(final T p0) throws Exception;
}
