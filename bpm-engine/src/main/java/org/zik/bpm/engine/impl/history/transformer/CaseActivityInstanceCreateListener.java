// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.transformer;

import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.delegate.DelegateCaseExecution;
import org.zik.bpm.engine.impl.history.producer.CmmnHistoryEventProducer;

public class CaseActivityInstanceCreateListener extends HistoryCaseExecutionListener
{
    public CaseActivityInstanceCreateListener(final CmmnHistoryEventProducer historyEventProducer) {
        super(historyEventProducer);
    }
    
    @Override
    protected HistoryEvent createHistoryEvent(final DelegateCaseExecution caseExecution) {
        this.ensureHistoryLevelInitialized();
        if (this.historyLevel.isHistoryEventProduced(HistoryEventTypes.CASE_ACTIVITY_INSTANCE_CREATE, caseExecution)) {
            return this.eventProducer.createCaseActivityInstanceCreateEvt(caseExecution);
        }
        return null;
    }
}