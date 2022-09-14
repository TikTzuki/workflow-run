// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history;

import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;

public class HistoryLevelActivity extends AbstractHistoryLevel
{
    @Override
    public int getId() {
        return 1;
    }
    
    @Override
    public String getName() {
        return "activity";
    }
    
    @Override
    public boolean isHistoryEventProduced(final HistoryEventType eventType, final Object entity) {
        return HistoryEventTypes.PROCESS_INSTANCE_START == eventType || HistoryEventTypes.PROCESS_INSTANCE_UPDATE == eventType || HistoryEventTypes.PROCESS_INSTANCE_MIGRATE == eventType || HistoryEventTypes.PROCESS_INSTANCE_END == eventType || HistoryEventTypes.TASK_INSTANCE_CREATE == eventType || HistoryEventTypes.TASK_INSTANCE_UPDATE == eventType || HistoryEventTypes.TASK_INSTANCE_MIGRATE == eventType || HistoryEventTypes.TASK_INSTANCE_COMPLETE == eventType || HistoryEventTypes.TASK_INSTANCE_DELETE == eventType || HistoryEventTypes.ACTIVITY_INSTANCE_START == eventType || HistoryEventTypes.ACTIVITY_INSTANCE_UPDATE == eventType || HistoryEventTypes.ACTIVITY_INSTANCE_MIGRATE == eventType || HistoryEventTypes.ACTIVITY_INSTANCE_END == eventType || HistoryEventTypes.CASE_INSTANCE_CREATE == eventType || HistoryEventTypes.CASE_INSTANCE_UPDATE == eventType || HistoryEventTypes.CASE_INSTANCE_CLOSE == eventType || HistoryEventTypes.CASE_ACTIVITY_INSTANCE_CREATE == eventType || HistoryEventTypes.CASE_ACTIVITY_INSTANCE_UPDATE == eventType || HistoryEventTypes.CASE_ACTIVITY_INSTANCE_END == eventType;
    }
}
