// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.delegate.DelegateTask;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.impl.history.event.HistoryEventProcessor;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.context.Context;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.migration.MigrationLogger;

public class MigratingUserTaskInstance implements MigratingInstance
{
    public static final MigrationLogger MIGRATION_LOGGER;
    protected TaskEntity userTask;
    protected MigratingActivityInstance migratingActivityInstance;
    
    public MigratingUserTaskInstance(final TaskEntity userTask, final MigratingActivityInstance migratingActivityInstance) {
        this.userTask = userTask;
        this.migratingActivityInstance = migratingActivityInstance;
    }
    
    @Override
    public void migrateDependentEntities() {
    }
    
    @Override
    public boolean isDetached() {
        return this.userTask.getExecutionId() == null;
    }
    
    @Override
    public void detachState() {
        this.userTask.getExecution().removeTask(this.userTask);
        this.userTask.setExecution(null);
    }
    
    @Override
    public void attachState(final MigratingScopeInstance owningInstance) {
        final ExecutionEntity representativeExecution = owningInstance.resolveRepresentativeExecution();
        representativeExecution.addTask(this.userTask);
        for (final VariableInstanceEntity variable : this.userTask.getVariablesInternal()) {
            variable.setExecution(representativeExecution);
        }
        this.userTask.setExecution(representativeExecution);
    }
    
    @Override
    public void attachState(final MigratingTransitionInstance targetTransitionInstance) {
        throw MigratingUserTaskInstance.MIGRATION_LOGGER.cannotAttachToTransitionInstance(this);
    }
    
    @Override
    public void migrateState() {
        this.userTask.setProcessDefinitionId(this.migratingActivityInstance.getTargetScope().getProcessDefinition().getId());
        this.userTask.setTaskDefinitionKey(this.migratingActivityInstance.getTargetScope().getId());
        this.migrateHistory();
    }
    
    protected void migrateHistory() {
        final HistoryLevel historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
        if (historyLevel.isHistoryEventProduced(HistoryEventTypes.TASK_INSTANCE_MIGRATE, this)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    return producer.createTaskInstanceMigrateEvt(MigratingUserTaskInstance.this.userTask);
                }
            });
        }
    }
    
    static {
        MIGRATION_LOGGER = ProcessEngineLogger.MIGRATION_LOGGER;
    }
}
