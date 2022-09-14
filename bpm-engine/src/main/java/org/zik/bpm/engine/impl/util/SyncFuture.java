// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SyncFuture<V> implements Future<V>
{
    private V result;
    private Throwable e;
    
    public SyncFuture(final V result) {
        this.result = result;
    }
    
    public SyncFuture(final Throwable e) {
        this.e = e;
    }
    
    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return false;
    }
    
    @Override
    public boolean isCancelled() {
        return false;
    }
    
    @Override
    public boolean isDone() {
        return true;
    }
    
    @Override
    public V get() throws InterruptedException, ExecutionException {
        if (this.e == null) {
            return this.result;
        }
        throw new ExecutionException(this.e);
    }
    
    @Override
    public V get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.get();
    }
}
