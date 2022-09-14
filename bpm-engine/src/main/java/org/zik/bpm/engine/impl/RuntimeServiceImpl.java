// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.runtime.ConditionEvaluationBuilder;
import org.zik.bpm.engine.impl.cmd.SetAnnotationForIncidentCmd;
import org.zik.bpm.engine.impl.cmd.ResolveIncidentCmd;
import org.zik.bpm.engine.impl.cmd.CreateIncidentCmd;
import org.zik.bpm.engine.runtime.Incident;
import org.zik.bpm.engine.runtime.RestartProcessInstanceBuilder;
import org.zik.bpm.engine.runtime.ModificationBuilder;
import org.zik.bpm.engine.impl.migration.MigrationPlanExecutionBuilderImpl;
import org.zik.bpm.engine.migration.MigrationPlanExecutionBuilder;
import org.zik.bpm.engine.migration.MigrationPlan;
import org.zik.bpm.engine.impl.migration.MigrationPlanBuilderImpl;
import org.zik.bpm.engine.migration.MigrationPlanBuilder;
import org.zik.bpm.engine.runtime.ProcessInstanceModificationBuilder;
import org.zik.bpm.engine.runtime.MessageCorrelationAsyncBuilder;
import org.zik.bpm.engine.runtime.MessageCorrelationBuilder;
import org.zik.bpm.engine.impl.cmd.MessageEventReceivedCmd;
import org.zik.bpm.engine.runtime.SignalEventReceivedBuilder;
import org.zik.bpm.engine.impl.runtime.UpdateProcessInstanceSuspensionStateBuilderImpl;
import org.zik.bpm.engine.runtime.UpdateProcessInstanceSuspensionStateSelectBuilder;
import org.zik.bpm.engine.impl.cmd.GetStartFormCmd;
import org.zik.bpm.engine.form.FormData;
import org.zik.bpm.engine.impl.cmd.GetActivityInstanceCmd;
import org.zik.bpm.engine.runtime.ActivityInstance;
import org.zik.bpm.engine.impl.cmd.FindActiveActivityIdsCmd;
import org.zik.bpm.engine.impl.cmd.SignalCmd;
import org.zik.bpm.engine.impl.cmd.PatchExecutionVariablesCmd;
import org.zik.bpm.engine.impl.cmd.RemoveExecutionVariablesCmd;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.cmd.batch.variables.SetVariablesToProcessInstancesBatchCmd;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.util.ExceptionUtil;
import org.zik.bpm.engine.impl.cmd.SetExecutionVariablesCmd;
import java.util.HashMap;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.cmd.GetExecutionVariableTypedCmd;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.impl.cmd.GetExecutionVariableCmd;
import java.util.Collection;
import org.zik.bpm.engine.impl.cmd.GetExecutionVariablesCmd;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.runtime.VariableInstanceQuery;
import org.zik.bpm.engine.runtime.EventSubscriptionQuery;
import org.zik.bpm.engine.runtime.IncidentQuery;
import org.zik.bpm.engine.runtime.NativeProcessInstanceQuery;
import org.zik.bpm.engine.runtime.NativeExecutionQuery;
import org.zik.bpm.engine.runtime.ExecutionQuery;
import org.zik.bpm.engine.impl.cmd.DeleteProcessInstancesCmd;
import org.zik.bpm.engine.impl.cmd.DeleteProcessInstanceCmd;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;
import org.zik.bpm.engine.impl.cmd.batch.DeleteProcessInstanceBatchCmd;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.runtime.ProcessInstanceQuery;
import java.util.List;
import org.zik.bpm.engine.runtime.ProcessInstantiationBuilder;
import java.util.Map;
import org.zik.bpm.engine.runtime.ProcessInstance;
import org.zik.bpm.engine.RuntimeService;

public class RuntimeServiceImpl extends ServiceImpl implements RuntimeService
{
    @Override
    public ProcessInstance startProcessInstanceByKey(final String processDefinitionKey) {
        return this.createProcessInstanceByKey(processDefinitionKey).execute();
    }
    
