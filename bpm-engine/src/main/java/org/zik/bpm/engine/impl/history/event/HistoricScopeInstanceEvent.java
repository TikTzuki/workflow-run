// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

import java.util.Date;

public class HistoricScopeInstanceEvent extends HistoryEvent
{
    private static final long serialVersionUID = 1L;
    protected Long durationInMillis;
    protected Date startTime;
    protected Date endTime;
    
    public Date getEndTime() {
        return this.endTime;
    }
    
    public void setEndTime(final Date endTime) {
        this.endTime = endTime;
    }
    
    public Date getStartTime() {
        return this.startTime;
    }
    
    public void setStartTime(final Date startTime) {
        this.startTime = startTime;
    }
    
    public Long getDurationInMillis() {
        if (this.durationInMillis != null) {
            return this.durationInMillis;
        }
        if (this.startTime != null && this.endTime != null) {
            return this.endTime.getTime() - this.startTime.getTime();
        }
        return null;
    }
    
    public void setDurationInMillis(final Long durationInMillis) {
        this.durationInMillis = durationInMillis;
    }
    
    public Long getDurationRaw() {
        return this.durationInMillis;
    }
}
