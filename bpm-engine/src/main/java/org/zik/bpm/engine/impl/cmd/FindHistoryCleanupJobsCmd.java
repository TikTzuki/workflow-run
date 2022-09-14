// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.runtime.Job;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class FindHistoryCleanupJobsCmd implements Command<List<Job>>, Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Override
    public List<Job> execute(final CommandContext commandContext) {
        return commandContext.getJobManager().findJobsByHandlerType("history-cleanup");
    }
}