    @Override
    public ProcessInstance startProcessInstanceByKey(final String processDefinitionKey, final String businessKey) {
        return this.createProcessInstanceByKey(processDefinitionKey).businessKey(businessKey).execute();
    }
    
    @Override
    public ProcessInstance startProcessInstanceByKey(final String processDefinitionKey, final String businessKey, final String caseInstanceId) {
        return this.createProcessInstanceByKey(processDefinitionKey).businessKey(businessKey).caseInstanceId(caseInstanceId).execute();
    }
    
    @Override
    public ProcessInstance startProcessInstanceByKey(final String processDefinitionKey, final Map<String, Object> variables) {
        return this.createProcessInstanceByKey(processDefinitionKey).setVariables(variables).execute();
    }
    
    @Override
    public ProcessInstance startProcessInstanceByKey(final String processDefinitionKey, final String businessKey, final Map<String, Object> variables) {
        return this.createProcessInstanceByKey(processDefinitionKey).businessKey(businessKey).setVariables(variables).execute();
    }
    
    @Override
    public ProcessInstance startProcessInstanceByKey(final String processDefinitionKey, final String businessKey, final String caseInstanceId, final Map<String, Object> variables) {
        return this.createProcessInstanceByKey(processDefinitionKey).businessKey(businessKey).caseInstanceId(caseInstanceId).setVariables(variables).execute();
    }
    
    @Override
    public ProcessInstance startProcessInstanceById(final String processDefinitionId) {
        return this.createProcessInstanceById(processDefinitionId).execute();
    }
    
    @Override
    public ProcessInstance startProcessInstanceById(final String processDefinitionId, final String businessKey) {
        return this.createProcessInstanceById(processDefinitionId).businessKey(businessKey).execute();
    }
    
    @Override
    public ProcessInstance startProcessInstanceById(final String processDefinitionId, final String businessKey, final String caseInstanceId) {
        return this.createProcessInstanceById(processDefinitionId).businessKey(businessKey).caseInstanceId(caseInstanceId).execute();
    }
    
    @Override
    public ProcessInstance startProcessInstanceById(final String processDefinitionId, final Map<String, Object> variables) {
        return this.createProcessInstanceById(processDefinitionId).setVariables(variables).execute();
    }
    
    @Override
    public ProcessInstance startProcessInstanceById(final String processDefinitionId, final String businessKey, final Map<String, Object> variables) {
        return this.createProcessInstanceById(processDefinitionId).businessKey(businessKey).setVariables(variables).execute();
    }
    
    @Override
    public ProcessInstance startProcessInstanceById(final String processDefinitionId, final String businessKey, final String caseInstanceId, final Map<String, Object> variables) {
        return this.createProcessInstanceById(processDefinitionId).businessKey(businessKey).caseInstanceId(caseInstanceId).setVariables(variables).execute();
    }
    
    @Override
    public void deleteProcessInstance(final String processInstanceId, final String deleteReason) {
        this.deleteProcessInstance(processInstanceId, deleteReason, false);
    }
    
    @Override
    public Batch deleteProcessInstancesAsync(final List<String> processInstanceIds, final ProcessInstanceQuery processInstanceQuery, final String deleteReason) {
        return this.deleteProcessInstancesAsync(processInstanceIds, processInstanceQuery, deleteReason, false);
    }
    
    @Override
    public Batch deleteProcessInstancesAsync(final List<String> processInstanceIds, final String deleteReason) {
        return this.deleteProcessInstancesAsync(processInstanceIds, null, deleteReason, false);
    }
    
    @Override
    public Batch deleteProcessInstancesAsync(final ProcessInstanceQuery processInstanceQuery, final String deleteReason) {
        return this.deleteProcessInstancesAsync(null, processInstanceQuery, deleteReason, false);
    }
    
    @Override
    public Batch deleteProcessInstancesAsync(final List<String> processInstanceIds, final ProcessInstanceQuery processInstanceQuery, final String deleteReason, final boolean skipCustomListeners) {
        return this.deleteProcessInstancesAsync(processInstanceIds, processInstanceQuery, deleteReason, skipCustomListeners, false);
    }
    
