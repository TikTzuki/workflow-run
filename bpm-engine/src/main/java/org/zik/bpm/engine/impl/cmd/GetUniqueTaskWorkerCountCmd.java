// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Date;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetUniqueTaskWorkerCountCmd implements Command<Long>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected Date startTime;
    protected Date endTime;
    
    public GetUniqueTaskWorkerCountCmd(final Date startTime, final Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    @Override
    public Long execute(final CommandContext commandContext) {
        return commandContext.getMeterLogManager().findUniqueTaskWorkerCount(this.startTime, this.endTime);
    }
}
