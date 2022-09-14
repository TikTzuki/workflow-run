// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.test;

import java.io.FileNotFoundException;
import org.zik.bpm.engine.ProcessEngineConfiguration;
import org.zik.bpm.engine.ProcessEngine;

public class PluggableProcessEngineTestCase extends AbstractProcessEngineTestCase
{
    protected static ProcessEngine cachedProcessEngine;
    
    @Override
    protected void initializeProcessEngine() {
        this.processEngine = getOrInitializeCachedProcessEngine();
    }
    
    private static ProcessEngine getOrInitializeCachedProcessEngine() {
        if (PluggableProcessEngineTestCase.cachedProcessEngine == null) {
            try {
                PluggableProcessEngineTestCase.cachedProcessEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("camunda.cfg.xml").buildProcessEngine();
            }
            catch (RuntimeException ex) {
                if (ex.getCause() == null || !(ex.getCause() instanceof FileNotFoundException)) {
                    throw ex;
                }
                PluggableProcessEngineTestCase.cachedProcessEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
            }
        }
        return PluggableProcessEngineTestCase.cachedProcessEngine;
    }
    
    public static ProcessEngine getProcessEngine() {
        return getOrInitializeCachedProcessEngine();
    }
}
