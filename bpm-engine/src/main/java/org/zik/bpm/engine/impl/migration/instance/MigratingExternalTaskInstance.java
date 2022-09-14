// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;
import org.zik.bpm.engine.impl.migration.MigrationLogger;

public class MigratingExternalTaskInstance implements MigratingInstance
{
    public static final MigrationLogger MIGRATION_LOGGER;
    protected ExternalTaskEntity externalTask;
    protected MigratingActivityInstance migratingActivityInstance;
    protected List<MigratingInstance> dependentInstances;
    
    public MigratingExternalTaskInstance(final ExternalTaskEntity externalTask, final MigratingActivityInstance migratingActivityInstance) {
        this.dependentInstances = new ArrayList<MigratingInstance>();
        this.externalTask = externalTask;
        this.migratingActivityInstance = migratingActivityInstance;
    }
    
    @Override
    public void migrateDependentEntities() {
        for (final MigratingInstance migratingDependentInstance : this.dependentInstances) {
            migratingDependentInstance.migrateState();
        }
    }
    
    @Override
    public boolean isDetached() {
        return this.externalTask.getExecutionId() == null;
    }
    
    @Override
    public void detachState() {
        this.externalTask.getExecution().removeExternalTask(this.externalTask);
        this.externalTask.setExecution(null);
    }
    
    @Override
    public void attachState(final MigratingScopeInstance owningInstance) {
        final ExecutionEntity representativeExecution = owningInstance.resolveRepresentativeExecution();
        representativeExecution.addExternalTask(this.externalTask);
        this.externalTask.setExecution(representativeExecution);
    }
    
    @Override
    public void attachState(final MigratingTransitionInstance targetTransitionInstance) {
        throw MigratingExternalTaskInstance.MIGRATION_LOGGER.cannotAttachToTransitionInstance(this);
    }
    
    @Override
    public void migrateState() {
        final ScopeImpl targetActivity = this.migratingActivityInstance.getTargetScope();
        final ProcessDefinition targetProcessDefinition = (ProcessDefinition)targetActivity.getProcessDefinition();
        this.externalTask.setActivityId(targetActivity.getId());
        this.externalTask.setProcessDefinitionId(targetProcessDefinition.getId());
        this.externalTask.setProcessDefinitionKey(targetProcessDefinition.getKey());
    }
    
    public String getId() {
        return this.externalTask.getId();
    }
    
    public ScopeImpl getTargetScope() {
        return this.migratingActivityInstance.getTargetScope();
    }
    
    public void addMigratingDependentInstance(final MigratingInstance migratingInstance) {
        this.dependentInstances.add(migratingInstance);
    }
    
    static {
        MIGRATION_LOGGER = ProcessEngineLogger.MIGRATION_LOGGER;
    }
}
