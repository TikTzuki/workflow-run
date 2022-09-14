// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.TaskManager;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SetTaskPriorityCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected int priority;
    protected String taskId;
    
    public SetTaskPriorityCmd(final String taskId, final int priority) {
        this.taskId = taskId;
        this.priority = priority;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("taskId", (Object)this.taskId);
        final TaskManager taskManager = commandContext.getTaskManager();
        final TaskEntity task = taskManager.findTaskById(this.taskId);
        EnsureUtil.ensureNotNull("Cannot find task with id " + this.taskId, "task", task);
        this.checkTaskPriority(task, commandContext);
        task.setPriority(this.priority);
        task.triggerUpdateEvent();
        task.logUserOperation("SetPriority");
        return null;
    }
    
    protected void checkTaskPriority(final TaskEntity task, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkTaskAssign(task);
        }
    }
}
