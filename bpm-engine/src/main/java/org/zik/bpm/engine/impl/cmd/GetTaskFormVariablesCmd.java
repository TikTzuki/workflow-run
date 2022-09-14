// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.cfg.CommandChecker;
import java.util.Iterator;
import org.zik.bpm.engine.form.TaskFormData;
import org.zik.bpm.engine.impl.task.TaskDefinition;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.TaskManager;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.form.FormField;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Collection;

public class GetTaskFormVariablesCmd extends AbstractGetFormVariablesCmd
{
    private static final long serialVersionUID = 1L;
    
    public GetTaskFormVariablesCmd(final String taskId, final Collection<String> variableNames, final boolean deserializeObjectValues) {
        super(taskId, variableNames, deserializeObjectValues);
    }
    
    @Override
    public VariableMap execute(final CommandContext commandContext) {
        final TaskManager taskManager = commandContext.getTaskManager();
        final TaskEntity task = taskManager.findTaskById(this.resourceId);
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "Cannot find task with id '" + this.resourceId + "'.", "task", task);
        this.checkGetTaskFormVariables(task, commandContext);
        final VariableMapImpl result = new VariableMapImpl();
        final TaskDefinition taskDefinition = task.getTaskDefinition();
        if (taskDefinition != null) {
            final TaskFormData taskFormData = taskDefinition.getTaskFormHandler().createTaskForm(task);
            for (final FormField formField : taskFormData.getFormFields()) {
                if (this.formVariableNames == null || this.formVariableNames.contains(formField.getId())) {
                    result.put(formField.getId(), (Object)this.createVariable(formField, task));
                }
            }
        }
        task.collectVariables(result, this.formVariableNames, false, this.deserializeObjectValues);
        return (VariableMap)result;
    }
    
    protected void checkGetTaskFormVariables(final TaskEntity task, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadTaskVariable(task);
        }
    }
}
