// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.interceptor;

public interface CommandContextListener
{
    void onCommandContextClose(final CommandContext p0);
    
    void onCommandFailed(final CommandContext p0, final Throwable p1);
}
