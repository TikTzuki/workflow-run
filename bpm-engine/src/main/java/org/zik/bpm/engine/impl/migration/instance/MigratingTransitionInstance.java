// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import java.util.Iterator;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.migration.MigrationInstruction;
import java.util.List;
import org.zik.bpm.engine.runtime.TransitionInstance;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.migration.MigrationLogger;

public class MigratingTransitionInstance extends MigratingProcessElementInstance implements MigratingInstance
{
    public static final MigrationLogger MIGRATION_LOGGER;
    protected ExecutionEntity representativeExecution;
    protected TransitionInstance transitionInstance;
    protected MigratingAsyncJobInstance jobInstance;
    protected List<MigratingInstance> migratingDependentInstances;
    protected boolean activeState;
    
    public MigratingTransitionInstance(final TransitionInstance transitionInstance, final MigrationInstruction migrationInstruction, final ScopeImpl sourceScope, final ScopeImpl targetScope, final ExecutionEntity asyncExecution) {
        this.migratingDependentInstances = new ArrayList<MigratingInstance>();
        this.transitionInstance = transitionInstance;
        this.migrationInstruction = migrationInstruction;
        this.sourceScope = sourceScope;
        this.targetScope = targetScope;
        this.currentScope = sourceScope;
        this.representativeExecution = asyncExecution;
        this.activeState = this.representativeExecution.isActive();
    }
    
    @Override
    public boolean isDetached() {
        return this.jobInstance.isDetached();
    }
    
    @Override
    public MigratingActivityInstance getParent() {
        return (MigratingActivityInstance)super.getParent();
    }
    
    @Override
    public void detachState() {
        this.jobInstance.detachState();
        for (final MigratingInstance dependentInstance : this.migratingDependentInstances) {
            dependentInstance.detachState();
        }
        final ExecutionEntity execution = this.resolveRepresentativeExecution();
        execution.setActive(false);
        this.getParent().destroyAttachableExecution(execution);
        this.setParent(null);
    }
    
    @Override
    public void attachState(final MigratingScopeInstance scopeInstance) {
        if (!(scopeInstance instanceof MigratingActivityInstance)) {
            throw MigratingTransitionInstance.MIGRATION_LOGGER.cannotHandleChild(scopeInstance, this);
        }
        final MigratingActivityInstance activityInstance = (MigratingActivityInstance)scopeInstance;
        this.setParent(activityInstance);
        (this.representativeExecution = activityInstance.createAttachableExecution()).setActivityInstanceId(null);
        this.representativeExecution.setActive(this.activeState);
        this.jobInstance.attachState(this);
        for (final MigratingInstance dependentInstance : this.migratingDependentInstances) {
            dependentInstance.attachState(this);
        }
    }
    
    @Override
    public ExecutionEntity resolveRepresentativeExecution() {
        if (this.representativeExecution.getReplacedBy() != null) {
            return this.representativeExecution.resolveReplacedBy();
        }
        return this.representativeExecution;
    }
    
    @Override
    public void attachState(final MigratingTransitionInstance targetTransitionInstance) {
        throw MigratingTransitionInstance.MIGRATION_LOGGER.cannotAttachToTransitionInstance(this);
    }
    
    public void setDependentJobInstance(final MigratingAsyncJobInstance jobInstance) {
        this.jobInstance = jobInstance;
    }
    
    @Override
    public void addMigratingDependentInstance(final MigratingInstance migratingInstance) {
        this.migratingDependentInstances.add(migratingInstance);
    }
    
    public List<MigratingInstance> getMigratingDependentInstances() {
        return this.migratingDependentInstances;
    }
    
    @Override
    public void migrateState() {
        final ExecutionEntity representativeExecution = this.resolveRepresentativeExecution();
        representativeExecution.setProcessDefinition(this.targetScope.getProcessDefinition());
        representativeExecution.setActivity((PvmActivity)this.targetScope);
    }
    
    @Override
    public void migrateDependentEntities() {
        this.jobInstance.migrateState();
        this.jobInstance.migrateDependentEntities();
        for (final MigratingInstance dependentInstance : this.migratingDependentInstances) {
            dependentInstance.migrateState();
            dependentInstance.migrateDependentEntities();
        }
    }
    
    public TransitionInstance getTransitionInstance() {
        return this.transitionInstance;
    }
    
    public boolean isAsyncAfter() {
        return this.jobInstance.isAsyncAfter();
    }
    
    public boolean isAsyncBefore() {
        return this.jobInstance.isAsyncBefore();
    }
    
    public MigratingJobInstance getJobInstance() {
        return this.jobInstance;
    }
    
    @Override
    public void setParent(final MigratingScopeInstance parentInstance) {
        if (parentInstance != null && !(parentInstance instanceof MigratingActivityInstance)) {
            throw MigratingTransitionInstance.MIGRATION_LOGGER.cannotHandleChild(parentInstance, this);
        }
        final MigratingActivityInstance parentActivityInstance = (MigratingActivityInstance)parentInstance;
        if (this.parentInstance != null) {
            ((MigratingActivityInstance)this.parentInstance).removeChild(this);
        }
        this.parentInstance = parentActivityInstance;
        if (parentInstance != null) {
            parentActivityInstance.addChild(this);
        }
    }
    
    static {
        MIGRATION_LOGGER = ProcessEngineLogger.MIGRATION_LOGGER;
    }
}
