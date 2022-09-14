// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg;

import org.zik.bpm.engine.ProcessEngine;

public interface ProcessEnginePlugin
{
    void preInit(final ProcessEngineConfigurationImpl p0);
    
    void postInit(final ProcessEngineConfigurationImpl p0);
    
    void postProcessEngineBuild(final ProcessEngine p0);
}
