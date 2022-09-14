// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import org.zik.bpm.engine.ProcessEngineConfiguration;
import org.zik.bpm.engine.ProcessEngineServices;

public interface ProcessEngine extends ProcessEngineServices
{
    public static final String VERSION = "fox";
    
    String getName();
    
    void close();
    
    ProcessEngineConfiguration getProcessEngineConfiguration();
}
