// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.calendar;

import org.zik.bpm.engine.task.Task;
import java.util.Date;

public interface BusinessCalendar
{
    Date resolveDuedate(final String p0);
    
    Date resolveDuedate(final String p0, final Date p1);
    
    Date resolveDuedate(final String p0, final Task p1);
}
