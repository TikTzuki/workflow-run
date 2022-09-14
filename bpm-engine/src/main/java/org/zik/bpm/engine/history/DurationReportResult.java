// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

public interface DurationReportResult extends ReportResult
{
    long getMinimum();
    
    long getMaximum();
    
    long getAverage();
}
