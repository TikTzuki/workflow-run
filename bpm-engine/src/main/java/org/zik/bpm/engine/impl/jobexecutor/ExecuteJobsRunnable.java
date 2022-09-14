// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.cmd.UnlockJobCmd;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.ExecuteJobsCmd;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.util.ClassLoaderUtil;
import org.zik.bpm.engine.impl.interceptor.ProcessDataContext;
import org.zik.bpm.engine.ProcessEngineConfiguration;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.context.Context;
import java.util.Collection;
import org.zik.bpm.engine.impl.ProcessEngineImpl;
import java.util.List;

public class ExecuteJobsRunnable implements Runnable
{
    private static final JobExecutorLogger LOG;
    protected final List<String> jobIds;
    protected JobExecutor jobExecutor;
    protected ProcessEngineImpl processEngine;
    
    public ExecuteJobsRunnable(final List<String> jobIds, final ProcessEngineImpl processEngine) {
        this.jobIds = jobIds;
        this.processEngine = processEngine;
        this.jobExecutor = processEngine.getProcessEngineConfiguration().getJobExecutor();
    }
    
    @Override
    public void run() {
        final JobExecutorContext jobExecutorContext = new JobExecutorContext();
        final List<String> currentProcessorJobQueue = jobExecutorContext.getCurrentProcessorJobQueue();
        final ProcessEngineConfigurationImpl engineConfiguration = this.processEngine.getProcessEngineConfiguration();
        final CommandExecutor commandExecutor = engineConfiguration.getCommandExecutorTxRequired();
        currentProcessorJobQueue.addAll(this.jobIds);
        Context.setJobExecutorContext(jobExecutorContext);
        final ClassLoader classLoaderBeforeExecution = this.switchClassLoader();
        try {
            while (!currentProcessorJobQueue.isEmpty()) {
                final String nextJobId = currentProcessorJobQueue.remove(0);
                if (this.jobExecutor.isActive()) {
                    final JobFailureCollector jobFailureCollector = new JobFailureCollector(nextJobId);
                    try {
                        this.executeJob(nextJobId, commandExecutor, jobFailureCollector);
                    }
                    catch (Throwable t) {
                        if (!ProcessEngineLogger.shouldLogJobException(engineConfiguration, jobFailureCollector.getJob())) {
                            continue;
                        }
                        ExecuteJobHelper.LOGGING_HANDLER.exceptionWhileExecutingJob(nextJobId, t);
                    }
                    finally {
                        new ProcessDataContext(engineConfiguration).clearMdc();
                    }
                }
                else {
                    try {
                        this.unlockJob(nextJobId, commandExecutor);
                    }
                    catch (Throwable t2) {
                        ExecuteJobsRunnable.LOG.exceptionWhileUnlockingJob(nextJobId, t2);
                    }
                }
            }
            this.jobExecutor.jobWasAdded();
        }
        finally {
            Context.removeJobExecutorContext();
            ClassLoaderUtil.setContextClassloader(classLoaderBeforeExecution);
        }
    }
    
    protected void executeJob(final String nextJobId, final CommandExecutor commandExecutor, final JobFailureCollector jobFailureCollector) {
        ExecuteJobHelper.executeJob(nextJobId, commandExecutor, jobFailureCollector, new ExecuteJobsCmd(nextJobId, jobFailureCollector), this.processEngine.getProcessEngineConfiguration());
    }
    
    protected void unlockJob(final String nextJobId, final CommandExecutor commandExecutor) {
        commandExecutor.execute((Command<Object>)new UnlockJobCmd(nextJobId));
    }
    
    protected ClassLoader switchClassLoader() {
        return ClassLoaderUtil.switchToProcessEngineClassloader();
    }
    
    static {
        LOG = ProcessEngineLogger.JOB_EXECUTOR_LOGGER;
    }
}
