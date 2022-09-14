// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.form.TaskFormData;
import org.zik.bpm.engine.impl.form.handler.TaskFormHandler;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.TaskManager;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.form.engine.FormEngine;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetRenderedTaskFormCmd implements Command<Object>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String taskId;
    protected String formEngineName;
    
    public GetRenderedTaskFormCmd(final String taskId, final String formEngineName) {
        this.taskId = taskId;
        this.formEngineName = formEngineName;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        final TaskManager taskManager = commandContext.getTaskManager();
        final TaskEntity task = taskManager.findTaskById(this.taskId);
        EnsureUtil.ensureNotNull("Task '" + this.taskId + "' not found", "task", task);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadTaskVariable(task);
        }
        EnsureUtil.ensureNotNull("Task form definition for '" + this.taskId + "' not found", "task.getTaskDefinition()", task.getTaskDefinition());
        final TaskFormHandler taskFormHandler = task.getTaskDefinition().getTaskFormHandler();
        if (taskFormHandler == null) {
            return null;
        }
        final FormEngine formEngine = Context.getProcessEngineConfiguration().getFormEngines().get(this.formEngineName);
        EnsureUtil.ensureNotNull("No formEngine '" + this.formEngineName + "' defined process engine configuration", "formEngine", formEngine);
        final TaskFormData taskForm = taskFormHandler.createTaskForm(task);
        return formEngine.renderTaskForm(taskForm);
    }
}
