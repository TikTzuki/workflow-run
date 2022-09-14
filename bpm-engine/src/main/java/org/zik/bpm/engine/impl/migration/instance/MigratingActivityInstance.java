// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import org.zik.bpm.engine.impl.pvm.delegate.ModificationObserverBehavior;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.pvm.delegate.MigrationObserverBehavior;
import org.zik.bpm.engine.impl.pvm.delegate.CompositeActivityBehavior;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.impl.history.event.HistoryEventProcessor;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.DelegateExecution;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.migration.MigrationInstruction;
import java.util.Set;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.runtime.ActivityInstance;
import org.zik.bpm.engine.impl.migration.MigrationLogger;

public class MigratingActivityInstance extends MigratingScopeInstance implements MigratingInstance
{
    public static final MigrationLogger MIGRATION_LOGGER;
    protected ActivityInstance activityInstance;
    protected ExecutionEntity representativeExecution;
    protected boolean activeState;
    protected List<RemovingInstance> removingDependentInstances;
    protected List<MigratingInstance> migratingDependentInstances;
    protected List<EmergingInstance> emergingDependentInstances;
    protected Set<MigratingActivityInstance> childActivityInstances;
    protected Set<MigratingTransitionInstance> childTransitionInstances;
    protected Set<MigratingEventScopeInstance> childCompensationInstances;
    protected Set<MigratingCompensationEventSubscriptionInstance> childCompensationSubscriptionInstances;
    protected MigratingActivityInstanceBehavior instanceBehavior;
    
    public MigratingActivityInstance(final ActivityInstance activityInstance, final MigrationInstruction migrationInstruction, final ScopeImpl sourceScope, final ScopeImpl targetScope, final ExecutionEntity scopeExecution) {
        this.removingDependentInstances = new ArrayList<RemovingInstance>();
        this.migratingDependentInstances = new ArrayList<MigratingInstance>();
        this.emergingDependentInstances = new ArrayList<EmergingInstance>();
        this.childActivityInstances = new HashSet<MigratingActivityInstance>();
        this.childTransitionInstances = new HashSet<MigratingTransitionInstance>();
        this.childCompensationInstances = new HashSet<MigratingEventScopeInstance>();
        this.childCompensationSubscriptionInstances = new HashSet<MigratingCompensationEventSubscriptionInstance>();
        this.activityInstance = activityInstance;
        this.migrationInstruction = migrationInstruction;
        this.sourceScope = sourceScope;
        this.currentScope = sourceScope;
        this.targetScope = targetScope;
        this.representativeExecution = scopeExecution;
        this.instanceBehavior = this.determineBehavior(sourceScope);
        if (activityInstance.getChildActivityInstances().length == 0 && activityInstance.getChildTransitionInstances().length == 0) {
            this.activeState = this.representativeExecution.isActive();
        }
    }
    
    public MigratingActivityInstance(final ScopeImpl targetScope, final ExecutionEntity scopeExecution) {
        this.removingDependentInstances = new ArrayList<RemovingInstance>();
        this.migratingDependentInstances = new ArrayList<MigratingInstance>();
        this.emergingDependentInstances = new ArrayList<EmergingInstance>();
        this.childActivityInstances = new HashSet<MigratingActivityInstance>();
        this.childTransitionInstances = new HashSet<MigratingTransitionInstance>();
        this.childCompensationInstances = new HashSet<MigratingEventScopeInstance>();
        this.childCompensationSubscriptionInstances = new HashSet<MigratingCompensationEventSubscriptionInstance>();
        this.targetScope = targetScope;
        this.currentScope = targetScope;
        this.representativeExecution = scopeExecution;
        this.instanceBehavior = this.determineBehavior(targetScope);
    }
    
    protected MigratingActivityInstanceBehavior determineBehavior(final ScopeImpl scope) {
        if (scope.isScope()) {
            return new MigratingScopeActivityInstanceBehavior();
        }
        return new MigratingNonScopeActivityInstanceBehavior();
    }
    
