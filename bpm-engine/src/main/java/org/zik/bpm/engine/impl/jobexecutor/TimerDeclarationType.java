// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.calendar.CycleBusinessCalendar;
import org.zik.bpm.engine.impl.calendar.DurationBusinessCalendar;

public enum TimerDeclarationType
{
    DATE("dueDate"), 
    DURATION(DurationBusinessCalendar.NAME), 
    CYCLE(CycleBusinessCalendar.NAME);
    
    public final String calendarName;
    
    private TimerDeclarationType(final String caledarName) {
        this.calendarName = caledarName;
    }
}
