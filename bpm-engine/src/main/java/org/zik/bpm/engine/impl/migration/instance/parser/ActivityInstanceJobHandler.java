// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance.parser;

import org.zik.bpm.engine.impl.migration.instance.EmergingInstance;
import org.zik.bpm.engine.impl.migration.instance.EmergingJobInstance;
import org.zik.bpm.engine.impl.jobexecutor.TimerEventJobHandler;
import org.zik.bpm.engine.impl.migration.instance.MigratingJobInstance;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionEntity;
import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.migration.MigrationInstruction;
import java.util.Iterator;
import org.zik.bpm.engine.impl.migration.instance.RemovingInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingInstance;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.migration.instance.MigratingTimerJobInstance;
import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.jobexecutor.TimerDeclarationImpl;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import java.util.List;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;

public class ActivityInstanceJobHandler implements MigratingDependentInstanceParseHandler<MigratingActivityInstance, List<JobEntity>>
{
    @Override
    public void handle(final MigratingInstanceParseContext parseContext, final MigratingActivityInstance activityInstance, final List<JobEntity> elements) {
        final Map<String, TimerDeclarationImpl> sourceTimerDeclarationsInEventScope = TimerDeclarationImpl.getDeclarationsForScope(activityInstance.getSourceScope());
        final Map<String, TimerDeclarationImpl> targetTimerDeclarationsInEventScope = new HashMap<String, TimerDeclarationImpl>(TimerDeclarationImpl.getDeclarationsForScope(activityInstance.getTargetScope()));
        final Map<String, Map<String, TimerDeclarationImpl>> sourceTimeoutListenerDeclarationsInEventScope = TimerDeclarationImpl.getTimeoutListenerDeclarationsForScope(activityInstance.getSourceScope());
        final Map<String, Map<String, TimerDeclarationImpl>> targetTimeoutListenerDeclarationsInEventScope = new HashMap<String, Map<String, TimerDeclarationImpl>>(TimerDeclarationImpl.getTimeoutListenerDeclarationsForScope(activityInstance.getTargetScope()));
        for (final JobEntity job : elements) {
            if (!isTimerJob(job)) {
                continue;
            }
            final MigrationInstruction migrationInstruction = parseContext.findSingleMigrationInstruction(job.getActivityId());
            final ActivityImpl targetActivity = parseContext.getTargetActivity(migrationInstruction);
            final JobHandlerConfiguration jobHandlerConfiguration = job.getJobHandlerConfiguration();
            if (targetActivity != null && activityInstance.migratesTo(targetActivity.getEventScope()) && isNoTimeoutListenerOrMigrates(job, jobHandlerConfiguration, targetActivity.getActivityId(), targetTimeoutListenerDeclarationsInEventScope)) {
                final JobDefinitionEntity targetJobDefinitionEntity = parseContext.getTargetJobDefinition(targetActivity.getActivityId(), job.getJobHandlerType());
                final TimerDeclarationImpl targetTimerDeclaration = this.getTargetTimerDeclaration(job, jobHandlerConfiguration, targetActivity.getActivityId(), targetTimeoutListenerDeclarationsInEventScope, targetTimerDeclarationsInEventScope);
                final MigratingJobInstance migratingTimerJobInstance = new MigratingTimerJobInstance(job, targetJobDefinitionEntity, targetActivity, migrationInstruction.isUpdateEventTrigger(), targetTimerDeclaration);
                activityInstance.addMigratingDependentInstance(migratingTimerJobInstance);
                parseContext.submit(migratingTimerJobInstance);
            }
            else {
                final MigratingJobInstance removingJobInstance = new MigratingTimerJobInstance(job);
                activityInstance.addRemovingDependentInstance(removingJobInstance);
                parseContext.submit(removingJobInstance);
            }
            parseContext.consume(job);
        }
        if (activityInstance.migrates()) {
            this.addEmergingTimerJobs(parseContext, activityInstance, sourceTimerDeclarationsInEventScope, targetTimerDeclarationsInEventScope);
            this.addEmergingTimeoutListenerJobs(parseContext, activityInstance, sourceTimeoutListenerDeclarationsInEventScope, targetTimeoutListenerDeclarationsInEventScope);
        }
    }
    
    protected TimerDeclarationImpl getTargetTimerDeclaration(final JobEntity job, final JobHandlerConfiguration jobHandlerConfiguration, final String targetActivity, final Map<String, Map<String, TimerDeclarationImpl>> targetTimeoutListenerDeclarationsInEventScope, final Map<String, TimerDeclarationImpl> targetTimerDeclarationsInEventScope) {
        if (isTimeoutListenerJobInTargetScope(jobHandlerConfiguration, targetActivity, targetTimeoutListenerDeclarationsInEventScope)) {
            return removeTimeoutListenerJobFromTargetScope(jobHandlerConfiguration, targetActivity, targetTimeoutListenerDeclarationsInEventScope);
        }
        return targetTimerDeclarationsInEventScope.remove(targetActivity);
    }
    
    protected static boolean isTimerJob(final JobEntity job) {
        return job != null && job.getType().equals("timer");
    }
    
    protected static boolean isNoTimeoutListenerOrMigrates(final JobEntity job, final JobHandlerConfiguration jobHandlerConfiguration, final String targetActivity, final Map<String, Map<String, TimerDeclarationImpl>> targetTimeoutListenerDeclarationsInEventScope) {
        return !"timer-task-listener".equals(job.getJobHandlerType()) || isTimeoutListenerJobInTargetScope(jobHandlerConfiguration, targetActivity, targetTimeoutListenerDeclarationsInEventScope);
    }
    
