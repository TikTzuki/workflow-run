// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.TaskManager;
import org.zik.bpm.engine.TaskAlreadyClaimedException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class ClaimTaskCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String taskId;
    protected String userId;
    
    public ClaimTaskCmd(final String taskId, final String userId) {
        this.taskId = taskId;
        this.userId = userId;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("taskId", (Object)this.taskId);
        final TaskManager taskManager = commandContext.getTaskManager();
        final TaskEntity task = taskManager.findTaskById(this.taskId);
        EnsureUtil.ensureNotNull("Cannot find task with id " + this.taskId, "task", task);
        this.checkClaimTask(task, commandContext);
        if (this.userId != null) {
            if (task.getAssignee() != null) {
                if (!task.getAssignee().equals(this.userId)) {
                    throw new TaskAlreadyClaimedException(task.getId(), task.getAssignee());
                }
            }
            else {
                task.setAssignee(this.userId);
            }
        }
        else {
            task.setAssignee(null);
        }
        task.triggerUpdateEvent();
        task.logUserOperation("Claim");
        return null;
    }
    
    protected void checkClaimTask(final TaskEntity task, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkTaskWork(task);
        }
    }
}
