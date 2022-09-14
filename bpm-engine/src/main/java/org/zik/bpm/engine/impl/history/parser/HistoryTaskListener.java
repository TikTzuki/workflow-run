// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.parser;

import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.history.handler.HistoryEventHandler;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.DelegateTask;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.delegate.TaskListener;

public abstract class HistoryTaskListener implements TaskListener
{
    protected final HistoryEventProducer eventProducer;
    protected HistoryLevel historyLevel;
    
    public HistoryTaskListener(final HistoryEventProducer historyEventProducer) {
        this.eventProducer = historyEventProducer;
    }
    
    @Override
    public void notify(final DelegateTask task) {
        final HistoryEventHandler historyEventHandler = Context.getProcessEngineConfiguration().getHistoryEventHandler();
        final ExecutionEntity execution = ((TaskEntity)task).getExecution();
        if (execution != null) {
            final HistoryEvent historyEvent = this.createHistoryEvent(task, execution);
            if (historyEvent != null) {
                historyEventHandler.handleEvent(historyEvent);
            }
        }
    }
    
    protected void ensureHistoryLevelInitialized() {
        if (this.historyLevel == null) {
            this.historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
        }
    }
    
    protected abstract HistoryEvent createHistoryEvent(final DelegateTask p0, final ExecutionEntity p1);
}
