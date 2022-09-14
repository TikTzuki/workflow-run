// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.calendar;

import java.util.HashMap;
import java.util.GregorianCalendar;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.ClockUtil;
import java.util.Date;
import org.zik.bpm.engine.task.Task;
import java.util.Map;

public class DefaultBusinessCalendar implements BusinessCalendar
{
    private static Map<String, Integer> units;
    
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
        Date resolvedDuedate = (startDate == null) ? ClockUtil.getCurrentTime() : startDate;
        final String[] split;
        final String[] tokens = split = duedate.split(" and ");
        for (final String token : split) {
            resolvedDuedate = this.addSingleUnitQuantity(resolvedDuedate, token);
        }
        return resolvedDuedate;
    }
    
    protected Date addSingleUnitQuantity(final Date startDate, final String singleUnitQuantity) {
        final int spaceIndex = singleUnitQuantity.indexOf(" ");
        if (spaceIndex == -1 || singleUnitQuantity.length() < spaceIndex + 1) {
            throw new ProcessEngineException("invalid duedate format: " + singleUnitQuantity);
        }
        final String quantityText = singleUnitQuantity.substring(0, spaceIndex);
        final Integer quantity = new Integer(quantityText);
        final String unitText = singleUnitQuantity.substring(spaceIndex + 1).trim().toLowerCase();
        final int unit = DefaultBusinessCalendar.units.get(unitText);
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        calendar.add(unit, quantity);
        return calendar.getTime();
    }
    
    static {
        (DefaultBusinessCalendar.units = new HashMap<String, Integer>()).put("millis", 14);
        DefaultBusinessCalendar.units.put("seconds", 13);
        DefaultBusinessCalendar.units.put("second", 13);
        DefaultBusinessCalendar.units.put("minute", 12);
        DefaultBusinessCalendar.units.put("minutes", 12);
        DefaultBusinessCalendar.units.put("hour", 10);
        DefaultBusinessCalendar.units.put("hours", 10);
        DefaultBusinessCalendar.units.put("day", 6);
        DefaultBusinessCalendar.units.put("days", 6);
        DefaultBusinessCalendar.units.put("week", 3);
        DefaultBusinessCalendar.units.put("weeks", 3);
        DefaultBusinessCalendar.units.put("month", 2);
        DefaultBusinessCalendar.units.put("months", 2);
        DefaultBusinessCalendar.units.put("year", 1);
        DefaultBusinessCalendar.units.put("years", 1);
    }
}
