// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.telemetry.dto;

import org.zik.bpm.engine.telemetry.Metric;

public class MetricImpl implements Metric
{
    protected long count;
    
    public MetricImpl(final long count) {
        this.count = count;
    }
    
    @Override
    public long getCount() {
        return this.count;
    }
    
    public void setCount(final long count) {
        this.count = count;
    }
}
