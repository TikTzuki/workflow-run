// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;

public interface CoreAtomicOperation<T extends CoreExecution>
{
    void execute(final T p0);
    
    boolean isAsync(final T p0);
    
    String getCanonicalName();
}
