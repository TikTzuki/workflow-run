// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.parser;

import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;

public class ProcessInstanceEndListener extends HistoryExecutionListener
{
    public ProcessInstanceEndListener(final HistoryEventProducer historyEventProducer) {
        super(historyEventProducer);
    }
    
    @Override
    protected HistoryEvent createHistoryEvent(final DelegateExecution execution) {
        this.ensureHistoryLevelInitialized();
        if (this.historyLevel.isHistoryEventProduced(HistoryEventTypes.PROCESS_INSTANCE_END, execution)) {
            return this.eventProducer.createProcessInstanceEndEvt(execution);
        }
        return null;
    }
}
