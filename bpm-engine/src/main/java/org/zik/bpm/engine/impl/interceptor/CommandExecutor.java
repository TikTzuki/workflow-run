// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.interceptor;

public interface CommandExecutor
{
     <T> T execute(final Command<T> p0);
}
