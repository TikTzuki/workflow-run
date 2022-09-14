// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

public interface JobAcquisitionStrategy
{
    void reconfigure(final JobAcquisitionContext p0);
    
    long getWaitTime();
    
    int getNumJobsToAcquire(final String p0);
}
