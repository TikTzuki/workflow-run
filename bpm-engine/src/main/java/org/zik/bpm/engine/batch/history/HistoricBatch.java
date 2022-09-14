// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.batch.history;

import java.util.Date;

public interface HistoricBatch
{
    String getId();
    
    String getType();
    
    int getTotalJobs();
    
    int getBatchJobsPerSeed();
    
    int getInvocationsPerBatchJob();
    
    String getSeedJobDefinitionId();
    
    String getMonitorJobDefinitionId();
    
    String getBatchJobDefinitionId();
    
    String getTenantId();
    
    String getCreateUserId();
    
    Date getStartTime();
    
    Date getEndTime();
    
    Date getRemovalTime();
}
