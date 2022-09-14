// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.calendar;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.joda.time.ReadablePeriod;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.DateTime;
import java.util.Date;
import org.zik.bpm.engine.task.Task;
import org.zik.bpm.engine.impl.util.EngineUtilLogger;

public class DueDateBusinessCalendar implements BusinessCalendar
{
    private static final EngineUtilLogger LOG;
    public static final String NAME = "dueDate";
    
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
            if (duedate.startsWith("P")) {
                DateTime start = null;
                if (startDate == null) {
                    start = DateTimeUtil.now();
                }
                else {
                    start = new DateTime((Object)startDate);
                }
                return start.plus((ReadablePeriod)ISOPeriodFormat.standard().parsePeriod(duedate)).toDate();
            }
            return DateTimeUtil.parseDateTime(duedate).toDate();
        }
        catch (Exception e) {
            throw DueDateBusinessCalendar.LOG.exceptionWhileResolvingDuedate(duedate, e);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.UTIL_LOGGER;
    }
}
