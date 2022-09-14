// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.calendar;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.zik.bpm.engine.impl.util.ClockUtil;
import java.util.Arrays;
import java.util.ArrayList;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import java.util.Date;
import org.zik.bpm.engine.impl.util.EngineUtilLogger;

public class DurationHelper
{
    public static final String PnW_PATTERN = "P\\d+W";
    private static final int MS_PER_WEEK = 604800000;
    private static final EngineUtilLogger LOG;
    Date start;
    Date end;
    Duration period;
    boolean isRepeat;
    int times;
    long repeatOffset;
    DatatypeFactory datatypeFactory;
    
    public DurationHelper(final String expressions) throws Exception {
        this(expressions, null);
    }
    
    public DurationHelper(final String expressions, final Date startDate) throws Exception {
        List<String> expression = new ArrayList<String>();
        if (expressions != null) {
            expression = Arrays.asList(expressions.split("/"));
        }
        this.datatypeFactory = DatatypeFactory.newInstance();
        if (expression.size() > 3 || expression.isEmpty()) {
            throw DurationHelper.LOG.cannotParseDuration(expressions);
        }
        if (expression.get(0).startsWith("R")) {
            this.isRepeat = true;
            this.times = ((expression.get(0).length() == 1) ? Integer.MAX_VALUE : Integer.parseInt(expression.get(0).substring(1)));
            expression = expression.subList(1, expression.size());
        }
        if (this.isDuration(expression.get(0))) {
            this.period = this.parsePeriod(expression.get(0));
            this.end = ((expression.size() == 1) ? null : DateTimeUtil.parseDate(expression.get(1)));
        }
        else {
            this.start = DateTimeUtil.parseDate(expression.get(0));
            if (this.isDuration(expression.get(1))) {
                this.period = this.parsePeriod(expression.get(1));
            }
            else {
                this.end = DateTimeUtil.parseDate(expression.get(1));
                this.period = this.datatypeFactory.newDuration(this.end.getTime() - this.start.getTime());
            }
        }
        if (this.start == null && this.end == null) {
            this.start = ((startDate == null) ? ClockUtil.getCurrentTime() : startDate);
        }
    }
    
    public Date getDateAfter() {
        return this.getDateAfter(null);
    }
    
    public Date getDateAfter(final Date date) {
        if (this.isRepeat) {
            return this.getDateAfterRepeat((date == null) ? ClockUtil.getCurrentTime() : date);
        }
        if (this.end != null) {
            return this.end;
        }
        return this.add(this.start, this.period);
    }
    
    public int getTimes() {
        return this.times;
    }
    
    public boolean isRepeat() {
        return this.isRepeat;
    }
    
    private Date getDateAfterRepeat(final Date date) {
        final Date dateWithoutOffset = new Date(date.getTime() - this.repeatOffset);
        if (this.start == null) {
            Date cur = this.add(this.end, this.period.negate());
            Date next = this.end;
            for (int i = 0; i < this.times && cur.after(date); cur = this.add(cur, this.period.negate()), ++i) {
                next = cur;
            }
            return next.before(date) ? null : next;
        }
        Date cur = this.start;
        for (int j = 0; j < this.times && !cur.after(dateWithoutOffset); cur = this.add(cur, this.period), ++j) {}
        if (cur.before(dateWithoutOffset)) {
            return null;
        }
        return (this.repeatOffset == 0L) ? cur : new Date(cur.getTime() + this.repeatOffset);
    }
    
    private Date add(final Date date, final Duration duration) {
        final Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        duration.addTo(calendar);
        return calendar.getTime();
    }
    
    private Duration parsePeriod(final String period) {
        if (period.matches("P\\d+W")) {
            return this.parsePnWDuration(period);
        }
        return this.datatypeFactory.newDuration(period);
    }
    
    private Duration parsePnWDuration(final String period) {
        final String weeks = period.replaceAll("\\D", "");
        final long duration = Long.parseLong(weeks) * 604800000L;
        return this.datatypeFactory.newDuration(duration);
    }
    
    private boolean isDuration(final String time) {
        return time.startsWith("P");
    }
    
    public void setRepeatOffset(final long repeatOffset) {
        this.repeatOffset = repeatOffset;
    }
    
    static {
        LOG = ProcessEngineLogger.UTIL_LOGGER;
    }
}
