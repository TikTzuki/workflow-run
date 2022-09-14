// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history;

import org.zik.bpm.engine.impl.history.event.HistoryEventType;

public interface HistoryLevel
{
    public static final HistoryLevel HISTORY_LEVEL_NONE = new HistoryLevelNone();
    public static final HistoryLevel HISTORY_LEVEL_ACTIVITY = new HistoryLevelActivity();
    public static final HistoryLevel HISTORY_LEVEL_AUDIT = new HistoryLevelAudit();
    public static final HistoryLevel HISTORY_LEVEL_FULL = new HistoryLevelFull();
    
    int getId();
    
    String getName();
    
    boolean isHistoryEventProduced(final HistoryEventType p0, final Object p1);
}
