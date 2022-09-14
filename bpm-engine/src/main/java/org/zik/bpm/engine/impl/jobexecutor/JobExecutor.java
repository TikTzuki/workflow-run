// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import org.zik.bpm.engine.impl.ProcessEngineImpl;
import java.util.List;

public abstract class JobExecutor
{
    private static final JobExecutorLogger LOG;
    protected String name;
    protected List<ProcessEngineImpl> processEngines;
    protected AcquireJobsCommandFactory acquireJobsCmdFactory;
    protected AcquireJobsRunnable acquireJobsRunnable;
    protected RejectedJobsHandler rejectedJobsHandler;
    protected Thread jobAcquisitionThread;
    protected boolean isAutoActivate;
    protected boolean isActive;
    protected int maxJobsPerAcquisition;
    protected int waitTimeInMillis;
    protected float waitIncreaseFactor;
    protected long maxWait;
    protected int backoffTimeInMillis;
    protected long maxBackoff;
    protected int backoffDecreaseThreshold;
    protected String lockOwner;
    protected int lockTimeInMillis;
    
    public JobExecutor() {
        this.name = "JobExecutor[" + this.getClass().getName() + "]";
        this.processEngines = new CopyOnWriteArrayList<ProcessEngineImpl>();
        this.isAutoActivate = false;
        this.isActive = false;
        this.maxJobsPerAcquisition = 3;
        this.waitTimeInMillis = 5000;
        this.waitIncreaseFactor = 2.0f;
        this.maxWait = 60000L;
        this.backoffTimeInMillis = 0;
        this.maxBackoff = 0L;
        this.backoffDecreaseThreshold = 100;
        this.lockOwner = UUID.randomUUID().toString();
        this.lockTimeInMillis = 300000;
    }
    
    public void start() {
        if (this.isActive) {
            return;
        }
        JobExecutor.LOG.startingUpJobExecutor(this.getClass().getName());
        this.ensureInitialization();
        this.startExecutingJobs();
        this.isActive = true;
    }
    
    public synchronized void shutdown() {
        if (!this.isActive) {
            return;
        }
        JobExecutor.LOG.shuttingDownTheJobExecutor(this.getClass().getName());
        this.acquireJobsRunnable.stop();
        this.stopExecutingJobs();
        this.ensureCleanup();
        this.isActive = false;
    }
    
    protected void ensureInitialization() {
        if (this.acquireJobsCmdFactory == null) {
            this.acquireJobsCmdFactory = new DefaultAcquireJobsCommandFactory(this);
        }
        this.acquireJobsRunnable = new SequentialJobAcquisitionRunnable(this);
    }
    
    protected void ensureCleanup() {
        this.acquireJobsCmdFactory = null;
        this.acquireJobsRunnable = null;
    }
    
    public void jobWasAdded() {
        if (this.isActive) {
            this.acquireJobsRunnable.jobWasAdded();
        }
    }
    
    public synchronized void registerProcessEngine(final ProcessEngineImpl processEngine) {
        this.processEngines.add(processEngine);
        if (this.processEngines.size() == 1 && this.isAutoActivate) {
            this.start();
        }
    }
    
    public synchronized void unregisterProcessEngine(final ProcessEngineImpl processEngine) {
        this.processEngines.remove(processEngine);
        if (this.processEngines.isEmpty() && this.isActive) {
            this.shutdown();
        }
    }
    
    protected abstract void startExecutingJobs();
    
    protected abstract void stopExecutingJobs();
    
    public abstract void executeJobs(final List<String> p0, final ProcessEngineImpl p1);
    
    @Deprecated
    public void executeJobs(final List<String> jobIds) {
        if (!this.processEngines.isEmpty()) {
            this.executeJobs(jobIds, this.processEngines.get(0));
        }
    }
    
    public void logAcquisitionAttempt(final ProcessEngineImpl engine) {
        if (engine.getProcessEngineConfiguration().isMetricsEnabled()) {
            engine.getProcessEngineConfiguration().getMetricsRegistry().markOccurrence("job-acquisition-attempt");
        }
    }
    
    public void logAcquiredJobs(final ProcessEngineImpl engine, final int numJobs) {
        if (engine != null && engine.getProcessEngineConfiguration().isMetricsEnabled()) {
            engine.getProcessEngineConfiguration().getMetricsRegistry().markOccurrence("job-acquired-success", numJobs);
        }
    }
    
    public void logAcquisitionFailureJobs(final ProcessEngineImpl engine, final int numJobs) {
        if (engine != null && engine.getProcessEngineConfiguration().isMetricsEnabled()) {
            engine.getProcessEngineConfiguration().getMetricsRegistry().markOccurrence("job-acquired-failure", numJobs);
        }
    }
    
    public void logRejectedExecution(final ProcessEngineImpl engine, final int numJobs) {
        if (engine != null && engine.getProcessEngineConfiguration().isMetricsEnabled()) {
            engine.getProcessEngineConfiguration().getMetricsRegistry().markOccurrence("job-execution-rejected", numJobs);
        }
    }
    
