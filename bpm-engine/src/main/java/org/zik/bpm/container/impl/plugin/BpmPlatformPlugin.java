// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.plugin;

import org.zik.bpm.application.ProcessApplicationInterface;

public interface BpmPlatformPlugin
{
    void postProcessApplicationDeploy(final ProcessApplicationInterface p0);
    
    void postProcessApplicationUndeploy(final ProcessApplicationInterface p0);
}
