// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.form.handler.TaskFormHandler;
import org.zik.bpm.engine.impl.task.TaskDefinition;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.TaskManager;
import org.zik.bpm.engine.task.DelegationState;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionVariableSnapshotObserver;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.variable.Variables;
import java.util.Map;
import java.io.Serializable;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SubmitTaskFormCmd implements Command<VariableMap>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String taskId;
    protected VariableMap properties;
    protected boolean returnVariables;
    protected boolean deserializeValues;
    
    public SubmitTaskFormCmd(final String taskId, final Map<String, Object> properties, final boolean returnVariables, final boolean deserializeValues) {
        this.taskId = taskId;
        this.properties = Variables.fromMap((Map)properties);
        this.returnVariables = returnVariables;
        this.deserializeValues = deserializeValues;
    }
    
    @Override
    public VariableMap execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("taskId", (Object)this.taskId);
        final TaskManager taskManager = commandContext.getTaskManager();
        final TaskEntity task = taskManager.findTaskById(this.taskId);
        EnsureUtil.ensureNotNull("Cannot find task with id " + this.taskId, "task", task);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkTaskWork(task);
        }
        final TaskDefinition taskDefinition = task.getTaskDefinition();
        if (taskDefinition != null) {
            final TaskFormHandler taskFormHandler = taskDefinition.getTaskFormHandler();
            taskFormHandler.submitFormVariables(this.properties, task);
        }
        else {
            task.setVariables((Map<String, ?>)this.properties);
        }
        final ExecutionEntity execution = task.getProcessInstance();
        ExecutionVariableSnapshotObserver variablesListener = null;
        if (this.returnVariables && execution != null) {
            variablesListener = new ExecutionVariableSnapshotObserver(execution, false, this.deserializeValues);
        }
        if (DelegationState.PENDING.equals(task.getDelegationState())) {
            task.resolve();
            task.logUserOperation("Resolve");
            task.triggerUpdateEvent();
        }
        else {
            task.logUserOperation("Complete");
            task.complete();
        }
        if (!this.returnVariables) {
            return null;
        }
        if (variablesListener != null) {
            return variablesListener.getVariables();
        }
        return (VariableMap)((task.getCaseDefinitionId() == null) ? null : task.getVariablesTyped(false));
    }
}
