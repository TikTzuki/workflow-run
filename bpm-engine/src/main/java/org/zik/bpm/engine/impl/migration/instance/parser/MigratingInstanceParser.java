// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance.parser;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.tree.TreeVisitor;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import java.util.Collection;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.migration.instance.MigratingProcessInstance;
import org.zik.bpm.engine.impl.migration.validation.instance.MigratingProcessInstanceValidationReportImpl;
import org.zik.bpm.engine.migration.MigrationPlan;
import org.zik.bpm.engine.impl.persistence.entity.IncidentEntity;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import org.zik.bpm.engine.impl.migration.instance.MigratingProcessElementInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingTransitionInstance;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import java.util.List;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.zik.bpm.engine.runtime.TransitionInstance;
import org.zik.bpm.engine.runtime.ActivityInstance;
import org.zik.bpm.engine.ProcessEngine;

public class MigratingInstanceParser
{
    protected ProcessEngine engine;
    protected MigratingInstanceParseHandler<ActivityInstance> activityInstanceHandler;
    protected MigratingInstanceParseHandler<TransitionInstance> transitionInstanceHandler;
    protected MigratingInstanceParseHandler<EventSubscriptionEntity> compensationInstanceHandler;
    protected MigratingDependentInstanceParseHandler<MigratingActivityInstance, List<JobEntity>> dependentActivityInstanceJobHandler;
    protected MigratingDependentInstanceParseHandler<MigratingTransitionInstance, List<JobEntity>> dependentTransitionInstanceJobHandler;
    protected MigratingDependentInstanceParseHandler<MigratingActivityInstance, List<EventSubscriptionEntity>> dependentEventSubscriptionHandler;
    protected MigratingDependentInstanceParseHandler<MigratingProcessElementInstance, List<VariableInstanceEntity>> dependentVariableHandler;
    protected MigratingInstanceParseHandler<IncidentEntity> incidentHandler;
    
    public MigratingInstanceParser(final ProcessEngine engine) {
        this.activityInstanceHandler = new ActivityInstanceHandler();
        this.transitionInstanceHandler = new TransitionInstanceHandler();
        this.compensationInstanceHandler = new CompensationInstanceHandler();
        this.dependentActivityInstanceJobHandler = new ActivityInstanceJobHandler();
        this.dependentTransitionInstanceJobHandler = new TransitionInstanceJobHandler();
        this.dependentEventSubscriptionHandler = new EventSubscriptionInstanceHandler();
        this.dependentVariableHandler = new VariableInstanceHandler();
        this.incidentHandler = new IncidentInstanceHandler();
        this.engine = engine;
    }
    
    public MigratingProcessInstance parse(final String processInstanceId, final MigrationPlan migrationPlan, final MigratingProcessInstanceValidationReportImpl processInstanceReport) {
        final CommandContext commandContext = Context.getCommandContext();
        final List<EventSubscriptionEntity> eventSubscriptions = this.fetchEventSubscriptions(commandContext, processInstanceId);
        final List<ExecutionEntity> executions = this.fetchExecutions(commandContext, processInstanceId);
        final List<ExternalTaskEntity> externalTasks = this.fetchExternalTasks(commandContext, processInstanceId);
        final List<IncidentEntity> incidents = this.fetchIncidents(commandContext, processInstanceId);
        final List<JobEntity> jobs = this.fetchJobs(commandContext, processInstanceId);
        final List<TaskEntity> tasks = this.fetchTasks(commandContext, processInstanceId);
        final List<VariableInstanceEntity> variables = this.fetchVariables(commandContext, processInstanceId);
        final ExecutionEntity processInstance = commandContext.getExecutionManager().findExecutionById(processInstanceId);
        processInstance.restoreProcessInstance(executions, eventSubscriptions, variables, tasks, jobs, incidents, externalTasks);
        final ProcessDefinitionEntity targetProcessDefinition = Context.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(migrationPlan.getTargetProcessDefinitionId());
        final List<JobDefinitionEntity> targetJobDefinitions = this.fetchJobDefinitions(commandContext, targetProcessDefinition.getId());
        final MigratingInstanceParseContext parseContext = new MigratingInstanceParseContext(this, migrationPlan, processInstance, targetProcessDefinition).eventSubscriptions(eventSubscriptions).externalTasks(externalTasks).incidents(incidents).jobs(jobs).tasks(tasks).targetJobDefinitions(targetJobDefinitions).variables(variables);
        final ActivityInstance activityInstance = this.engine.getRuntimeService().getActivityInstance(processInstanceId);
        final ActivityInstanceWalker activityInstanceWalker = new ActivityInstanceWalker(activityInstance);
        activityInstanceWalker.addPreVisitor(new TreeVisitor<ActivityInstance>() {
            @Override
            public void visit(final ActivityInstance obj) {
                MigratingInstanceParser.this.activityInstanceHandler.handle(parseContext, obj);
            }
        });
        activityInstanceWalker.walkWhile();
        final CompensationEventSubscriptionWalker compensateSubscriptionsWalker = new CompensationEventSubscriptionWalker(parseContext.getMigratingActivityInstances());
        compensateSubscriptionsWalker.addPreVisitor(new TreeVisitor<EventSubscriptionEntity>() {
            @Override
            public void visit(final EventSubscriptionEntity obj) {
                MigratingInstanceParser.this.compensationInstanceHandler.handle(parseContext, obj);
            }
        });
        compensateSubscriptionsWalker.walkWhile();
        for (final IncidentEntity incidentEntity : incidents) {
            this.incidentHandler.handle(parseContext, incidentEntity);
        }
        parseContext.validateNoEntitiesLeft(processInstanceReport);
        return parseContext.getMigratingProcessInstance();
    }
    
