// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.management.JobDefinition;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import java.util.ArrayList;
import java.util.List;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;

public abstract class MigratingJobInstance implements MigratingInstance, RemovingInstance
{
    protected JobEntity jobEntity;
    protected JobDefinitionEntity targetJobDefinitionEntity;
    protected ScopeImpl targetScope;
    protected List<MigratingInstance> migratingDependentInstances;
    
    public MigratingJobInstance(final JobEntity jobEntity, final JobDefinitionEntity jobDefinitionEntity, final ScopeImpl targetScope) {
        this.migratingDependentInstances = new ArrayList<MigratingInstance>();
        this.jobEntity = jobEntity;
        this.targetJobDefinitionEntity = jobDefinitionEntity;
        this.targetScope = targetScope;
    }
    
    public MigratingJobInstance(final JobEntity jobEntity) {
        this(jobEntity, null, null);
    }
    
    public JobEntity getJobEntity() {
        return this.jobEntity;
    }
    
    public void addMigratingDependentInstance(final MigratingInstance migratingInstance) {
        this.migratingDependentInstances.add(migratingInstance);
    }
    
    @Override
    public boolean isDetached() {
        return this.jobEntity.getExecutionId() == null;
    }
    
    @Override
    public void detachState() {
        this.jobEntity.setExecution(null);
        for (final MigratingInstance dependentInstance : this.migratingDependentInstances) {
            dependentInstance.detachState();
        }
    }
    
    @Override
    public void attachState(final MigratingScopeInstance newOwningInstance) {
        this.attachTo(newOwningInstance.resolveRepresentativeExecution());
        for (final MigratingInstance dependentInstance : this.migratingDependentInstances) {
            dependentInstance.attachState(newOwningInstance);
        }
    }
    
    @Override
    public void attachState(final MigratingTransitionInstance targetTransitionInstance) {
        this.attachTo(targetTransitionInstance.resolveRepresentativeExecution());
        for (final MigratingInstance dependentInstance : this.migratingDependentInstances) {
            dependentInstance.attachState(targetTransitionInstance);
        }
    }
    
    protected void attachTo(final ExecutionEntity execution) {
        this.jobEntity.setExecution(execution);
    }
    
    @Override
    public void migrateState() {
        final String activityId = this.targetScope.getId();
        this.jobEntity.setActivityId(activityId);
        this.migrateJobHandlerConfiguration();
        if (this.targetJobDefinitionEntity != null) {
            this.jobEntity.setJobDefinition(this.targetJobDefinitionEntity);
        }
        final ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity)this.targetScope.getProcessDefinition();
        this.jobEntity.setProcessDefinitionId(processDefinition.getId());
        this.jobEntity.setProcessDefinitionKey(processDefinition.getKey());
        this.jobEntity.setDeploymentId(processDefinition.getDeploymentId());
    }
    
    @Override
    public void migrateDependentEntities() {
        for (final MigratingInstance migratingDependentInstance : this.migratingDependentInstances) {
            migratingDependentInstance.migrateState();
        }
    }
    
    @Override
    public void remove() {
        this.jobEntity.delete();
    }
    
    public boolean migrates() {
        return this.targetScope != null;
    }
    
    public ScopeImpl getTargetScope() {
        return this.targetScope;
    }
    
    public JobDefinitionEntity getTargetJobDefinitionEntity() {
        return this.targetJobDefinitionEntity;
    }
    
    protected abstract void migrateJobHandlerConfiguration();
}
