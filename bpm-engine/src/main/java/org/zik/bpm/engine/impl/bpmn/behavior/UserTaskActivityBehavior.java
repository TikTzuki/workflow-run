// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import java.util.Collection;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import org.zik.bpm.engine.impl.migration.instance.MigratingInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingUserTaskInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;
import org.zik.bpm.engine.impl.migration.instance.parser.MigratingInstanceParseContext;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.task.TaskDefinition;
import org.zik.bpm.engine.impl.el.ExpressionManager;
import org.zik.bpm.engine.impl.task.TaskDecorator;
import org.zik.bpm.engine.impl.pvm.delegate.MigrationObserverBehavior;

public class UserTaskActivityBehavior extends TaskActivityBehavior implements MigrationObserverBehavior
{
    protected TaskDecorator taskDecorator;
    
    @Deprecated
    public UserTaskActivityBehavior(final ExpressionManager expressionManager, final TaskDefinition taskDefinition) {
        this.taskDecorator = new TaskDecorator(taskDefinition, expressionManager);
    }
    
    public UserTaskActivityBehavior(final TaskDecorator taskDecorator) {
        this.taskDecorator = taskDecorator;
    }
    
    public void performExecution(final ActivityExecution execution) throws Exception {
        final TaskEntity task = new TaskEntity((ExecutionEntity)execution);
        task.insert();
        this.taskDecorator.decorate(task, execution);
        task.transitionTo(TaskEntity.TaskState.STATE_CREATED);
    }
    
    @Override
    public void signal(final ActivityExecution execution, final String signalName, final Object signalData) throws Exception {
        this.leave(execution);
    }
    
    @Override
    public void migrateScope(final ActivityExecution scopeExecution) {
    }
    
    @Override
    public void onParseMigratingInstance(final MigratingInstanceParseContext parseContext, final MigratingActivityInstance migratingInstance) {
        final ExecutionEntity execution = migratingInstance.resolveRepresentativeExecution();
        for (final TaskEntity task : execution.getTasks()) {
            migratingInstance.addMigratingDependentInstance(new MigratingUserTaskInstance(task, migratingInstance));
            parseContext.consume(task);
            final Collection<VariableInstanceEntity> variables = task.getVariablesInternal();
            if (variables != null) {
                for (final VariableInstanceEntity variable : variables) {
                    parseContext.consume(variable);
                }
            }
        }
    }
    
    public TaskDefinition getTaskDefinition() {
        return this.taskDecorator.getTaskDefinition();
    }
    
    public ExpressionManager getExpressionManager() {
        return this.taskDecorator.getExpressionManager();
    }
    
    public TaskDecorator getTaskDecorator() {
        return this.taskDecorator;
    }
}
