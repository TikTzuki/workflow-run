// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.handler;

import java.util.List;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;

public interface HistoryEventHandler
{
    void handleEvent(final HistoryEvent p0);
    
    void handleEvents(final List<HistoryEvent> p0);
}
