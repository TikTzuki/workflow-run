// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.metrics;

import java.util.HashMap;
import java.util.Map;

public class MetricsRegistry
{
    protected Map<String, Meter> dbMeters;
    protected Map<String, Meter> telemetryMeters;
    
    public MetricsRegistry() {
        this.dbMeters = new HashMap<String, Meter>();
        this.telemetryMeters = new HashMap<String, Meter>();
    }
    
    public Meter getDbMeterByName(final String name) {
        return this.dbMeters.get(name);
    }
    
    public Map<String, Meter> getDbMeters() {
        return this.dbMeters;
    }
    
    public Map<String, Meter> getTelemetryMeters() {
        return this.telemetryMeters;
    }
    
    public void clearTelemetryMetrics() {
        this.telemetryMeters.values().forEach(Meter::getAndClear);
    }
    
    public void markOccurrence(final String name) {
        this.markOccurrence(name, 1L);
    }
    
    public void markOccurrence(final String name, final long times) {
        this.markOccurrence(this.dbMeters, name, times);
        this.markOccurrence(this.telemetryMeters, name, times);
    }
    
    public void markTelemetryOccurrence(final String name, final long times) {
        this.markOccurrence(this.telemetryMeters, name, times);
    }
    
    protected void markOccurrence(final Map<String, Meter> meters, final String name, final long times) {
        final Meter meter = meters.get(name);
        if (meter != null) {
            meter.markTimes(times);
        }
    }
    
    public void createMeter(final String name) {
        final Meter dbMeter = new Meter(name);
        this.dbMeters.put(name, dbMeter);
        final Meter telemetryMeter = new Meter(name);
        this.telemetryMeters.put(name, telemetryMeter);
    }
    
    public void createDbMeter(final String name) {
        final Meter dbMeter = new Meter(name);
        this.dbMeters.put(name, dbMeter);
    }
}
