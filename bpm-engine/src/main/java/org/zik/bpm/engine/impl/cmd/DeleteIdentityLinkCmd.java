// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.persistence.entity.TaskManager;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public abstract class DeleteIdentityLinkCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String userId;
    protected String groupId;
    protected String type;
    protected String taskId;
    protected TaskEntity task;
    
    public DeleteIdentityLinkCmd(final String taskId, final String userId, final String groupId, final String type) {
        this.validateParams(userId, groupId, type, taskId);
        this.taskId = taskId;
        this.userId = userId;
        this.groupId = groupId;
        this.type = type;
    }
    
    protected void validateParams(final String userId, final String groupId, final String type, final String taskId) {
        EnsureUtil.ensureNotNull("taskId", (Object)taskId);
        EnsureUtil.ensureNotNull("type is required when adding a new task identity link", "type", type);
        if ("assignee".equals(type) || "owner".equals(type)) {
            if (groupId != null) {
                throw new ProcessEngineException("Incompatible usage: cannot use type '" + type + "' together with a groupId");
            }
        }
        else if (userId == null && groupId == null) {
            throw new ProcessEngineException("userId and groupId cannot both be null");
        }
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("taskId", (Object)this.taskId);
        final TaskManager taskManager = commandContext.getTaskManager();
        this.task = taskManager.findTaskById(this.taskId);
        EnsureUtil.ensureNotNull("Cannot find task with id " + this.taskId, "task", this.task);
        this.checkDeleteIdentityLink(this.task, commandContext);
        if ("assignee".equals(this.type)) {
            this.task.setAssignee(null);
        }
        else if ("owner".equals(this.type)) {
            this.task.setOwner(null);
        }
        else {
            this.task.deleteIdentityLink(this.userId, this.groupId, this.type);
        }
        this.task.triggerUpdateEvent();
        return null;
    }
    
    protected void checkDeleteIdentityLink(final TaskEntity task, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkTaskAssign(task);
        }
    }
}
