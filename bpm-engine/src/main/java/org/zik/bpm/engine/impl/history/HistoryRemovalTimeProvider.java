// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history;

import org.zik.bpm.engine.impl.batch.history.HistoricBatchEntity;
import org.zik.bpm.engine.repository.DecisionDefinition;
import org.zik.bpm.engine.impl.history.event.HistoricDecisionInstanceEntity;
import java.util.Date;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.history.event.HistoricProcessInstanceEventEntity;

public interface HistoryRemovalTimeProvider
{
    Date calculateRemovalTime(final HistoricProcessInstanceEventEntity p0, final ProcessDefinition p1);
    
    Date calculateRemovalTime(final HistoricDecisionInstanceEntity p0, final DecisionDefinition p1);
    
    Date calculateRemovalTime(final HistoricBatchEntity p0);
}
