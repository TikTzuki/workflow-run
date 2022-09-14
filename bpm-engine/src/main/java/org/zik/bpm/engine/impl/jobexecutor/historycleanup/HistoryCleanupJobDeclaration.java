// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor.historycleanup;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ConstantValueProvider;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ParameterValueProvider;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.context.Context;
import java.util.Date;
import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.cmd.CommandLogger;
import org.zik.bpm.engine.impl.persistence.entity.EverLivingJobEntity;
import org.zik.bpm.engine.impl.jobexecutor.JobDeclaration;

public class HistoryCleanupJobDeclaration extends JobDeclaration<HistoryCleanupContext, EverLivingJobEntity>
{
    private static final CommandLogger LOG;
    
    public HistoryCleanupJobDeclaration() {
        super("history-cleanup");
    }
    
    @Override
    protected ExecutionEntity resolveExecution(final HistoryCleanupContext context) {
        return null;
    }
    
    @Override
    protected EverLivingJobEntity newJobInstance(final HistoryCleanupContext context) {
        return new EverLivingJobEntity();
    }
    
    @Override
    protected void postInitialize(final HistoryCleanupContext context, final EverLivingJobEntity job) {
    }
    
    @Override
    public EverLivingJobEntity reconfigure(final HistoryCleanupContext context, final EverLivingJobEntity job) {
        final HistoryCleanupJobHandlerConfiguration configuration = this.resolveJobHandlerConfiguration(context);
        job.setJobHandlerConfiguration(configuration);
        return job;
    }
    
    @Override
    protected HistoryCleanupJobHandlerConfiguration resolveJobHandlerConfiguration(final HistoryCleanupContext context) {
        final HistoryCleanupJobHandlerConfiguration config = new HistoryCleanupJobHandlerConfiguration();
        config.setImmediatelyDue(context.isImmediatelyDue());
        config.setMinuteFrom(context.getMinuteFrom());
        config.setMinuteTo(context.getMinuteTo());
        return config;
    }
    
    @Override
    public Date resolveDueDate(final HistoryCleanupContext context) {
        return this.resolveDueDate(context.isImmediatelyDue());
    }
    
    private Date resolveDueDate(final boolean isImmediatelyDue) {
        final CommandContext commandContext = Context.getCommandContext();
        if (isImmediatelyDue) {
            return ClockUtil.getCurrentTime();
        }
        final BatchWindow currentOrNextBatchWindow = commandContext.getProcessEngineConfiguration().getBatchWindowManager().getCurrentOrNextBatchWindow(ClockUtil.getCurrentTime(), commandContext.getProcessEngineConfiguration());
        if (currentOrNextBatchWindow != null) {
            return currentOrNextBatchWindow.getStart();
        }
        return null;
    }
    
    @Override
    public ParameterValueProvider getJobPriorityProvider() {
        final long historyCleanupJobPriority = Context.getProcessEngineConfiguration().getHistoryCleanupJobPriority();
        return new ConstantValueProvider(historyCleanupJobPriority);
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
