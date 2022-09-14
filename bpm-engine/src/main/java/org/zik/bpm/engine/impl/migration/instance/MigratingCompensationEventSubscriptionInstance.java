// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.migration.MigrationInstruction;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.zik.bpm.engine.impl.migration.MigrationLogger;

public class MigratingCompensationEventSubscriptionInstance extends MigratingProcessElementInstance implements RemovingInstance
{
    public static final MigrationLogger MIGRATION_LOGGER;
    protected EventSubscriptionEntity eventSubscription;
    
    public MigratingCompensationEventSubscriptionInstance(final MigrationInstruction migrationInstruction, final ScopeImpl sourceScope, final ScopeImpl targetScope, final EventSubscriptionEntity eventSubscription) {
        this.migrationInstruction = migrationInstruction;
        this.eventSubscription = eventSubscription;
        this.sourceScope = sourceScope;
        this.targetScope = targetScope;
        this.currentScope = sourceScope;
    }
    
    @Override
    public boolean isDetached() {
        return this.eventSubscription.getExecutionId() == null;
    }
    
    @Override
    public void detachState() {
        this.eventSubscription.setExecution(null);
    }
    
    @Override
    public void attachState(final MigratingTransitionInstance targetTransitionInstance) {
        throw MigratingCompensationEventSubscriptionInstance.MIGRATION_LOGGER.cannotAttachToTransitionInstance(this);
    }
    
    @Override
    public void migrateState() {
        this.eventSubscription.setActivity((ActivityImpl)this.targetScope);
        this.currentScope = this.targetScope;
    }
    
    @Override
    public void migrateDependentEntities() {
    }
    
    @Override
    public void addMigratingDependentInstance(final MigratingInstance migratingInstance) {
    }
    
    @Override
    public ExecutionEntity resolveRepresentativeExecution() {
        return null;
    }
    
    @Override
    public void attachState(final MigratingScopeInstance targetActivityInstance) {
        this.setParent(targetActivityInstance);
        final ExecutionEntity representativeExecution = targetActivityInstance.resolveRepresentativeExecution();
        this.eventSubscription.setExecution(representativeExecution);
    }
    
    @Override
    public void setParent(final MigratingScopeInstance parentInstance) {
        if (this.parentInstance != null) {
            this.parentInstance.removeChild(this);
        }
        if ((this.parentInstance = parentInstance) != null) {
            parentInstance.addChild(this);
        }
    }
    
    @Override
    public void remove() {
        this.eventSubscription.delete();
    }
    
    static {
        MIGRATION_LOGGER = ProcessEngineLogger.MIGRATION_LOGGER;
    }
}
