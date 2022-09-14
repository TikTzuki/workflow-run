// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import java.util.HashMap;
import java.util.Map;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionEntity;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionManager;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import java.util.Collections;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.persistence.entity.IncidentEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.history.event.HistoryEventProcessor;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SetProcessDefinitionVersionCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    private final String processInstanceId;
    private final Integer processDefinitionVersion;
    
    public SetProcessDefinitionVersionCmd(final String processInstanceId, final Integer processDefinitionVersion) {
        EnsureUtil.ensureNotEmpty("The process instance id is mandatory", "processInstanceId", processInstanceId);
        EnsureUtil.ensureNotNull("The process definition version is mandatory", "processDefinitionVersion", processDefinitionVersion);
        EnsureUtil.ensurePositive("The process definition version must be positive", "processDefinitionVersion", (long)processDefinitionVersion);
        this.processInstanceId = processInstanceId;
        this.processDefinitionVersion = processDefinitionVersion;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final ProcessEngineConfigurationImpl configuration = commandContext.getProcessEngineConfiguration();
        final ExecutionManager executionManager = commandContext.getExecutionManager();
        final ExecutionEntity processInstance = executionManager.findExecutionById(this.processInstanceId);
        if (processInstance == null) {
            throw new ProcessEngineException("No process instance found for id = '" + this.processInstanceId + "'.");
        }
        if (!processInstance.isProcessInstanceExecution()) {
            throw new ProcessEngineException("A process instance id is required, but the provided id '" + this.processInstanceId + "' points to a child execution of process instance '" + processInstance.getProcessInstanceId() + "'. Please invoke the " + this.getClass().getSimpleName() + " with a root execution id.");
        }
        final ProcessDefinitionImpl currentProcessDefinitionImpl = processInstance.getProcessDefinition();
        final DeploymentCache deploymentCache = configuration.getDeploymentCache();
        ProcessDefinitionEntity currentProcessDefinition;
        if (currentProcessDefinitionImpl instanceof ProcessDefinitionEntity) {
            currentProcessDefinition = (ProcessDefinitionEntity)currentProcessDefinitionImpl;
        }
        else {
            currentProcessDefinition = deploymentCache.findDeployedProcessDefinitionById(currentProcessDefinitionImpl.getId());
        }
        final ProcessDefinitionEntity newProcessDefinition = deploymentCache.findDeployedProcessDefinitionByKeyVersionAndTenantId(currentProcessDefinition.getKey(), this.processDefinitionVersion, currentProcessDefinition.getTenantId());
        this.validateAndSwitchVersionOfExecution(commandContext, processInstance, newProcessDefinition);
        final HistoryLevel historyLevel = configuration.getHistoryLevel();
        if (historyLevel.isHistoryEventProduced(HistoryEventTypes.PROCESS_INSTANCE_UPDATE, processInstance)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    return producer.createProcessInstanceUpdateEvt(processInstance);
                }
            });
        }
        final List<ExecutionEntity> childExecutions = executionManager.findExecutionsByProcessInstanceId(this.processInstanceId);
        for (final ExecutionEntity executionEntity : childExecutions) {
            this.validateAndSwitchVersionOfExecution(commandContext, executionEntity, newProcessDefinition);
        }
        final List<JobEntity> jobs = commandContext.getJobManager().findJobsByProcessInstanceId(this.processInstanceId);
        final List<JobDefinitionEntity> currentJobDefinitions = commandContext.getJobDefinitionManager().findByProcessDefinitionId(currentProcessDefinition.getId());
        final List<JobDefinitionEntity> newVersionJobDefinitions = commandContext.getJobDefinitionManager().findByProcessDefinitionId(newProcessDefinition.getId());
        final Map<String, String> jobDefinitionMapping = this.getJobDefinitionMapping(currentJobDefinitions, newVersionJobDefinitions);
        for (final JobEntity jobEntity : jobs) {
            this.switchVersionOfJob(jobEntity, newProcessDefinition, jobDefinitionMapping);
        }
        final List<IncidentEntity> incidents = commandContext.getIncidentManager().findIncidentsByProcessInstance(this.processInstanceId);
        for (final IncidentEntity incidentEntity : incidents) {
            this.switchVersionOfIncident(commandContext, incidentEntity, newProcessDefinition);
        }
        final PropertyChange change = new PropertyChange("processDefinitionVersion", currentProcessDefinition.getVersion(), this.processDefinitionVersion);
        commandContext.getOperationLogManager().logProcessInstanceOperation("ModifyProcessInstance", this.processInstanceId, null, null, Collections.singletonList(change));
        return null;
    }
    
    protected Map<String, String> getJobDefinitionMapping(final List<JobDefinitionEntity> currentJobDefinitions, final List<JobDefinitionEntity> newVersionJobDefinitions) {
        final Map<String, String> mapping = new HashMap<String, String>();
        for (final JobDefinitionEntity currentJobDefinition : currentJobDefinitions) {
            for (final JobDefinitionEntity newJobDefinition : newVersionJobDefinitions) {
                if (this.jobDefinitionsMatch(currentJobDefinition, newJobDefinition)) {
                    mapping.put(currentJobDefinition.getId(), newJobDefinition.getId());
                    break;
                }
            }
        }
        return mapping;
    }
    
    protected boolean jobDefinitionsMatch(final JobDefinitionEntity currentJobDefinition, final JobDefinitionEntity newJobDefinition) {
        final boolean activitiesMatch = currentJobDefinition.getActivityId().equals(newJobDefinition.getActivityId());
        final boolean typesMatch = (currentJobDefinition.getJobType() == null && newJobDefinition.getJobType() == null) || (currentJobDefinition.getJobType() != null && currentJobDefinition.getJobType().equals(newJobDefinition.getJobType()));
        final boolean configurationsMatch = (currentJobDefinition.getJobConfiguration() == null && newJobDefinition.getJobConfiguration() == null) || (currentJobDefinition.getJobConfiguration() != null && currentJobDefinition.getJobConfiguration().equals(newJobDefinition.getJobConfiguration()));
        return activitiesMatch && typesMatch && configurationsMatch;
    }
    
    protected void switchVersionOfJob(final JobEntity jobEntity, final ProcessDefinitionEntity newProcessDefinition, final Map<String, String> jobDefinitionMapping) {
        jobEntity.setProcessDefinitionId(newProcessDefinition.getId());
        jobEntity.setDeploymentId(newProcessDefinition.getDeploymentId());
        final String newJobDefinitionId = jobDefinitionMapping.get(jobEntity.getJobDefinitionId());
        jobEntity.setJobDefinitionId(newJobDefinitionId);
    }
    
    protected void switchVersionOfIncident(final CommandContext commandContext, final IncidentEntity incidentEntity, final ProcessDefinitionEntity newProcessDefinition) {
        incidentEntity.setProcessDefinitionId(newProcessDefinition.getId());
    }
    
    protected void validateAndSwitchVersionOfExecution(final CommandContext commandContext, final ExecutionEntity execution, final ProcessDefinitionEntity newProcessDefinition) {
        if (execution.getActivity() != null) {
            final String activityId = execution.getActivity().getId();
            final PvmActivity newActivity = newProcessDefinition.findActivity(activityId);
            if (newActivity == null) {
                throw new ProcessEngineException("The new process definition (key = '" + newProcessDefinition.getKey() + "') does not contain the current activity (id = '" + activityId + "') of the process instance (id = '" + this.processInstanceId + "').");
            }
            execution.setActivity(newActivity);
        }
        execution.setProcessDefinition(newProcessDefinition);
        final List<TaskEntity> tasks = commandContext.getTaskManager().findTasksByExecutionId(execution.getId());
        for (final TaskEntity taskEntity : tasks) {
            taskEntity.setProcessDefinitionId(newProcessDefinition.getId());
        }
    }
}