    @Override
    public Batch deleteProcessInstancesAsync(final List<String> processInstanceIds, final ProcessInstanceQuery processInstanceQuery, final String deleteReason, final boolean skipCustomListeners, final boolean skipSubprocesses) {
        return this.commandExecutor.execute((Command<Batch>)new DeleteProcessInstanceBatchCmd(processInstanceIds, processInstanceQuery, null, deleteReason, skipCustomListeners, skipSubprocesses));
    }
    
    @Override
    public Batch deleteProcessInstancesAsync(final List<String> processInstanceIds, final ProcessInstanceQuery processInstanceQuery, final HistoricProcessInstanceQuery historicProcessInstanceQuery, final String deleteReason, final boolean skipCustomListeners, final boolean skipSubprocesses) {
        return this.commandExecutor.execute((Command<Batch>)new DeleteProcessInstanceBatchCmd(processInstanceIds, processInstanceQuery, historicProcessInstanceQuery, deleteReason, skipCustomListeners, skipSubprocesses));
    }
    
    @Override
    public void deleteProcessInstance(final String processInstanceId, final String deleteReason, final boolean skipCustomListeners) {
        this.deleteProcessInstance(processInstanceId, deleteReason, skipCustomListeners, false);
    }
    
    @Override
    public void deleteProcessInstance(final String processInstanceId, final String deleteReason, final boolean skipCustomListeners, final boolean externallyTerminated) {
        this.deleteProcessInstance(processInstanceId, deleteReason, skipCustomListeners, externallyTerminated, false);
    }
    
    @Override
    public void deleteProcessInstance(final String processInstanceId, final String deleteReason, final boolean skipCustomListeners, final boolean externallyTerminated, final boolean skipIoMappings) {
        this.deleteProcessInstance(processInstanceId, deleteReason, skipCustomListeners, externallyTerminated, skipIoMappings, false);
    }
    
    @Override
    public void deleteProcessInstance(final String processInstanceId, final String deleteReason, final boolean skipCustomListeners, final boolean externallyTerminated, final boolean skipIoMappings, final boolean skipSubprocesses) {
        this.commandExecutor.execute((Command<Object>)new DeleteProcessInstanceCmd(processInstanceId, deleteReason, skipCustomListeners, externallyTerminated, skipIoMappings, skipSubprocesses, true));
    }
    
    @Override
    public void deleteProcessInstanceIfExists(final String processInstanceId, final String deleteReason, final boolean skipCustomListeners, final boolean externallyTerminated, final boolean skipIoMappings, final boolean skipSubprocesses) {
        this.commandExecutor.execute((Command<Object>)new DeleteProcessInstanceCmd(processInstanceId, deleteReason, skipCustomListeners, externallyTerminated, skipIoMappings, skipSubprocesses, false));
    }
    
    @Override
    public void deleteProcessInstances(final List<String> processInstanceIds, final String deleteReason, final boolean skipCustomListeners, final boolean externallyTerminated) {
        this.deleteProcessInstances(processInstanceIds, deleteReason, skipCustomListeners, externallyTerminated, false);
    }
    
    @Override
    public void deleteProcessInstances(final List<String> processInstanceIds, final String deleteReason, final boolean skipCustomListeners, final boolean externallyTerminated, final boolean skipSubprocesses) {
        this.commandExecutor.execute((Command<Object>)new DeleteProcessInstancesCmd(processInstanceIds, deleteReason, skipCustomListeners, externallyTerminated, skipSubprocesses, true));
    }
    
    @Override
    public void deleteProcessInstancesIfExists(final List<String> processInstanceIds, final String deleteReason, final boolean skipCustomListeners, final boolean externallyTerminated, final boolean skipSubprocesses) {
        this.commandExecutor.execute((Command<Object>)new DeleteProcessInstancesCmd(processInstanceIds, deleteReason, skipCustomListeners, externallyTerminated, skipSubprocesses, false));
    }
    
