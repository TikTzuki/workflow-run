// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import org.zik.bpm.engine.batch.Batch;

public interface SetRemovalTimeToHistoricProcessInstancesBuilder
{
    SetRemovalTimeToHistoricProcessInstancesBuilder byQuery(final HistoricProcessInstanceQuery p0);
    
    SetRemovalTimeToHistoricProcessInstancesBuilder byIds(final String... p0);
    
    SetRemovalTimeToHistoricProcessInstancesBuilder hierarchical();
    
    Batch executeAsync();
}
