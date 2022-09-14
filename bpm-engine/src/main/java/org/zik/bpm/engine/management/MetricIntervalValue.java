// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.management;

import java.util.Date;

public interface MetricIntervalValue
{
    String getName();
    
    String getReporter();
    
    Date getTimestamp();
    
    long getValue();
}
