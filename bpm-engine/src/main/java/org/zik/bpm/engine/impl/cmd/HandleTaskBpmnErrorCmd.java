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

public class HandleTaskBpmnErrorCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String taskId;
    protected String errorCode;
    protected String errorMessage;
    protected Map<String, Object> variables;
    
    public HandleTaskBpmnErrorCmd(final String taskId, final String errorCode) {
        this.taskId = taskId;
        this.errorCode = errorCode;
    }
    
    public HandleTaskBpmnErrorCmd(final String taskId, final String errorCode, final String errorMessage) {
        this(taskId, errorCode);
        this.errorMessage = errorMessage;
    }
    
    public HandleTaskBpmnErrorCmd(final String taskId, final String errorCode, final String errorMessage, final Map<String, Object> variables) {
        this(taskId, errorCode, errorMessage);
        this.variables = variables;
    }
    
    protected void validateInput() {
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "taskId", this.taskId);
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "errorCode", this.errorCode);
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        this.validateInput();
        final TaskEntity task = commandContext.getTaskManager().findTaskById(this.taskId);
        EnsureUtil.ensureNotNull(NotFoundException.class, "Cannot find task with id " + this.taskId, "task", task);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkTaskWork(task);
        }
        task.bpmnError(this.errorCode, this.errorMessage, this.variables);
        return null;
    }
}
