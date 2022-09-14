// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.jobexecutor.historycleanup.HistoryCleanupJobDeclaration;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.persistence.entity.PropertyManager;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import java.util.ListIterator;
import java.util.Date;
import org.zik.bpm.engine.impl.jobexecutor.historycleanup.HistoryCleanupContext;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobManager;
import org.zik.bpm.engine.impl.context.Context;
import java.util.List;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.persistence.entity.AuthorizationManager;
import org.zik.bpm.engine.impl.jobexecutor.historycleanup.HistoryCleanupHelper;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.jobexecutor.JobDeclaration;
import org.zik.bpm.engine.runtime.Job;
import org.zik.bpm.engine.impl.interceptor.Command;

public class HistoryCleanupCmd implements Command<Job>
{
    private static final CommandLogger LOG;
    public static final JobDeclaration HISTORY_CLEANUP_JOB_DECLARATION;
    public static final int MAX_THREADS_NUMBER = 8;
    private boolean immediatelyDue;
    
    public HistoryCleanupCmd(final boolean immediatelyDue) {
        this.immediatelyDue = immediatelyDue;
    }
    
    @Override
    public Job execute(final CommandContext commandContext) {
        if (!this.isHistoryCleanupEnabled(commandContext)) {
            throw new BadUserRequestException("History cleanup is disabled for this engine");
        }
        final AuthorizationManager authorizationManager = commandContext.getAuthorizationManager();
        final ProcessEngineConfigurationImpl processEngineConfiguration = commandContext.getProcessEngineConfiguration();
        authorizationManager.checkCamundaAdmin();
        if (!this.willBeScheduled()) {
            HistoryCleanupCmd.LOG.debugHistoryCleanupWrongConfiguration();
        }
        List<Job> historyCleanupJobs = this.getHistoryCleanupJobs();
        final int degreeOfParallelism = processEngineConfiguration.getHistoryCleanupDegreeOfParallelism();
        final int[][] minuteChunks = HistoryCleanupHelper.listMinuteChunks(degreeOfParallelism);
        if (this.shouldCreateJobs(historyCleanupJobs)) {
            historyCleanupJobs = this.createJobs(degreeOfParallelism, minuteChunks);
        }
        else if (this.shouldReconfigureJobs(historyCleanupJobs)) {
            historyCleanupJobs = this.reconfigureJobs(historyCleanupJobs, degreeOfParallelism, minuteChunks);
        }
        else if (this.shouldSuspendJobs(historyCleanupJobs)) {
            this.suspendJobs(historyCleanupJobs);
        }
        this.writeUserOperationLog(commandContext);
        return (historyCleanupJobs.size() > 0) ? historyCleanupJobs.get(0) : null;
    }
    
    protected List<Job> getHistoryCleanupJobs() {
        final CommandContext commandContext = Context.getCommandContext();
        return commandContext.getJobManager().findJobsByHandlerType("history-cleanup");
    }
    
    protected boolean shouldCreateJobs(final List<Job> jobs) {
        return jobs.isEmpty() && this.willBeScheduled();
    }
    
    protected boolean shouldReconfigureJobs(final List<Job> jobs) {
        return !jobs.isEmpty() && this.willBeScheduled();
    }
    
    protected boolean shouldSuspendJobs(final List<Job> jobs) {
        return !jobs.isEmpty() && !this.willBeScheduled();
    }
    
    protected boolean willBeScheduled() {
        final CommandContext commandContext = Context.getCommandContext();
        return this.immediatelyDue || HistoryCleanupHelper.isBatchWindowConfigured(commandContext);
    }
    
    protected List<Job> createJobs(final int degreeOfParallelism, final int[][] minuteChunks) {
        final CommandContext commandContext = Context.getCommandContext();
        final JobManager jobManager = commandContext.getJobManager();
        this.acquireExclusiveLock(commandContext);
        final List<Job> historyCleanupJobs = this.getHistoryCleanupJobs();
        if (historyCleanupJobs.isEmpty()) {
            for (final int[] minuteChunk : minuteChunks) {
                final JobEntity job = this.createJob(minuteChunk);
                jobManager.insertAndHintJobExecutor(job);
                historyCleanupJobs.add(job);
            }
        }
        return historyCleanupJobs;
    }
    
    protected List<Job> reconfigureJobs(final List<Job> historyCleanupJobs, final int degreeOfParallelism, final int[][] minuteChunks) {
        final CommandContext commandContext = Context.getCommandContext();
        final JobManager jobManager = commandContext.getJobManager();
        final int size = Math.min(degreeOfParallelism, historyCleanupJobs.size());
        for (int i = 0; i < size; ++i) {
            final JobEntity historyCleanupJob = historyCleanupJobs.get(i);
            final HistoryCleanupContext historyCleanupContext = this.createCleanupContext(minuteChunks[i]);
            HistoryCleanupCmd.HISTORY_CLEANUP_JOB_DECLARATION.reconfigure(historyCleanupContext, historyCleanupJob);
            final Date newDueDate = HistoryCleanupCmd.HISTORY_CLEANUP_JOB_DECLARATION.resolveDueDate(historyCleanupContext);
            jobManager.reschedule(historyCleanupJob, newDueDate);
        }
        final int delta = degreeOfParallelism - historyCleanupJobs.size();
        if (delta > 0) {
            for (int j = size; j < degreeOfParallelism; ++j) {
                final JobEntity job = this.createJob(minuteChunks[j]);
                jobManager.insertAndHintJobExecutor(job);
                historyCleanupJobs.add(job);
            }
        }
        else if (delta < 0) {
            final ListIterator<Job> iterator = historyCleanupJobs.listIterator(size);
            while (iterator.hasNext()) {
                final JobEntity job = iterator.next();
                jobManager.deleteJob(job);
                iterator.remove();
            }
        }
        return historyCleanupJobs;
    }
    
    protected void suspendJobs(final List<Job> jobs) {
        for (final Job job : jobs) {
            final JobEntity jobInstance = (JobEntity)job;
            jobInstance.setSuspensionState(SuspensionState.SUSPENDED.getStateCode());
            jobInstance.setDuedate(null);
        }
    }
    
    protected JobEntity createJob(final int[] minuteChunk) {
        final HistoryCleanupContext historyCleanupContext = this.createCleanupContext(minuteChunk);
        return HistoryCleanupCmd.HISTORY_CLEANUP_JOB_DECLARATION.createJobInstance(historyCleanupContext);
    }
    
    protected HistoryCleanupContext createCleanupContext(final int[] minuteChunk) {
        final int minuteFrom = minuteChunk[0];
        final int minuteTo = minuteChunk[1];
        return new HistoryCleanupContext(this.immediatelyDue, minuteFrom, minuteTo);
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext) {
        final PropertyChange propertyChange = new PropertyChange("immediatelyDue", null, this.immediatelyDue);
        commandContext.getOperationLogManager().logJobOperation("CreateHistoryCleanupJobs", null, null, null, null, null, propertyChange);
    }
    
    protected boolean isHistoryCleanupEnabled(final CommandContext commandContext) {
        return commandContext.getProcessEngineConfiguration().isHistoryCleanupEnabled();
    }
    
    protected void acquireExclusiveLock(final CommandContext commandContext) {
        final PropertyManager propertyManager = commandContext.getPropertyManager();
        propertyManager.acquireExclusiveLockForHistoryCleanupJob();
    }
    
    @Override
    public boolean isRetryable() {
        return true;
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
        HISTORY_CLEANUP_JOB_DECLARATION = new HistoryCleanupJobDeclaration();
    }
}
