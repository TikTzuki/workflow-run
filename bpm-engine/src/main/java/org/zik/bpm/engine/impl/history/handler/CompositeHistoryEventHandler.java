// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.handler;

import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import java.util.Iterator;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class CompositeHistoryEventHandler implements HistoryEventHandler
{
    protected final List<HistoryEventHandler> historyEventHandlers;
    
    public CompositeHistoryEventHandler() {
        this.historyEventHandlers = new ArrayList<HistoryEventHandler>();
    }
    
    public CompositeHistoryEventHandler(final HistoryEventHandler... historyEventHandlers) {
        this.historyEventHandlers = new ArrayList<HistoryEventHandler>();
        this.initializeHistoryEventHandlers(Arrays.asList(historyEventHandlers));
    }
    
    public CompositeHistoryEventHandler(final List<HistoryEventHandler> historyEventHandlers) {
        this.historyEventHandlers = new ArrayList<HistoryEventHandler>();
        this.initializeHistoryEventHandlers(historyEventHandlers);
    }
    
    private void initializeHistoryEventHandlers(final List<HistoryEventHandler> historyEventHandlers) {
        EnsureUtil.ensureNotNull("History event handler", historyEventHandlers);
        for (final HistoryEventHandler historyEventHandler : historyEventHandlers) {
            EnsureUtil.ensureNotNull("History event handler", historyEventHandler);
            this.historyEventHandlers.add(historyEventHandler);
        }
    }
    
    public void add(final HistoryEventHandler historyEventHandler) {
        EnsureUtil.ensureNotNull("History event handler", historyEventHandler);
        this.historyEventHandlers.add(historyEventHandler);
    }
    
    @Override
    public void handleEvent(final HistoryEvent historyEvent) {
        for (final HistoryEventHandler historyEventHandler : this.historyEventHandlers) {
            historyEventHandler.handleEvent(historyEvent);
        }
    }
    
    @Override
    public void handleEvents(final List<HistoryEvent> historyEvents) {
        for (final HistoryEvent historyEvent : historyEvents) {
            this.handleEvent(historyEvent);
        }
    }
}