    @Override
    public void detachChildren() {
        final Set<MigratingActivityInstance> childrenCopy = new HashSet<MigratingActivityInstance>(this.childActivityInstances);
        for (final MigratingActivityInstance child : childrenCopy) {
            child.detachDependentInstances();
        }
        for (final MigratingActivityInstance child : childrenCopy) {
            child.detachState();
        }
        final Set<MigratingTransitionInstance> transitionChildrenCopy = new HashSet<MigratingTransitionInstance>(this.childTransitionInstances);
        for (final MigratingTransitionInstance child2 : transitionChildrenCopy) {
            child2.detachState();
        }
        final Set<MigratingEventScopeInstance> compensationChildrenCopy = new HashSet<MigratingEventScopeInstance>(this.childCompensationInstances);
        for (final MigratingEventScopeInstance child3 : compensationChildrenCopy) {
            child3.detachState();
        }
        final Set<MigratingCompensationEventSubscriptionInstance> compensationSubscriptionsChildrenCopy = new HashSet<MigratingCompensationEventSubscriptionInstance>(this.childCompensationSubscriptionInstances);
        for (final MigratingCompensationEventSubscriptionInstance child4 : compensationSubscriptionsChildrenCopy) {
            child4.detachState();
        }
    }
    
    public void detachDependentInstances() {
        for (final MigratingInstance dependentInstance : this.migratingDependentInstances) {
            if (!dependentInstance.isDetached()) {
                dependentInstance.detachState();
            }
        }
    }
    
    @Override
    public boolean isDetached() {
        return this.instanceBehavior.isDetached();
    }
    
    @Override
    public void detachState() {
        this.detachDependentInstances();
        this.instanceBehavior.detachState();
        this.setParent(null);
    }
    
    @Override
    public void attachState(final MigratingScopeInstance activityInstance) {
        this.setParent(activityInstance);
        this.instanceBehavior.attachState();
        for (final MigratingInstance dependentInstance : this.migratingDependentInstances) {
            dependentInstance.attachState(this);
        }
    }
    
    @Override
    public void attachState(final MigratingTransitionInstance targetTransitionInstance) {
        throw MigratingActivityInstance.MIGRATION_LOGGER.cannotAttachToTransitionInstance(this);
    }
    
    @Override
    public void migrateDependentEntities() {
        for (final MigratingInstance migratingInstance : this.migratingDependentInstances) {
            migratingInstance.migrateState();
            migratingInstance.migrateDependentEntities();
        }
        final ExecutionEntity representativeExecution = this.resolveRepresentativeExecution();
        for (final EmergingInstance emergingInstance : this.emergingDependentInstances) {
            emergingInstance.create(representativeExecution);
        }
    }
    
    @Override
    public ExecutionEntity resolveRepresentativeExecution() {
        return this.instanceBehavior.resolveRepresentativeExecution();
    }
    
    @Override
    public void addMigratingDependentInstance(final MigratingInstance migratingInstance) {
        this.migratingDependentInstances.add(migratingInstance);
    }
    
    public List<MigratingInstance> getMigratingDependentInstances() {
        return this.migratingDependentInstances;
    }
    
    public void addRemovingDependentInstance(final RemovingInstance removingInstance) {
        this.removingDependentInstances.add(removingInstance);
    }
    
    public void addEmergingDependentInstance(final EmergingInstance emergingInstance) {
        this.emergingDependentInstances.add(emergingInstance);
    }
    
    public void addChild(final MigratingTransitionInstance transitionInstance) {
        this.childTransitionInstances.add(transitionInstance);
    }
    
    public void removeChild(final MigratingTransitionInstance transitionInstance) {
        this.childTransitionInstances.remove(transitionInstance);
    }
    
    public void addChild(final MigratingActivityInstance activityInstance) {
        this.childActivityInstances.add(activityInstance);
    }
    
    public void removeChild(final MigratingActivityInstance activityInstance) {
        this.childActivityInstances.remove(activityInstance);
    }
    
