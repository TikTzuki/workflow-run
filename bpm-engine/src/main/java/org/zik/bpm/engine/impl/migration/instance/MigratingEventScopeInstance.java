// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Collection;
import java.util.Iterator;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import java.util.ArrayList;
import java.util.HashSet;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.migration.MigrationInstruction;
import java.util.List;
import java.util.Set;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.migration.MigrationLogger;

public class MigratingEventScopeInstance extends MigratingScopeInstance
{
    public static final MigrationLogger MIGRATION_LOGGER;
    protected MigratingCompensationEventSubscriptionInstance migratingEventSubscription;
    protected ExecutionEntity eventScopeExecution;
    protected Set<MigratingEventScopeInstance> childInstances;
    protected Set<MigratingCompensationEventSubscriptionInstance> childCompensationSubscriptionInstances;
    protected List<MigratingInstance> migratingDependentInstances;
    
    public MigratingEventScopeInstance(final MigrationInstruction migrationInstruction, final ExecutionEntity eventScopeExecution, final ScopeImpl sourceScope, final ScopeImpl targetScope, final MigrationInstruction eventSubscriptionInstruction, final EventSubscriptionEntity eventSubscription, final ScopeImpl eventSubscriptionSourceScope, final ScopeImpl eventSubscriptionTargetScope) {
        this.childInstances = new HashSet<MigratingEventScopeInstance>();
        this.childCompensationSubscriptionInstances = new HashSet<MigratingCompensationEventSubscriptionInstance>();
        this.migratingDependentInstances = new ArrayList<MigratingInstance>();
        this.migratingEventSubscription = new MigratingCompensationEventSubscriptionInstance(eventSubscriptionInstruction, eventSubscriptionSourceScope, eventSubscriptionTargetScope, eventSubscription);
        this.migrationInstruction = migrationInstruction;
        this.eventScopeExecution = eventScopeExecution;
        this.sourceScope = sourceScope;
        this.targetScope = targetScope;
    }
    
    public MigratingEventScopeInstance(final EventSubscriptionEntity eventSubscription, final ExecutionEntity eventScopeExecution, final ScopeImpl targetScope) {
        this.childInstances = new HashSet<MigratingEventScopeInstance>();
        this.childCompensationSubscriptionInstances = new HashSet<MigratingCompensationEventSubscriptionInstance>();
        this.migratingDependentInstances = new ArrayList<MigratingInstance>();
        this.migratingEventSubscription = new MigratingCompensationEventSubscriptionInstance(null, null, targetScope, eventSubscription);
        this.eventScopeExecution = eventScopeExecution;
        this.targetScope = targetScope;
        this.currentScope = targetScope;
    }
    
    @Override
    public boolean isDetached() {
        return this.eventScopeExecution.getParentId() == null;
    }
    
    @Override
    public void detachState() {
        this.migratingEventSubscription.detachState();
        this.eventScopeExecution.setParent(null);
    }
    
    @Override
    public void attachState(final MigratingScopeInstance targetActivityInstance) {
        this.setParent(targetActivityInstance);
        this.migratingEventSubscription.attachState(targetActivityInstance);
        final ExecutionEntity representativeExecution = targetActivityInstance.resolveRepresentativeExecution();
        this.eventScopeExecution.setParent(representativeExecution);
    }
    
    @Override
    public void attachState(final MigratingTransitionInstance targetTransitionInstance) {
        throw MigratingEventScopeInstance.MIGRATION_LOGGER.cannotAttachToTransitionInstance(this);
    }
    
    @Override
    public void migrateState() {
        this.migratingEventSubscription.migrateState();
        this.eventScopeExecution.setActivity((PvmActivity)this.targetScope);
        this.eventScopeExecution.setProcessDefinition(this.targetScope.getProcessDefinition());
        this.currentScope = this.targetScope;
    }
    
    @Override
    public void migrateDependentEntities() {
        for (final MigratingInstance dependentEntity : this.migratingDependentInstances) {
            dependentEntity.migrateState();
        }
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
    public void addMigratingDependentInstance(final MigratingInstance migratingInstance) {
        this.migratingDependentInstances.add(migratingInstance);
    }
    
    @Override
    public ExecutionEntity resolveRepresentativeExecution() {
        return this.eventScopeExecution;
    }
    
    @Override
    public void removeChild(final MigratingScopeInstance migratingScopeInstance) {
        this.childInstances.remove(migratingScopeInstance);
    }
    
    @Override
    public void addChild(final MigratingScopeInstance migratingScopeInstance) {
        if (migratingScopeInstance instanceof MigratingEventScopeInstance) {
            this.childInstances.add((MigratingEventScopeInstance)migratingScopeInstance);
            return;
        }
        throw MigratingEventScopeInstance.MIGRATION_LOGGER.cannotHandleChild(this, migratingScopeInstance);
    }
    
    @Override
    public void addChild(final MigratingCompensationEventSubscriptionInstance migratingEventSubscription) {
        this.childCompensationSubscriptionInstances.add(migratingEventSubscription);
    }
    
    @Override
    public void removeChild(final MigratingCompensationEventSubscriptionInstance migratingEventSubscription) {
        this.childCompensationSubscriptionInstances.remove(migratingEventSubscription);
    }
    
    @Override
    public boolean migrates() {
        return this.targetScope != null;
    }
    
    @Override
    public void detachChildren() {
        final Set<MigratingProcessElementInstance> childrenCopy = new HashSet<MigratingProcessElementInstance>(this.getChildren());
        for (final MigratingProcessElementInstance child : childrenCopy) {
            child.detachState();
        }
    }
    
    @Override
    public void remove(final boolean skipCustomListeners, final boolean skipIoMappings) {
        this.eventScopeExecution.remove();
        this.migratingEventSubscription.remove();
        this.setParent(null);
    }
    
    @Override
    public Collection<MigratingProcessElementInstance> getChildren() {
        final Set<MigratingProcessElementInstance> children = new HashSet<MigratingProcessElementInstance>(this.childInstances);
        children.addAll(this.childCompensationSubscriptionInstances);
        return children;
    }
    
    @Override
    public Collection<MigratingScopeInstance> getChildScopeInstances() {
        return new HashSet<MigratingScopeInstance>(this.childInstances);
    }
    
    @Override
    public void removeUnmappedDependentInstances() {
    }
    
    public MigratingCompensationEventSubscriptionInstance getEventSubscription() {
        return this.migratingEventSubscription;
    }
    
    static {
        MIGRATION_LOGGER = ProcessEngineLogger.MIGRATION_LOGGER;
    }
}