    protected static boolean isTimeoutListenerJobInTargetScope(final JobHandlerConfiguration jobHandlerConfiguration, final String targetActivity, final Map<String, Map<String, TimerDeclarationImpl>> targetTimeoutListenerDeclarationsInEventScope) {
        return jobHandlerConfiguration instanceof TimerEventJobHandler.TimerJobConfiguration && targetTimeoutListenerDeclarationsInEventScope.containsKey(targetActivity) && targetTimeoutListenerDeclarationsInEventScope.get(targetActivity).containsKey(((TimerEventJobHandler.TimerJobConfiguration)jobHandlerConfiguration).getTimerElementSecondaryKey());
    }
    
    protected static TimerDeclarationImpl removeTimeoutListenerJobFromTargetScope(final JobHandlerConfiguration jobHandlerConfiguration, final String targetActivity, final Map<String, Map<String, TimerDeclarationImpl>> targetTimeoutListenerDeclarationsInEventScope) {
        if (isTimeoutListenerJobInTargetScope(jobHandlerConfiguration, targetActivity, targetTimeoutListenerDeclarationsInEventScope)) {
            final Map<String, TimerDeclarationImpl> activityDeclarations = targetTimeoutListenerDeclarationsInEventScope.get(targetActivity);
            final TimerDeclarationImpl declaration = activityDeclarations.remove(((TimerEventJobHandler.TimerJobConfiguration)jobHandlerConfiguration).getTimerElementSecondaryKey());
            if (activityDeclarations.isEmpty()) {
                targetTimeoutListenerDeclarationsInEventScope.remove(targetActivity);
            }
            return declaration;
        }
        return null;
    }
    
    protected void addEmergingTimerJobs(final MigratingInstanceParseContext parseContext, final MigratingActivityInstance activityInstance, final Map<String, TimerDeclarationImpl> sourceTimerDeclarationsInEventScope, final Map<String, TimerDeclarationImpl> targetTimerDeclarationsInEventScope) {
        for (final TimerDeclarationImpl targetTimerDeclaration : targetTimerDeclarationsInEventScope.values()) {
            if (!this.isNonInterruptingTimerTriggeredAlready(parseContext, sourceTimerDeclarationsInEventScope, targetTimerDeclaration)) {
                activityInstance.addEmergingDependentInstance(new EmergingJobInstance(targetTimerDeclaration));
            }
        }
    }
    
    protected void addEmergingTimeoutListenerJobs(final MigratingInstanceParseContext parseContext, final MigratingActivityInstance activityInstance, final Map<String, Map<String, TimerDeclarationImpl>> sourceTimeoutListenerDeclarationsInEventScope, final Map<String, Map<String, TimerDeclarationImpl>> targetTimeoutListenerDeclarationsInEventScope) {
        for (final Map<String, TimerDeclarationImpl> targetTimerDeclarations : targetTimeoutListenerDeclarationsInEventScope.values()) {
            for (final Map.Entry<String, TimerDeclarationImpl> targetTimerDeclaration : targetTimerDeclarations.entrySet()) {
                if (!this.isNonInterruptingTimeoutListenerTriggeredAlready(parseContext, sourceTimeoutListenerDeclarationsInEventScope, targetTimerDeclaration)) {
                    activityInstance.addEmergingDependentInstance(new EmergingJobInstance(targetTimerDeclaration.getValue()));
                }
            }
        }
    }
    
    protected boolean isNonInterruptingTimerTriggeredAlready(final MigratingInstanceParseContext parseContext, final Map<String, TimerDeclarationImpl> sourceTimerDeclarationsInEventScope, final TimerDeclarationImpl targetTimerDeclaration) {
        if (targetTimerDeclaration.isInterruptingTimer() || targetTimerDeclaration.getJobHandlerType() != "timer-transition" || sourceTimerDeclarationsInEventScope.values().size() == 0) {
            return false;
        }
        for (final TimerDeclarationImpl sourceTimerDeclaration : sourceTimerDeclarationsInEventScope.values()) {
            final MigrationInstruction migrationInstruction = parseContext.findSingleMigrationInstruction(sourceTimerDeclaration.getActivityId());
            final ActivityImpl targetActivity = parseContext.getTargetActivity(migrationInstruction);
            if (targetActivity != null && targetTimerDeclaration.getActivityId().equals(targetActivity.getActivityId())) {
                return true;
            }
        }
        return false;
    }
    
    protected boolean isNonInterruptingTimeoutListenerTriggeredAlready(final MigratingInstanceParseContext parseContext, final Map<String, Map<String, TimerDeclarationImpl>> sourceTimeoutListenerDeclarationsInEventScope, final Map.Entry<String, TimerDeclarationImpl> targetTimerDeclarationEntry) {
        final TimerDeclarationImpl targetTimerDeclaration = targetTimerDeclarationEntry.getValue();
        if (targetTimerDeclaration.isInterruptingTimer() || targetTimerDeclaration.getJobHandlerType() != "timer-task-listener" || sourceTimeoutListenerDeclarationsInEventScope.values().size() == 0) {
            return false;
        }
        for (final Map.Entry<String, Map<String, TimerDeclarationImpl>> sourceTimerDeclarationsEntry : sourceTimeoutListenerDeclarationsInEventScope.entrySet()) {
            final MigrationInstruction migrationInstruction = parseContext.findSingleMigrationInstruction(sourceTimerDeclarationsEntry.getKey());
            final ActivityImpl targetActivity = parseContext.getTargetActivity(migrationInstruction);
            if (targetActivity != null && targetTimerDeclaration.getActivityId().equals(targetActivity.getActivityId())) {
                for (final Map.Entry<String, TimerDeclarationImpl> sourceTimerDeclarationEntry : sourceTimerDeclarationsEntry.getValue().entrySet()) {
                    if (sourceTimerDeclarationEntry.getKey().equals(targetTimerDeclarationEntry.getKey())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
