// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class SingleConsumerCondition
{
    private final Thread consumer;
    
    public SingleConsumerCondition(final Thread consumer) {
        if (consumer == null) {
            throw new IllegalArgumentException("Consumer thread cannot be null");
        }
        this.consumer = consumer;
    }
    
    public void signal() {
        LockSupport.unpark(this.consumer);
    }
    
    public void await(final long millis) {
        if (Thread.currentThread() != this.consumer) {
            throw new RuntimeException("Wrong usage of SingleConsumerCondition: can only await in consumer thread.");
        }
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(millis));
    }
}
