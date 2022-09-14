// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.bpmn.parser.EventSubscriptionDeclaration;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.zik.bpm.engine.impl.migration.MigrationLogger;

public class MigratingEventSubscriptionInstance implements MigratingInstance, RemovingInstance, EmergingInstance
{
    public static final MigrationLogger MIGRATION_LOGGER;
    protected EventSubscriptionEntity eventSubscriptionEntity;
    protected ScopeImpl targetScope;
    protected boolean updateEvent;
    protected EventSubscriptionDeclaration targetDeclaration;
    protected EventSubscriptionDeclaration eventSubscriptionDeclaration;
    
    public MigratingEventSubscriptionInstance(final EventSubscriptionEntity eventSubscriptionEntity, final ScopeImpl targetScope, final boolean updateEvent, final EventSubscriptionDeclaration targetDeclaration) {
        this.eventSubscriptionEntity = eventSubscriptionEntity;
        this.targetScope = targetScope;
        this.updateEvent = updateEvent;
        this.targetDeclaration = targetDeclaration;
    }
    
    public MigratingEventSubscriptionInstance(final EventSubscriptionEntity eventSubscriptionEntity) {
        this(eventSubscriptionEntity, null, false, null);
    }
    
    public MigratingEventSubscriptionInstance(final EventSubscriptionDeclaration eventSubscriptionDeclaration) {
        this.eventSubscriptionDeclaration = eventSubscriptionDeclaration;
    }
    
    @Override
    public boolean isDetached() {
        return this.eventSubscriptionEntity.getExecutionId() == null;
    }
    
    @Override
    public void detachState() {
        this.eventSubscriptionEntity.setExecution(null);
    }
    
    @Override
    public void attachState(final MigratingScopeInstance newOwningInstance) {
        this.eventSubscriptionEntity.setExecution(newOwningInstance.resolveRepresentativeExecution());
    }
    
    @Override
    public void attachState(final MigratingTransitionInstance targetTransitionInstance) {
        throw MigratingEventSubscriptionInstance.MIGRATION_LOGGER.cannotAttachToTransitionInstance(this);
    }
    
    @Override
    public void migrateState() {
        if (this.updateEvent) {
            this.targetDeclaration.updateSubscription(this.eventSubscriptionEntity);
        }
        this.eventSubscriptionEntity.setActivity((ActivityImpl)this.targetScope);
    }
    
    @Override
    public void migrateDependentEntities() {
    }
    
    @Override
    public void create(final ExecutionEntity scopeExecution) {
        this.eventSubscriptionDeclaration.createSubscriptionForExecution(scopeExecution);
    }
    
    @Override
    public void remove() {
        this.eventSubscriptionEntity.delete();
    }
    
    static {
        MIGRATION_LOGGER = ProcessEngineLogger.MIGRATION_LOGGER;
    }
}