    @Override
    public ExecutionQuery createExecutionQuery() {
        return new ExecutionQueryImpl(this.commandExecutor);
    }
    
    @Override
    public NativeExecutionQuery createNativeExecutionQuery() {
        return new NativeExecutionQueryImpl(this.commandExecutor);
    }
    
    @Override
    public NativeProcessInstanceQuery createNativeProcessInstanceQuery() {
        return new NativeProcessInstanceQueryImpl(this.commandExecutor);
    }
    
    @Override
    public IncidentQuery createIncidentQuery() {
        return new IncidentQueryImpl(this.commandExecutor);
    }
    
    @Override
    public EventSubscriptionQuery createEventSubscriptionQuery() {
        return new EventSubscriptionQueryImpl(this.commandExecutor);
    }
    
    @Override
    public VariableInstanceQuery createVariableInstanceQuery() {
        return new VariableInstanceQueryImpl(this.commandExecutor);
    }
    
    public VariableMap getVariables(final String executionId) {
        return this.getVariablesTyped(executionId);
    }
    
    @Override
    public VariableMap getVariablesTyped(final String executionId) {
        return this.getVariablesTyped(executionId, true);
    }
    
    @Override
    public VariableMap getVariablesTyped(final String executionId, final boolean deserializeObjectValues) {
        return this.commandExecutor.execute((Command<VariableMap>)new GetExecutionVariablesCmd(executionId, null, false, deserializeObjectValues));
    }
    
    public VariableMap getVariablesLocal(final String executionId) {
        return this.getVariablesLocalTyped(executionId);
    }
    
    @Override
    public VariableMap getVariablesLocalTyped(final String executionId) {
        return this.getVariablesLocalTyped(executionId, true);
    }
    
    @Override
    public VariableMap getVariablesLocalTyped(final String executionId, final boolean deserializeObjectValues) {
        return this.commandExecutor.execute((Command<VariableMap>)new GetExecutionVariablesCmd(executionId, null, true, deserializeObjectValues));
    }
    
    public VariableMap getVariables(final String executionId, final Collection<String> variableNames) {
        return this.getVariablesTyped(executionId, variableNames, true);
    }
    
    @Override
    public VariableMap getVariablesTyped(final String executionId, final Collection<String> variableNames, final boolean deserializeObjectValues) {
        return this.commandExecutor.execute((Command<VariableMap>)new GetExecutionVariablesCmd(executionId, variableNames, false, deserializeObjectValues));
    }
    
    public VariableMap getVariablesLocal(final String executionId, final Collection<String> variableNames) {
        return this.getVariablesLocalTyped(executionId, variableNames, true);
    }
    
    @Override
    public VariableMap getVariablesLocalTyped(final String executionId, final Collection<String> variableNames, final boolean deserializeObjectValues) {
        return this.commandExecutor.execute((Command<VariableMap>)new GetExecutionVariablesCmd(executionId, variableNames, true, deserializeObjectValues));
    }
    
    @Override
    public Object getVariable(final String executionId, final String variableName) {
        return this.commandExecutor.execute((Command<Object>)new GetExecutionVariableCmd(executionId, variableName, false));
    }
    
    @Override
    public <T extends TypedValue> T getVariableTyped(final String executionId, final String variableName) {
        return this.getVariableTyped(executionId, variableName, true);
    }
    
    @Override
    public <T extends TypedValue> T getVariableTyped(final String executionId, final String variableName, final boolean deserializeObjectValue) {
        return this.commandExecutor.execute(new GetExecutionVariableTypedCmd<T>(executionId, variableName, false, deserializeObjectValue));
    }
    
    @Override
    public <T extends TypedValue> T getVariableLocalTyped(final String executionId, final String variableName) {
        return this.getVariableLocalTyped(executionId, variableName, true);
    }
    
    @Override
    public <T extends TypedValue> T getVariableLocalTyped(final String executionId, final String variableName, final boolean deserializeObjectValue) {
        return this.commandExecutor.execute(new GetExecutionVariableTypedCmd<T>(executionId, variableName, true, deserializeObjectValue));
    }
    
