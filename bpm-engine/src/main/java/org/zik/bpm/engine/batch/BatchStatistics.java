// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.batch;

public interface BatchStatistics extends Batch
{
    int getRemainingJobs();
    
    int getCompletedJobs();
    
    int getFailedJobs();
}
