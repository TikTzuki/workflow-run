// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Iterator;
import org.zik.bpm.engine.impl.jobexecutor.JobExecutorContext;
import org.zik.bpm.engine.IdentityService;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.interceptor.CommandContextListener;
import java.util.List;
import java.util.Collections;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.jobexecutor.JobFailureCollector;
import org.zik.bpm.engine.impl.jobexecutor.JobExecutorLogger;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class ExecuteJobsCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    private static final JobExecutorLogger LOG;
    protected String jobId;
    protected JobFailureCollector jobFailureCollector;
    
    public ExecuteJobsCmd(final String jobId, final JobFailureCollector jobFailureCollector) {
        this.jobId = jobId;
        this.jobFailureCollector = jobFailureCollector;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("jobId", (Object)this.jobId);
        final JobEntity job = commandContext.getDbEntityManager().selectById(JobEntity.class, this.jobId);
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final IdentityService identityService = processEngineConfiguration.getIdentityService();
        final JobExecutorContext jobExecutorContext = Context.getJobExecutorContext();
        if (job != null) {
            this.jobFailureCollector.setJob(job);
            if (jobExecutorContext == null) {
                for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
                    checker.checkUpdateJob(job);
                }
                commandContext.getOperationLogManager().logJobOperation("Execute", this.jobId, job.getJobDefinitionId(), job.getProcessInstanceId(), job.getProcessDefinitionId(), job.getProcessDefinitionKey(), PropertyChange.EMPTY_CHANGE);
            }
            else {
                jobExecutorContext.setCurrentJob(job);
                final String tenantId = job.getTenantId();
                if (tenantId != null) {
                    identityService.setAuthentication(null, null, Collections.singletonList(tenantId));
                }
            }
            try {
                commandContext.registerCommandContextListener(this.jobFailureCollector);
                commandContext.setCurrentJob(job);
                job.execute(commandContext);
            }
            catch (Throwable t) {
                final String failedActivityId = Context.getCommandInvocationContext().getProcessDataContext().getLatestActivityId();
                this.jobFailureCollector.setFailedActivityId(failedActivityId);
                throw t;
            }
            finally {
                if (jobExecutorContext != null) {
                    jobExecutorContext.setCurrentJob(null);
                    identityService.clearAuthentication();
                }
            }
            return null;
        }
        if (jobExecutorContext != null) {
            ExecuteJobsCmd.LOG.debugAcquiredJobNotFound(this.jobId);
            return null;
        }
        throw ExecuteJobsCmd.LOG.jobNotFoundException(this.jobId);
    }
    
    static {
        LOG = ProcessEngineLogger.JOB_EXECUTOR_LOGGER;
    }
}
