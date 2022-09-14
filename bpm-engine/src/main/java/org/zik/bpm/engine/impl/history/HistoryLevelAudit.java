// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history;

import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;

public class HistoryLevelAudit extends HistoryLevelActivity
{
    @Override
    public int getId() {
        return 2;
    }
    
    @Override
    public String getName() {
        return "audit";
    }
    
    @Override
    public boolean isHistoryEventProduced(final HistoryEventType eventType, final Object entity) {
        return super.isHistoryEventProduced(eventType, entity) || HistoryEventTypes.VARIABLE_INSTANCE_CREATE == eventType || HistoryEventTypes.VARIABLE_INSTANCE_UPDATE == eventType || HistoryEventTypes.VARIABLE_INSTANCE_MIGRATE == eventType || HistoryEventTypes.VARIABLE_INSTANCE_DELETE == eventType || HistoryEventTypes.FORM_PROPERTY_UPDATE == eventType;
    }
}
