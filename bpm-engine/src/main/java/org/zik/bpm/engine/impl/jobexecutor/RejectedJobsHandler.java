// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.ProcessEngineImpl;
import java.util.List;

public interface RejectedJobsHandler
{
    void jobsRejected(final List<String> p0, final ProcessEngineImpl p1, final JobExecutor p2);
}
