// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor.historycleanup;

import java.util.Date;

public class BatchWindow
{
    public Date start;
    public Date end;
    
    public BatchWindow() {
    }
    
    public BatchWindow(final Date start, final Date end) {
        this.start = start;
        this.end = end;
    }
    
    public Date getStart() {
        return this.start;
    }
    
    public void setStart(final Date start) {
        this.start = start;
    }
    
    public Date getEnd() {
        return this.end;
    }
    
    public void setEnd(final Date end) {
        this.end = end;
    }
    
    public boolean isWithin(final Date date) {
        return (date.after(this.start) || date.equals(this.start)) && date.before(this.end);
    }
}
