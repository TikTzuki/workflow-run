// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;
import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import java.util.Map;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import org.zik.bpm.engine.impl.core.variable.scope.VariableInstanceLifecycleListener;

public class SetTaskVariablesCmd extends AbstractSetVariableCmd implements VariableInstanceLifecycleListener<VariableInstanceEntity>
{
    private static final long serialVersionUID = 1L;
    protected boolean taskLocalVariablesUpdated;
    
    public SetTaskVariablesCmd(final String taskId, final Map<String, ?> variables, final boolean isLocal) {
        super(taskId, variables, isLocal);
        this.taskLocalVariablesUpdated = false;
    }
    
    @Override
    protected TaskEntity getEntity() {
        EnsureUtil.ensureNotNull("taskId", (Object)this.entityId);
        final TaskEntity task = this.commandContext.getTaskManager().findTaskById(this.entityId);
        EnsureUtil.ensureNotNull("task " + this.entityId + " doesn't exist", "task", task);
        this.checkSetTaskVariables(task);
        task.addCustomLifecycleListener(this);
        return task;
    }
    
    @Override
    protected void onSuccess(final AbstractVariableScope scope) {
        final TaskEntity task = (TaskEntity)scope;
        if (this.taskLocalVariablesUpdated) {
            task.triggerUpdateEvent();
        }
        task.removeCustomLifecycleListener(this);
        super.onSuccess(scope);
    }
    
    @Override
    protected ExecutionEntity getContextExecution() {
        return this.getEntity().getExecution();
    }
    
    @Override
    protected void logVariableOperation(final AbstractVariableScope scope) {
        final TaskEntity task = (TaskEntity)scope;
        this.commandContext.getOperationLogManager().logVariableOperation(this.getLogEntryOperation(), null, task.getId(), PropertyChange.EMPTY_CHANGE);
    }
    
    protected void checkSetTaskVariables(final TaskEntity task) {
        for (final CommandChecker checker : this.commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkUpdateTaskVariable(task);
        }
    }
    
    protected void onLocalVariableChanged() {
        this.taskLocalVariablesUpdated = true;
    }
    
    @Override
    public void onCreate(final VariableInstanceEntity variableInstance, final AbstractVariableScope sourceScope) {
        this.onLocalVariableChanged();
    }
    
    @Override
    public void onDelete(final VariableInstanceEntity variableInstance, final AbstractVariableScope sourceScope) {
        this.onLocalVariableChanged();
    }
    
    @Override
    public void onUpdate(final VariableInstanceEntity variableInstance, final AbstractVariableScope sourceScope) {
        this.onLocalVariableChanged();
    }
}
