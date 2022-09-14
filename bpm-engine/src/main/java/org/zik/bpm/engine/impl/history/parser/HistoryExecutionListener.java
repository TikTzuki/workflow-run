// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.parser;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.history.handler.HistoryEventHandler;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.delegate.ExecutionListener;

public abstract class HistoryExecutionListener implements ExecutionListener
{
    protected final HistoryEventProducer eventProducer;
    protected HistoryLevel historyLevel;
    
    public HistoryExecutionListener(final HistoryEventProducer historyEventProducer) {
        this.eventProducer = historyEventProducer;
    }
    
    @Override
    public void notify(final DelegateExecution execution) throws Exception {
        final HistoryEventHandler historyEventHandler = Context.getProcessEngineConfiguration().getHistoryEventHandler();
        final HistoryEvent historyEvent = this.createHistoryEvent(execution);
        if (historyEvent != null) {
            historyEventHandler.handleEvent(historyEvent);
        }
    }
    
    protected void ensureHistoryLevelInitialized() {
        if (this.historyLevel == null) {
            this.historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
        }
    }
    
    protected abstract HistoryEvent createHistoryEvent(final DelegateExecution p0);
}
