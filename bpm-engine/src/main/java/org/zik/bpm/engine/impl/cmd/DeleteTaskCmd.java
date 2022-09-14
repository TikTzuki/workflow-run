// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.TaskManager;
import org.zik.bpm.engine.impl.context.Context;
import java.util.Iterator;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Collection;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteTaskCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String taskId;
    protected Collection<String> taskIds;
    protected boolean cascade;
    protected String deleteReason;
    
    public DeleteTaskCmd(final String taskId, final String deleteReason, final boolean cascade) {
        this.taskId = taskId;
        this.cascade = cascade;
        this.deleteReason = deleteReason;
    }
    
    public DeleteTaskCmd(final Collection<String> taskIds, final String deleteReason, final boolean cascade) {
        this.taskIds = taskIds;
        this.cascade = cascade;
        this.deleteReason = deleteReason;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        if (this.taskId != null) {
            this.deleteTask(this.taskId, commandContext);
        }
        else {
            if (this.taskIds == null) {
                throw new ProcessEngineException("taskId and taskIds are null");
            }
            for (final String taskId : this.taskIds) {
                this.deleteTask(taskId, commandContext);
            }
        }
        return null;
    }
    
    protected void deleteTask(final String taskId, final CommandContext commandContext) {
        final TaskManager taskManager = commandContext.getTaskManager();
        final TaskEntity task = taskManager.findTaskById(taskId);
        if (task != null) {
            if (task.getExecutionId() != null) {
                throw new ProcessEngineException("The task cannot be deleted because is part of a running process");
            }
            if (task.getCaseExecutionId() != null) {
                throw new ProcessEngineException("The task cannot be deleted because is part of a running case instance");
            }
            this.checkDeleteTask(task, commandContext);
            task.logUserOperation("Delete");
            final String reason = (this.deleteReason == null || this.deleteReason.length() == 0) ? "deleted" : this.deleteReason;
            task.delete(reason, this.cascade);
        }
        else if (this.cascade) {
            Context.getCommandContext().getHistoricTaskInstanceManager().deleteHistoricTaskInstanceById(taskId);
        }
    }
    
    protected void checkDeleteTask(final TaskEntity task, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkDeleteTask(task);
        }
    }
}
