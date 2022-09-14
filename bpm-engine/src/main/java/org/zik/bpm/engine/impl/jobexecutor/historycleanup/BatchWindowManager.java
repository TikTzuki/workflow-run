// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor.historycleanup;

import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import java.util.Date;

public interface BatchWindowManager
{
    BatchWindow getCurrentOrNextBatchWindow(final Date p0, final ProcessEngineConfigurationImpl p1);
    
    BatchWindow getNextBatchWindow(final Date p0, final ProcessEngineConfigurationImpl p1);
    
    boolean isBatchWindowConfigured(final ProcessEngineConfigurationImpl p0);
}
