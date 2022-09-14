// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.calendar;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.util.ClockUtil;
import java.util.Date;
import org.zik.bpm.engine.task.Task;
import org.zik.bpm.engine.impl.util.EngineUtilLogger;

public class CycleBusinessCalendar implements BusinessCalendar
{
    private static final EngineUtilLogger LOG;
    public static String NAME;
    
    @Override
    public Date resolveDuedate(final String duedateDescription, final Task task) {
        return this.resolveDuedate(duedateDescription);
    }
    
    @Override
    public Date resolveDuedate(final String duedateDescription) {
        return this.resolveDuedate(duedateDescription, (Date)null);
    }
    
    @Override
    public Date resolveDuedate(final String duedateDescription, final Date startDate) {
        return this.resolveDuedate(duedateDescription, startDate, 0L);
    }
    
    public Date resolveDuedate(final String duedateDescription, final Date startDate, final long repeatOffset) {
        try {
            if (duedateDescription.startsWith("R")) {
                final DurationHelper durationHelper = new DurationHelper(duedateDescription, startDate);
                durationHelper.setRepeatOffset(repeatOffset);
                return durationHelper.getDateAfter(startDate);
            }
            final CronExpression ce = new CronExpression(duedateDescription);
            return ce.getTimeAfter((startDate == null) ? ClockUtil.getCurrentTime() : startDate);
        }
        catch (Exception e) {
            throw CycleBusinessCalendar.LOG.exceptionWhileParsingCronExpresison(duedateDescription, e);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.UTIL_LOGGER;
        CycleBusinessCalendar.NAME = "cycle";
    }
}
