// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import org.zik.bpm.engine.query.Query;

public interface CleanableHistoricBatchReport extends Query<CleanableHistoricBatchReport, CleanableHistoricBatchReportResult>
{
    CleanableHistoricBatchReport orderByFinishedBatchOperation();
}
