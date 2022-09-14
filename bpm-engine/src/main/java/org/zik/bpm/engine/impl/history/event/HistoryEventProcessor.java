// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

import java.util.Collections;
import java.util.List;
import org.zik.bpm.engine.impl.history.handler.HistoryEventHandler;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.impl.context.Context;

public class HistoryEventProcessor
{
    public static void processHistoryEvents(final HistoryEventCreator creator) {
        final HistoryEventProducer historyEventProducer = Context.getProcessEngineConfiguration().getHistoryEventProducer();
        final HistoryEventHandler historyEventHandler = Context.getProcessEngineConfiguration().getHistoryEventHandler();
        final HistoryEvent singleEvent = creator.createHistoryEvent(historyEventProducer);
        if (singleEvent != null) {
            historyEventHandler.handleEvent(singleEvent);
            creator.postHandleSingleHistoryEventCreated(singleEvent);
        }
        final List<HistoryEvent> eventList = creator.createHistoryEvents(historyEventProducer);
        historyEventHandler.handleEvents(eventList);
    }
    
    public static class HistoryEventCreator
    {
        public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
            return null;
        }
        
        public List<HistoryEvent> createHistoryEvents(final HistoryEventProducer producer) {
            return Collections.emptyList();
        }
        
        public void postHandleSingleHistoryEventCreated(final HistoryEvent event) {
        }
    }
}
