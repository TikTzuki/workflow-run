// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;

public interface HostnameProvider
{
    String getHostname(final ProcessEngineConfigurationImpl p0);
}
