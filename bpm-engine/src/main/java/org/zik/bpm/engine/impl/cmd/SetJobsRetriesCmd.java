// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Collection;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.util.List;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SetJobsRetriesCmd extends AbstractSetJobRetriesCmd implements Command<Void>, Serializable
{
    protected final List<String> jobIds;
    protected final int retries;
    
    public SetJobsRetriesCmd(final List<String> jobIds, final int retries) {
        EnsureUtil.ensureNotEmpty("Job ID's", jobIds);
        EnsureUtil.ensureGreaterThanOrEqual("Retries count", retries, 0L);
        this.jobIds = jobIds;
        this.retries = retries;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        for (final String id : this.jobIds) {
            this.setJobRetriesByJobId(id, this.retries, commandContext);
        }
        return null;
    }
}
