// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetTaskVariableCmd implements Command<Object>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String taskId;
    protected String variableName;
    protected boolean isLocal;
    
    public GetTaskVariableCmd(final String taskId, final String variableName, final boolean isLocal) {
        this.taskId = taskId;
        this.variableName = variableName;
        this.isLocal = isLocal;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("taskId", (Object)this.taskId);
        EnsureUtil.ensureNotNull("variableName", (Object)this.variableName);
        final TaskEntity task = Context.getCommandContext().getTaskManager().findTaskById(this.taskId);
        EnsureUtil.ensureNotNull("task " + this.taskId + " doesn't exist", "task", task);
        this.checkGetTaskVariable(task, commandContext);
        Object value;
        if (this.isLocal) {
            value = task.getVariableLocal(this.variableName);
        }
        else {
            value = task.getVariable(this.variableName);
        }
        return value;
    }
    
    protected void checkGetTaskVariable(final TaskEntity task, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadTaskVariable(task);
        }
    }
}
