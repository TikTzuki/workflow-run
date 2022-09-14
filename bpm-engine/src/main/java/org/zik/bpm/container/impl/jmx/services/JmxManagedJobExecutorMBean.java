// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.jmx.services;

public interface JmxManagedJobExecutorMBean
{
    String getName();
    
    void setMaxJobsPerAcquisition(final int p0);
    
    int getMaxJobsPerAcquisition();
    
    void setLockOwner(final String p0);
    
    String getLockOwner();
    
    void setLockTimeInMillis(final int p0);
    
    int getLockTimeInMillis();
    
    void setWaitTimeInMillis(final int p0);
    
    int getWaitTimeInMillis();
    
    void shutdown();
    
    void start();
    
    boolean isActive();
}
