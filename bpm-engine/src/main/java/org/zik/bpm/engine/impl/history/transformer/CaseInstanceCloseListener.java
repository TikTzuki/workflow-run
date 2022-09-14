// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.transformer;

import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.delegate.DelegateCaseExecution;
import org.zik.bpm.engine.impl.history.producer.CmmnHistoryEventProducer;

public class CaseInstanceCloseListener extends HistoryCaseExecutionListener
{
    public CaseInstanceCloseListener(final CmmnHistoryEventProducer historyEventProducer) {
        super(historyEventProducer);
    }
    
    @Override
    protected HistoryEvent createHistoryEvent(final DelegateCaseExecution caseExecution) {
        this.ensureHistoryLevelInitialized();
        if (this.historyLevel.isHistoryEventProduced(HistoryEventTypes.CASE_INSTANCE_CLOSE, caseExecution)) {
            return this.eventProducer.createCaseInstanceCloseEvt(caseExecution);
        }
        return null;
    }
}
