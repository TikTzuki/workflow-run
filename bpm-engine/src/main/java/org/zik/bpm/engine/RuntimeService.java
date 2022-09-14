// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import org.zik.bpm.engine.runtime.ConditionEvaluationBuilder;
import org.zik.bpm.engine.runtime.Incident;
import org.zik.bpm.engine.runtime.RestartProcessInstanceBuilder;
import org.zik.bpm.engine.runtime.ModificationBuilder;
import org.zik.bpm.engine.migration.MigrationPlanExecutionBuilder;
import org.zik.bpm.engine.migration.MigrationPlan;
import org.zik.bpm.engine.migration.MigrationPlanBuilder;
import org.zik.bpm.engine.runtime.ProcessInstantiationBuilder;
import org.zik.bpm.engine.runtime.ProcessInstanceModificationBuilder;
import org.zik.bpm.engine.runtime.MessageCorrelationAsyncBuilder;
import org.zik.bpm.engine.runtime.MessageCorrelationBuilder;
import org.zik.bpm.engine.runtime.SignalEventReceivedBuilder;
import org.zik.bpm.engine.runtime.UpdateProcessInstanceSuspensionStateSelectBuilder;
import org.zik.bpm.engine.runtime.VariableInstanceQuery;
import org.zik.bpm.engine.runtime.EventSubscriptionQuery;
import org.zik.bpm.engine.runtime.IncidentQuery;
import org.zik.bpm.engine.runtime.NativeProcessInstanceQuery;
import org.zik.bpm.engine.runtime.NativeExecutionQuery;
import org.zik.bpm.engine.runtime.ExecutionQuery;
import org.camunda.bpm.engine.variable.value.TypedValue;
import java.util.Collection;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.runtime.ActivityInstance;
import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.runtime.ProcessInstanceQuery;
import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.runtime.ProcessInstance;

public interface RuntimeService
{
    ProcessInstance startProcessInstanceByKey(final String p0);
    
    ProcessInstance startProcessInstanceByKey(final String p0, final String p1);
    
    ProcessInstance startProcessInstanceByKey(final String p0, final String p1, final String p2);
    
    ProcessInstance startProcessInstanceByKey(final String p0, final Map<String, Object> p1);
    
    ProcessInstance startProcessInstanceByKey(final String p0, final String p1, final Map<String, Object> p2);
    
    ProcessInstance startProcessInstanceByKey(final String p0, final String p1, final String p2, final Map<String, Object> p3);
    
    ProcessInstance startProcessInstanceById(final String p0);
    
    ProcessInstance startProcessInstanceById(final String p0, final String p1);
    
    ProcessInstance startProcessInstanceById(final String p0, final String p1, final String p2);
    
    ProcessInstance startProcessInstanceById(final String p0, final Map<String, Object> p1);
    
    ProcessInstance startProcessInstanceById(final String p0, final String p1, final Map<String, Object> p2);
    
    ProcessInstance startProcessInstanceById(final String p0, final String p1, final String p2, final Map<String, Object> p3);
    
    ProcessInstance startProcessInstanceByMessage(final String p0);
    
    ProcessInstance startProcessInstanceByMessage(final String p0, final String p1);
    
    ProcessInstance startProcessInstanceByMessage(final String p0, final Map<String, Object> p1);
    
    ProcessInstance startProcessInstanceByMessage(final String p0, final String p1, final Map<String, Object> p2);
    
    ProcessInstance startProcessInstanceByMessageAndProcessDefinitionId(final String p0, final String p1);
    
    ProcessInstance startProcessInstanceByMessageAndProcessDefinitionId(final String p0, final String p1, final String p2);
    
    ProcessInstance startProcessInstanceByMessageAndProcessDefinitionId(final String p0, final String p1, final Map<String, Object> p2);
    
    ProcessInstance startProcessInstanceByMessageAndProcessDefinitionId(final String p0, final String p1, final String p2, final Map<String, Object> p3);
    
    void deleteProcessInstance(final String p0, final String p1);
    
    Batch deleteProcessInstancesAsync(final List<String> p0, final ProcessInstanceQuery p1, final String p2);
    
    Batch deleteProcessInstancesAsync(final List<String> p0, final ProcessInstanceQuery p1, final String p2, final boolean p3);
    
    Batch deleteProcessInstancesAsync(final List<String> p0, final ProcessInstanceQuery p1, final String p2, final boolean p3, final boolean p4);
    
    Batch deleteProcessInstancesAsync(final List<String> p0, final ProcessInstanceQuery p1, final HistoricProcessInstanceQuery p2, final String p3, final boolean p4, final boolean p5);
    
    Batch deleteProcessInstancesAsync(final ProcessInstanceQuery p0, final String p1);
    
    Batch deleteProcessInstancesAsync(final List<String> p0, final String p1);
    
    void deleteProcessInstance(final String p0, final String p1, final boolean p2);
    
    void deleteProcessInstance(final String p0, final String p1, final boolean p2, final boolean p3);
    
    void deleteProcessInstances(final List<String> p0, final String p1, final boolean p2, final boolean p3);
    
    void deleteProcessInstances(final List<String> p0, final String p1, final boolean p2, final boolean p3, final boolean p4);
    
    void deleteProcessInstancesIfExists(final List<String> p0, final String p1, final boolean p2, final boolean p3, final boolean p4);
    
    void deleteProcessInstance(final String p0, final String p1, final boolean p2, final boolean p3, final boolean p4);
    
    void deleteProcessInstance(final String p0, final String p1, final boolean p2, final boolean p3, final boolean p4, final boolean p5);
    
    void deleteProcessInstanceIfExists(final String p0, final String p1, final boolean p2, final boolean p3, final boolean p4, final boolean p5);
    
