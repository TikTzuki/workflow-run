// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.TaskManager;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionVariableSnapshotObserver;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Map;
import java.io.Serializable;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.interceptor.Command;

public class CompleteTaskCmd implements Command<VariableMap>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String taskId;
    protected Map<String, Object> variables;
    protected boolean returnVariables;
    protected boolean deserializeReturnedVariables;
    
    public CompleteTaskCmd(final String taskId, final Map<String, Object> variables) {
        this(taskId, variables, false, false);
    }
    
    public CompleteTaskCmd(final String taskId, final Map<String, Object> variables, final boolean returnVariables, final boolean deserializeReturnedVariables) {
        this.taskId = taskId;
        this.variables = variables;
        this.returnVariables = returnVariables;
        this.deserializeReturnedVariables = deserializeReturnedVariables;
    }
    
    @Override
    public VariableMap execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("taskId", (Object)this.taskId);
        final TaskManager taskManager = commandContext.getTaskManager();
        final TaskEntity task = taskManager.findTaskById(this.taskId);
        EnsureUtil.ensureNotNull("Cannot find task with id " + this.taskId, "task", task);
        this.checkCompleteTask(task, commandContext);
        if (this.variables != null) {
            task.setExecutionVariables(this.variables);
        }
        final ExecutionEntity execution = task.getProcessInstance();
        ExecutionVariableSnapshotObserver variablesListener = null;
        if (this.returnVariables && execution != null) {
            variablesListener = new ExecutionVariableSnapshotObserver(execution, false, this.deserializeReturnedVariables);
        }
        this.completeTask(task);
        if (!this.returnVariables) {
            return null;
        }
        if (variablesListener != null) {
            return variablesListener.getVariables();
        }
        return (VariableMap)((task.getCaseDefinitionId() != null) ? null : task.getVariablesTyped(false));
    }
    
    protected void completeTask(final TaskEntity task) {
        task.logUserOperation("Complete");
        task.complete();
    }
    
    protected void checkCompleteTask(final TaskEntity task, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkTaskWork(task);
        }
    }
}
