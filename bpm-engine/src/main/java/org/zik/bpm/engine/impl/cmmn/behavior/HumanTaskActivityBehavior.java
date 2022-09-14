// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.behavior;

import org.zik.bpm.engine.impl.el.ExpressionManager;
import org.zik.bpm.engine.impl.task.TaskDefinition;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;
import org.zik.bpm.engine.impl.task.TaskDecorator;

public class HumanTaskActivityBehavior extends TaskActivityBehavior
{
    protected TaskDecorator taskDecorator;
    
    @Override
    protected void performStart(final CmmnActivityExecution execution) {
        execution.createTask(this.taskDecorator);
    }
    
    @Override
    protected void performTerminate(final CmmnActivityExecution execution) {
        this.terminating(execution);
        super.performTerminate(execution);
    }
    
    @Override
    protected void performExit(final CmmnActivityExecution execution) {
        this.terminating(execution);
        super.performExit(execution);
    }
    
    protected void terminating(final CmmnActivityExecution execution) {
        final TaskEntity task = this.getTask(execution);
        if (task != null) {
            task.delete("terminated", false);
        }
    }
    
    @Override
    protected void completing(final CmmnActivityExecution execution) {
        final TaskEntity task = this.getTask(execution);
        if (task != null) {
            task.caseExecutionCompleted();
        }
    }
    
    @Override
    protected void manualCompleting(final CmmnActivityExecution execution) {
        this.completing(execution);
    }
    
    protected void suspending(final CmmnActivityExecution execution) {
        final String id = execution.getId();
        Context.getCommandContext().getTaskManager().updateTaskSuspensionStateByCaseExecutionId(id, SuspensionState.SUSPENDED);
    }
    
    @Override
    protected void resuming(final CmmnActivityExecution execution) {
        final String id = execution.getId();
        Context.getCommandContext().getTaskManager().updateTaskSuspensionStateByCaseExecutionId(id, SuspensionState.ACTIVE);
    }
    
    protected TaskEntity getTask(final CmmnActivityExecution execution) {
        return Context.getCommandContext().getTaskManager().findTaskByCaseExecutionId(execution.getId());
    }
    
    @Override
    protected String getTypeName() {
        return "human task";
    }
    
    public TaskDecorator getTaskDecorator() {
        return this.taskDecorator;
    }
    
    public void setTaskDecorator(final TaskDecorator taskDecorator) {
        this.taskDecorator = taskDecorator;
    }
    
    public TaskDefinition getTaskDefinition() {
        return this.taskDecorator.getTaskDefinition();
    }
    
    public ExpressionManager getExpressionManager() {
        return this.taskDecorator.getExpressionManager();
    }
}
