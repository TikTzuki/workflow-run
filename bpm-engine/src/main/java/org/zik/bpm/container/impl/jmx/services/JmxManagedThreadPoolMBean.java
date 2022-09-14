// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.jmx.services;

public interface JmxManagedThreadPoolMBean
{
    int getQueueCount();
    
    long getCompletedTaskCount();
    
    long getTaskCount();
    
    int getLargestPoolSize();
    
    int getActiveCount();
    
    int getPoolSize();
    
    void purgeThreadPool();
    
    int getMaximumPoolSize();
    
    void setMaximumPoolSize(final int p0);
    
    void setCorePoolSize(final int p0);
}
