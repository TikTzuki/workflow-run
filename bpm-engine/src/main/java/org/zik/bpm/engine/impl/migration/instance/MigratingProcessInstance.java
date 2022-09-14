// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.zik.bpm.engine.runtime.TransitionInstance;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.runtime.ActivityInstance;
import org.zik.bpm.engine.migration.MigrationInstruction;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import java.util.List;
import org.zik.bpm.engine.impl.migration.MigrationLogger;

public class MigratingProcessInstance
{
    protected static final MigrationLogger LOGGER;
    protected String processInstanceId;
    protected List<MigratingActivityInstance> migratingActivityInstances;
    protected List<MigratingTransitionInstance> migratingTransitionInstances;
    protected List<MigratingEventScopeInstance> migratingEventScopeInstances;
    protected List<MigratingCompensationEventSubscriptionInstance> migratingCompensationSubscriptionInstances;
    protected MigratingActivityInstance rootInstance;
    protected ProcessDefinitionEntity sourceDefinition;
    protected ProcessDefinitionEntity targetDefinition;
    
    public MigratingProcessInstance(final String processInstanceId, final ProcessDefinitionEntity sourceDefinition, final ProcessDefinitionEntity targetDefinition) {
        this.processInstanceId = processInstanceId;
        this.migratingActivityInstances = new ArrayList<MigratingActivityInstance>();
        this.migratingTransitionInstances = new ArrayList<MigratingTransitionInstance>();
        this.migratingEventScopeInstances = new ArrayList<MigratingEventScopeInstance>();
        this.migratingCompensationSubscriptionInstances = new ArrayList<MigratingCompensationEventSubscriptionInstance>();
        this.sourceDefinition = sourceDefinition;
        this.targetDefinition = targetDefinition;
    }
    
    public MigratingActivityInstance getRootInstance() {
        return this.rootInstance;
    }
    
    public void setRootInstance(final MigratingActivityInstance rootInstance) {
        this.rootInstance = rootInstance;
    }
    
    public Collection<MigratingActivityInstance> getMigratingActivityInstances() {
        return this.migratingActivityInstances;
    }
    
    public Collection<MigratingTransitionInstance> getMigratingTransitionInstances() {
        return this.migratingTransitionInstances;
    }
    
    public Collection<MigratingEventScopeInstance> getMigratingEventScopeInstances() {
        return this.migratingEventScopeInstances;
    }
    
    public Collection<MigratingCompensationEventSubscriptionInstance> getMigratingCompensationSubscriptionInstances() {
        return this.migratingCompensationSubscriptionInstances;
    }
    
    public Collection<MigratingScopeInstance> getMigratingScopeInstances() {
        final Set<MigratingScopeInstance> result = new HashSet<MigratingScopeInstance>();
        result.addAll(this.migratingActivityInstances);
        result.addAll(this.migratingEventScopeInstances);
        return result;
    }
    
    public ProcessDefinitionEntity getSourceDefinition() {
        return this.sourceDefinition;
    }
    
    public ProcessDefinitionEntity getTargetDefinition() {
        return this.targetDefinition;
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public MigratingActivityInstance addActivityInstance(final MigrationInstruction migrationInstruction, final ActivityInstance activityInstance, final ScopeImpl sourceScope, final ScopeImpl targetScope, final ExecutionEntity scopeExecution) {
        final MigratingActivityInstance migratingActivityInstance = new MigratingActivityInstance(activityInstance, migrationInstruction, sourceScope, targetScope, scopeExecution);
        this.migratingActivityInstances.add(migratingActivityInstance);
        if (this.processInstanceId.equals(activityInstance.getId())) {
            this.rootInstance = migratingActivityInstance;
        }
        return migratingActivityInstance;
    }
    
    public MigratingTransitionInstance addTransitionInstance(final MigrationInstruction migrationInstruction, final TransitionInstance transitionInstance, final ScopeImpl sourceScope, final ScopeImpl targetScope, final ExecutionEntity asyncExecution) {
        final MigratingTransitionInstance migratingTransitionInstance = new MigratingTransitionInstance(transitionInstance, migrationInstruction, sourceScope, targetScope, asyncExecution);
        this.migratingTransitionInstances.add(migratingTransitionInstance);
        return migratingTransitionInstance;
    }
    
    public MigratingEventScopeInstance addEventScopeInstance(final MigrationInstruction migrationInstruction, final ExecutionEntity eventScopeExecution, final ScopeImpl sourceScope, final ScopeImpl targetScope, final MigrationInstruction eventSubscriptionInstruction, final EventSubscriptionEntity eventSubscription, final ScopeImpl eventSubscriptionSourceScope, final ScopeImpl eventSubscriptionTargetScope) {
        final MigratingEventScopeInstance compensationInstance = new MigratingEventScopeInstance(migrationInstruction, eventScopeExecution, sourceScope, targetScope, eventSubscriptionInstruction, eventSubscription, eventSubscriptionSourceScope, eventSubscriptionTargetScope);
        this.migratingEventScopeInstances.add(compensationInstance);
        return compensationInstance;
    }
    
    public MigratingCompensationEventSubscriptionInstance addCompensationSubscriptionInstance(final MigrationInstruction eventSubscriptionInstruction, final EventSubscriptionEntity eventSubscription, final ScopeImpl sourceScope, final ScopeImpl targetScope) {
        final MigratingCompensationEventSubscriptionInstance compensationInstance = new MigratingCompensationEventSubscriptionInstance(eventSubscriptionInstruction, sourceScope, targetScope, eventSubscription);
        this.migratingCompensationSubscriptionInstances.add(compensationInstance);
        return compensationInstance;
    }
    
    static {
        LOGGER = ProcessEngineLogger.MIGRATION_LOGGER;
    }
}
