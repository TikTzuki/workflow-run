// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.interceptor;

public interface Command<T>
{
    T execute(final CommandContext p0);
    
    default boolean isRetryable() {
        return false;
    }
}
