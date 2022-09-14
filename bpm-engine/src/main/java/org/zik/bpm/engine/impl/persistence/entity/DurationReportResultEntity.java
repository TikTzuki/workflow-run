// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.history.DurationReportResult;

public class DurationReportResultEntity extends ReportResultEntity implements DurationReportResult
{
    protected long minimum;
    protected long maximum;
    protected long average;
    
    @Override
    public long getMinimum() {
        return this.minimum;
    }
    
    public void setMinimum(final long minimum) {
        this.minimum = minimum;
    }
    
    @Override
    public long getMaximum() {
        return this.maximum;
    }
    
    public void setMaximum(final long maximum) {
        this.maximum = maximum;
    }
    
    @Override
    public long getAverage() {
        return this.average;
    }
    
    public void setAverage(final long average) {
        this.average = average;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[period=" + this.period + ", periodUnit=" + this.periodUnit + ", minimum=" + this.minimum + ", maximum=" + this.maximum + ", average=" + this.average + "]";
    }
}