    @Override
    public void addChild(final MigratingScopeInstance migratingActivityInstance) {
        if (migratingActivityInstance instanceof MigratingActivityInstance) {
            this.addChild((MigratingActivityInstance)migratingActivityInstance);
        }
        else {
            if (!(migratingActivityInstance instanceof MigratingEventScopeInstance)) {
                throw MigratingActivityInstance.MIGRATION_LOGGER.cannotHandleChild(this, migratingActivityInstance);
            }
            this.addChild((MigratingEventScopeInstance)migratingActivityInstance);
        }
    }
    
    @Override
    public void removeChild(final MigratingScopeInstance child) {
        if (child instanceof MigratingActivityInstance) {
            this.removeChild((MigratingActivityInstance)child);
        }
        else {
            if (!(child instanceof MigratingEventScopeInstance)) {
                throw MigratingActivityInstance.MIGRATION_LOGGER.cannotHandleChild(this, child);
            }
            this.removeChild((MigratingEventScopeInstance)child);
        }
    }
    
    public void addChild(final MigratingEventScopeInstance compensationInstance) {
        this.childCompensationInstances.add(compensationInstance);
    }
    
    public void removeChild(final MigratingEventScopeInstance compensationInstance) {
        this.childCompensationInstances.remove(compensationInstance);
    }
    
    @Override
    public void addChild(final MigratingCompensationEventSubscriptionInstance migratingEventSubscription) {
        this.childCompensationSubscriptionInstances.add(migratingEventSubscription);
    }
    
    @Override
    public void removeChild(final MigratingCompensationEventSubscriptionInstance migratingEventSubscription) {
        this.childCompensationSubscriptionInstances.remove(migratingEventSubscription);
    }
    
    public ActivityInstance getActivityInstance() {
        return this.activityInstance;
    }
    
    public String getActivityInstanceId() {
        if (this.activityInstance != null) {
            return this.activityInstance.getId();
        }
        final ExecutionEntity execution = this.resolveRepresentativeExecution();
        return execution.getParentActivityInstanceId();
    }
    
    @Override
    public MigratingActivityInstance getParent() {
        return (MigratingActivityInstance)super.getParent();
    }
    
    @Override
    public Set<MigratingProcessElementInstance> getChildren() {
        final Set<MigratingProcessElementInstance> childInstances = new HashSet<MigratingProcessElementInstance>();
        childInstances.addAll(this.childActivityInstances);
        childInstances.addAll(this.childTransitionInstances);
        childInstances.addAll(this.childCompensationInstances);
        childInstances.addAll(this.childCompensationSubscriptionInstances);
        return childInstances;
    }
    
    @Override
    public Collection<MigratingScopeInstance> getChildScopeInstances() {
        final Set<MigratingScopeInstance> childInstances = new HashSet<MigratingScopeInstance>();
        childInstances.addAll(this.childActivityInstances);
        childInstances.addAll(this.childCompensationInstances);
        return childInstances;
    }
    
    public Set<MigratingActivityInstance> getChildActivityInstances() {
        return this.childActivityInstances;
    }
    
    public Set<MigratingTransitionInstance> getChildTransitionInstances() {
        return this.childTransitionInstances;
    }
    
    public Set<MigratingEventScopeInstance> getChildCompensationInstances() {
        return this.childCompensationInstances;
    }
    
    @Override
    public boolean migrates() {
        return this.targetScope != null;
    }
    
    @Override
    public void removeUnmappedDependentInstances() {
        for (final RemovingInstance removingInstance : this.removingDependentInstances) {
            removingInstance.remove();
        }
    }
    
    @Override
    public void remove(final boolean skipCustomListeners, final boolean skipIoMappings) {
        this.instanceBehavior.remove(skipCustomListeners, skipIoMappings);
    }
    
    @Override
    public void migrateState() {
        this.instanceBehavior.migrateState();
    }
    
