// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container;

import org.zik.bpm.engine.impl.ProcessEngineImpl;
import java.util.List;

public interface ExecutorService
{
    boolean schedule(final Runnable p0, final boolean p1);
    
    Runnable getExecuteJobsRunnable(final List<String> p0, final ProcessEngineImpl p1);
}
