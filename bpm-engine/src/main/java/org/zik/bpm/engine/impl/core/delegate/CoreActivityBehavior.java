// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.delegate;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;

public interface CoreActivityBehavior<T extends BaseDelegateExecution>
{
    void execute(final T p0) throws Exception;
}