    public List<ProcessEngineImpl> getProcessEngines() {
        return this.processEngines;
    }
    
    public Iterator<ProcessEngineImpl> engineIterator() {
        return this.processEngines.iterator();
    }
    
    public boolean hasRegisteredEngine(final ProcessEngineImpl engine) {
        return this.processEngines.contains(engine);
    }
    
    @Deprecated
    public CommandExecutor getCommandExecutor() {
        if (this.processEngines.isEmpty()) {
            return null;
        }
        return this.processEngines.get(0).getProcessEngineConfiguration().getCommandExecutorTxRequired();
    }
    
    @Deprecated
    public void setCommandExecutor(final CommandExecutor commandExecutorTxRequired) {
    }
    
    public int getWaitTimeInMillis() {
        return this.waitTimeInMillis;
    }
    
    public void setWaitTimeInMillis(final int waitTimeInMillis) {
        this.waitTimeInMillis = waitTimeInMillis;
    }
    
    public int getBackoffTimeInMillis() {
        return this.backoffTimeInMillis;
    }
    
    public void setBackoffTimeInMillis(final int backoffTimeInMillis) {
        this.backoffTimeInMillis = backoffTimeInMillis;
    }
    
    public int getLockTimeInMillis() {
        return this.lockTimeInMillis;
    }
    
    public void setLockTimeInMillis(final int lockTimeInMillis) {
        this.lockTimeInMillis = lockTimeInMillis;
    }
    
    public String getLockOwner() {
        return this.lockOwner;
    }
    
    public void setLockOwner(final String lockOwner) {
        this.lockOwner = lockOwner;
    }
    
    public boolean isAutoActivate() {
        return this.isAutoActivate;
    }
    
    public void setProcessEngines(final List<ProcessEngineImpl> processEngines) {
        this.processEngines = processEngines;
    }
    
    public void setAutoActivate(final boolean isAutoActivate) {
        this.isAutoActivate = isAutoActivate;
    }
    
    public int getMaxJobsPerAcquisition() {
        return this.maxJobsPerAcquisition;
    }
    
    public void setMaxJobsPerAcquisition(final int maxJobsPerAcquisition) {
        this.maxJobsPerAcquisition = maxJobsPerAcquisition;
    }
    
    public float getWaitIncreaseFactor() {
        return this.waitIncreaseFactor;
    }
    
    public void setWaitIncreaseFactor(final float waitIncreaseFactor) {
        this.waitIncreaseFactor = waitIncreaseFactor;
    }
    
    public long getMaxWait() {
        return this.maxWait;
    }
    
    public void setMaxWait(final long maxWait) {
        this.maxWait = maxWait;
    }
    
    public long getMaxBackoff() {
        return this.maxBackoff;
    }
    
    public void setMaxBackoff(final long maxBackoff) {
        this.maxBackoff = maxBackoff;
    }
    
    public int getBackoffDecreaseThreshold() {
        return this.backoffDecreaseThreshold;
    }
    
    public void setBackoffDecreaseThreshold(final int backoffDecreaseThreshold) {
        this.backoffDecreaseThreshold = backoffDecreaseThreshold;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Command<AcquiredJobs> getAcquireJobsCmd(final int numJobs) {
        return this.acquireJobsCmdFactory.getCommand(numJobs);
    }
    
    public AcquireJobsCommandFactory getAcquireJobsCmdFactory() {
        return this.acquireJobsCmdFactory;
    }
    
    public void setAcquireJobsCmdFactory(final AcquireJobsCommandFactory acquireJobsCmdFactory) {
        this.acquireJobsCmdFactory = acquireJobsCmdFactory;
    }
    
    public boolean isActive() {
        return this.isActive;
    }
    
    public RejectedJobsHandler getRejectedJobsHandler() {
        return this.rejectedJobsHandler;
    }
    
    public void setRejectedJobsHandler(final RejectedJobsHandler rejectedJobsHandler) {
        this.rejectedJobsHandler = rejectedJobsHandler;
    }
    
    protected void startJobAcquisitionThread() {
        if (this.jobAcquisitionThread == null) {
            (this.jobAcquisitionThread = new Thread(this.acquireJobsRunnable, this.getName())).start();
        }
    }
    
    protected void stopJobAcquisitionThread() {
        try {
            this.jobAcquisitionThread.join();
        }
        catch (InterruptedException e) {
            JobExecutor.LOG.interruptedWhileShuttingDownjobExecutor(e);
        }
        this.jobAcquisitionThread = null;
    }
    
    public AcquireJobsRunnable getAcquireJobsRunnable() {
        return this.acquireJobsRunnable;
    }
    
    public Runnable getExecuteJobsRunnable(final List<String> jobIds, final ProcessEngineImpl processEngine) {
        return new ExecuteJobsRunnable(jobIds, processEngine);
    }
    
    static {
        LOG = ProcessEngineLogger.JOB_EXECUTOR_LOGGER;
    }
}
