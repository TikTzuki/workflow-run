// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg;

import org.zik.bpm.engine.ProcessEngine;

public class AbstractProcessEnginePlugin implements ProcessEnginePlugin
{
    @Override
    public void preInit(final ProcessEngineConfigurationImpl processEngineConfiguration) {
    }
    
    @Override
    public void postInit(final ProcessEngineConfigurationImpl processEngineConfiguration) {
    }
    
    @Override
    public void postProcessEngineBuild(final ProcessEngine processEngine) {
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
