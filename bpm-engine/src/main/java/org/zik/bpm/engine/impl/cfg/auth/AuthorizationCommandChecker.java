// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg.auth;

import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.AuthorizationManager;
import org.zik.bpm.engine.authorization.SystemPermissions;
import org.zik.bpm.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.zik.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.zik.bpm.engine.impl.history.event.HistoricExternalTaskLogEntity;
import org.zik.bpm.engine.authorization.UserOperationLogCategoryPermissions;
import org.zik.bpm.engine.history.UserOperationLogEntry;
import org.zik.bpm.engine.runtime.CaseExecution;
import org.zik.bpm.engine.impl.persistence.entity.HistoricJobLogEventEntity;
import org.zik.bpm.engine.history.HistoricDecisionInstance;
import org.zik.bpm.engine.history.HistoricCaseInstance;
import org.zik.bpm.engine.history.HistoricProcessInstance;
import org.zik.bpm.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionEntity;
import org.zik.bpm.engine.impl.batch.history.HistoricBatchEntity;
import org.zik.bpm.engine.impl.batch.BatchEntity;
import org.zik.bpm.engine.authorization.TaskPermissions;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.authorization.ProcessInstancePermissions;
import org.zik.bpm.engine.impl.db.CompositePermissionCheck;
import org.zik.bpm.engine.authorization.ProcessDefinitionPermissions;
import org.zik.bpm.engine.impl.db.PermissionCheckBuilder;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.repository.CaseDefinition;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.authorization.Permissions;
import org.zik.bpm.engine.repository.DecisionDefinition;
import org.zik.bpm.engine.impl.cfg.CommandChecker;

public class AuthorizationCommandChecker implements CommandChecker
{
    @Override
    public void checkEvaluateDecision(final DecisionDefinition decisionDefinition) {
        this.getAuthorizationManager().checkAuthorization(Permissions.CREATE_INSTANCE, Resources.DECISION_DEFINITION, decisionDefinition.getKey());
    }
    
    @Override
    public void checkCreateProcessInstance(final ProcessDefinition processDefinition) {
        this.getAuthorizationManager().checkAuthorization(Permissions.CREATE, Resources.PROCESS_INSTANCE);
        this.getAuthorizationManager().checkAuthorization(Permissions.CREATE_INSTANCE, Resources.PROCESS_DEFINITION, processDefinition.getKey());
    }
    
    @Override
    public void checkReadProcessDefinition(final ProcessDefinition processDefinition) {
        this.getAuthorizationManager().checkAuthorization(Permissions.READ, Resources.PROCESS_DEFINITION, processDefinition.getKey());
    }
    
    @Override
    public void checkCreateCaseInstance(final CaseDefinition caseDefinition) {
    }
    
    @Override
    public void checkUpdateProcessDefinitionById(final String processDefinitionId) {
        if (this.getAuthorizationManager().isAuthorizationEnabled()) {
            final ProcessDefinitionEntity processDefinition = this.findLatestProcessDefinitionById(processDefinitionId);
            if (processDefinition != null) {
                this.checkUpdateProcessDefinitionByKey(processDefinition.getKey());
            }
        }
    }
    
    @Override
    public void checkUpdateProcessDefinitionSuspensionStateById(final String processDefinitionId) {
        if (this.getAuthorizationManager().isAuthorizationEnabled()) {
            final ProcessDefinitionEntity processDefinition = this.findLatestProcessDefinitionById(processDefinitionId);
            if (processDefinition != null) {
                this.checkUpdateProcessDefinitionSuspensionStateByKey(processDefinition.getKey());
            }
        }
    }
    
    @Override
    public void checkUpdateDecisionDefinitionById(final String decisionDefinitionId) {
        if (this.getAuthorizationManager().isAuthorizationEnabled()) {
            final DecisionDefinitionEntity decisionDefinition = this.findLatestDecisionDefinitionById(decisionDefinitionId);
            if (decisionDefinition != null) {
                this.checkUpdateDecisionDefinition(decisionDefinition);
            }
        }
    }
    
