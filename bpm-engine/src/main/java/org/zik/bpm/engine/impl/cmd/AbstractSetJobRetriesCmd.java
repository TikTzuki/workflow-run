// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.interceptor.CommandContext;

public class AbstractSetJobRetriesCmd
{
    protected static final String RETRIES = "retries";
    
    protected void setJobRetriesByJobId(final String jobId, final int retries, final CommandContext commandContext) {
        final JobEntity job = commandContext.getJobManager().findJobById(jobId);
        if (job != null) {
            for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
                checker.checkUpdateRetriesJob(job);
            }
            if (job.isInInconsistentLockState()) {
                job.resetLock();
            }
            final int oldRetries = job.getRetries();
            job.setRetries(retries);
            final PropertyChange propertyChange = new PropertyChange("retries", oldRetries, job.getRetries());
            commandContext.getOperationLogManager().logJobOperation(this.getLogEntryOperation(), job.getId(), job.getJobDefinitionId(), job.getProcessInstanceId(), job.getProcessDefinitionId(), job.getProcessDefinitionKey(), propertyChange);
            return;
        }
        throw new ProcessEngineException("No job found with id '" + jobId + "'.");
    }
    
    protected String getLogEntryOperation() {
        return "SetJobRetries";
    }
}
