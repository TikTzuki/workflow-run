// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import org.zik.bpm.engine.impl.pvm.process.TransitionImpl;
import java.util.List;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.pvm.PvmTransition;
import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.runtime.operation.PvmAtomicOperation;
import org.zik.bpm.engine.management.JobDefinition;
import org.zik.bpm.engine.impl.jobexecutor.AsyncContinuationJobHandler;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;

public class MigratingAsyncJobInstance extends MigratingJobInstance
{
    public MigratingAsyncJobInstance(final JobEntity jobEntity, final JobDefinitionEntity jobDefinitionEntity, final ScopeImpl targetScope) {
        super(jobEntity, jobDefinitionEntity, targetScope);
    }
    
    @Override
    protected void migrateJobHandlerConfiguration() {
        final AsyncContinuationJobHandler.AsyncContinuationConfiguration configuration = (AsyncContinuationJobHandler.AsyncContinuationConfiguration)this.jobEntity.getJobHandlerConfiguration();
        if (this.isAsyncAfter()) {
            this.updateAsyncAfterTargetConfiguration(configuration);
        }
        else {
            this.updateAsyncBeforeTargetConfiguration();
        }
    }
    
    public boolean isAsyncAfter() {
        final JobDefinition jobDefinition = this.jobEntity.getJobDefinition();
        return "async-after".equals(jobDefinition.getJobConfiguration());
    }
    
    public boolean isAsyncBefore() {
        return !this.isAsyncAfter();
    }
    
    protected void updateAsyncBeforeTargetConfiguration() {
        final AsyncContinuationJobHandler.AsyncContinuationConfiguration targetConfiguration = new AsyncContinuationJobHandler.AsyncContinuationConfiguration();
        final AsyncContinuationJobHandler.AsyncContinuationConfiguration currentConfiguration = (AsyncContinuationJobHandler.AsyncContinuationConfiguration)this.jobEntity.getJobHandlerConfiguration();
        if (PvmAtomicOperation.PROCESS_START.getCanonicalName().equals(currentConfiguration.getAtomicOperation())) {
            targetConfiguration.setAtomicOperation(PvmAtomicOperation.PROCESS_START.getCanonicalName());
        }
        else if (((ActivityImpl)this.targetScope).getIncomingTransitions().isEmpty()) {
            targetConfiguration.setAtomicOperation(PvmAtomicOperation.ACTIVITY_START_CREATE_SCOPE.getCanonicalName());
        }
        else {
            targetConfiguration.setAtomicOperation(PvmAtomicOperation.TRANSITION_CREATE_SCOPE.getCanonicalName());
        }
        this.jobEntity.setJobHandlerConfiguration(targetConfiguration);
    }
    
    protected void updateAsyncAfterTargetConfiguration(final AsyncContinuationJobHandler.AsyncContinuationConfiguration currentConfiguration) {
        final ActivityImpl targetActivity = (ActivityImpl)this.targetScope;
        final List<PvmTransition> outgoingTransitions = targetActivity.getOutgoingTransitions();
        final AsyncContinuationJobHandler.AsyncContinuationConfiguration targetConfiguration = new AsyncContinuationJobHandler.AsyncContinuationConfiguration();
        if (outgoingTransitions.isEmpty()) {
            targetConfiguration.setAtomicOperation(PvmAtomicOperation.ACTIVITY_END.getCanonicalName());
        }
        else {
            targetConfiguration.setAtomicOperation(PvmAtomicOperation.TRANSITION_NOTIFY_LISTENER_TAKE.getCanonicalName());
            if (outgoingTransitions.size() == 1) {
                targetConfiguration.setTransitionId(outgoingTransitions.get(0).getId());
            }
            else {
                TransitionImpl matchingTargetTransition = null;
                final String currentTransitionId = currentConfiguration.getTransitionId();
                if (currentTransitionId != null) {
                    matchingTargetTransition = targetActivity.findOutgoingTransition(currentTransitionId);
                }
                if (matchingTargetTransition == null) {
                    throw new ProcessEngineException("Cannot determine matching outgoing sequence flow");
                }
                targetConfiguration.setTransitionId(matchingTargetTransition.getId());
            }
        }
        this.jobEntity.setJobHandlerConfiguration(targetConfiguration);
    }
}
