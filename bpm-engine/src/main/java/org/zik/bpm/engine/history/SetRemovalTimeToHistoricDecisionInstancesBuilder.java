// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import org.zik.bpm.engine.batch.Batch;

public interface SetRemovalTimeToHistoricDecisionInstancesBuilder
{
    SetRemovalTimeToHistoricDecisionInstancesBuilder byQuery(final HistoricDecisionInstanceQuery p0);
    
    SetRemovalTimeToHistoricDecisionInstancesBuilder byIds(final String... p0);
    
    SetRemovalTimeToHistoricDecisionInstancesBuilder hierarchical();
    
    Batch executeAsync();
}
