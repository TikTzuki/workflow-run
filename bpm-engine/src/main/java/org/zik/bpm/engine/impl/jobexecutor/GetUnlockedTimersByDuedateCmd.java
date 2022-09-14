// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.Page;
import java.util.Date;
import org.zik.bpm.engine.impl.persistence.entity.TimerEntity;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetUnlockedTimersByDuedateCmd implements Command<List<TimerEntity>>
{
    protected Date duedate;
    protected Page page;
    
    public GetUnlockedTimersByDuedateCmd(final Date duedate, final Page page) {
        this.duedate = duedate;
        this.page = page;
    }
    
    @Override
    public List<TimerEntity> execute(final CommandContext commandContext) {
        return Context.getCommandContext().getJobManager().findUnlockedTimersByDuedate(this.duedate, this.page);
    }
}
