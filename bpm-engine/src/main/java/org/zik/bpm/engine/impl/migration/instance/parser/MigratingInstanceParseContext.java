// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance.parser;

import org.zik.bpm.engine.impl.util.StringUtil;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.migration.validation.instance.MigratingProcessInstanceValidationReportImpl;
import org.zik.bpm.engine.runtime.TransitionInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingProcessElementInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingTransitionInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingScopeInstance;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import java.util.Iterator;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import java.util.HashSet;
import org.zik.bpm.engine.impl.context.Context;
import java.util.HashMap;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.migration.MigrationPlan;
import org.zik.bpm.engine.migration.MigrationInstruction;
import org.zik.bpm.engine.impl.ActivityExecutionTreeMapping;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionEntity;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.persistence.entity.IncidentEntity;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import java.util.Collection;
import org.zik.bpm.engine.impl.migration.instance.MigratingExternalTaskInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingJobInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingEventScopeInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;
import java.util.Map;
import org.zik.bpm.engine.impl.migration.instance.MigratingProcessInstance;

public class MigratingInstanceParseContext
{
    protected MigratingProcessInstance migratingProcessInstance;
    protected Map<String, MigratingActivityInstance> activityInstances;
    protected Map<String, MigratingEventScopeInstance> compensationInstances;
    protected Map<String, MigratingJobInstance> migratingJobs;
    protected Map<String, MigratingExternalTaskInstance> migratingExternalTasks;
    protected Collection<EventSubscriptionEntity> eventSubscriptions;
    protected Collection<IncidentEntity> incidents;
    protected Collection<JobEntity> jobs;
    protected Collection<TaskEntity> tasks;
    protected Collection<ExternalTaskEntity> externalTasks;
    protected Collection<VariableInstanceEntity> variables;
    protected ProcessDefinitionEntity sourceProcessDefinition;
    protected ProcessDefinitionEntity targetProcessDefinition;
    protected Map<String, List<JobDefinitionEntity>> targetJobDefinitions;
    protected ActivityExecutionTreeMapping mapping;
    protected Map<String, List<MigrationInstruction>> instructionsBySourceScope;
    protected MigratingInstanceParser parser;
    
    public MigratingInstanceParseContext(final MigratingInstanceParser parser, final MigrationPlan migrationPlan, final ExecutionEntity processInstance, final ProcessDefinitionEntity targetProcessDefinition) {
        this.activityInstances = new HashMap<String, MigratingActivityInstance>();
        this.compensationInstances = new HashMap<String, MigratingEventScopeInstance>();
        this.migratingJobs = new HashMap<String, MigratingJobInstance>();
        this.migratingExternalTasks = new HashMap<String, MigratingExternalTaskInstance>();
        this.parser = parser;
        this.sourceProcessDefinition = processInstance.getProcessDefinition();
        this.targetProcessDefinition = targetProcessDefinition;
        this.migratingProcessInstance = new MigratingProcessInstance(processInstance.getId(), this.sourceProcessDefinition, targetProcessDefinition);
        this.mapping = new ActivityExecutionTreeMapping(Context.getCommandContext(), processInstance.getId());
        this.instructionsBySourceScope = this.organizeInstructionsBySourceScope(migrationPlan);
    }
    
    public MigratingInstanceParseContext jobs(final Collection<JobEntity> jobs) {
        this.jobs = new HashSet<JobEntity>(jobs);
        return this;
    }
    
    public MigratingInstanceParseContext incidents(final Collection<IncidentEntity> incidents) {
        this.incidents = new HashSet<IncidentEntity>(incidents);
        return this;
    }
    
    public MigratingInstanceParseContext tasks(final Collection<TaskEntity> tasks) {
        this.tasks = new HashSet<TaskEntity>(tasks);
        return this;
    }
    
    public MigratingInstanceParseContext externalTasks(final Collection<ExternalTaskEntity> externalTasks) {
        this.externalTasks = new HashSet<ExternalTaskEntity>(externalTasks);
        return this;
    }
    
