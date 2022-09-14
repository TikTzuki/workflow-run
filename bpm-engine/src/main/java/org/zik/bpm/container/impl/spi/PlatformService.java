// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.spi;

public interface PlatformService<S>
{
    void start(final PlatformServiceContainer p0);
    
    void stop(final PlatformServiceContainer p0);
    
    S getValue();
}
