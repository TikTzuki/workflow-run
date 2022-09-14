// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.exception.NotAllowedException;
import org.zik.bpm.engine.exception.NullValueException;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.task.Task;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SaveTaskCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected TaskEntity task;
    
    public SaveTaskCmd(final Task task) {
        this.task = (TaskEntity)task;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("task", this.task);
        this.validateStandaloneTask(this.task, commandContext);
        String operation;
        if (this.task.getRevision() == 0) {
            try {
                this.checkCreateTask(this.task, commandContext);
                this.task.ensureParentTaskActive();
                this.task.propagateParentTaskTenantId();
                this.task.insert();
                operation = "Create";
                this.task.executeMetrics("activity-instance-start", commandContext);
            }
            catch (NullValueException e) {
                throw new NotValidException(e.getMessage(), e);
            }
            this.task.fireAuthorizationProvider();
            this.task.transitionTo(TaskEntity.TaskState.STATE_CREATED);
        }
        else {
            this.checkTaskAssign(this.task, commandContext);
            this.task.update();
            operation = "Update";
            this.task.fireAuthorizationProvider();
            this.task.triggerUpdateEvent();
        }
        this.task.executeMetrics("unique-task-workers", commandContext);
        this.task.logUserOperation(operation);
        return null;
    }
    
    protected void validateStandaloneTask(final TaskEntity task, final CommandContext commandContext) {
        final boolean standaloneTasksEnabled = commandContext.getProcessEngineConfiguration().isStandaloneTasksEnabled();
        if (!standaloneTasksEnabled && task.isStandaloneTask()) {
            throw new NotAllowedException("Cannot save standalone task. They are disabled in the process engine configuration.");
        }
    }
    
    protected void checkTaskAssign(final TaskEntity task, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkTaskAssign(task);
        }
    }
    
    protected void checkCreateTask(final TaskEntity task, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkCreateTask(task);
        }
    }
}
