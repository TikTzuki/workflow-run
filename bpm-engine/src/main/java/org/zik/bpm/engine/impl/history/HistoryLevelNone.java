// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history;

import org.zik.bpm.engine.impl.history.event.HistoryEventType;

public class HistoryLevelNone extends AbstractHistoryLevel
{
    @Override
    public int getId() {
        return 0;
    }
    
    @Override
    public String getName() {
        return "none";
    }
    
    @Override
    public boolean isHistoryEventProduced(final HistoryEventType eventType, final Object entity) {
        return false;
    }
}
