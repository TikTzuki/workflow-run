// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.transformer;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.DelegateCaseExecution;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.history.producer.CmmnHistoryEventProducer;
import org.zik.bpm.engine.delegate.CaseExecutionListener;

public abstract class HistoryCaseExecutionListener implements CaseExecutionListener
{
    protected CmmnHistoryEventProducer eventProducer;
    protected HistoryLevel historyLevel;
    
    public HistoryCaseExecutionListener(final CmmnHistoryEventProducer historyEventProducer) {
        this.eventProducer = historyEventProducer;
    }
    
    @Override
    public void notify(final DelegateCaseExecution caseExecution) throws Exception {
        final HistoryEvent historyEvent = this.createHistoryEvent(caseExecution);
        if (historyEvent != null) {
            Context.getProcessEngineConfiguration().getHistoryEventHandler().handleEvent(historyEvent);
        }
    }
    
    protected void ensureHistoryLevelInitialized() {
        if (this.historyLevel == null) {
            this.historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
        }
    }
    
    protected abstract HistoryEvent createHistoryEvent(final DelegateCaseExecution p0);
}
