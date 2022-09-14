// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.jmx.services;

import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.engine.ProcessEngineConfiguration;

public class JmxManagedProcessEngineController extends JmxManagedProcessEngine implements JmxManagedProcessEngineMBean
{
    protected ProcessEngineConfiguration processEngineConfiguration;
    
    public JmxManagedProcessEngineController(final ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
    
    @Override
    public void start(final PlatformServiceContainer contanier) {
        this.processEngine = this.processEngineConfiguration.buildProcessEngine();
    }
    
    @Override
    public void stop(final PlatformServiceContainer container) {
        this.processEngine.close();
    }
}
