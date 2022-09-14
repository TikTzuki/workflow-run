// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.query.PeriodUnit;
import org.zik.bpm.engine.history.ReportResult;

public abstract class ReportResultEntity implements ReportResult
{
    protected int period;
    protected PeriodUnit periodUnit;
    
    @Override
    public int getPeriod() {
        return this.period;
    }
    
    public void setPeriod(final int period) {
        this.period = period;
    }
    
    @Override
    public PeriodUnit getPeriodUnit() {
        return this.periodUnit;
    }
    
    public void setPeriodUnit(final String periodUnit) {
        this.periodUnit = PeriodUnit.valueOf(periodUnit);
    }
}
