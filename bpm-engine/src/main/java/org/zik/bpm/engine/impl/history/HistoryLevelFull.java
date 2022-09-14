// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history;

import org.zik.bpm.engine.impl.history.event.HistoryEventType;

public class HistoryLevelFull extends AbstractHistoryLevel
{
    @Override
    public int getId() {
        return 3;
    }
    
    @Override
    public String getName() {
        return "full";
    }
    
    @Override
    public boolean isHistoryEventProduced(final HistoryEventType eventType, final Object entity) {
        return true;
    }
}
