// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionManager;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.ProcessEngineException;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SetJobRetriesCmd extends AbstractSetJobRetriesCmd implements Command<Void>, Serializable
{
    protected static final long serialVersionUID = 1L;
    protected final String jobId;
    protected final String jobDefinitionId;
    protected final int retries;
    
    public SetJobRetriesCmd(final String jobId, final String jobDefinitionId, final int retries) {
        if ((jobId == null || jobId.isEmpty()) && (jobDefinitionId == null || jobDefinitionId.isEmpty())) {
            throw new ProcessEngineException("Either job definition id or job id has to be provided as parameter.");
        }
        if (retries < 0) {
            throw new ProcessEngineException("The number of job retries must be a non-negative Integer, but '" + retries + "' has been provided.");
        }
        this.jobId = jobId;
        this.jobDefinitionId = jobDefinitionId;
        this.retries = retries;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        if (this.jobId != null) {
            this.setJobRetriesByJobId(this.jobId, this.retries, commandContext);
        }
        else {
            this.setJobRetriesByJobDefinitionId(commandContext);
        }
        return null;
    }
    
    protected void setJobRetriesByJobDefinitionId(final CommandContext commandContext) {
        final JobDefinitionManager jobDefinitionManager = commandContext.getJobDefinitionManager();
        final JobDefinitionEntity jobDefinition = jobDefinitionManager.findById(this.jobDefinitionId);
        if (jobDefinition != null) {
            final String processDefinitionId = jobDefinition.getProcessDefinitionId();
            for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
                checker.checkUpdateRetriesProcessInstanceByProcessDefinitionId(processDefinitionId);
            }
        }
        commandContext.getJobManager().updateFailedJobRetriesByJobDefinitionId(this.jobDefinitionId, this.retries);
        final PropertyChange propertyChange = new PropertyChange("retries", null, this.retries);
        commandContext.getOperationLogManager().logJobOperation(this.getLogEntryOperation(), null, this.jobDefinitionId, null, null, null, propertyChange);
    }
}
