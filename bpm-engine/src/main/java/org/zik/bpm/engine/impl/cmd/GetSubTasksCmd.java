// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.query.Query;
import org.zik.bpm.engine.impl.TaskQueryImpl;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.task.Task;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetSubTasksCmd implements Command<List<Task>>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String parentTaskId;
    
    public GetSubTasksCmd(final String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }
    
    @Override
    public List<Task> execute(final CommandContext commandContext) {
        return ((Query<T, Task>)new TaskQueryImpl().taskParentTaskId(this.parentTaskId)).list();
    }
}
