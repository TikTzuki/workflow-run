// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.history.HistoricProcessInstance;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import java.util.Arrays;
import org.zik.bpm.engine.MismatchingMessageCorrelationException;
import org.zik.bpm.engine.impl.runtime.CorrelationSet;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.application.impl.ProcessApplicationIdentifier;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.zik.bpm.engine.OptimisticLockingException;
import org.zik.bpm.engine.impl.util.ClassNameUtil;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class CommandLogger extends ProcessEngineLogger
{
    public void debugCreatingNewDeployment() {
        this.logDebug("001", "Creating new deployment", new Object[0]);
    }
    
    public void usingExistingDeployment() {
        this.logDebug("002", "Using existing deployment", new Object[0]);
    }
    
    public void debugModificationInstruction(final String processInstanceId, final int i, final String describe) {
        this.logDebug("003", "Modifying process instance '{}': Instruction {}: {}", new Object[] { processInstanceId, i, describe });
    }
    
    public void debugStartingInstruction(final String processInstanceId, final int i, final String describe) {
        this.logDebug("004", "Starting process instance '{}': Instruction {}: {}", new Object[] { processInstanceId, i, describe });
    }
    
    public void debugStartingCommand(final Command<?> cmd) {
        this.logDebug("005", "Starting command -------------------- {} ----------------------", new Object[] { ClassNameUtil.getClassNameWithoutPackage(cmd) });
    }
    
    public void debugFinishingCommand(final Command<?> cmd) {
        this.logDebug("006", "Finishing command -------------------- {} ----------------------", new Object[] { ClassNameUtil.getClassNameWithoutPackage(cmd) });
    }
    
    public void debugWaitingFor(final long waitTime) {
        this.logDebug("007", "Waiting for {} before retrying command", new Object[] { waitTime });
    }
    
    public void debugCaughtOptimisticLockingException(final OptimisticLockingException e) {
        this.logDebug("008", "caught optimistic locking excpetion", new Object[] { e });
    }
    
    public void debugOpeningNewCommandContext() {
        this.logDebug("009", "opening new command context", new Object[0]);
    }
    
    public void debugReusingExistingCommandContext() {
        this.logDebug("010", "reusing existing command context", new Object[0]);
    }
    
    public void closingCommandContext() {
        this.logDebug("011", "closing existing command context", new Object[0]);
    }
    
    public void calledInsideTransaction() {
        this.logDebug("012", "called inside transaction skipping", new Object[0]);
    }
    
    public void maskedExceptionInCommandContext(final Throwable throwable) {
        this.logDebug("013", "masked exception in command context. for root cause, see below as it will be rethrown later.", new Object[] { throwable });
    }
    
    public void exceptionWhileRollingBackTransaction(final Exception e) {
        this.logError("014", "exception while rolling back transaction", new Object[] { e });
    }
    
    public void exceptionWhileGettingValueForVariable(final Exception t) {
        this.logDebug("015", "exception while getting value for variable {}", new Object[] { t.getMessage(), t });
    }
    
    public void couldNotFindProcessDefinitionForEventSubscription(final EventSubscriptionEntity messageEventSubscription, final String processDefinitionId) {
        this.logDebug("016", "Found event subscription with {} but process definition {} could not be found.", new Object[] { messageEventSubscription, processDefinitionId });
    }
    
    public void debugIgnoringEventSubscription(final EventSubscriptionEntity eventSubscription, final String processDefinitionId) {
        this.logDebug("017", "Found event subscription with {} but process definition {} could not be found.", new Object[] { eventSubscription, processDefinitionId });
    }
    
    public void debugProcessingDeployment(final String name) {
        this.logDebug("018", "Processing deployment {}", new Object[] { name });
    }
    
    public void debugProcessingResource(final String name) {
        this.logDebug("019", "Processing resource {}", new Object[] { name });
    }
    
    public ProcessEngineException paWithNameNotRegistered(final String name) {
        return new ProcessEngineException(this.exceptionMessage("020", "A process application with name '{}' is not registered", new Object[] { name }));
    }
    
    public ProcessEngineException cannotReolvePa(final ProcessApplicationIdentifier processApplicationIdentifier) {
        return new ProcessEngineException(this.exceptionMessage("021", "Cannot resolve process application based on {}", new Object[] { processApplicationIdentifier }));
    }
    
    public void warnDisabledDeploymentLock() {
        this.logWarn("022", "No exclusive lock is aquired while deploying because it is disabled. This can lead to problems when multiple process engines use the same data source (i.e. in cluster mode).", new Object[0]);
    }
    
    public BadUserRequestException exceptionStartProcessInstanceByIdAndTenantId() {
        return new BadUserRequestException(this.exceptionMessage("023", "Cannot specify a tenant-id when start a process instance by process definition id.", new Object[0]));
    }
    
    public BadUserRequestException exceptionStartProcessInstanceAtStartActivityAndSkipListenersOrMapping() {
        return new BadUserRequestException(this.exceptionMessage("024", "Cannot skip custom listeners or input/output mappings when start a process instance at default start activity.", new Object[0]));
    }
    
    public BadUserRequestException exceptionCorrelateMessageWithProcessDefinitionId() {
        return new BadUserRequestException(this.exceptionMessage("025", "Cannot specify a process definition id when correlate a message, except for explicit correlation of a start message.", new Object[0]));
    }
    
    public BadUserRequestException exceptionCorrelateStartMessageWithCorrelationVariables() {
        return new BadUserRequestException(this.exceptionMessage("026", "Cannot specify correlation variables of a process instance when correlate a start message.", new Object[0]));
    }
    
    public BadUserRequestException exceptionDeliverSignalToSingleExecutionWithTenantId() {
        return new BadUserRequestException(this.exceptionMessage("027", "Cannot specify a tenant-id when deliver a signal to a single execution.", new Object[0]));
    }
    
    public BadUserRequestException exceptionCorrelateMessageWithProcessInstanceAndTenantId() {
        return new BadUserRequestException(this.exceptionMessage("028", "Cannot specify a tenant-id when correlate a message to a single process instance.", new Object[0]));
    }
    
    public BadUserRequestException exceptionCorrelateMessageWithProcessDefinitionAndTenantId() {
        return new BadUserRequestException(this.exceptionMessage("029", "Cannot specify a tenant-id when correlate a start message to a specific version of a process definition.", new Object[0]));
    }
    
    public MismatchingMessageCorrelationException exceptionCorrelateMessageToSingleProcessDefinition(final String messageName, final long processDefinitionCound, final CorrelationSet correlationSet) {
        return new MismatchingMessageCorrelationException(this.exceptionMessage("030", "Cannot correlate a message with name '{}' to a single process definition. {} process definitions match the correlations keys: {}", new Object[] { messageName, processDefinitionCound, correlationSet }));
    }
    
    public MismatchingMessageCorrelationException exceptionCorrelateMessageToSingleExecution(final String messageName, final long executionCound, final CorrelationSet correlationSet) {
        return new MismatchingMessageCorrelationException(this.exceptionMessage("031", "Cannot correlate a message with name '{}' to a single execution. {} executions match the correlation keys: {}", new Object[] { messageName, executionCound, correlationSet }));
    }
    
    public BadUserRequestException exceptionUpdateSuspensionStateForTenantOnlyByProcessDefinitionKey() {
        return new BadUserRequestException(this.exceptionMessage("032", "Can only specify a tenant-id when update the suspension state which is referenced by process definition key.", new Object[0]));
    }
    
    public ProcessEngineException exceptionBpmnErrorPropagationFailed(final String errorCode, final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("033", "Propagation of bpmn error {} failed. ", new Object[] { errorCode }), cause);
    }
    
    public ProcessEngineException exceptionCommandWithUnauthorizedTenant(final String command) {
        return new ProcessEngineException(this.exceptionMessage("034", "Cannot {} because it belongs to no authenticated tenant.", new Object[] { command }));
    }
    
    public void warnDeploymentResourceHasWrongName(final String resourceName, final String[] suffixes) {
        this.logWarn("035", String.format("Deployment resource '%s' will be ignored as its name must have one of suffixes %s.", resourceName, Arrays.toString(suffixes)), new Object[0]);
    }
    
    public ProcessEngineException processInstanceDoesNotExist(final String processInstanceId) {
        return new ProcessEngineException(this.exceptionMessage("036", "Process instance '{}' cannot be modified. The process instance does not exist", new Object[] { processInstanceId }));
    }
    
    public ProcessEngineException processDefinitionOfInstanceDoesNotMatchModification(final ExecutionEntity processInstance, final String processDefinitionId) {
        return new ProcessEngineException(this.exceptionMessage("037", "Process instance '{}' cannot be modified. Its process definition '{}' does not match given process definition '{}'", new Object[] { processInstance.getId(), processInstance.getProcessDefinitionId(), processDefinitionId }));
    }
    
    public void debugHistoryCleanupWrongConfiguration() {
        this.logDebug("038", "History cleanup won't be scheduled. Either configure batch window or call it with immediatelyDue = true.", new Object[0]);
    }
    
    public ProcessEngineException processDefinitionOfHistoricInstanceDoesNotMatchTheGivenOne(final HistoricProcessInstance historicProcessInstance, final String processDefinitionId) {
        return new ProcessEngineException(this.exceptionMessage("039", "Historic process instance '{}' cannot be restarted. Its process definition '{}' does not match given process definition '{}'", new Object[] { historicProcessInstance.getId(), historicProcessInstance.getProcessDefinitionId(), processDefinitionId }));
    }
    
    public ProcessEngineException historicProcessInstanceActive(final HistoricProcessInstance historicProcessInstance) {
        return new ProcessEngineException(this.exceptionMessage("040", "Historic process instance '{}' cannot be restarted. It is not completed or terminated.", new Object[] { historicProcessInstance.getId(), historicProcessInstance.getProcessDefinitionId() }));
    }
    
    public ProcessEngineException exceptionWhenStartFormScriptEvaluation(final String processDefinitionId, final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("041", "Unable to evaluate script when rendering start form of the process definition '{}'.", new Object[] { processDefinitionId }));
    }
    
    public ProcessEngineException exceptionWhenEvaluatingConditionalStartEventByProcessDefinition(final String processDefinitionId) {
        return new ProcessEngineException(this.exceptionMessage("042", "Process definition with id '{}' does not declare conditional start event.", new Object[] { processDefinitionId }));
    }
    
    public ProcessEngineException exceptionWhenEvaluatingConditionalStartEvent() {
        return new ProcessEngineException(this.exceptionMessage("043", "No subscriptions were found during evaluation of the conditional start events.", new Object[0]));
    }
    
    public BadUserRequestException exceptionSettingTransientVariablesAsyncNotSupported(final String variableName) {
        return new BadUserRequestException(this.exceptionMessage("044", "Setting transient variable '{}' asynchronously is currently not supported.", new Object[] { variableName }));
    }
    
    public void crdbTransactionRetryAttempt(final Throwable cause) {
        this.logDebug("045", "A CockroachDB transaction retry attempt will be made. Reason: {}", new Object[] { cause.getMessage() });
    }
    
    public void debugNotAllowedToResolveCalledProcess(final String calledProcessId, final String callingProcessId, final String callActivityId, final Throwable cause) {
        this.logDebug("046", "Resolving a called process definition {} for {} in {} was not possible. Reason: {}", new Object[] { calledProcessId, callActivityId, callingProcessId, cause.getMessage() });
    }
}
