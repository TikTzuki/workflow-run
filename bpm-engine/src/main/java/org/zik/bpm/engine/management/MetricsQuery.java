// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.management;

import java.util.List;
import java.util.Date;

public interface MetricsQuery
{
    MetricsQuery name(final String p0);
    
    MetricsQuery reporter(final String p0);
    
    MetricsQuery startDate(final Date p0);
    
    MetricsQuery endDate(final Date p0);
    
    MetricsQuery offset(final int p0);
    
    MetricsQuery limit(final int p0);
    
    MetricsQuery aggregateByReporter();
    
    List<MetricIntervalValue> interval();
    
    List<MetricIntervalValue> interval(final long p0);
    
    long sum();
}
