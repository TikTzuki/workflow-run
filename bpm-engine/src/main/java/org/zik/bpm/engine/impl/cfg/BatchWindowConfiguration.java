// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg;

import java.text.ParseException;
import org.zik.bpm.engine.impl.jobexecutor.historycleanup.HistoryCleanupHelper;
import java.util.Date;

public class BatchWindowConfiguration
{
    protected static final ConfigurationLogger LOG;
    private String startTime;
    private Date startTimeAsDate;
    private String endTime;
    private Date endTimeAsDate;
    
    public BatchWindowConfiguration() {
        this.endTime = "00:00";
    }
    
    public BatchWindowConfiguration(final String startTime, final String endTime) {
        this.endTime = "00:00";
        this.startTime = startTime;
        this.initStartTimeAsDate();
        if (endTime != null) {
            this.endTime = endTime;
        }
        this.initEndTimeAsDate();
    }
    
    private void initStartTimeAsDate() {
        try {
            this.startTimeAsDate = HistoryCleanupHelper.parseTimeConfiguration(this.startTime);
        }
        catch (ParseException e) {
            throw BatchWindowConfiguration.LOG.invalidPropertyValue("startTime", this.startTime);
        }
    }
    
    private void initEndTimeAsDate() {
        try {
            this.endTimeAsDate = HistoryCleanupHelper.parseTimeConfiguration(this.endTime);
        }
        catch (ParseException e) {
            throw BatchWindowConfiguration.LOG.invalidPropertyValue("endTime", this.endTime);
        }
    }
    
    public String getStartTime() {
        return this.startTime;
    }
    
    public void setStartTime(final String startTime) {
        this.startTime = startTime;
        this.initStartTimeAsDate();
    }
    
    public String getEndTime() {
        return this.endTime;
    }
    
    public void setEndTime(final String endTime) {
        this.endTime = endTime;
        this.initEndTimeAsDate();
    }
    
    public Date getStartTimeAsDate() {
        return this.startTimeAsDate;
    }
    
    public Date getEndTimeAsDate() {
        return this.endTimeAsDate;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final BatchWindowConfiguration that = (BatchWindowConfiguration)o;
        if (this.startTime != null) {
            if (this.startTime.equals(that.startTime)) {
                return (this.endTime != null) ? this.endTime.equals(that.endTime) : (that.endTime == null);
            }
        }
        else if (that.startTime == null) {
            return (this.endTime != null) ? this.endTime.equals(that.endTime) : (that.endTime == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.startTime != null) ? this.startTime.hashCode() : 0;
        result = 31 * result + ((this.endTime != null) ? this.endTime.hashCode() : 0);
        return result;
    }
    
    static {
        LOG = ConfigurationLogger.CONFIG_LOGGER;
    }
}
