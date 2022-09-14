// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.metrics;

import java.util.concurrent.atomic.AtomicLong;

public class Meter
{
    protected AtomicLong counter;
    protected String name;
    
    public Meter(final String name) {
        this.counter = new AtomicLong(0L);
        this.name = name;
    }
    
    public void mark() {
        this.counter.incrementAndGet();
    }
    
    public void markTimes(final long times) {
        this.counter.addAndGet(times);
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public long getAndClear() {
        return this.counter.getAndSet(0L);
    }
    
    public long get(final boolean clear) {
        return clear ? this.getAndClear() : this.get();
    }
    
    public long get() {
        return this.counter.get();
    }
}
