// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.OptimisticLockingException;
import org.zik.bpm.engine.impl.interceptor.ProcessDataContext;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.ExecuteJobsCmd;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;

public class ExecuteJobHelper
{
    private static final JobExecutorLogger LOG;
    public static ExceptionLoggingHandler LOGGING_HANDLER;
    
    public static void executeJob(final String jobId, final CommandExecutor commandExecutor) {
        final JobFailureCollector jobFailureCollector = new JobFailureCollector(jobId);
        executeJob(jobId, commandExecutor, jobFailureCollector, new ExecuteJobsCmd(jobId, jobFailureCollector));
    }
    
    public static void executeJob(final String nextJobId, final CommandExecutor commandExecutor, final JobFailureCollector jobFailureCollector, final Command<Void> cmd) {
        executeJob(nextJobId, commandExecutor, jobFailureCollector, cmd, null);
    }
    
    public static void executeJob(final String nextJobId, final CommandExecutor commandExecutor, final JobFailureCollector jobFailureCollector, final Command<Void> cmd, final ProcessEngineConfigurationImpl configuration) {
        try {
            commandExecutor.execute(cmd);
        }
        catch (RuntimeException exception) {
            handleJobFailure(nextJobId, jobFailureCollector, exception);
            throw exception;
        }
        catch (Throwable exception2) {
            handleJobFailure(nextJobId, jobFailureCollector, exception2);
            throw ExecuteJobHelper.LOG.wrapJobExecutionFailure(jobFailureCollector, exception2);
        }
        finally {
            ProcessDataContext processDataContext = null;
            if (configuration != null) {
                processDataContext = new ProcessDataContext(configuration, true);
                processDataContext.clearMdc();
            }
            invokeJobListener(commandExecutor, jobFailureCollector);
            if (processDataContext != null) {
                processDataContext.updateMdcFromCurrentValues();
            }
        }
    }
    
    protected static void invokeJobListener(final CommandExecutor commandExecutor, final JobFailureCollector jobFailureCollector) {
        if (jobFailureCollector.getJobId() != null) {
            if (jobFailureCollector.getFailure() != null) {
                final FailedJobListener failedJobListener = createFailedJobListener(commandExecutor, jobFailureCollector);
                final OptimisticLockingException exception = callFailedJobListenerWithRetries(commandExecutor, failedJobListener);
                if (exception != null) {
                    throw exception;
                }
            }
            else {
                final SuccessfulJobListener successListener = createSuccessfulJobListener(commandExecutor);
                commandExecutor.execute((Command<Object>)successListener);
            }
        }
    }
    
    private static OptimisticLockingException callFailedJobListenerWithRetries(final CommandExecutor commandExecutor, final FailedJobListener failedJobListener) {
        try {
            commandExecutor.execute((Command<Object>)failedJobListener);
            return null;
        }
        catch (OptimisticLockingException ex) {
            failedJobListener.incrementCountRetries();
            if (failedJobListener.getRetriesLeft() > 0) {
                return callFailedJobListenerWithRetries(commandExecutor, failedJobListener);
            }
            return ex;
        }
    }
    
    protected static void handleJobFailure(final String nextJobId, final JobFailureCollector jobFailureCollector, final Throwable exception) {
        jobFailureCollector.setFailure(exception);
    }
    
    protected static FailedJobListener createFailedJobListener(final CommandExecutor commandExecutor, final JobFailureCollector jobFailureCollector) {
        return new FailedJobListener(commandExecutor, jobFailureCollector);
    }
    
    protected static SuccessfulJobListener createSuccessfulJobListener(final CommandExecutor commandExecutor) {
        return new SuccessfulJobListener();
    }
    
    static {
        LOG = ProcessEngineLogger.JOB_EXECUTOR_LOGGER;
        ExecuteJobHelper.LOGGING_HANDLER = new ExceptionLoggingHandler() {
            @Override
            public void exceptionWhileExecutingJob(final String jobId, final Throwable exception) {
                ExecuteJobHelper.LOG.exceptionWhileExecutingJob(jobId, exception);
            }
        };
    }
    
    public interface ExceptionLoggingHandler
    {
        void exceptionWhileExecutingJob(final String p0, final Throwable p1);
    }
}
