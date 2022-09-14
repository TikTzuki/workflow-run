// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.form.handler.TaskFormHandler;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.TaskManager;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.form.TaskFormData;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetTaskFormCmd implements Command<TaskFormData>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String taskId;
    
    public GetTaskFormCmd(final String taskId) {
        this.taskId = taskId;
    }
    
    @Override
    public TaskFormData execute(final CommandContext commandContext) {
        final TaskManager taskManager = commandContext.getTaskManager();
        final TaskEntity task = taskManager.findTaskById(this.taskId);
        EnsureUtil.ensureNotNull("No task found for taskId '" + this.taskId + "'", "task", task);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadTaskVariable(task);
        }
        if (task.getTaskDefinition() != null) {
            final TaskFormHandler taskFormHandler = task.getTaskDefinition().getTaskFormHandler();
            EnsureUtil.ensureNotNull("No taskFormHandler specified for task '" + this.taskId + "'", "taskFormHandler", taskFormHandler);
            return taskFormHandler.createTaskForm(task);
        }
        return null;
    }
}
