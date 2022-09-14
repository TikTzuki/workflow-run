// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor.historycleanup;

import java.util.Calendar;
import org.zik.bpm.engine.impl.cfg.BatchWindowConfiguration;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import java.util.Date;

public class DefaultBatchWindowManager implements BatchWindowManager
{
    public BatchWindow getPreviousDayBatchWindow(final Date date, final ProcessEngineConfigurationImpl configuration) {
        final Date previousDay = addDays(date, -1);
        return this.getBatchWindowForDate(previousDay, configuration);
    }
    
    private BatchWindow getBatchWindowForDate(final Date date, final ProcessEngineConfigurationImpl configuration) {
        BatchWindowConfiguration batchWindowConfiguration = configuration.getHistoryCleanupBatchWindows().get(this.dayOfWeek(date));
        if (batchWindowConfiguration == null && configuration.getHistoryCleanupBatchWindowStartTime() != null) {
            batchWindowConfiguration = new BatchWindowConfiguration(configuration.getHistoryCleanupBatchWindowStartTime(), configuration.getHistoryCleanupBatchWindowEndTime());
        }
        if (batchWindowConfiguration == null) {
            return null;
        }
        final Date startTime = updateTime(date, batchWindowConfiguration.getStartTimeAsDate());
        Date endTime = updateTime(date, batchWindowConfiguration.getEndTimeAsDate());
        if (!endTime.after(startTime)) {
            endTime = addDays(endTime, 1);
        }
        return new BatchWindow(startTime, endTime);
    }
    
    private Integer dayOfWeek(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(7);
    }
    
    @Override
    public BatchWindow getCurrentOrNextBatchWindow(final Date date, final ProcessEngineConfigurationImpl configuration) {
        final BatchWindow previousDayBatchWindow = this.getPreviousDayBatchWindow(date, configuration);
        if (previousDayBatchWindow != null && previousDayBatchWindow.isWithin(date)) {
            return previousDayBatchWindow;
        }
        final BatchWindow currentDayBatchWindow = this.getBatchWindowForDate(date, configuration);
        if (currentDayBatchWindow != null && (currentDayBatchWindow.isWithin(date) || date.before(currentDayBatchWindow.getStart()))) {
            return currentDayBatchWindow;
        }
        for (int i = 1; i <= 7; ++i) {
            final Date dateToCheck = addDays(date, i);
            final BatchWindow batchWindowForDate = this.getBatchWindowForDate(dateToCheck, configuration);
            if (batchWindowForDate != null) {
                return batchWindowForDate;
            }
        }
        return null;
    }
    
    @Override
    public BatchWindow getNextBatchWindow(final Date date, final ProcessEngineConfigurationImpl configuration) {
        final BatchWindow currentDayBatchWindow = this.getBatchWindowForDate(date, configuration);
        if (currentDayBatchWindow != null && date.before(currentDayBatchWindow.getStart())) {
            return currentDayBatchWindow;
        }
        for (int i = 1; i <= 7; ++i) {
            final Date dateToCheck = addDays(date, i);
            final BatchWindow batchWindowForDate = this.getBatchWindowForDate(dateToCheck, configuration);
            if (batchWindowForDate != null) {
                return batchWindowForDate;
            }
        }
        return null;
    }
    
    @Override
    public boolean isBatchWindowConfigured(final ProcessEngineConfigurationImpl configuration) {
        return configuration.getHistoryCleanupBatchWindowStartTimeAsDate() != null || !configuration.getHistoryCleanupBatchWindows().isEmpty();
    }
    
    private static Date updateTime(final Date now, final Date newTime) {
        final Calendar c = Calendar.getInstance();
        c.setTime(now);
        final Calendar newTimeCalendar = Calendar.getInstance();
        newTimeCalendar.setTime(newTime);
        c.set(15, newTimeCalendar.get(15));
        c.set(16, newTimeCalendar.get(16));
        c.set(11, newTimeCalendar.get(11));
        c.set(12, newTimeCalendar.get(12));
        c.set(13, newTimeCalendar.get(13));
        c.set(14, newTimeCalendar.get(14));
        return c.getTime();
    }
    
    private static Date addDays(final Date date, final int amount) {
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(5, amount);
        return c.getTime();
    }
}
