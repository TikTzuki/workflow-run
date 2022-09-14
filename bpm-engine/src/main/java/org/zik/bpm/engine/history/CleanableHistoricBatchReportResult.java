// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

public interface CleanableHistoricBatchReportResult
{
    String getBatchType();
    
    Integer getHistoryTimeToLive();
    
    long getFinishedBatchesCount();
    
    long getCleanableBatchesCount();
}
