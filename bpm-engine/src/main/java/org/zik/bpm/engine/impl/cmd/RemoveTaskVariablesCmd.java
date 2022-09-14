// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import java.util.Collection;

public class RemoveTaskVariablesCmd extends AbstractRemoveVariableCmd
{
    private static final long serialVersionUID = 1L;
    
    public RemoveTaskVariablesCmd(final String taskId, final Collection<String> variableNames, final boolean isLocal) {
        super(taskId, variableNames, isLocal);
    }
    
    @Override
    protected TaskEntity getEntity() {
        EnsureUtil.ensureNotNull("taskId", (Object)this.entityId);
        final TaskEntity task = this.commandContext.getTaskManager().findTaskById(this.entityId);
        EnsureUtil.ensureNotNull("Cannot find task with id " + this.entityId, "task", task);
        this.checkRemoveTaskVariables(task);
        return task;
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
    
    protected void checkRemoveTaskVariables(final TaskEntity task) {
        for (final CommandChecker checker : this.commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkUpdateTaskVariable(task);
        }
    }
}
