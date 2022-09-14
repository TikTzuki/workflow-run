// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.ArrayList;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteJobsCmd implements Command<Void>
{
    private static final long serialVersionUID = 1L;
    protected List<String> jobIds;
    protected boolean cascade;
    
    public DeleteJobsCmd(final List<String> jobIds) {
        this(jobIds, false);
    }
    
    public DeleteJobsCmd(final List<String> jobIds, final boolean cascade) {
        this.jobIds = jobIds;
        this.cascade = cascade;
    }
    
    public DeleteJobsCmd(final String jobId) {
        this(jobId, false);
    }
    
    public DeleteJobsCmd(final String jobId, final boolean cascade) {
        (this.jobIds = new ArrayList<String>()).add(jobId);
        this.cascade = cascade;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        JobEntity jobToDelete = null;
        for (final String jobId : this.jobIds) {
            jobToDelete = Context.getCommandContext().getJobManager().findJobById(jobId);
            if (jobToDelete != null) {
                jobToDelete.delete();
                if (!this.cascade) {
                    continue;
                }
                commandContext.getHistoricJobLogManager().deleteHistoricJobLogByJobId(jobId);
            }
        }
        return null;
    }
}
