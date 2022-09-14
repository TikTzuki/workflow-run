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
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetTaskVariableCmdTyped implements Command<TypedValue>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String taskId;
    protected String variableName;
    protected boolean isLocal;
    protected boolean deserializeValue;
    
    public GetTaskVariableCmdTyped(final String taskId, final String variableName, final boolean isLocal, final boolean deserializeValue) {
        this.taskId = taskId;
        this.variableName = variableName;
        this.isLocal = isLocal;
        this.deserializeValue = deserializeValue;
    }
    
    @Override
    public TypedValue execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("taskId", (Object)this.taskId);
        EnsureUtil.ensureNotNull("variableName", (Object)this.variableName);
        final TaskEntity task = Context.getCommandContext().getTaskManager().findTaskById(this.taskId);
        EnsureUtil.ensureNotNull("task " + this.taskId + " doesn't exist", "task", task);
        this.checkGetTaskVariableTyped(task, commandContext);
        TypedValue value;
        if (this.isLocal) {
            value = task.getVariableLocalTyped(this.variableName, this.deserializeValue);
        }
        else {
            value = task.getVariableTyped(this.variableName, this.deserializeValue);
        }
        return value;
    }
    
    protected void checkGetTaskVariableTyped(final TaskEntity task, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadTaskVariable(task);
        }
    }
}