    @Override
    public void checkUpdateProcessDefinitionByKey(final String processDefinitionKey) {
        this.getAuthorizationManager().checkAuthorization(Permissions.UPDATE, Resources.PROCESS_DEFINITION, processDefinitionKey);
    }
    
    @Override
    public void checkUpdateProcessDefinitionSuspensionStateByKey(final String processDefinitionKey) {
        final CompositePermissionCheck suspensionStatePermission = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.PROCESS_DEFINITION, processDefinitionKey, ProcessDefinitionPermissions.SUSPEND).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, processDefinitionKey, Permissions.UPDATE).build();
        this.getAuthorizationManager().checkAuthorization(suspensionStatePermission);
    }
    
    @Override
    public void checkDeleteProcessDefinitionById(final String processDefinitionId) {
        if (this.getAuthorizationManager().isAuthorizationEnabled()) {
            final ProcessDefinitionEntity processDefinition = this.findLatestProcessDefinitionById(processDefinitionId);
            if (processDefinition != null) {
                this.checkDeleteProcessDefinitionByKey(processDefinition.getKey());
            }
        }
    }
    
    @Override
    public void checkDeleteProcessDefinitionByKey(final String processDefinitionKey) {
        this.getAuthorizationManager().checkAuthorization(Permissions.DELETE, Resources.PROCESS_DEFINITION, processDefinitionKey);
    }
    
    @Override
    public void checkUpdateProcessInstanceByProcessDefinitionId(final String processDefinitionId) {
        if (this.getAuthorizationManager().isAuthorizationEnabled()) {
            final ProcessDefinitionEntity processDefinition = this.findLatestProcessDefinitionById(processDefinitionId);
            if (processDefinition != null) {
                this.checkUpdateProcessInstanceByProcessDefinitionKey(processDefinition.getKey());
            }
        }
    }
    
    @Override
    public void checkUpdateRetriesProcessInstanceByProcessDefinitionId(final String processDefinitionId) {
        if (this.getAuthorizationManager().isAuthorizationEnabled()) {
            final ProcessDefinitionEntity processDefinition = this.findLatestProcessDefinitionById(processDefinitionId);
            if (processDefinition != null) {
                final CompositePermissionCheck retryJobPermission = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.PROCESS_INSTANCE, "*", ProcessInstancePermissions.RETRY_JOB).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, processDefinition.getKey(), ProcessDefinitionPermissions.RETRY_JOB).atomicCheckForResourceId(Resources.PROCESS_INSTANCE, "*", Permissions.UPDATE).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, processDefinition.getKey(), Permissions.UPDATE_INSTANCE).build();
                this.getAuthorizationManager().checkAuthorization(retryJobPermission);
            }
        }
    }
    
    @Override
    public void checkUpdateProcessInstanceSuspensionStateByProcessDefinitionId(final String processDefinitionId) {
        if (this.getAuthorizationManager().isAuthorizationEnabled()) {
            final ProcessDefinitionEntity processDefinition = this.findLatestProcessDefinitionById(processDefinitionId);
            if (processDefinition != null) {
                this.checkUpdateProcessInstanceSuspensionStateByProcessDefinitionKey(processDefinition.getKey());
            }
        }
    }
    
    @Override
    public void checkUpdateProcessInstanceByProcessDefinitionKey(final String processDefinitionKey) {
        final CompositePermissionCheck suspensionStatePermission = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.PROCESS_INSTANCE, null, Permissions.UPDATE).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, processDefinitionKey, Permissions.UPDATE_INSTANCE).build();
        this.getAuthorizationManager().checkAuthorization(suspensionStatePermission);
    }
    
    @Override
    public void checkUpdateProcessInstanceSuspensionStateByProcessDefinitionKey(final String processDefinitionKey) {
        final CompositePermissionCheck suspensionStatePermission = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.PROCESS_INSTANCE, null, ProcessInstancePermissions.SUSPEND).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, processDefinitionKey, ProcessDefinitionPermissions.SUSPEND_INSTANCE).atomicCheckForResourceId(Resources.PROCESS_INSTANCE, null, Permissions.UPDATE).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, processDefinitionKey, Permissions.UPDATE_INSTANCE).build();
        this.getAuthorizationManager().checkAuthorization(suspensionStatePermission);
    }
    
    @Override
    public void checkReadProcessInstance(final String processInstanceId) {
        final ExecutionEntity execution = this.findExecutionById(processInstanceId);
        if (execution != null) {
            this.checkReadProcessInstance(execution);
        }
    }
    
    @Override
    public void checkDeleteProcessInstance(final ExecutionEntity execution) {
        final ProcessDefinitionEntity processDefinition = execution.getProcessDefinition();
        final CompositePermissionCheck deleteInstancePermission = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.PROCESS_INSTANCE, execution.getProcessInstanceId(), Permissions.DELETE).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, processDefinition.getKey(), Permissions.DELETE_INSTANCE).build();
        this.getAuthorizationManager().checkAuthorization(deleteInstancePermission);
    }
    
    @Override
    public void checkUpdateProcessInstanceById(final String processInstanceId) {
        final ExecutionEntity execution = this.findExecutionById(processInstanceId);
        if (execution != null) {
            this.checkUpdateProcessInstance(execution);
        }
    }
    
    @Override
    public void checkUpdateProcessInstance(final ExecutionEntity execution) {
        final ProcessDefinitionEntity processDefinition = execution.getProcessDefinition();
        final CompositePermissionCheck suspensionStatePermission = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.PROCESS_INSTANCE, execution.getProcessInstanceId(), Permissions.UPDATE).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, processDefinition.getKey(), Permissions.UPDATE_INSTANCE).build();
        this.getAuthorizationManager().checkAuthorization(suspensionStatePermission);
    }
    
    @Override
    public void checkUpdateProcessInstanceVariables(final ExecutionEntity execution) {
        final ProcessDefinitionEntity processDefinition = execution.getProcessDefinition();
        final CompositePermissionCheck suspensionStatePermission = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.PROCESS_INSTANCE, execution.getProcessInstanceId(), ProcessInstancePermissions.UPDATE_VARIABLE).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, processDefinition.getKey(), ProcessDefinitionPermissions.UPDATE_INSTANCE_VARIABLE).atomicCheckForResourceId(Resources.PROCESS_INSTANCE, execution.getProcessInstanceId(), Permissions.UPDATE).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, processDefinition.getKey(), Permissions.UPDATE_INSTANCE).build();
        this.getAuthorizationManager().checkAuthorization(suspensionStatePermission);
    }
    
    @Override
    public void checkUpdateProcessInstanceSuspensionStateById(final String processInstanceId) {
        final ExecutionEntity execution = this.findExecutionById(processInstanceId);
        if (execution != null) {
            this.checkUpdateProcessInstanceSuspensionState(execution);
        }
    }
    
    public void checkUpdateProcessInstanceSuspensionState(final ExecutionEntity execution) {
        final ProcessDefinitionEntity processDefinition = execution.getProcessDefinition();
        final CompositePermissionCheck suspensionStatePermission = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.PROCESS_INSTANCE, execution.getProcessInstanceId(), ProcessInstancePermissions.SUSPEND).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, processDefinition.getKey(), ProcessDefinitionPermissions.SUSPEND_INSTANCE).atomicCheckForResourceId(Resources.PROCESS_INSTANCE, execution.getProcessInstanceId(), Permissions.UPDATE).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, processDefinition.getKey(), Permissions.UPDATE_INSTANCE).build();
        this.getAuthorizationManager().checkAuthorization(suspensionStatePermission);
    }
    
    @Override
    public void checkUpdateJob(final JobEntity job) {
        if (job.getProcessDefinitionKey() == null) {
            return;
        }
        final CompositePermissionCheck retryJobPermission = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.PROCESS_INSTANCE, job.getProcessInstanceId(), Permissions.UPDATE).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, job.getProcessDefinitionKey(), Permissions.UPDATE_INSTANCE).build();
        this.getAuthorizationManager().checkAuthorization(retryJobPermission);
    }
    
    @Override
    public void checkUpdateRetriesJob(final JobEntity job) {
        if (job.getProcessDefinitionKey() == null) {
            return;
        }
        final CompositePermissionCheck retryJobPermission = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.PROCESS_INSTANCE, job.getProcessInstanceId(), ProcessInstancePermissions.RETRY_JOB).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, job.getProcessDefinitionKey(), ProcessDefinitionPermissions.RETRY_JOB).atomicCheckForResourceId(Resources.PROCESS_INSTANCE, job.getProcessInstanceId(), Permissions.UPDATE).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, job.getProcessDefinitionKey(), Permissions.UPDATE_INSTANCE).build();
        this.getAuthorizationManager().checkAuthorization(retryJobPermission);
    }
    
    @Override
    public void checkCreateMigrationPlan(final ProcessDefinition sourceProcessDefinition, final ProcessDefinition targetProcessDefinition) {
        this.checkReadProcessDefinition(sourceProcessDefinition);
        this.checkReadProcessDefinition(targetProcessDefinition);
    }
    
    @Override
    public void checkMigrateProcessInstance(final ExecutionEntity processInstance, final ProcessDefinition targetProcessDefinition) {
    }
    
    @Override
    public void checkReadProcessInstance(final ExecutionEntity execution) {
        final ProcessDefinitionEntity processDefinition = execution.getProcessDefinition();
        final CompositePermissionCheck readProcessInstancePermission = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.PROCESS_INSTANCE, execution.getProcessInstanceId(), Permissions.READ).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, processDefinition.getKey(), Permissions.READ_INSTANCE).build();
        this.getAuthorizationManager().checkAuthorization(readProcessInstancePermission);
    }
    
    @Override
    public void checkReadProcessInstanceVariable(final ExecutionEntity execution) {
        if (this.getAuthorizationManager().isEnsureSpecificVariablePermission()) {
            final ProcessDefinitionEntity processDefinition = execution.getProcessDefinition();
            final CompositePermissionCheck readProcessInstancePermission = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.PROCESS_DEFINITION, processDefinition.getKey(), ProcessDefinitionPermissions.READ_INSTANCE_VARIABLE).build();
            this.getAuthorizationManager().checkAuthorization(readProcessInstancePermission);
        }
        else {
            this.checkReadProcessInstance(execution);
        }
    }
    
    @Override
    public void checkReadJob(final JobEntity job) {
        if (job.getProcessDefinitionKey() == null) {
            return;
        }
        final CompositePermissionCheck readJobPermission = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.PROCESS_INSTANCE, job.getProcessInstanceId(), Permissions.READ).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, job.getProcessDefinitionKey(), Permissions.READ_INSTANCE).build();
        this.getAuthorizationManager().checkAuthorization(readJobPermission);
    }
    
    @Override
    public void checkReadTask(final TaskEntity task) {
        this.checkTaskPermission(task, Permissions.READ_TASK, Permissions.READ);
    }
    
    @Override
    public void checkReadTaskVariable(final TaskEntity task) {
        Permission readProcessInstanceTaskPermission;
        Permission readStandaloneTaskPermission;
        if (this.getAuthorizationManager().isEnsureSpecificVariablePermission()) {
            readProcessInstanceTaskPermission = ProcessDefinitionPermissions.READ_TASK_VARIABLE;
            readStandaloneTaskPermission = TaskPermissions.READ_VARIABLE;
        }
        else {
            readProcessInstanceTaskPermission = Permissions.READ_TASK;
            readStandaloneTaskPermission = Permissions.READ;
        }
        this.checkTaskPermission(task, readProcessInstanceTaskPermission, readStandaloneTaskPermission);
    }
    
    protected void checkTaskPermission(final TaskEntity task, final Permission processDefinitionPermission, final Permission taskPermission) {
        final String taskId = task.getId();
        final String executionId = task.getExecutionId();
        if (executionId != null) {
            final ExecutionEntity execution = task.getExecution();
            final ProcessDefinitionEntity processDefinition = execution.getProcessDefinition();
            final CompositePermissionCheck readTaskPermission = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.TASK, taskId, taskPermission).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, processDefinition.getKey(), processDefinitionPermission).build();
            this.getAuthorizationManager().checkAuthorization(readTaskPermission);
        }
        else {
            final String caseExecutionId = task.getCaseExecutionId();
            if (caseExecutionId == null) {
                this.getAuthorizationManager().checkAuthorization(taskPermission, Resources.TASK, taskId);
            }
        }
    }
    
    @Override
    public void checkUpdateTaskVariable(final TaskEntity task) {
        final String taskId = task.getId();
        final String executionId = task.getExecutionId();
        if (executionId != null) {
            final ExecutionEntity execution = task.getExecution();
            final ProcessDefinitionEntity processDefinition = execution.getProcessDefinition();
            final CompositePermissionCheck updateTaskPermissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.TASK, taskId, TaskPermissions.UPDATE_VARIABLE).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, processDefinition.getKey(), ProcessDefinitionPermissions.UPDATE_TASK_VARIABLE).atomicCheckForResourceId(Resources.TASK, taskId, Permissions.UPDATE).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, processDefinition.getKey(), Permissions.UPDATE_TASK).build();
            this.getAuthorizationManager().checkAuthorization(updateTaskPermissionCheck);
        }
        else {
            final String caseExecutionId = task.getCaseExecutionId();
            if (caseExecutionId == null) {
                final CompositePermissionCheck updateTaskPermissionCheck2 = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.TASK, taskId, TaskPermissions.UPDATE_VARIABLE).atomicCheckForResourceId(Resources.TASK, taskId, Permissions.UPDATE).build();
                this.getAuthorizationManager().checkAuthorization(updateTaskPermissionCheck2);
            }
        }
    }
    
    @Override
    public void checkCreateBatch(final Permission permission) {
        final CompositePermissionCheck createBatchPermission = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.BATCH, null, permission).atomicCheckForResourceId(Resources.BATCH, null, Permissions.CREATE).build();
        this.getAuthorizationManager().checkAuthorization(createBatchPermission);
    }
    
    @Override
    public void checkDeleteBatch(final BatchEntity batch) {
        this.getAuthorizationManager().checkAuthorization(Permissions.DELETE, Resources.BATCH, batch.getId());
    }
    
    @Override
    public void checkDeleteHistoricBatch(final HistoricBatchEntity batch) {
        this.getAuthorizationManager().checkAuthorization(Permissions.DELETE_HISTORY, Resources.BATCH, batch.getId());
    }
    
    @Override
    public void checkSuspendBatch(final BatchEntity batch) {
        this.getAuthorizationManager().checkAuthorization(Permissions.UPDATE, Resources.BATCH, batch.getId());
    }
    
    @Override
    public void checkActivateBatch(final BatchEntity batch) {
        this.getAuthorizationManager().checkAuthorization(Permissions.UPDATE, Resources.BATCH, batch.getId());
    }
    
    @Override
    public void checkReadHistoricBatch() {
        this.getAuthorizationManager().checkAuthorization(Permissions.READ_HISTORY, Resources.BATCH);
    }
    
    @Override
    public void checkCreateDeployment() {
        this.getAuthorizationManager().checkAuthorization(Permissions.CREATE, Resources.DEPLOYMENT);
    }
    
    @Override
    public void checkReadDeployment(final String deploymentId) {
        this.getAuthorizationManager().checkAuthorization(Permissions.READ, Resources.DEPLOYMENT, deploymentId);
    }
    
    @Override
    public void checkDeleteDeployment(final String deploymentId) {
        this.getAuthorizationManager().checkAuthorization(Permissions.DELETE, Resources.DEPLOYMENT, deploymentId);
    }
    
    @Override
    public void checkReadDecisionDefinition(final DecisionDefinitionEntity decisionDefinition) {
        this.getAuthorizationManager().checkAuthorization(Permissions.READ, Resources.DECISION_DEFINITION, decisionDefinition.getKey());
    }
    
    public void checkUpdateDecisionDefinition(final DecisionDefinitionEntity decisionDefinition) {
        this.getAuthorizationManager().checkAuthorization(Permissions.UPDATE, Resources.DECISION_DEFINITION, decisionDefinition.getKey());
    }
    
    @Override
    public void checkReadDecisionRequirementsDefinition(final DecisionRequirementsDefinitionEntity decisionRequirementsDefinition) {
        this.getAuthorizationManager().checkAuthorization(Permissions.READ, Resources.DECISION_REQUIREMENTS_DEFINITION, decisionRequirementsDefinition.getKey());
    }
    
    @Override
    public void checkReadCaseDefinition(final CaseDefinition caseDefinition) {
    }
    
    @Override
    public void checkUpdateCaseDefinition(final CaseDefinition caseDefinition) {
    }
    
    @Override
    public void checkDeleteHistoricTaskInstance(final HistoricTaskInstanceEntity task) {
        if (task != null && task.getProcessDefinitionKey() != null) {
            this.getAuthorizationManager().checkAuthorization(Permissions.DELETE_HISTORY, Resources.PROCESS_DEFINITION, task.getProcessDefinitionKey());
        }
    }
    
    @Override
    public void checkDeleteHistoricProcessInstance(final HistoricProcessInstance instance) {
        this.getAuthorizationManager().checkAuthorization(Permissions.DELETE_HISTORY, Resources.PROCESS_DEFINITION, instance.getProcessDefinitionKey());
    }
    
    @Override
    public void checkDeleteHistoricCaseInstance(final HistoricCaseInstance instance) {
    }
    
    @Override
    public void checkDeleteHistoricDecisionInstance(final String decisionDefinitionKey) {
        this.getAuthorizationManager().checkAuthorization(Permissions.DELETE_HISTORY, Resources.DECISION_DEFINITION, decisionDefinitionKey);
    }
    
    @Override
    public void checkDeleteHistoricDecisionInstance(final HistoricDecisionInstance decisionInstance) {
        this.getAuthorizationManager().checkAuthorization(Permissions.DELETE_HISTORY, Resources.DECISION_DEFINITION, decisionInstance.getDecisionDefinitionKey());
    }
    
    @Override
    public void checkReadHistoricJobLog(final HistoricJobLogEventEntity historicJobLog) {
        if (historicJobLog.getProcessDefinitionKey() != null) {
            this.getAuthorizationManager().checkAuthorization(Permissions.READ_HISTORY, Resources.PROCESS_DEFINITION, historicJobLog.getProcessDefinitionKey());
        }
    }
    
    @Override
    public void checkReadHistoryAnyProcessDefinition() {
        this.getAuthorizationManager().checkAuthorization(Permissions.READ_HISTORY, Resources.PROCESS_DEFINITION, "*");
    }
    
    @Override
    public void checkReadHistoryProcessDefinition(final String processDefinitionKey) {
        this.getAuthorizationManager().checkAuthorization(Permissions.READ_HISTORY, Resources.PROCESS_DEFINITION, processDefinitionKey);
    }
    
    @Override
    public void checkUpdateCaseInstance(final CaseExecution caseExecution) {
    }
    
    @Override
    public void checkReadCaseInstance(final CaseExecution caseExecution) {
    }
    
    @Override
    public void checkTaskAssign(final TaskEntity task) {
        final String taskId = task.getId();
        final String executionId = task.getExecutionId();
        if (executionId != null) {
            final CompositePermissionCheck taskWorkPermission = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.TASK, taskId, Permissions.TASK_ASSIGN).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, task.getProcessDefinition().getKey(), Permissions.TASK_ASSIGN).atomicCheckForResourceId(Resources.TASK, taskId, Permissions.UPDATE).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, task.getProcessDefinition().getKey(), Permissions.UPDATE_TASK).build();
            this.getAuthorizationManager().checkAuthorization(taskWorkPermission);
        }
        else {
            final String caseExecutionId = task.getCaseExecutionId();
            if (caseExecutionId == null) {
                final CompositePermissionCheck taskWorkPermission2 = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.TASK, taskId, Permissions.TASK_ASSIGN).atomicCheckForResourceId(Resources.TASK, taskId, Permissions.UPDATE).build();
                this.getAuthorizationManager().checkAuthorization(taskWorkPermission2);
            }
        }
    }
    
    @Override
    public void checkCreateTask(final TaskEntity entity) {
        this.getAuthorizationManager().checkAuthorization(Permissions.CREATE, Resources.TASK);
    }
    
    @Override
    public void checkCreateTask() {
        this.getAuthorizationManager().checkAuthorization(Permissions.CREATE, Resources.TASK);
    }
    
    @Override
    public void checkTaskWork(final TaskEntity task) {
        final String taskId = task.getId();
        final String executionId = task.getExecutionId();
        if (executionId != null) {
            final CompositePermissionCheck taskWorkPermission = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.TASK, taskId, Permissions.TASK_WORK).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, task.getProcessDefinition().getKey(), Permissions.TASK_WORK).atomicCheckForResourceId(Resources.TASK, taskId, Permissions.UPDATE).atomicCheckForResourceId(Resources.PROCESS_DEFINITION, task.getProcessDefinition().getKey(), Permissions.UPDATE_TASK).build();
            this.getAuthorizationManager().checkAuthorization(taskWorkPermission);
        }
        else {
            final String caseExecutionId = task.getCaseExecutionId();
            if (caseExecutionId == null) {
                final CompositePermissionCheck taskWorkPermission2 = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.TASK, taskId, Permissions.TASK_WORK).atomicCheckForResourceId(Resources.TASK, taskId, Permissions.UPDATE).build();
                this.getAuthorizationManager().checkAuthorization(taskWorkPermission2);
            }
        }
    }
    
    @Override
    public void checkDeleteTask(final TaskEntity task) {
        final String taskId = task.getId();
        final String executionId = task.getExecutionId();
        final String caseExecutionId = task.getCaseExecutionId();
        if (executionId == null && caseExecutionId == null) {
            this.getAuthorizationManager().checkAuthorization(Permissions.DELETE, Resources.TASK, taskId);
        }
    }
    
    public void checkUserOperationLog(final UserOperationLogEntry entry, final ProcessDefinitionPermissions processDefinitionPermission, final UserOperationLogCategoryPermissions operationLogCategoryPermission) {
        if (entry != null) {
            final String category = entry.getCategory();
            final String processDefinitionKey = entry.getProcessDefinitionKey();
            if (category != null || processDefinitionKey != null) {
                CompositePermissionCheck permissionCheck = null;
                if (category == null) {
                    permissionCheck = new PermissionCheckBuilder().atomicCheckForResourceId(Resources.PROCESS_DEFINITION, processDefinitionKey, processDefinitionPermission).build();
                }
                else if (processDefinitionKey == null) {
                    permissionCheck = new PermissionCheckBuilder().atomicCheckForResourceId(Resources.OPERATION_LOG_CATEGORY, category, operationLogCategoryPermission).build();
                }
                else {
                    permissionCheck = new PermissionCheckBuilder().disjunctive().atomicCheckForResourceId(Resources.PROCESS_DEFINITION, processDefinitionKey, processDefinitionPermission).atomicCheckForResourceId(Resources.OPERATION_LOG_CATEGORY, category, operationLogCategoryPermission).build();
                }
                this.getAuthorizationManager().checkAuthorization(permissionCheck);
            }
        }
    }
    
    @Override
    public void checkDeleteUserOperationLog(final UserOperationLogEntry entry) {
        this.checkUserOperationLog(entry, ProcessDefinitionPermissions.DELETE_HISTORY, UserOperationLogCategoryPermissions.DELETE);
    }
    
    @Override
    public void checkUpdateUserOperationLog(final UserOperationLogEntry entry) {
        this.checkUserOperationLog(entry, ProcessDefinitionPermissions.UPDATE_HISTORY, UserOperationLogCategoryPermissions.UPDATE);
    }
    
    @Override
    public void checkReadHistoricExternalTaskLog(final HistoricExternalTaskLogEntity historicExternalTaskLog) {
        if (historicExternalTaskLog.getProcessDefinitionKey() != null) {
            this.getAuthorizationManager().checkAuthorization(Permissions.READ_HISTORY, Resources.PROCESS_DEFINITION, historicExternalTaskLog.getProcessDefinitionKey());
        }
    }
    
    @Override
    public void checkDeleteHistoricVariableInstance(final HistoricVariableInstanceEntity variable) {
        if (variable != null && variable.getProcessDefinitionKey() != null) {
            this.getAuthorizationManager().checkAuthorization(Permissions.DELETE_HISTORY, Resources.PROCESS_DEFINITION, variable.getProcessDefinitionKey());
        }
    }
    
    @Override
    public void checkDeleteHistoricVariableInstancesByProcessInstance(final HistoricProcessInstanceEntity instance) {
        this.checkDeleteHistoricProcessInstance(instance);
    }
    
    @Override
    public void checkReadTelemetryData() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.READ, Resources.SYSTEM);
    }
    
    @Override
    public void checkConfigureTelemetry() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.SET, Resources.SYSTEM);
    }
    
    @Override
    public void checkReadTelemetryCollectionStatusData() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.READ, Resources.SYSTEM);
    }
    
    @Override
    public void checkReadHistoryLevel() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.READ, Resources.SYSTEM);
    }
    
    @Override
    public void checkReadTableCount() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.READ, Resources.SYSTEM);
    }
    
    @Override
    public void checkReadTableName() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.READ, Resources.SYSTEM);
    }
    
    @Override
    public void checkReadTableMetaData() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.READ, Resources.SYSTEM);
    }
    
    @Override
    public void checkReadProperties() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.READ, Resources.SYSTEM);
    }
    
    @Override
    public void checkSetProperty() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.SET, Resources.SYSTEM);
    }
    
    @Override
    public void checkDeleteProperty() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.DELETE, Resources.SYSTEM);
    }
    
    @Override
    public void checkDeleteLicenseKey() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.DELETE, Resources.SYSTEM);
    }
    
    @Override
    public void checkSetLicenseKey() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.SET, Resources.SYSTEM);
    }
    
    @Override
    public void checkReadLicenseKey() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.READ, Resources.SYSTEM);
    }
    
    @Override
    public void checkRegisterProcessApplication() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.SET, Resources.SYSTEM);
    }
    
    @Override
    public void checkUnregisterProcessApplication() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.SET, Resources.SYSTEM);
    }
    
    @Override
    public void checkReadRegisteredDeployments() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.READ, Resources.SYSTEM);
    }
    
    @Override
    public void checkReadProcessApplicationForDeployment() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.READ, Resources.SYSTEM);
    }
    
    @Override
    public void checkRegisterDeployment() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.SET, Resources.SYSTEM);
    }
    
    @Override
    public void checkUnregisterDeployment() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.SET, Resources.SYSTEM);
    }
    
    @Override
    public void checkDeleteMetrics() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.DELETE, Resources.SYSTEM);
    }
    
    @Override
    public void checkDeleteTaskMetrics() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.DELETE, Resources.SYSTEM);
    }
    
    @Override
    public void checkReadSchemaLog() {
        this.getAuthorizationManager().checkAuthorization(SystemPermissions.READ, Resources.SYSTEM);
    }
    
    protected AuthorizationManager getAuthorizationManager() {
        return Context.getCommandContext().getAuthorizationManager();
    }
    
    protected ProcessDefinitionEntity findLatestProcessDefinitionById(final String processDefinitionId) {
        return Context.getCommandContext().getProcessDefinitionManager().findLatestProcessDefinitionById(processDefinitionId);
    }
    
    protected DecisionDefinitionEntity findLatestDecisionDefinitionById(final String decisionDefinitionId) {
        return Context.getCommandContext().getDecisionDefinitionManager().findDecisionDefinitionById(decisionDefinitionId);
    }
    
    protected ExecutionEntity findExecutionById(final String processInstanceId) {
        return Context.getCommandContext().getExecutionManager().findExecutionById(processInstanceId);
    }
}