    public MigratingInstanceParseContext eventSubscriptions(final Collection<EventSubscriptionEntity> eventSubscriptions) {
        this.eventSubscriptions = new HashSet<EventSubscriptionEntity>(eventSubscriptions);
        return this;
    }
    
    public MigratingInstanceParseContext targetJobDefinitions(final Collection<JobDefinitionEntity> jobDefinitions) {
        this.targetJobDefinitions = new HashMap<String, List<JobDefinitionEntity>>();
        for (final JobDefinitionEntity jobDefinition : jobDefinitions) {
            CollectionUtil.addToMapOfLists(this.targetJobDefinitions, jobDefinition.getActivityId(), jobDefinition);
        }
        return this;
    }
    
    public MigratingInstanceParseContext variables(final Collection<VariableInstanceEntity> variables) {
        this.variables = new HashSet<VariableInstanceEntity>(variables);
        return this;
    }
    
    public void submit(final MigratingActivityInstance activityInstance) {
        this.activityInstances.put(activityInstance.getActivityInstance().getId(), activityInstance);
    }
    
    public void submit(final MigratingEventScopeInstance compensationInstance) {
        final ExecutionEntity scopeExecution = compensationInstance.resolveRepresentativeExecution();
        if (scopeExecution != null) {
            this.compensationInstances.put(scopeExecution.getId(), compensationInstance);
        }
    }
    
    public void submit(final MigratingJobInstance job) {
        this.migratingJobs.put(job.getJobEntity().getId(), job);
    }
    
    public void submit(final MigratingExternalTaskInstance externalTask) {
        this.migratingExternalTasks.put(externalTask.getId(), externalTask);
    }
    
    public void consume(final TaskEntity task) {
        this.tasks.remove(task);
    }
    
    public void consume(final ExternalTaskEntity externalTask) {
        this.externalTasks.remove(externalTask);
    }
    
    public void consume(final IncidentEntity incident) {
        this.incidents.remove(incident);
    }
    
    public void consume(final JobEntity job) {
        this.jobs.remove(job);
    }
    
    public void consume(final EventSubscriptionEntity eventSubscription) {
        this.eventSubscriptions.remove(eventSubscription);
    }
    
    public void consume(final VariableInstanceEntity variableInstance) {
        this.variables.remove(variableInstance);
    }
    
    public MigratingProcessInstance getMigratingProcessInstance() {
        return this.migratingProcessInstance;
    }
    
    public Collection<MigratingActivityInstance> getMigratingActivityInstances() {
        return this.activityInstances.values();
    }
    
    public ProcessDefinitionImpl getSourceProcessDefinition() {
        return this.sourceProcessDefinition;
    }
    
    public ProcessDefinitionImpl getTargetProcessDefinition() {
        return this.targetProcessDefinition;
    }
    
    public ActivityImpl getTargetActivity(final MigrationInstruction instruction) {
        if (instruction != null) {
            return this.targetProcessDefinition.findActivity(instruction.getTargetActivityId());
        }
        return null;
    }
    
    public JobDefinitionEntity getTargetJobDefinition(final String activityId, final String jobHandlerType) {
        final List<JobDefinitionEntity> jobDefinitionsForActivity = this.targetJobDefinitions.get(activityId);
        if (jobDefinitionsForActivity != null) {
            for (final JobDefinitionEntity jobDefinition : jobDefinitionsForActivity) {
                if (jobHandlerType.equals(jobDefinition.getJobType())) {
                    return jobDefinition;
                }
            }
        }
        return null;
    }
    
    public ActivityExecutionTreeMapping getMapping() {
        return this.mapping;
    }
    
    public MigrationInstruction getInstructionFor(final String scopeId) {
        final List<MigrationInstruction> instructions = this.instructionsBySourceScope.get(scopeId);
        if (instructions == null || instructions.isEmpty()) {
            return null;
        }
        return instructions.get(0);
    }
    
