// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.task.Event;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetTaskEventsCmd implements Command<List<Event>>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String taskId;
    
    public GetTaskEventsCmd(final String taskId) {
        this.taskId = taskId;
    }
    
    @Override
    public List<Event> execute(final CommandContext commandContext) {
        return commandContext.getCommentManager().findEventsByTaskId(this.taskId);
    }
}