    List<String> getActiveActivityIds(final String p0);
    
    ActivityInstance getActivityInstance(final String p0);
    
    void signal(final String p0);
    
    void signal(final String p0, final String p1, final Object p2, final Map<String, Object> p3);
    
    void signal(final String p0, final Map<String, Object> p1);
    
    Map<String, Object> getVariables(final String p0);
    
    VariableMap getVariablesTyped(final String p0);
    
    VariableMap getVariablesTyped(final String p0, final boolean p1);
    
    Map<String, Object> getVariablesLocal(final String p0);
    
    VariableMap getVariablesLocalTyped(final String p0);
    
    VariableMap getVariablesLocalTyped(final String p0, final boolean p1);
    
    Map<String, Object> getVariables(final String p0, final Collection<String> p1);
    
    VariableMap getVariablesTyped(final String p0, final Collection<String> p1, final boolean p2);
    
    Map<String, Object> getVariablesLocal(final String p0, final Collection<String> p1);
    
    VariableMap getVariablesLocalTyped(final String p0, final Collection<String> p1, final boolean p2);
    
    Object getVariable(final String p0, final String p1);
    
     <T extends TypedValue> T getVariableTyped(final String p0, final String p1);
    
     <T extends TypedValue> T getVariableTyped(final String p0, final String p1, final boolean p2);
    
    Object getVariableLocal(final String p0, final String p1);
    
     <T extends TypedValue> T getVariableLocalTyped(final String p0, final String p1);
    
     <T extends TypedValue> T getVariableLocalTyped(final String p0, final String p1, final boolean p2);
    
    void setVariable(final String p0, final String p1, final Object p2);
    
    void setVariableLocal(final String p0, final String p1, final Object p2);
    
    void setVariables(final String p0, final Map<String, ?> p1);
    
    void setVariablesLocal(final String p0, final Map<String, ?> p1);
    
    Batch setVariablesAsync(final List<String> p0, final ProcessInstanceQuery p1, final HistoricProcessInstanceQuery p2, final Map<String, ?> p3);
    
    Batch setVariablesAsync(final List<String> p0, final Map<String, ?> p1);
    
    Batch setVariablesAsync(final ProcessInstanceQuery p0, final Map<String, ?> p1);
    
    Batch setVariablesAsync(final HistoricProcessInstanceQuery p0, final Map<String, ?> p1);
    
    void removeVariable(final String p0, final String p1);
    
    void removeVariableLocal(final String p0, final String p1);
    
    void removeVariables(final String p0, final Collection<String> p1);
    
    void removeVariablesLocal(final String p0, final Collection<String> p1);
    
    ExecutionQuery createExecutionQuery();
    
    NativeExecutionQuery createNativeExecutionQuery();
    
    ProcessInstanceQuery createProcessInstanceQuery();
    
    NativeProcessInstanceQuery createNativeProcessInstanceQuery();
    
    IncidentQuery createIncidentQuery();
    
    EventSubscriptionQuery createEventSubscriptionQuery();
    
    VariableInstanceQuery createVariableInstanceQuery();
    
    void suspendProcessInstanceById(final String p0);
    
    void suspendProcessInstanceByProcessDefinitionId(final String p0);
    
    void suspendProcessInstanceByProcessDefinitionKey(final String p0);
    
    void activateProcessInstanceById(final String p0);
    
    void activateProcessInstanceByProcessDefinitionId(final String p0);
    
    void activateProcessInstanceByProcessDefinitionKey(final String p0);
    
    UpdateProcessInstanceSuspensionStateSelectBuilder updateProcessInstanceSuspensionState();
    
    void signalEventReceived(final String p0);
    
    void signalEventReceived(final String p0, final Map<String, Object> p1);
    
    void signalEventReceived(final String p0, final String p1);
    
    void signalEventReceived(final String p0, final String p1, final Map<String, Object> p2);
    
    SignalEventReceivedBuilder createSignalEvent(final String p0);
    
    void messageEventReceived(final String p0, final String p1);
    
    void messageEventReceived(final String p0, final String p1, final Map<String, Object> p2);
    
    MessageCorrelationBuilder createMessageCorrelation(final String p0);
    
    void correlateMessage(final String p0);
    
    void correlateMessage(final String p0, final String p1);
    
    void correlateMessage(final String p0, final Map<String, Object> p1);
    
    void correlateMessage(final String p0, final String p1, final Map<String, Object> p2);
    
    void correlateMessage(final String p0, final Map<String, Object> p1, final Map<String, Object> p2);
    
    void correlateMessage(final String p0, final String p1, final Map<String, Object> p2, final Map<String, Object> p3);
    
    MessageCorrelationAsyncBuilder createMessageCorrelationAsync(final String p0);
    
    ProcessInstanceModificationBuilder createProcessInstanceModification(final String p0);
    
    ProcessInstantiationBuilder createProcessInstanceById(final String p0);
    
    ProcessInstantiationBuilder createProcessInstanceByKey(final String p0);
    
    MigrationPlanBuilder createMigrationPlan(final String p0, final String p1);
    
    MigrationPlanExecutionBuilder newMigration(final MigrationPlan p0);
    
    ModificationBuilder createModification(final String p0);
    
    RestartProcessInstanceBuilder restartProcessInstances(final String p0);
    
    Incident createIncident(final String p0, final String p1, final String p2);
    
    Incident createIncident(final String p0, final String p1, final String p2, final String p3);
    
    void resolveIncident(final String p0);
    
    void setAnnotationForIncidentById(final String p0, final String p1);
    
    void clearAnnotationForIncidentById(final String p0);
    
    ConditionEvaluationBuilder createConditionEvaluation();
}