    @Override
    public Object getVariableLocal(final String executionId, final String variableName) {
        return this.commandExecutor.execute((Command<Object>)new GetExecutionVariableCmd(executionId, variableName, true));
    }
    
    @Override
    public void setVariable(final String executionId, final String variableName, final Object value) {
        EnsureUtil.ensureNotNull("variableName", (Object)variableName);
        final Map<String, Object> variables = new HashMap<String, Object>();
        variables.put(variableName, value);
        this.setVariables(executionId, variables);
    }
    
    @Override
    public void setVariableLocal(final String executionId, final String variableName, final Object value) {
        EnsureUtil.ensureNotNull("variableName", (Object)variableName);
        final Map<String, Object> variables = new HashMap<String, Object>();
        variables.put(variableName, value);
        this.setVariablesLocal(executionId, variables);
    }
    
    @Override
    public void setVariables(final String executionId, final Map<String, ?> variables) {
        this.setVariables(executionId, variables, false);
    }
    
    @Override
    public void setVariablesLocal(final String executionId, final Map<String, ?> variables) {
        this.setVariables(executionId, variables, true);
    }
    
    protected void setVariables(final String executionId, final Map<String, ?> variables, final boolean local) {
        try {
            this.commandExecutor.execute((Command<Object>)new SetExecutionVariablesCmd(executionId, variables, local));
        }
        catch (ProcessEngineException ex) {
            if (ExceptionUtil.checkValueTooLongException(ex)) {
                throw new BadUserRequestException("Variable value is too long", ex);
            }
            throw ex;
        }
    }
    
    @Override
    public Batch setVariablesAsync(final List<String> processInstanceIds, final ProcessInstanceQuery processInstanceQuery, final HistoricProcessInstanceQuery historicProcessInstanceQuery, final Map<String, ?> variables) {
        return this.commandExecutor.execute((Command<Batch>)new SetVariablesToProcessInstancesBatchCmd(processInstanceIds, processInstanceQuery, historicProcessInstanceQuery, variables));
    }
    
    @Override
    public Batch setVariablesAsync(final List<String> processInstanceIds, final Map<String, ?> variables) {
        return this.commandExecutor.execute((Command<Batch>)new SetVariablesToProcessInstancesBatchCmd(processInstanceIds, null, null, variables));
    }
    
    @Override
    public Batch setVariablesAsync(final ProcessInstanceQuery processInstanceQuery, final Map<String, ?> variables) {
        return this.commandExecutor.execute((Command<Batch>)new SetVariablesToProcessInstancesBatchCmd(null, processInstanceQuery, null, variables));
    }
    
    @Override
    public Batch setVariablesAsync(final HistoricProcessInstanceQuery historicProcessInstanceQuery, final Map<String, ?> variables) {
        return this.commandExecutor.execute((Command<Batch>)new SetVariablesToProcessInstancesBatchCmd(null, null, historicProcessInstanceQuery, variables));
    }
    
    @Override
    public void removeVariable(final String executionId, final String variableName) {
        final Collection<String> variableNames = new ArrayList<String>();
        variableNames.add(variableName);
        this.commandExecutor.execute((Command<Object>)new RemoveExecutionVariablesCmd(executionId, variableNames, false));
    }
    
    @Override
    public void removeVariableLocal(final String executionId, final String variableName) {
        final Collection<String> variableNames = new ArrayList<String>();
        variableNames.add(variableName);
        this.commandExecutor.execute((Command<Object>)new RemoveExecutionVariablesCmd(executionId, variableNames, true));
    }
    
    @Override
    public void removeVariables(final String executionId, final Collection<String> variableNames) {
        this.commandExecutor.execute((Command<Object>)new RemoveExecutionVariablesCmd(executionId, variableNames, false));
    }
    
    @Override
    public void removeVariablesLocal(final String executionId, final Collection<String> variableNames) {
        this.commandExecutor.execute((Command<Object>)new RemoveExecutionVariablesCmd(executionId, variableNames, true));
    }
    
