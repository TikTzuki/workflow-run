// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg.multitenancy;

import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.TenantManager;
import org.zik.bpm.engine.impl.history.event.HistoricExternalTaskLogEntity;
import org.zik.bpm.engine.history.UserOperationLogEntry;
import org.zik.bpm.engine.runtime.CaseExecution;
import org.zik.bpm.engine.impl.persistence.entity.HistoricJobLogEventEntity;
import org.zik.bpm.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.zik.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.zik.bpm.engine.history.HistoricDecisionInstance;
import org.zik.bpm.engine.history.HistoricCaseInstance;
import org.zik.bpm.engine.history.HistoricProcessInstance;
import org.zik.bpm.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionEntity;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.zik.bpm.engine.impl.batch.history.HistoricBatchEntity;
import org.zik.bpm.engine.impl.batch.BatchEntity;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.repository.CaseDefinition;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.repository.DecisionDefinition;
import org.zik.bpm.engine.impl.cmd.CommandLogger;
import org.zik.bpm.engine.impl.cfg.CommandChecker;

public class TenantCommandChecker implements CommandChecker
{
    protected static final CommandLogger LOG;
    
    @Override
    public void checkEvaluateDecision(final DecisionDefinition decisionDefinition) {
        if (!this.getTenantManager().isAuthenticatedTenant(decisionDefinition.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("evaluate the decision '" + decisionDefinition.getId() + "'");
        }
    }
    
    @Override
    public void checkCreateProcessInstance(final ProcessDefinition processDefinition) {
        if (!this.getTenantManager().isAuthenticatedTenant(processDefinition.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("create an instance of the process definition '" + processDefinition.getId() + "'");
        }
    }
    
    @Override
    public void checkReadProcessDefinition(final ProcessDefinition processDefinition) {
        if (!this.getTenantManager().isAuthenticatedTenant(processDefinition.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("get the process definition '" + processDefinition.getId() + "'");
        }
    }
    
    @Override
    public void checkCreateCaseInstance(final CaseDefinition caseDefinition) {
        if (!this.getTenantManager().isAuthenticatedTenant(caseDefinition.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("create an instance of the case definition '" + caseDefinition.getId() + "'");
        }
    }
    
    @Override
    public void checkUpdateProcessDefinitionById(final String processDefinitionId) {
        if (this.getTenantManager().isTenantCheckEnabled()) {
            final ProcessDefinitionEntity processDefinition = this.findLatestProcessDefinitionById(processDefinitionId);
            if (processDefinition != null && !this.getTenantManager().isAuthenticatedTenant(processDefinition.getTenantId())) {
                throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("update the process definition '" + processDefinitionId + "'");
            }
        }
    }
    
    @Override
    public void checkUpdateProcessDefinitionSuspensionStateById(final String processDefinitionId) {
        this.checkUpdateProcessDefinitionById(processDefinitionId);
    }
    
    @Override
    public void checkUpdateProcessDefinitionByKey(final String processDefinitionKey) {
    }
    
    @Override
    public void checkUpdateProcessDefinitionSuspensionStateByKey(final String processDefinitionKey) {
    }
    
    @Override
    public void checkDeleteProcessDefinitionById(final String processDefinitionId) {
        if (this.getTenantManager().isTenantCheckEnabled()) {
            final ProcessDefinitionEntity processDefinition = this.findLatestProcessDefinitionById(processDefinitionId);
            if (processDefinition != null && !this.getTenantManager().isAuthenticatedTenant(processDefinition.getTenantId())) {
                throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("delete the process definition '" + processDefinitionId + "'");
            }
        }
    }
    
    @Override
    public void checkDeleteProcessDefinitionByKey(final String processDefinitionKey) {
    }
    
    @Override
    public void checkUpdateProcessInstanceByProcessDefinitionId(final String processDefinitionId) {
        if (this.getTenantManager().isTenantCheckEnabled()) {
            final ProcessDefinitionEntity processDefinition = this.findLatestProcessDefinitionById(processDefinitionId);
            if (processDefinition != null && !this.getTenantManager().isAuthenticatedTenant(processDefinition.getTenantId())) {
                throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("update the process definition '" + processDefinitionId + "'");
            }
        }
    }
    
    @Override
    public void checkUpdateRetriesProcessInstanceByProcessDefinitionId(final String processDefinitionId) {
        this.checkUpdateProcessInstanceByProcessDefinitionId(processDefinitionId);
    }
    
    @Override
    public void checkUpdateProcessInstanceSuspensionStateByProcessDefinitionId(final String processDefinitionId) {
        this.checkUpdateProcessInstanceByProcessDefinitionId(processDefinitionId);
    }
    
    @Override
    public void checkUpdateProcessInstance(final ExecutionEntity execution) {
        if (execution != null && !this.getTenantManager().isAuthenticatedTenant(execution.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("update the process instance '" + execution.getId() + "'");
        }
    }
    
    @Override
    public void checkUpdateProcessInstanceVariables(final ExecutionEntity execution) {
        this.checkUpdateProcessInstance(execution);
    }
    
    @Override
    public void checkUpdateJob(final JobEntity job) {
        if (job != null && !this.getTenantManager().isAuthenticatedTenant(job.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("update the job '" + job.getId() + "'");
        }
    }
    
    @Override
    public void checkUpdateRetriesJob(final JobEntity job) {
        this.checkUpdateJob(job);
    }
    
    @Override
    public void checkUpdateProcessInstanceByProcessDefinitionKey(final String processDefinitionKey) {
    }
    
    @Override
    public void checkUpdateProcessInstanceSuspensionStateByProcessDefinitionKey(final String processDefinitionKey) {
    }
    
    @Override
    public void checkUpdateProcessInstanceById(final String processInstanceId) {
        if (this.getTenantManager().isTenantCheckEnabled()) {
            final ExecutionEntity execution = this.findExecutionById(processInstanceId);
            if (execution != null && !this.getTenantManager().isAuthenticatedTenant(execution.getTenantId())) {
                throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("update the process instance '" + processInstanceId + "'");
            }
        }
    }
    
    @Override
    public void checkUpdateProcessInstanceSuspensionStateById(final String processInstanceId) {
        this.checkUpdateProcessInstanceById(processInstanceId);
    }
    
    @Override
    public void checkCreateMigrationPlan(final ProcessDefinition sourceProcessDefinition, final ProcessDefinition targetProcessDefinition) {
        final String sourceTenant = sourceProcessDefinition.getTenantId();
        final String targetTenant = targetProcessDefinition.getTenantId();
        if (!this.getTenantManager().isAuthenticatedTenant(sourceTenant)) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("get process definition '" + sourceProcessDefinition.getId() + "'");
        }
        if (!this.getTenantManager().isAuthenticatedTenant(targetTenant)) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("get process definition '" + targetProcessDefinition.getId() + "'");
        }
        if (sourceTenant != null && targetTenant != null && !sourceTenant.equals(targetTenant)) {
            throw ProcessEngineLogger.MIGRATION_LOGGER.cannotMigrateBetweenTenants(sourceTenant, targetTenant);
        }
    }
    
    @Override
    public void checkReadProcessInstance(final String processInstanceId) {
        if (this.getTenantManager().isTenantCheckEnabled()) {
            final ExecutionEntity execution = this.findExecutionById(processInstanceId);
            if (execution != null && !this.getTenantManager().isAuthenticatedTenant(execution.getTenantId())) {
                throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("read the process instance '" + processInstanceId + "'");
            }
        }
    }
    
    @Override
    public void checkReadJob(final JobEntity job) {
        if (job != null && !this.getTenantManager().isAuthenticatedTenant(job.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("read the job '" + job.getId() + "'");
        }
    }
    
    @Override
    public void checkReadProcessInstance(final ExecutionEntity execution) {
        if (execution != null && !this.getTenantManager().isAuthenticatedTenant(execution.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("read the process instance '" + execution.getId() + "'");
        }
    }
    
    @Override
    public void checkReadProcessInstanceVariable(final ExecutionEntity execution) {
        this.checkReadProcessInstance(execution);
    }
    
    @Override
    public void checkDeleteProcessInstance(final ExecutionEntity execution) {
        if (execution != null && !this.getTenantManager().isAuthenticatedTenant(execution.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("delete the process instance '" + execution.getId() + "'");
        }
    }
    
    @Override
    public void checkMigrateProcessInstance(final ExecutionEntity processInstance, final ProcessDefinition targetProcessDefinition) {
        final String sourceTenant = processInstance.getTenantId();
        final String targetTenant = targetProcessDefinition.getTenantId();
        if (this.getTenantManager().isTenantCheckEnabled() && processInstance != null && !this.getTenantManager().isAuthenticatedTenant(processInstance.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("migrate process instance '" + processInstance.getId() + "'");
        }
        if (targetTenant != null && (sourceTenant == null || !sourceTenant.equals(targetTenant))) {
            throw ProcessEngineLogger.MIGRATION_LOGGER.cannotMigrateInstanceBetweenTenants(processInstance.getId(), sourceTenant, targetTenant);
        }
    }
    
    @Override
    public void checkReadTask(final TaskEntity task) {
        if (task != null && !this.getTenantManager().isAuthenticatedTenant(task.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("read the task '" + task.getId() + "'");
        }
    }
    
    @Override
    public void checkReadTaskVariable(final TaskEntity task) {
        this.checkReadTask(task);
    }
    
    @Override
    public void checkUpdateTaskVariable(final TaskEntity task) {
        if (task != null && !this.getTenantManager().isAuthenticatedTenant(task.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("update the task '" + task.getId() + "'");
        }
    }
    
    @Override
    public void checkCreateBatch(final Permission permission) {
    }
    
    @Override
    public void checkDeleteBatch(final BatchEntity batch) {
        if (batch != null && !this.getTenantManager().isAuthenticatedTenant(batch.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("delete batch '" + batch.getId() + "'");
        }
    }
    
    @Override
    public void checkDeleteHistoricBatch(final HistoricBatchEntity batch) {
        if (batch != null && !this.getTenantManager().isAuthenticatedTenant(batch.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("delete historic batch '" + batch.getId() + "'");
        }
    }
    
    @Override
    public void checkSuspendBatch(final BatchEntity batch) {
        if (batch != null && !this.getTenantManager().isAuthenticatedTenant(batch.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("suspend batch '" + batch.getId() + "'");
        }
    }
    
    @Override
    public void checkActivateBatch(final BatchEntity batch) {
        if (batch != null && !this.getTenantManager().isAuthenticatedTenant(batch.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("activate batch '" + batch.getId() + "'");
        }
    }
    
    @Override
    public void checkReadHistoricBatch() {
    }
    
    @Override
    public void checkCreateDeployment() {
    }
    
    @Override
    public void checkReadDeployment(final String deploymentId) {
        if (this.getTenantManager().isTenantCheckEnabled()) {
            final DeploymentEntity deployment = this.findDeploymentById(deploymentId);
            if (deployment != null && !this.getTenantManager().isAuthenticatedTenant(deployment.getTenantId())) {
                throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("get the deployment '" + deploymentId + "'");
            }
        }
    }
    
    @Override
    public void checkDeleteDeployment(final String deploymentId) {
        if (this.getTenantManager().isTenantCheckEnabled()) {
            final DeploymentEntity deployment = this.findDeploymentById(deploymentId);
            if (deployment != null && !this.getTenantManager().isAuthenticatedTenant(deployment.getTenantId())) {
                throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("delete the deployment '" + deploymentId + "'");
            }
        }
    }
    
    @Override
    public void checkDeleteTask(final TaskEntity task) {
        if (task != null && !this.getTenantManager().isAuthenticatedTenant(task.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("delete the task '" + task.getId() + "'");
        }
    }
    
    @Override
    public void checkTaskAssign(final TaskEntity task) {
        if (task != null && !this.getTenantManager().isAuthenticatedTenant(task.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("assign the task '" + task.getId() + "'");
        }
    }
    
    @Override
    public void checkCreateTask(final TaskEntity task) {
        if (task != null && !this.getTenantManager().isAuthenticatedTenant(task.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("create the task '" + task.getId() + "'");
        }
    }
    
    @Override
    public void checkCreateTask() {
    }
    
    @Override
    public void checkTaskWork(final TaskEntity task) {
        if (task != null && !this.getTenantManager().isAuthenticatedTenant(task.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("work on task '" + task.getId() + "'");
        }
    }
    
    @Override
    public void checkReadDecisionDefinition(final DecisionDefinitionEntity decisionDefinition) {
        if (decisionDefinition != null && !this.getTenantManager().isAuthenticatedTenant(decisionDefinition.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("get the decision definition '" + decisionDefinition.getId() + "'");
        }
    }
    
    @Override
    public void checkUpdateDecisionDefinitionById(final String decisionDefinitionId) {
        if (this.getTenantManager().isTenantCheckEnabled()) {
            final DecisionDefinitionEntity decisionDefinition = this.findLatestDecisionDefinitionById(decisionDefinitionId);
            if (decisionDefinition != null && !this.getTenantManager().isAuthenticatedTenant(decisionDefinition.getTenantId())) {
                throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("update the decision definition '" + decisionDefinitionId + "'");
            }
        }
    }
    
    @Override
    public void checkReadDecisionRequirementsDefinition(final DecisionRequirementsDefinitionEntity decisionRequirementsDefinition) {
        if (decisionRequirementsDefinition != null && !this.getTenantManager().isAuthenticatedTenant(decisionRequirementsDefinition.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("get the decision requirements definition '" + decisionRequirementsDefinition.getId() + "'");
        }
    }
    
    @Override
    public void checkReadCaseDefinition(final CaseDefinition caseDefinition) {
        if (caseDefinition != null && !this.getTenantManager().isAuthenticatedTenant(caseDefinition.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("get the case definition '" + caseDefinition.getId() + "'");
        }
    }
    
    @Override
    public void checkUpdateCaseDefinition(final CaseDefinition caseDefinition) {
        if (caseDefinition != null && !this.getTenantManager().isAuthenticatedTenant(caseDefinition.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("update the case definition '" + caseDefinition.getId() + "'");
        }
    }
    
    @Override
    public void checkDeleteHistoricTaskInstance(final HistoricTaskInstanceEntity task) {
        if (task != null && !this.getTenantManager().isAuthenticatedTenant(task.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("delete the historic task instance '" + task.getId() + "'");
        }
    }
    
    @Override
    public void checkDeleteHistoricProcessInstance(final HistoricProcessInstance instance) {
        if (instance != null && !this.getTenantManager().isAuthenticatedTenant(instance.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("delete the historic process instance '" + instance.getId() + "'");
        }
    }
    
    @Override
    public void checkDeleteHistoricCaseInstance(final HistoricCaseInstance instance) {
        if (instance != null && !this.getTenantManager().isAuthenticatedTenant(instance.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("delete the historic case instance '" + instance.getId() + "'");
        }
    }
    
    @Override
    public void checkDeleteHistoricDecisionInstance(final String decisionDefinitionKey) {
    }
    
    @Override
    public void checkDeleteHistoricDecisionInstance(final HistoricDecisionInstance decisionInstance) {
        if (decisionInstance != null && !this.getTenantManager().isAuthenticatedTenant(decisionInstance.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("delete the historic decision instance '" + decisionInstance.getId() + "'");
        }
    }
    
    @Override
    public void checkDeleteHistoricVariableInstance(final HistoricVariableInstanceEntity variable) {
        if (variable != null && !this.getTenantManager().isAuthenticatedTenant(variable.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("delete the historic variable instance '" + variable.getId() + "'");
        }
    }
    
    @Override
    public void checkDeleteHistoricVariableInstancesByProcessInstance(final HistoricProcessInstanceEntity instance) {
        if (instance != null && !this.getTenantManager().isAuthenticatedTenant(instance.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("delete the historic variable instances of process instance '" + instance.getId() + "'");
        }
    }
    
    @Override
    public void checkReadHistoricJobLog(final HistoricJobLogEventEntity historicJobLog) {
        if (historicJobLog != null && !this.getTenantManager().isAuthenticatedTenant(historicJobLog.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("get the historic job log '" + historicJobLog.getId() + "'");
        }
    }
    
    @Override
    public void checkReadHistoryAnyProcessDefinition() {
    }
    
    @Override
    public void checkReadHistoryProcessDefinition(final String processDefinitionId) {
    }
    
    @Override
    public void checkUpdateCaseInstance(final CaseExecution caseExecution) {
        if (caseExecution != null && !this.getTenantManager().isAuthenticatedTenant(caseExecution.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("update the case execution '" + caseExecution.getId() + "'");
        }
    }
    
    @Override
    public void checkReadCaseInstance(final CaseExecution caseExecution) {
        if (caseExecution != null && !this.getTenantManager().isAuthenticatedTenant(caseExecution.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("get the case execution '" + caseExecution.getId() + "'");
        }
    }
    
    @Override
    public void checkDeleteUserOperationLog(final UserOperationLogEntry entry) {
    }
    
    @Override
    public void checkUpdateUserOperationLog(final UserOperationLogEntry entry) {
    }
    
    @Override
    public void checkReadHistoricExternalTaskLog(final HistoricExternalTaskLogEntity historicExternalTaskLog) {
        if (historicExternalTaskLog != null && !this.getTenantManager().isAuthenticatedTenant(historicExternalTaskLog.getTenantId())) {
            throw TenantCommandChecker.LOG.exceptionCommandWithUnauthorizedTenant("get the historic external task log '" + historicExternalTaskLog.getId() + "'");
        }
    }
    
    @Override
    public void checkReadTelemetryData() {
    }
    
    @Override
    public void checkConfigureTelemetry() {
    }
    
    @Override
    public void checkReadTelemetryCollectionStatusData() {
    }
    
    @Override
    public void checkReadHistoryLevel() {
    }
    
    @Override
    public void checkReadTableCount() {
    }
    
    @Override
    public void checkReadTableName() {
    }
    
    @Override
    public void checkReadTableMetaData() {
    }
    
    @Override
    public void checkReadProperties() {
    }
    
    @Override
    public void checkSetProperty() {
    }
    
    @Override
    public void checkDeleteProperty() {
    }
    
    @Override
    public void checkDeleteLicenseKey() {
    }
    
    @Override
    public void checkSetLicenseKey() {
    }
    
    @Override
    public void checkReadLicenseKey() {
    }
    
    @Override
    public void checkRegisterProcessApplication() {
    }
    
    @Override
    public void checkUnregisterProcessApplication() {
    }
    
    @Override
    public void checkReadRegisteredDeployments() {
    }
    
    @Override
    public void checkReadProcessApplicationForDeployment() {
    }
    
    @Override
    public void checkRegisterDeployment() {
    }
    
    @Override
    public void checkUnregisterDeployment() {
    }
    
    @Override
    public void checkDeleteMetrics() {
    }
    
    @Override
    public void checkDeleteTaskMetrics() {
    }
    
    @Override
    public void checkReadSchemaLog() {
    }
    
    protected TenantManager getTenantManager() {
        return Context.getCommandContext().getTenantManager();
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
    
    protected DeploymentEntity findDeploymentById(final String deploymentId) {
        return Context.getCommandContext().getDeploymentManager().findDeploymentById(deploymentId);
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
