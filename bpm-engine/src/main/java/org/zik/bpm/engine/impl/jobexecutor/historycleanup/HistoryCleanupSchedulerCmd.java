// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor.historycleanup;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.context.Context;
import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import java.util.Date;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Map;
import org.zik.bpm.engine.impl.jobexecutor.JobExecutorLogger;
import org.zik.bpm.engine.impl.interceptor.Command;

public class HistoryCleanupSchedulerCmd implements Command<Void>
{
    protected static final JobExecutorLogger LOG;
    protected boolean isRescheduleNow;
    protected HistoryCleanupJobHandlerConfiguration configuration;
    protected String jobId;
    protected Map<String, Long> reports;
    
    public HistoryCleanupSchedulerCmd(final boolean isRescheduleNow, final Map<String, Long> reports, final HistoryCleanupJobHandlerConfiguration configuration, final String jobId) {
        this.isRescheduleNow = isRescheduleNow;
        this.configuration = configuration;
        this.jobId = jobId;
        this.reports = reports;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        if (this.isMetricsEnabled()) {
            this.reportMetrics(commandContext);
        }
        final JobEntity jobEntity = commandContext.getJobManager().findJobById(this.jobId);
        boolean rescheduled = false;
        if (this.isRescheduleNow) {
            commandContext.getJobManager().reschedule(jobEntity, ClockUtil.getCurrentTime());
            rescheduled = true;
            this.cancelCountEmptyRuns(this.configuration, jobEntity);
        }
        else if (HistoryCleanupHelper.isWithinBatchWindow(ClockUtil.getCurrentTime(), commandContext.getProcessEngineConfiguration())) {
            final Date nextRunDate = this.configuration.getNextRunWithDelay(ClockUtil.getCurrentTime());
            if (HistoryCleanupHelper.isWithinBatchWindow(nextRunDate, commandContext.getProcessEngineConfiguration())) {
                commandContext.getJobManager().reschedule(jobEntity, nextRunDate);
                rescheduled = true;
                this.incrementCountEmptyRuns(this.configuration, jobEntity);
            }
        }
        if (!rescheduled) {
            if (HistoryCleanupHelper.isBatchWindowConfigured(commandContext)) {
                this.rescheduleRegularCall(commandContext, jobEntity);
            }
            else {
                this.suspendJob(jobEntity);
            }
            this.cancelCountEmptyRuns(this.configuration, jobEntity);
        }
        return null;
    }
    
    protected void rescheduleRegularCall(final CommandContext commandContext, final JobEntity jobEntity) {
        final BatchWindow nextBatchWindow = commandContext.getProcessEngineConfiguration().getBatchWindowManager().getNextBatchWindow(ClockUtil.getCurrentTime(), commandContext.getProcessEngineConfiguration());
        if (nextBatchWindow != null) {
            commandContext.getJobManager().reschedule(jobEntity, nextBatchWindow.getStart());
        }
        else {
            HistoryCleanupSchedulerCmd.LOG.warnHistoryCleanupBatchWindowNotFound();
            this.suspendJob(jobEntity);
        }
    }
    
    protected void suspendJob(final JobEntity jobEntity) {
        jobEntity.setSuspensionState(SuspensionState.SUSPENDED.getStateCode());
    }
    
    protected void incrementCountEmptyRuns(final HistoryCleanupJobHandlerConfiguration configuration, final JobEntity jobEntity) {
        configuration.setCountEmptyRuns(configuration.getCountEmptyRuns() + 1);
        jobEntity.setJobHandlerConfiguration(configuration);
    }
    
    protected void cancelCountEmptyRuns(final HistoryCleanupJobHandlerConfiguration configuration, final JobEntity jobEntity) {
        configuration.setCountEmptyRuns(0);
        jobEntity.setJobHandlerConfiguration(configuration);
    }
    
    protected void reportMetrics(final CommandContext commandContext) {
        final ProcessEngineConfigurationImpl engineConfiguration = commandContext.getProcessEngineConfiguration();
        if (engineConfiguration.isHistoryCleanupMetricsEnabled()) {
            for (final Map.Entry<String, Long> report : this.reports.entrySet()) {
                engineConfiguration.getDbMetricsReporter().reportValueAtOnce(report.getKey(), report.getValue());
            }
        }
    }
    
    protected boolean isMetricsEnabled() {
        return Context.getProcessEngineConfiguration().isHistoryCleanupMetricsEnabled();
    }
    
    static {
        LOG = ProcessEngineLogger.JOB_EXECUTOR_LOGGER;
    }
}
