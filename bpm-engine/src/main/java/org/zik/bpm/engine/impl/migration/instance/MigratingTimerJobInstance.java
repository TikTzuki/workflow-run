// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import org.zik.bpm.engine.impl.persistence.entity.TimerEntity;
import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.jobexecutor.TimerEventJobHandler;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.jobexecutor.TimerDeclarationImpl;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;

public class MigratingTimerJobInstance extends MigratingJobInstance
{
    protected ScopeImpl timerTriggerTargetScope;
    protected TimerDeclarationImpl targetJobDeclaration;
    protected boolean updateEvent;
    
    public MigratingTimerJobInstance(final JobEntity jobEntity) {
        super(jobEntity);
    }
    
    public MigratingTimerJobInstance(final JobEntity jobEntity, final JobDefinitionEntity jobDefinitionEntity, final ScopeImpl targetScope, final boolean updateEvent, final TimerDeclarationImpl targetTimerDeclaration) {
        super(jobEntity, jobDefinitionEntity, targetScope);
        this.timerTriggerTargetScope = this.determineTimerTriggerTargetScope(jobEntity, targetScope);
        this.updateEvent = updateEvent;
        this.targetJobDeclaration = targetTimerDeclaration;
    }
    
    protected ScopeImpl determineTimerTriggerTargetScope(final JobEntity jobEntity, final ScopeImpl targetScope) {
        if ("timer-start-event-subprocess".equals(jobEntity.getJobHandlerType())) {
            return targetScope.getFlowScope();
        }
        return targetScope;
    }
    
    @Override
    protected void migrateJobHandlerConfiguration() {
        final TimerEventJobHandler.TimerJobConfiguration configuration = (TimerEventJobHandler.TimerJobConfiguration)this.jobEntity.getJobHandlerConfiguration();
        configuration.setTimerElementKey(this.timerTriggerTargetScope.getId());
        this.jobEntity.setJobHandlerConfiguration(configuration);
        if (this.updateEvent) {
            this.targetJobDeclaration.updateJob((TimerEntity)this.jobEntity);
        }
    }
}
