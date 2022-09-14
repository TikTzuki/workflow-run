// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.history.CleanableHistoricBatchReportResult;

public class CleanableHistoricBatchesReportResultEntity implements CleanableHistoricBatchReportResult
{
    protected String batchType;
    protected Integer historyTimeToLive;
    protected long finishedBatchesCount;
    protected long cleanableBatchesCount;
    
    @Override
    public String getBatchType() {
        return this.batchType;
    }
    
    public void setBatchType(final String batchType) {
        this.batchType = batchType;
    }
    
    @Override
    public Integer getHistoryTimeToLive() {
        return this.historyTimeToLive;
    }
    
    public void setHistoryTimeToLive(final Integer historyTimeToLive) {
        this.historyTimeToLive = historyTimeToLive;
    }
    
    @Override
    public long getFinishedBatchesCount() {
        return this.finishedBatchesCount;
    }
    
    public void setFinishedBatchesCount(final long finishedBatchCount) {
        this.finishedBatchesCount = finishedBatchCount;
    }
    
    @Override
    public long getCleanableBatchesCount() {
        return this.cleanableBatchesCount;
    }
    
    public void setCleanableBatchesCount(final long cleanableBatchCount) {
        this.cleanableBatchesCount = cleanableBatchCount;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[batchType = " + this.batchType + ", historyTimeToLive = " + this.historyTimeToLive + ", finishedBatchesCount = " + this.finishedBatchesCount + ", cleanableBatchesCount = " + this.cleanableBatchesCount + "]";
    }
}