    public void updateVariables(final String executionId, final Map<String, ?> modifications, final Collection<String> deletions) {
        this.updateVariables(executionId, modifications, deletions, false);
    }
    
    public void updateVariablesLocal(final String executionId, final Map<String, ?> modifications, final Collection<String> deletions) {
        this.updateVariables(executionId, modifications, deletions, true);
    }
    
    protected void updateVariables(final String executionId, final Map<String, ?> modifications, final Collection<String> deletions, final boolean local) {
        try {
            this.commandExecutor.execute((Command<Object>)new PatchExecutionVariablesCmd(executionId, modifications, deletions, local));
        }
        catch (ProcessEngineException ex) {
            if (ExceptionUtil.checkValueTooLongException(ex)) {
                throw new BadUserRequestException("Variable value is too long", ex);
            }
            throw ex;
        }
    }
    
    @Override
    public void signal(final String executionId) {
        this.commandExecutor.execute((Command<Object>)new SignalCmd(executionId, null, null, null));
    }
    
    @Override
    public void signal(final String executionId, final String signalName, final Object signalData, final Map<String, Object> processVariables) {
        this.commandExecutor.execute((Command<Object>)new SignalCmd(executionId, signalName, signalData, processVariables));
    }
    
    @Override
    public void signal(final String executionId, final Map<String, Object> processVariables) {
        this.commandExecutor.execute((Command<Object>)new SignalCmd(executionId, null, null, processVariables));
    }
    
    @Override
    public ProcessInstanceQuery createProcessInstanceQuery() {
        return new ProcessInstanceQueryImpl(this.commandExecutor);
    }
    
    @Override
    public List<String> getActiveActivityIds(final String executionId) {
        return this.commandExecutor.execute((Command<List<String>>)new FindActiveActivityIdsCmd(executionId));
    }
    
    @Override
    public ActivityInstance getActivityInstance(final String processInstanceId) {
        return this.commandExecutor.execute((Command<ActivityInstance>)new GetActivityInstanceCmd(processInstanceId));
    }
    
    public FormData getFormInstanceById(final String processDefinitionId) {
        return this.commandExecutor.execute((Command<FormData>)new GetStartFormCmd(processDefinitionId));
    }
    
    @Override
    public void suspendProcessInstanceById(final String processInstanceId) {
        this.updateProcessInstanceSuspensionState().byProcessInstanceId(processInstanceId).suspend();
    }
    
    @Override
    public void suspendProcessInstanceByProcessDefinitionId(final String processDefinitionId) {
        this.updateProcessInstanceSuspensionState().byProcessDefinitionId(processDefinitionId).suspend();
    }
    
    @Override
    public void suspendProcessInstanceByProcessDefinitionKey(final String processDefinitionKey) {
        this.updateProcessInstanceSuspensionState().byProcessDefinitionKey(processDefinitionKey).suspend();
    }
    
    @Override
    public void activateProcessInstanceById(final String processInstanceId) {
        this.updateProcessInstanceSuspensionState().byProcessInstanceId(processInstanceId).activate();
    }
    
    @Override
    public void activateProcessInstanceByProcessDefinitionId(final String processDefinitionId) {
        this.updateProcessInstanceSuspensionState().byProcessDefinitionId(processDefinitionId).activate();
    }
    
    @Override
    public void activateProcessInstanceByProcessDefinitionKey(final String processDefinitionKey) {
        this.updateProcessInstanceSuspensionState().byProcessDefinitionKey(processDefinitionKey).activate();
    }
    
    @Override
    public UpdateProcessInstanceSuspensionStateSelectBuilder updateProcessInstanceSuspensionState() {
        return new UpdateProcessInstanceSuspensionStateBuilderImpl(this.commandExecutor);
    }
    
    @Override
    public ProcessInstance startProcessInstanceByMessage(final String messageName) {
        return this.createMessageCorrelation(messageName).correlateStartMessage();
    }
    
