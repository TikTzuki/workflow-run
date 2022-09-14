// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import java.util.Map;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class HandleTaskEscalationCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String taskId;
    protected String escalationCode;
    protected Map<String, Object> variables;
    
    public HandleTaskEscalationCmd(final String taskId, final String escalationCode) {
        this.taskId = taskId;
        this.escalationCode = escalationCode;
    }
    
    public HandleTaskEscalationCmd(final String taskId, final String escalationCode, final Map<String, Object> variables) {
        this(taskId, escalationCode);
        this.variables = variables;
    }
    
    protected void validateInput() {
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "taskId", this.taskId);
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "escalationCode", this.escalationCode);
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        this.validateInput();
        final TaskEntity task = commandContext.getTaskManager().findTaskById(this.taskId);
        EnsureUtil.ensureNotNull(NotFoundException.class, "Cannot find task with id " + this.taskId, "task", task);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkTaskWork(task);
        }
        task.escalation(this.escalationCode, this.variables);
        return null;
    }
}
