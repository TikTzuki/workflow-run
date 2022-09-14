// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.delegate;

import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.engine.ProcessEngineServices;

public interface ProcessEngineServicesAware
{
    ProcessEngineServices getProcessEngineServices();
    
    ProcessEngine getProcessEngine();
}
