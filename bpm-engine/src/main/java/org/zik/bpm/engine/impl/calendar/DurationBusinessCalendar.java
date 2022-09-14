// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.calendar;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Date;
import org.zik.bpm.engine.task.Task;
import org.zik.bpm.engine.impl.util.EngineUtilLogger;

public class DurationBusinessCalendar implements BusinessCalendar
{
    private static final EngineUtilLogger LOG;
    public static String NAME;
    
    @Override
    public Date resolveDuedate(final String duedate, final Task task) {
        return this.resolveDuedate(duedate);
    }
    
    @Override
    public Date resolveDuedate(final String duedate) {
        return this.resolveDuedate(duedate, (Date)null);
    }
    
    @Override
    public Date resolveDuedate(final String duedate, final Date startDate) {
        try {
            final DurationHelper dh = new DurationHelper(duedate, startDate);
            return dh.getDateAfter(startDate);
        }
        catch (Exception e) {
            throw DurationBusinessCalendar.LOG.exceptionWhileResolvingDuedate(duedate, e);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.UTIL_LOGGER;
        DurationBusinessCalendar.NAME = "duration";
    }
}
