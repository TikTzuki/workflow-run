// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.jmx.services;

import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import org.zik.bpm.engine.impl.jobexecutor.JobExecutor;
import org.zik.bpm.container.impl.spi.PlatformService;

public class JmxManagedJobExecutor implements PlatformService<JobExecutor>, JmxManagedJobExecutorMBean
{
    protected final JobExecutor jobExecutor;
    
    public JmxManagedJobExecutor(final JobExecutor jobExecutor) {
        this.jobExecutor = jobExecutor;
    }
    
    @Override
    public void start(final PlatformServiceContainer mBeanServiceContainer) {
    }
    
    @Override
    public void stop(final PlatformServiceContainer mBeanServiceContainer) {
        this.shutdown();
    }
    
    @Override
    public void start() {
        this.jobExecutor.start();
    }
    
    @Override
    public void shutdown() {
        this.jobExecutor.shutdown();
    }
    
    @Override
    public int getWaitTimeInMillis() {
        return this.jobExecutor.getWaitTimeInMillis();
    }
    
    @Override
    public void setWaitTimeInMillis(final int waitTimeInMillis) {
        this.jobExecutor.setWaitTimeInMillis(waitTimeInMillis);
    }
    
    @Override
    public int getLockTimeInMillis() {
        return this.jobExecutor.getLockTimeInMillis();
    }
    
    @Override
    public void setLockTimeInMillis(final int lockTimeInMillis) {
        this.jobExecutor.setLockTimeInMillis(lockTimeInMillis);
    }
    
    @Override
    public String getLockOwner() {
        return this.jobExecutor.getLockOwner();
    }
    
    @Override
    public void setLockOwner(final String lockOwner) {
        this.jobExecutor.setLockOwner(lockOwner);
    }
    
    @Override
    public int getMaxJobsPerAcquisition() {
        return this.jobExecutor.getMaxJobsPerAcquisition();
    }
    
    @Override
    public void setMaxJobsPerAcquisition(final int maxJobsPerAcquisition) {
        this.jobExecutor.setMaxJobsPerAcquisition(maxJobsPerAcquisition);
    }
    
    @Override
    public String getName() {
        return this.jobExecutor.getName();
    }
    
    @Override
    public JobExecutor getValue() {
        return this.jobExecutor;
    }
    
    @Override
    public boolean isActive() {
        return this.jobExecutor.isActive();
    }
}
