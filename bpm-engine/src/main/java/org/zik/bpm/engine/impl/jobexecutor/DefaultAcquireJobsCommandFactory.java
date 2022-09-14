// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.cmd.AcquireJobsCmd;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DefaultAcquireJobsCommandFactory implements AcquireJobsCommandFactory
{
    protected JobExecutor jobExecutor;
    
    public DefaultAcquireJobsCommandFactory(final JobExecutor jobExecutor) {
        this.jobExecutor = jobExecutor;
    }
    
    @Override
    public Command<AcquiredJobs> getCommand(final int numJobsToAcquire) {
        return new AcquireJobsCmd(this.jobExecutor, numJobsToAcquire);
    }
}
