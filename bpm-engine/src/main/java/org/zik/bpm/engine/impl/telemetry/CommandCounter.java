// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.telemetry;

import java.util.concurrent.atomic.AtomicLong;

public class CommandCounter
{
    protected String name;
    protected AtomicLong count;
    
    public CommandCounter(final String name) {
        this.count = new AtomicLong(0L);
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void mark() {
        this.count.incrementAndGet();
    }
    
    public void mark(final long times) {
        this.count.addAndGet(times);
    }
    
    public long getAndClear() {
        return this.count.getAndSet(0L);
    }
    
    public long get(final boolean clear) {
        return clear ? this.getAndClear() : this.get();
    }
    
    public long get() {
        return this.count.get();
    }
}
