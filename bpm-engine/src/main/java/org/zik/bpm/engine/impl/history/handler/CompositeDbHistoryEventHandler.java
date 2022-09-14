// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.handler;

import java.util.List;

public class CompositeDbHistoryEventHandler extends CompositeHistoryEventHandler
{
    public CompositeDbHistoryEventHandler() {
        this.addDefaultDbHistoryEventHandler();
    }
    
    public CompositeDbHistoryEventHandler(final HistoryEventHandler... historyEventHandlers) {
        super(historyEventHandlers);
        this.addDefaultDbHistoryEventHandler();
    }
    
    public CompositeDbHistoryEventHandler(final List<HistoryEventHandler> historyEventHandlers) {
        super(historyEventHandlers);
        this.addDefaultDbHistoryEventHandler();
    }
    
    private void addDefaultDbHistoryEventHandler() {
        this.historyEventHandlers.add(new DbHistoryEventHandler());
    }
}
