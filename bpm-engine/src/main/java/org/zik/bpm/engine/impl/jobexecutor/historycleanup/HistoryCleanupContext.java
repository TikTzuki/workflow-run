// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor.historycleanup;

public class HistoryCleanupContext
{
    private boolean immediatelyDue;
    private int minuteFrom;
    private int minuteTo;
    
    public HistoryCleanupContext(final boolean immediatelyDue, final int minuteFrom, final int minuteTo) {
        this.immediatelyDue = immediatelyDue;
        this.minuteFrom = minuteFrom;
        this.minuteTo = minuteTo;
    }
    
    public HistoryCleanupContext(final int minuteFrom, final int minuteTo) {
        this.minuteFrom = minuteFrom;
        this.minuteTo = minuteTo;
    }
    
    public boolean isImmediatelyDue() {
        return this.immediatelyDue;
    }
    
    public void setImmediatelyDue(final boolean immediatelyDue) {
        this.immediatelyDue = immediatelyDue;
    }
    
    public int getMinuteFrom() {
        return this.minuteFrom;
    }
    
    public void setMinuteFrom(final int minuteFrom) {
        this.minuteFrom = minuteFrom;
    }
    
    public int getMinuteTo() {
        return this.minuteTo;
    }
    
    public void setMinuteTo(final int minuteTo) {
        this.minuteTo = minuteTo;
    }
}