    public MigratingActivityInstance getMigratingActivityInstanceById(final String activityInstanceId) {
        return this.activityInstances.get(activityInstanceId);
    }
    
    public MigratingScopeInstance getMigratingCompensationInstanceByExecutionId(final String id) {
        return this.compensationInstances.get(id);
    }
    
    public MigratingJobInstance getMigratingJobInstanceById(final String jobId) {
        return this.migratingJobs.get(jobId);
    }
    
    public MigratingExternalTaskInstance getMigratingExternalTaskInstanceById(final String externalTaskId) {
        return this.migratingExternalTasks.get(externalTaskId);
    }
    
    public MigrationInstruction findSingleMigrationInstruction(final String sourceScopeId) {
        final List<MigrationInstruction> instructions = this.instructionsBySourceScope.get(sourceScopeId);
        if (instructions != null && !instructions.isEmpty()) {
            return instructions.get(0);
        }
        return null;
    }
    
    protected Map<String, List<MigrationInstruction>> organizeInstructionsBySourceScope(final MigrationPlan migrationPlan) {
        final Map<String, List<MigrationInstruction>> organizedInstructions = new HashMap<String, List<MigrationInstruction>>();
        for (final MigrationInstruction instruction : migrationPlan.getInstructions()) {
            CollectionUtil.addToMapOfLists(organizedInstructions, instruction.getSourceActivityId(), instruction);
        }
        return organizedInstructions;
    }
    
    public void handleDependentActivityInstanceJobs(final MigratingActivityInstance migratingInstance, final List<JobEntity> jobs) {
        this.parser.getDependentActivityInstanceJobHandler().handle(this, migratingInstance, jobs);
    }
    
    public void handleDependentTransitionInstanceJobs(final MigratingTransitionInstance migratingInstance, final List<JobEntity> jobs) {
        this.parser.getDependentTransitionInstanceJobHandler().handle(this, migratingInstance, jobs);
    }
    
    public void handleDependentEventSubscriptions(final MigratingActivityInstance migratingInstance, final List<EventSubscriptionEntity> eventSubscriptions) {
        this.parser.getDependentEventSubscriptionHandler().handle(this, migratingInstance, eventSubscriptions);
    }
    
    public void handleDependentVariables(final MigratingProcessElementInstance migratingInstance, final List<VariableInstanceEntity> variables) {
        this.parser.getDependentVariablesHandler().handle(this, migratingInstance, variables);
    }
    
    public void handleTransitionInstance(final TransitionInstance transitionInstance) {
        this.parser.getTransitionInstanceHandler().handle(this, transitionInstance);
    }
    
    public void validateNoEntitiesLeft(final MigratingProcessInstanceValidationReportImpl processInstanceReport) {
        processInstanceReport.setProcessInstanceId(this.migratingProcessInstance.getProcessInstanceId());
        this.ensureNoEntitiesAreLeft("tasks", this.tasks, processInstanceReport);
        this.ensureNoEntitiesAreLeft("externalTask", this.externalTasks, processInstanceReport);
        this.ensureNoEntitiesAreLeft("incidents", this.incidents, processInstanceReport);
        this.ensureNoEntitiesAreLeft("jobs", this.jobs, processInstanceReport);
        this.ensureNoEntitiesAreLeft("event subscriptions", this.eventSubscriptions, processInstanceReport);
        this.ensureNoEntitiesAreLeft("variables", this.variables, processInstanceReport);
    }
    
    public void ensureNoEntitiesAreLeft(final String entityName, final Collection<? extends DbEntity> dbEntities, final MigratingProcessInstanceValidationReportImpl processInstanceReport) {
        if (!dbEntities.isEmpty()) {
            processInstanceReport.addFailure("Process instance contains not migrated " + entityName + ": [" + StringUtil.joinDbEntityIds(dbEntities) + "]");
        }
    }
}
