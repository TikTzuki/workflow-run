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

public class DelegateTaskCmd implements Command<Object>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String taskId;
    protected String userId;
    
    public DelegateTaskCmd(final String taskId, final String userId) {
        this.taskId = taskId;
        this.userId = userId;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("taskId", (Object)this.taskId);
        final TaskManager taskManager = commandContext.getTaskManager();
        final TaskEntity task = taskManager.findTaskById(this.taskId);
        EnsureUtil.ensureNotNull("Cannot find task with id " + this.taskId, "task", task);
        this.checkDelegateTask(task, commandContext);
        task.delegate(this.userId);
        task.triggerUpdateEvent();
        task.logUserOperation("Delegate");
        return null;
    }
    
    protected void checkDelegateTask(final TaskEntity task, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkTaskAssign(task);
        }
    }
}
