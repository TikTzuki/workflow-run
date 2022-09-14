// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import java.util.Date;
import org.zik.bpm.engine.impl.persistence.entity.TimerEntity;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.cfg.TransactionListener;

public class RepeatingFailedJobListener implements TransactionListener
{
    protected CommandExecutor commandExecutor;
    protected String jobId;
    
    public RepeatingFailedJobListener(final CommandExecutor commandExecutor, final String jobId) {
        this.commandExecutor = commandExecutor;
        this.jobId = jobId;
    }
    
    @Override
    public void execute(final CommandContext commandContext) {
        final CreateNewTimerJobCommand cmd = new CreateNewTimerJobCommand(this.jobId);
        this.commandExecutor.execute((Command<Object>)cmd);
    }
    
    protected class CreateNewTimerJobCommand implements Command<Void>
    {
        protected String jobId;
        
        public CreateNewTimerJobCommand(final String jobId) {
            this.jobId = jobId;
        }
        
        @Override
        public Void execute(final CommandContext commandContext) {
            final TimerEntity failedJob = (TimerEntity)commandContext.getJobManager().findJobById(this.jobId);
            final Date newDueDate = failedJob.calculateRepeat();
            if (newDueDate != null) {
                failedJob.createNewTimerJob(newDueDate);
                final TimerEventJobHandler.TimerJobConfiguration config = (TimerEventJobHandler.TimerJobConfiguration)failedJob.getJobHandlerConfiguration();
                config.setFollowUpJobCreated(true);
                failedJob.setJobHandlerConfiguration(config);
            }
            return null;
        }
    }
}