    protected void migrateHistory(final DelegateExecution execution) {
        if (this.activityInstance.getId().equals(this.activityInstance.getProcessInstanceId())) {
            this.migrateProcessInstanceHistory(execution);
        }
        else {
            this.migrateActivityInstanceHistory(execution);
        }
    }
    
    protected void migrateProcessInstanceHistory(final DelegateExecution execution) {
        final HistoryLevel historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
        if (!historyLevel.isHistoryEventProduced(HistoryEventTypes.PROCESS_INSTANCE_MIGRATE, this)) {
            return;
        }
        HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
            @Override
            public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                return producer.createProcessInstanceMigrateEvt(execution);
            }
        });
    }
    
    protected void migrateActivityInstanceHistory(final DelegateExecution execution) {
        final HistoryLevel historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
        if (!historyLevel.isHistoryEventProduced(HistoryEventTypes.ACTIVITY_INSTANCE_MIGRATE, this)) {
            return;
        }
        HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
            @Override
            public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                return producer.createActivityInstanceMigrateEvt(MigratingActivityInstance.this);
            }
        });
    }
    
    public ExecutionEntity createAttachableExecution() {
        return this.instanceBehavior.createAttachableExecution();
    }
    
    public void destroyAttachableExecution(final ExecutionEntity execution) {
        this.instanceBehavior.destroyAttachableExecution(execution);
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
    
    static {
        MIGRATION_LOGGER = ProcessEngineLogger.MIGRATION_LOGGER;
    }
    
    protected class MigratingNonScopeActivityInstanceBehavior implements MigratingActivityInstanceBehavior
    {
        @Override
        public boolean isDetached() {
            return this.resolveRepresentativeExecution().getActivity() == null;
        }
        
        @Override
        public void detachState() {
            final ExecutionEntity currentExecution = this.resolveRepresentativeExecution();
            currentExecution.setActivity(null);
            currentExecution.leaveActivityInstance();
            currentExecution.setActive(false);
            MigratingActivityInstance.this.getParent().destroyAttachableExecution(currentExecution);
        }
        
        @Override
        public void attachState() {
            (MigratingActivityInstance.this.representativeExecution = MigratingActivityInstance.this.getParent().createAttachableExecution()).setActivity((PvmActivity)MigratingActivityInstance.this.sourceScope);
            MigratingActivityInstance.this.representativeExecution.setActivityInstanceId(MigratingActivityInstance.this.activityInstance.getId());
            MigratingActivityInstance.this.representativeExecution.setActive(MigratingActivityInstance.this.activeState);
        }
        
        @Override
        public void migrateState() {
            final ExecutionEntity currentExecution = this.resolveRepresentativeExecution();
            currentExecution.setProcessDefinition(MigratingActivityInstance.this.targetScope.getProcessDefinition());
            currentExecution.setActivity((PvmActivity)MigratingActivityInstance.this.targetScope);
            MigratingActivityInstance.this.currentScope = MigratingActivityInstance.this.targetScope;
            if (MigratingActivityInstance.this.targetScope.isScope()) {
                this.becomeScope();
            }
            MigratingActivityInstance.this.migrateHistory(currentExecution);
        }
        
        protected void becomeScope() {
            for (final MigratingInstance dependentInstance : MigratingActivityInstance.this.migratingDependentInstances) {
                dependentInstance.detachState();
            }
            ExecutionEntity currentExecution = this.resolveRepresentativeExecution();
            currentExecution = currentExecution.createExecution();
            final ExecutionEntity parent = currentExecution.getParent();
            parent.setActivity(null);
            if (!parent.isConcurrent()) {
                parent.leaveActivityInstance();
            }
            MigratingActivityInstance.this.representativeExecution = currentExecution;
            for (final MigratingInstance dependentInstance2 : MigratingActivityInstance.this.migratingDependentInstances) {
                dependentInstance2.attachState(MigratingActivityInstance.this);
            }
            MigratingActivityInstance.this.instanceBehavior = new MigratingScopeActivityInstanceBehavior();
        }
        
        @Override
        public ExecutionEntity resolveRepresentativeExecution() {
            if (MigratingActivityInstance.this.representativeExecution.getReplacedBy() != null) {
                return MigratingActivityInstance.this.representativeExecution.resolveReplacedBy();
            }
            return MigratingActivityInstance.this.representativeExecution;
        }
        
        @Override
        public void remove(final boolean skipCustomListeners, final boolean skipIoMappings) {
        }
        
        @Override
        public ExecutionEntity createAttachableExecution() {
            throw MigratingActivityInstance.MIGRATION_LOGGER.cannotBecomeSubordinateInNonScope(MigratingActivityInstance.this);
        }
        
        @Override
        public void destroyAttachableExecution(final ExecutionEntity execution) {
            throw MigratingActivityInstance.MIGRATION_LOGGER.cannotDestroySubordinateInNonScope(MigratingActivityInstance.this);
        }
    }
    
    protected class MigratingScopeActivityInstanceBehavior implements MigratingActivityInstanceBehavior
    {
        @Override
        public boolean isDetached() {
            final ExecutionEntity representativeExecution = this.resolveRepresentativeExecution();
            return representativeExecution != representativeExecution.getProcessInstance() && representativeExecution.getParent() == null;
        }
        
        @Override
        public void detachState() {
            final ExecutionEntity currentScopeExecution = this.resolveRepresentativeExecution();
            final ExecutionEntity parentExecution = currentScopeExecution.getParent();
            currentScopeExecution.setParent(null);
            if (MigratingActivityInstance.this.sourceScope.getActivityBehavior() instanceof CompositeActivityBehavior) {
                parentExecution.leaveActivityInstance();
            }
            MigratingActivityInstance.this.getParent().destroyAttachableExecution(parentExecution);
        }
        
        @Override
        public void attachState() {
            final ExecutionEntity newParentExecution = MigratingActivityInstance.this.getParent().createAttachableExecution();
            final ExecutionEntity currentScopeExecution = this.resolveRepresentativeExecution();
            currentScopeExecution.setParent(newParentExecution);
            if (MigratingActivityInstance.this.sourceScope.getActivityBehavior() instanceof CompositeActivityBehavior) {
                newParentExecution.setActivityInstanceId(MigratingActivityInstance.this.activityInstance.getId());
            }
        }
        
        @Override
        public void migrateState() {
            ExecutionEntity currentScopeExecution = this.resolveRepresentativeExecution();
            currentScopeExecution.setProcessDefinition(MigratingActivityInstance.this.targetScope.getProcessDefinition());
            final ExecutionEntity parentExecution = currentScopeExecution.getParent();
            if (parentExecution != null && parentExecution.isConcurrent()) {
                parentExecution.setProcessDefinition(MigratingActivityInstance.this.targetScope.getProcessDefinition());
            }
            MigratingActivityInstance.this.currentScope = MigratingActivityInstance.this.targetScope;
            if (!MigratingActivityInstance.this.targetScope.isScope()) {
                this.becomeNonScope();
                currentScopeExecution = this.resolveRepresentativeExecution();
            }
            if (this.isLeafActivity(MigratingActivityInstance.this.targetScope)) {
                currentScopeExecution.setActivity((PvmActivity)MigratingActivityInstance.this.targetScope);
            }
            if (MigratingActivityInstance.this.sourceScope.getActivityBehavior() instanceof MigrationObserverBehavior) {
                ((MigrationObserverBehavior)MigratingActivityInstance.this.sourceScope.getActivityBehavior()).migrateScope(currentScopeExecution);
            }
            MigratingActivityInstance.this.migrateHistory(currentScopeExecution);
        }
        
        protected void becomeNonScope() {
            for (final MigratingInstance dependentInstance : MigratingActivityInstance.this.migratingDependentInstances) {
                dependentInstance.detachState();
            }
            final ExecutionEntity parentExecution = MigratingActivityInstance.this.representativeExecution.getParent();
            parentExecution.setActivity(MigratingActivityInstance.this.representativeExecution.getActivity());
            parentExecution.setActivityInstanceId(MigratingActivityInstance.this.representativeExecution.getActivityInstanceId());
            parentExecution.setActive(MigratingActivityInstance.this.representativeExecution.isActive());
            MigratingActivityInstance.this.representativeExecution.remove();
            MigratingActivityInstance.this.representativeExecution = parentExecution;
            for (final MigratingInstance dependentInstance2 : MigratingActivityInstance.this.migratingDependentInstances) {
                dependentInstance2.attachState(MigratingActivityInstance.this);
            }
            MigratingActivityInstance.this.instanceBehavior = new MigratingNonScopeActivityInstanceBehavior();
        }
        
        protected boolean isLeafActivity(final ScopeImpl scope) {
            return scope.getActivities().isEmpty();
        }
        
        @Override
        public ExecutionEntity resolveRepresentativeExecution() {
            return MigratingActivityInstance.this.representativeExecution;
        }
        
        @Override
        public void remove(final boolean skipCustomListeners, final boolean skipIoMappings) {
            final ExecutionEntity currentExecution = this.resolveRepresentativeExecution();
            final ExecutionEntity parentExecution = currentExecution.getParent();
            currentExecution.setActivity((PvmActivity)MigratingActivityInstance.this.sourceScope);
            currentExecution.setActivityInstanceId(MigratingActivityInstance.this.activityInstance.getId());
            currentExecution.deleteCascade("migration", skipCustomListeners, skipIoMappings);
            MigratingActivityInstance.this.getParent().destroyAttachableExecution(parentExecution);
            MigratingActivityInstance.this.setParent(null);
            for (final MigratingTransitionInstance child : MigratingActivityInstance.this.childTransitionInstances) {
                child.setParent(null);
            }
            for (final MigratingActivityInstance child2 : MigratingActivityInstance.this.childActivityInstances) {
                child2.setParent(null);
            }
            for (final MigratingEventScopeInstance child3 : MigratingActivityInstance.this.childCompensationInstances) {
                child3.setParent(null);
            }
        }
        
        @Override
        public ExecutionEntity createAttachableExecution() {
            ExecutionEntity attachableExecution;
            final ExecutionEntity scopeExecution = attachableExecution = this.resolveRepresentativeExecution();
            if (MigratingActivityInstance.this.currentScope.getActivityBehavior() instanceof ModificationObserverBehavior) {
                final ModificationObserverBehavior behavior = (ModificationObserverBehavior)MigratingActivityInstance.this.currentScope.getActivityBehavior();
                attachableExecution = (ExecutionEntity)behavior.createInnerInstance(scopeExecution);
            }
            else if (!scopeExecution.getNonEventScopeExecutions().isEmpty() || scopeExecution.getActivity() != null) {
                attachableExecution = (ExecutionEntity)scopeExecution.createConcurrentExecution();
                attachableExecution.setActive(false);
                scopeExecution.forceUpdate();
            }
            return attachableExecution;
        }
        
        @Override
        public void destroyAttachableExecution(final ExecutionEntity execution) {
            if (MigratingActivityInstance.this.currentScope.getActivityBehavior() instanceof ModificationObserverBehavior) {
                final ModificationObserverBehavior behavior = (ModificationObserverBehavior)MigratingActivityInstance.this.currentScope.getActivityBehavior();
                behavior.destroyInnerInstance(execution);
            }
            else if (execution.isConcurrent()) {
                execution.remove();
                execution.getParent().tryPruneLastConcurrentChild();
                execution.getParent().forceUpdate();
            }
        }
    }
    
    protected interface MigratingActivityInstanceBehavior
    {
        boolean isDetached();
        
        void detachState();
        
        void attachState();
        
        void migrateState();
        
        void remove(final boolean p0, final boolean p1);
        
        ExecutionEntity resolveRepresentativeExecution();
        
        ExecutionEntity createAttachableExecution();
        
        void destroyAttachableExecution(final ExecutionEntity p0);
    }
}