    public MigratingInstanceParseHandler<ActivityInstance> getActivityInstanceHandler() {
        return this.activityInstanceHandler;
    }
    
    public MigratingInstanceParseHandler<TransitionInstance> getTransitionInstanceHandler() {
        return this.transitionInstanceHandler;
    }
    
    public MigratingDependentInstanceParseHandler<MigratingActivityInstance, List<EventSubscriptionEntity>> getDependentEventSubscriptionHandler() {
        return this.dependentEventSubscriptionHandler;
    }
    
    public MigratingDependentInstanceParseHandler<MigratingActivityInstance, List<JobEntity>> getDependentActivityInstanceJobHandler() {
        return this.dependentActivityInstanceJobHandler;
    }
    
    public MigratingDependentInstanceParseHandler<MigratingTransitionInstance, List<JobEntity>> getDependentTransitionInstanceJobHandler() {
        return this.dependentTransitionInstanceJobHandler;
    }
    
    public MigratingInstanceParseHandler<IncidentEntity> getIncidentHandler() {
        return this.incidentHandler;
    }
    
    public MigratingDependentInstanceParseHandler<MigratingProcessElementInstance, List<VariableInstanceEntity>> getDependentVariablesHandler() {
        return this.dependentVariableHandler;
    }
    
    protected List<ExecutionEntity> fetchExecutions(final CommandContext commandContext, final String processInstanceId) {
        return commandContext.getExecutionManager().findExecutionsByProcessInstanceId(processInstanceId);
    }
    
    protected List<EventSubscriptionEntity> fetchEventSubscriptions(final CommandContext commandContext, final String processInstanceId) {
        return commandContext.getEventSubscriptionManager().findEventSubscriptionsByProcessInstanceId(processInstanceId);
    }
    
    protected List<ExternalTaskEntity> fetchExternalTasks(final CommandContext commandContext, final String processInstanceId) {
        return commandContext.getExternalTaskManager().findExternalTasksByProcessInstanceId(processInstanceId);
    }
    
    protected List<JobEntity> fetchJobs(final CommandContext commandContext, final String processInstanceId) {
        return commandContext.getJobManager().findJobsByProcessInstanceId(processInstanceId);
    }
    
    protected List<IncidentEntity> fetchIncidents(final CommandContext commandContext, final String processInstanceId) {
        return commandContext.getIncidentManager().findIncidentsByProcessInstance(processInstanceId);
    }
    
    protected List<TaskEntity> fetchTasks(final CommandContext commandContext, final String processInstanceId) {
        return commandContext.getTaskManager().findTasksByProcessInstanceId(processInstanceId);
    }
    
    protected List<JobDefinitionEntity> fetchJobDefinitions(final CommandContext commandContext, final String processDefinitionId) {
        return commandContext.getJobDefinitionManager().findByProcessDefinitionId(processDefinitionId);
    }
    
    protected List<VariableInstanceEntity> fetchVariables(final CommandContext commandContext, final String processInstanceId) {
        return commandContext.getVariableInstanceManager().findVariableInstancesByProcessInstanceId(processInstanceId);
    }
}
