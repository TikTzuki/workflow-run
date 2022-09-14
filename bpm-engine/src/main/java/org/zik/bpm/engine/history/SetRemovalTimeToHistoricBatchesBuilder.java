// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.batch.history.HistoricBatchQuery;

public interface SetRemovalTimeToHistoricBatchesBuilder
{
    SetRemovalTimeToHistoricBatchesBuilder byQuery(final HistoricBatchQuery p0);
    
    SetRemovalTimeToHistoricBatchesBuilder byIds(final String... p0);
    
    Batch executeAsync();
}