    @Override
    public ProcessInstance startProcessInstanceByMessage(final String messageName, final String businessKey) {
        return this.createMessageCorrelation(messageName).processInstanceBusinessKey(businessKey).correlateStartMessage();
    }
    
    @Override
    public ProcessInstance startProcessInstanceByMessage(final String messageName, final Map<String, Object> processVariables) {
        return this.createMessageCorrelation(messageName).setVariables(processVariables).correlateStartMessage();
    }
    
    @Override
    public ProcessInstance startProcessInstanceByMessage(final String messageName, final String businessKey, final Map<String, Object> processVariables) {
        return this.createMessageCorrelation(messageName).processInstanceBusinessKey(businessKey).setVariables(processVariables).correlateStartMessage();
    }
    
    @Override
    public ProcessInstance startProcessInstanceByMessageAndProcessDefinitionId(final String messageName, final String processDefinitionId) {
        return this.createMessageCorrelation(messageName).processDefinitionId(processDefinitionId).correlateStartMessage();
    }
    
    @Override
    public ProcessInstance startProcessInstanceByMessageAndProcessDefinitionId(final String messageName, final String processDefinitionId, final String businessKey) {
        return this.createMessageCorrelation(messageName).processDefinitionId(processDefinitionId).processInstanceBusinessKey(businessKey).correlateStartMessage();
    }
    
    @Override
    public ProcessInstance startProcessInstanceByMessageAndProcessDefinitionId(final String messageName, final String processDefinitionId, final Map<String, Object> processVariables) {
        return this.createMessageCorrelation(messageName).processDefinitionId(processDefinitionId).setVariables(processVariables).correlateStartMessage();
    }
    
    @Override
    public ProcessInstance startProcessInstanceByMessageAndProcessDefinitionId(final String messageName, final String processDefinitionId, final String businessKey, final Map<String, Object> processVariables) {
        return this.createMessageCorrelation(messageName).processDefinitionId(processDefinitionId).processInstanceBusinessKey(businessKey).setVariables(processVariables).correlateStartMessage();
    }
    
    @Override
    public void signalEventReceived(final String signalName) {
        this.createSignalEvent(signalName).send();
    }
    
    @Override
    public void signalEventReceived(final String signalName, final Map<String, Object> processVariables) {
        this.createSignalEvent(signalName).setVariables(processVariables).send();
    }
    
    @Override
    public void signalEventReceived(final String signalName, final String executionId) {
        this.createSignalEvent(signalName).executionId(executionId).send();
    }
    
    @Override
    public void signalEventReceived(final String signalName, final String executionId, final Map<String, Object> processVariables) {
        this.createSignalEvent(signalName).executionId(executionId).setVariables(processVariables).send();
    }
    
    @Override
    public SignalEventReceivedBuilder createSignalEvent(final String signalName) {
        return new SignalEventReceivedBuilderImpl(this.commandExecutor, signalName);
    }
    
    @Override
    public void messageEventReceived(final String messageName, final String executionId) {
        EnsureUtil.ensureNotNull("messageName", (Object)messageName);
        this.commandExecutor.execute((Command<Object>)new MessageEventReceivedCmd(messageName, executionId, null));
    }
    
    @Override
    public void messageEventReceived(final String messageName, final String executionId, final Map<String, Object> processVariables) {
        EnsureUtil.ensureNotNull("messageName", (Object)messageName);
        this.commandExecutor.execute((Command<Object>)new MessageEventReceivedCmd(messageName, executionId, processVariables));
    }
    
    @Override
    public MessageCorrelationBuilder createMessageCorrelation(final String messageName) {
        return new MessageCorrelationBuilderImpl(this.commandExecutor, messageName);
    }
    
    @Override
    public void correlateMessage(final String messageName, final Map<String, Object> correlationKeys, final Map<String, Object> processVariables) {
        this.createMessageCorrelation(messageName).processInstanceVariablesEqual(correlationKeys).setVariables(processVariables).correlate();
    }
    
