// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.TaskManager;
import org.zik.bpm.engine.impl.persistence.entity.IdentityLinkEntity;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.task.IdentityLink;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetIdentityLinksForTaskCmd implements Command<List<IdentityLink>>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String taskId;
    
    public GetIdentityLinksForTaskCmd(final String taskId) {
        this.taskId = taskId;
    }
    
    @Override
    public List<IdentityLink> execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("taskId", (Object)this.taskId);
        final TaskManager taskManager = commandContext.getTaskManager();
        final TaskEntity task = taskManager.findTaskById(this.taskId);
        EnsureUtil.ensureNotNull("Cannot find task with id " + this.taskId, "task", task);
        this.checkGetIdentityLink(task, commandContext);
        final List<IdentityLink> identityLinks = (List<IdentityLink>)task.getIdentityLinks();
        if (task.getAssignee() != null) {
            final IdentityLinkEntity identityLink = new IdentityLinkEntity();
            identityLink.setUserId(task.getAssignee());
            identityLink.setTask(task);
            identityLink.setType("assignee");
            identityLinks.add(identityLink);
        }
        if (task.getOwner() != null) {
            final IdentityLinkEntity identityLink = new IdentityLinkEntity();
            identityLink.setUserId(task.getOwner());
            identityLink.setTask(task);
            identityLink.setType("owner");
            identityLinks.add(identityLink);
        }
        return (List<IdentityLink>)task.getIdentityLinks();
    }
    
    protected void checkGetIdentityLink(final TaskEntity task, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadTask(task);
        }
    }
}
