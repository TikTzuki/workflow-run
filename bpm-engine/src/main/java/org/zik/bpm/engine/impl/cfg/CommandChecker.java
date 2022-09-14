// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg;

import org.zik.bpm.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.zik.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.zik.bpm.engine.impl.history.event.HistoricExternalTaskLogEntity;
import org.zik.bpm.engine.history.UserOperationLogEntry;
import org.zik.bpm.engine.runtime.CaseExecution;
import org.zik.bpm.engine.impl.persistence.entity.HistoricJobLogEventEntity;
import org.zik.bpm.engine.history.HistoricDecisionInstance;
import org.zik.bpm.engine.history.HistoricCaseInstance;
import org.zik.bpm.engine.history.HistoricProcessInstance;
import org.zik.bpm.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionEntity;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionEntity;
import org.zik.bpm.engine.impl.batch.history.HistoricBatchEntity;
import org.zik.bpm.engine.impl.batch.BatchEntity;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.repository.CaseDefinition;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.repository.DecisionDefinition;

public interface CommandChecker
{
    void checkEvaluateDecision(final DecisionDefinition p0);
    
    void checkCreateProcessInstance(final ProcessDefinition p0);
    
    void checkReadProcessDefinition(final ProcessDefinition p0);
    
    void checkCreateCaseInstance(final CaseDefinition p0);
    
    void checkUpdateProcessDefinitionById(final String p0);
    
    void checkUpdateProcessDefinitionSuspensionStateById(final String p0);
    
    void checkUpdateProcessInstanceByProcessDefinitionId(final String p0);
    
    void checkUpdateRetriesProcessInstanceByProcessDefinitionId(final String p0);
    
    void checkUpdateProcessInstanceSuspensionStateByProcessDefinitionId(final String p0);
    
    void checkUpdateDecisionDefinitionById(final String p0);
    
    void checkUpdateProcessDefinitionByKey(final String p0);
    
    void checkUpdateProcessDefinitionSuspensionStateByKey(final String p0);
    
    void checkDeleteProcessDefinitionById(final String p0);
    
    void checkDeleteProcessDefinitionByKey(final String p0);
    
    void checkUpdateProcessInstanceByProcessDefinitionKey(final String p0);
    
    void checkUpdateProcessInstanceSuspensionStateByProcessDefinitionKey(final String p0);
    
    void checkUpdateProcessInstanceById(final String p0);
    
    void checkUpdateProcessInstanceSuspensionStateById(final String p0);
    
    void checkUpdateProcessInstance(final ExecutionEntity p0);
    
    void checkUpdateProcessInstanceVariables(final ExecutionEntity p0);
    
    void checkCreateMigrationPlan(final ProcessDefinition p0, final ProcessDefinition p1);
    
    void checkMigrateProcessInstance(final ExecutionEntity p0, final ProcessDefinition p1);
    
    void checkReadProcessInstance(final String p0);
    
    void checkReadJob(final JobEntity p0);
    
    void checkUpdateJob(final JobEntity p0);
    
    void checkUpdateRetriesJob(final JobEntity p0);
    
    void checkReadProcessInstance(final ExecutionEntity p0);
    
    void checkReadProcessInstanceVariable(final ExecutionEntity p0);
    
    void checkDeleteProcessInstance(final ExecutionEntity p0);
    
    void checkReadTask(final TaskEntity p0);
    
    void checkReadTaskVariable(final TaskEntity p0);
    
    void checkUpdateTaskVariable(final TaskEntity p0);
    
    void checkCreateBatch(final Permission p0);
    
    void checkDeleteBatch(final BatchEntity p0);
    
    void checkDeleteHistoricBatch(final HistoricBatchEntity p0);
    
    void checkSuspendBatch(final BatchEntity p0);
    
    void checkActivateBatch(final BatchEntity p0);
    
    void checkReadHistoricBatch();
    
    void checkCreateDeployment();
    
    void checkReadDeployment(final String p0);
    
    void checkDeleteDeployment(final String p0);
    
    void checkTaskAssign(final TaskEntity p0);
    
    void checkCreateTask(final TaskEntity p0);
    
    void checkCreateTask();
    
    void checkTaskWork(final TaskEntity p0);
    
    void checkDeleteTask(final TaskEntity p0);
    
    void checkReadDecisionDefinition(final DecisionDefinitionEntity p0);
    
    void checkReadDecisionRequirementsDefinition(final DecisionRequirementsDefinitionEntity p0);
    
    void checkReadCaseDefinition(final CaseDefinition p0);
    
    void checkUpdateCaseDefinition(final CaseDefinition p0);
    
    void checkDeleteHistoricTaskInstance(final HistoricTaskInstanceEntity p0);
    
    void checkDeleteHistoricProcessInstance(final HistoricProcessInstance p0);
    
    void checkDeleteHistoricCaseInstance(final HistoricCaseInstance p0);
    
    void checkDeleteHistoricDecisionInstance(final String p0);
    
    void checkDeleteHistoricDecisionInstance(final HistoricDecisionInstance p0);
    
    void checkReadHistoricJobLog(final HistoricJobLogEventEntity p0);
    
    void checkReadHistoryAnyProcessDefinition();
    
    void checkReadHistoryProcessDefinition(final String p0);
    
    void checkUpdateCaseInstance(final CaseExecution p0);
    
    void checkDeleteUserOperationLog(final UserOperationLogEntry p0);
    
    void checkUpdateUserOperationLog(final UserOperationLogEntry p0);
    
    void checkReadCaseInstance(final CaseExecution p0);
    
    void checkReadHistoricExternalTaskLog(final HistoricExternalTaskLogEntity p0);
    
    void checkDeleteHistoricVariableInstance(final HistoricVariableInstanceEntity p0);
    
    void checkDeleteHistoricVariableInstancesByProcessInstance(final HistoricProcessInstanceEntity p0);
    
    void checkReadTelemetryData();
    
    void checkConfigureTelemetry();
    
    void checkReadTelemetryCollectionStatusData();
    
    void checkReadHistoryLevel();
    
    void checkReadTableCount();
    
    void checkReadTableName();
    
    void checkReadTableMetaData();
    
    void checkReadProperties();
    
    void checkSetProperty();
    
    void checkDeleteProperty();
    
    void checkDeleteLicenseKey();
    
    void checkSetLicenseKey();
    
    void checkReadLicenseKey();
    
    void checkRegisterProcessApplication();
    
    void checkUnregisterProcessApplication();
    
    void checkReadRegisteredDeployments();
    
    void checkReadProcessApplicationForDeployment();
    
    void checkRegisterDeployment();
    
    void checkUnregisterDeployment();
    
    void checkDeleteMetrics();
    
    void checkDeleteTaskMetrics();
    
    void checkReadSchemaLog();
}
