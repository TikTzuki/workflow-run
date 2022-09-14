// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.test;

import org.zik.bpm.engine.ProcessEngineConfiguration;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;

public abstract class ResourceProcessEngineTestCase extends AbstractProcessEngineTestCase
{
    protected String engineConfigurationResource;
    
    public ResourceProcessEngineTestCase(final String configurationResource) {
        this.engineConfigurationResource = configurationResource;
    }
    
    @Override
    protected void closeDownProcessEngine() {
        super.closeDownProcessEngine();
        this.processEngine.close();
        this.processEngine = null;
    }
    
    @Override
    protected void initializeProcessEngine() {
        final ProcessEngineConfigurationImpl processEngineConfig = (ProcessEngineConfigurationImpl)ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(this.engineConfigurationResource);
        this.processEngine = processEngineConfig.buildProcessEngine();
    }
}
