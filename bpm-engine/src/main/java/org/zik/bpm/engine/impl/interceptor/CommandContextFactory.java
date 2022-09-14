// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.interceptor;

import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;

public class CommandContextFactory
{
    protected ProcessEngineConfigurationImpl processEngineConfiguration;
    
    public CommandContext createCommandContext() {
        return new CommandContext(this.processEngineConfiguration);
    }
    
    public ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
        return this.processEngineConfiguration;
    }
    
    public void setProcessEngineConfiguration(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
}