    @Override
    public void correlateMessage(final String messageName, final String businessKey, final Map<String, Object> correlationKeys, final Map<String, Object> processVariables) {
        this.createMessageCorrelation(messageName).processInstanceVariablesEqual(correlationKeys).processInstanceBusinessKey(businessKey).setVariables(processVariables).correlate();
    }
    
    @Override
    public void correlateMessage(final String messageName) {
        this.createMessageCorrelation(messageName).correlate();
    }
    
    @Override
    public void correlateMessage(final String messageName, final String businessKey) {
        this.createMessageCorrelation(messageName).processInstanceBusinessKey(businessKey).correlate();
    }
    
    @Override
    public void correlateMessage(final String messageName, final Map<String, Object> correlationKeys) {
        this.createMessageCorrelation(messageName).processInstanceVariablesEqual(correlationKeys).correlate();
    }
    
    @Override
    public void correlateMessage(final String messageName, final String businessKey, final Map<String, Object> processVariables) {
        this.createMessageCorrelation(messageName).processInstanceBusinessKey(businessKey).setVariables(processVariables).correlate();
    }
    
    @Override
    public MessageCorrelationAsyncBuilder createMessageCorrelationAsync(final String messageName) {
        return new MessageCorrelationAsyncBuilderImpl(this.commandExecutor, messageName);
    }
    
    @Override
    public ProcessInstanceModificationBuilder createProcessInstanceModification(final String processInstanceId) {
        return new ProcessInstanceModificationBuilderImpl(this.commandExecutor, processInstanceId);
    }
    
    @Override
    public ProcessInstantiationBuilder createProcessInstanceById(final String processDefinitionId) {
        return ProcessInstantiationBuilderImpl.createProcessInstanceById(this.commandExecutor, processDefinitionId);
    }
    
    @Override
    public ProcessInstantiationBuilder createProcessInstanceByKey(final String processDefinitionKey) {
        return ProcessInstantiationBuilderImpl.createProcessInstanceByKey(this.commandExecutor, processDefinitionKey);
    }
    
    @Override
    public MigrationPlanBuilder createMigrationPlan(final String sourceProcessDefinitionId, final String targetProcessDefinitionId) {
        return new MigrationPlanBuilderImpl(this.commandExecutor, sourceProcessDefinitionId, targetProcessDefinitionId);
    }
    
    @Override
    public MigrationPlanExecutionBuilder newMigration(final MigrationPlan migrationPlan) {
        return new MigrationPlanExecutionBuilderImpl(this.commandExecutor, migrationPlan);
    }
    
    @Override
    public ModificationBuilder createModification(final String processDefinitionId) {
        return new ModificationBuilderImpl(this.commandExecutor, processDefinitionId);
    }
    
    @Override
    public RestartProcessInstanceBuilder restartProcessInstances(final String processDefinitionId) {
        return new RestartProcessInstanceBuilderImpl(this.commandExecutor, processDefinitionId);
    }
    
    @Override
    public Incident createIncident(final String incidentType, final String executionId, final String configuration) {
        return this.createIncident(incidentType, executionId, configuration, null);
    }
    
    @Override
    public Incident createIncident(final String incidentType, final String executionId, final String configuration, final String message) {
        return this.commandExecutor.execute((Command<Incident>)new CreateIncidentCmd(incidentType, executionId, configuration, message));
    }
    
    @Override
    public void resolveIncident(final String incidentId) {
        this.commandExecutor.execute((Command<Object>)new ResolveIncidentCmd(incidentId));
    }
    
    @Override
    public void setAnnotationForIncidentById(final String incidentId, final String annotation) {
        this.commandExecutor.execute((Command<Object>)new SetAnnotationForIncidentCmd(incidentId, annotation));
    }
    
    @Override
    public void clearAnnotationForIncidentById(final String incidentId) {
        this.commandExecutor.execute((Command<Object>)new SetAnnotationForIncidentCmd(incidentId, null));
    }
    
    @Override
    public ConditionEvaluationBuilder createConditionEvaluation() {
        return new ConditionEvaluationBuilderImpl(this.commandExecutor);
    }
}
