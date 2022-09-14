// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.producer;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.history.event.HistoricProcessInstanceEventEntity;
import org.zik.bpm.engine.impl.cfg.multitenancy.TenantIdProvider;
import org.zik.bpm.engine.impl.cfg.multitenancy.TenantIdProviderHistoricDecisionInstanceContext;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.history.event.HistoricDecisionOutputInstanceEntity;
import org.camunda.bpm.dmn.engine.delegate.DmnEvaluatedOutput;
import org.camunda.bpm.dmn.engine.delegate.DmnEvaluatedDecisionRule;
import org.camunda.bpm.engine.variable.Variables;
import org.zik.bpm.engine.impl.history.event.HistoricDecisionInputInstanceEntity;
import org.camunda.bpm.dmn.engine.delegate.DmnEvaluatedInput;
import org.camunda.bpm.engine.variable.value.DoubleValue;
import org.camunda.bpm.engine.variable.value.LongValue;
import org.camunda.bpm.engine.variable.value.IntegerValue;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.history.HistoricDecisionOutputInstance;
import org.zik.bpm.engine.history.HistoricDecisionInputInstance;
import java.util.Date;
import java.util.Collections;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionLiteralExpressionEvaluationEvent;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionTableEvaluationEvent;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.repository.DecisionDefinition;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.history.event.HistoricDecisionEvaluationEvent;
import java.util.Collection;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.delegate.DelegateCaseExecution;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.history.event.HistoricDecisionInstanceEntity;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionLogicEvaluationEvent;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationEvent;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;

public class DefaultDmnHistoryEventProducer implements DmnHistoryEventProducer
{
    protected static final EnginePersistenceLogger LOG;
    
    @Override
    public HistoryEvent createDecisionEvaluatedEvt(final DelegateExecution execution, final DmnDecisionEvaluationEvent evaluationEvent) {
        return this.createHistoryEvent(evaluationEvent, new HistoricDecisionInstanceSupplier() {
            @Override
            public HistoricDecisionInstanceEntity createHistoricDecisionInstance(final DmnDecisionLogicEvaluationEvent evaluationEvent, final HistoricDecisionInstanceEntity rootDecisionInstance) {
                return DefaultDmnHistoryEventProducer.this.createDecisionEvaluatedEvt(evaluationEvent, (ExecutionEntity)execution);
            }
        });
    }
    
    @Override
    public HistoryEvent createDecisionEvaluatedEvt(final DelegateCaseExecution execution, final DmnDecisionEvaluationEvent evaluationEvent) {
        return this.createHistoryEvent(evaluationEvent, new HistoricDecisionInstanceSupplier() {
            @Override
            public HistoricDecisionInstanceEntity createHistoricDecisionInstance(final DmnDecisionLogicEvaluationEvent evaluationEvent, final HistoricDecisionInstanceEntity rootDecisionInstance) {
                return DefaultDmnHistoryEventProducer.this.createDecisionEvaluatedEvt(evaluationEvent, (CaseExecutionEntity)execution);
            }
        });
    }
    
    @Override
    public HistoryEvent createDecisionEvaluatedEvt(final DmnDecisionEvaluationEvent evaluationEvent) {
        return this.createHistoryEvent(evaluationEvent, new HistoricDecisionInstanceSupplier() {
            @Override
            public HistoricDecisionInstanceEntity createHistoricDecisionInstance(final DmnDecisionLogicEvaluationEvent evaluationEvent, final HistoricDecisionInstanceEntity rootDecisionInstance) {
                return DefaultDmnHistoryEventProducer.this.createDecisionEvaluatedEvt(evaluationEvent, rootDecisionInstance);
            }
        });
    }
    
    protected HistoryEvent createHistoryEvent(final DmnDecisionEvaluationEvent evaluationEvent, final HistoricDecisionInstanceSupplier supplier) {
        final HistoricDecisionEvaluationEvent event = this.newDecisionEvaluationEvent(evaluationEvent);
        final HistoricDecisionInstanceEntity rootDecisionEvent = supplier.createHistoricDecisionInstance(evaluationEvent.getDecisionResult(), null);
        event.setRootHistoricDecisionInstance(rootDecisionEvent);
        final List<HistoricDecisionInstanceEntity> requiredDecisionEvents = new ArrayList<HistoricDecisionInstanceEntity>();
        for (final DmnDecisionLogicEvaluationEvent requiredDecisionResult : evaluationEvent.getRequiredDecisionResults()) {
            final HistoricDecisionInstanceEntity requiredDecisionEvent = supplier.createHistoricDecisionInstance(requiredDecisionResult, rootDecisionEvent);
            requiredDecisionEvents.add(requiredDecisionEvent);
        }
        event.setRequiredHistoricDecisionInstances(requiredDecisionEvents);
        return event;
    }
    
    protected HistoricDecisionInstanceEntity createDecisionEvaluatedEvt(final DmnDecisionLogicEvaluationEvent evaluationEvent, final ExecutionEntity execution) {
        final HistoricDecisionInstanceEntity event = this.newDecisionInstanceEventEntity(execution, evaluationEvent);
        this.setReferenceToProcessInstance(event, execution);
        if (this.isHistoryRemovalTimeStrategyStart()) {
            this.provideRemovalTime(event);
        }
        this.initDecisionInstanceEvent(event, evaluationEvent, HistoryEventTypes.DMN_DECISION_EVALUATE);
        final DecisionDefinition decisionDefinition = (DecisionDefinition)evaluationEvent.getDecision();
        String tenantId = execution.getTenantId();
        if (tenantId == null) {
            tenantId = this.provideTenantId(decisionDefinition, event);
        }
        event.setTenantId(tenantId);
        return event;
    }
    
    protected HistoricDecisionInstanceEntity createDecisionEvaluatedEvt(final DmnDecisionLogicEvaluationEvent evaluationEvent, final CaseExecutionEntity execution) {
        final HistoricDecisionInstanceEntity event = this.newDecisionInstanceEventEntity(execution, evaluationEvent);
        this.initDecisionInstanceEvent(event, evaluationEvent, HistoryEventTypes.DMN_DECISION_EVALUATE);
        this.setReferenceToCaseInstance(event, execution);
        final DecisionDefinition decisionDefinition = (DecisionDefinition)evaluationEvent.getDecision();
        String tenantId = execution.getTenantId();
        if (tenantId == null) {
            tenantId = this.provideTenantId(decisionDefinition, event);
        }
        event.setTenantId(tenantId);
        return event;
    }
    
    protected HistoricDecisionInstanceEntity createDecisionEvaluatedEvt(final DmnDecisionLogicEvaluationEvent evaluationEvent, final HistoricDecisionInstanceEntity rootDecisionInstance) {
        final HistoricDecisionInstanceEntity event = this.newDecisionInstanceEventEntity(evaluationEvent);
        this.initDecisionInstanceEvent(event, evaluationEvent, HistoryEventTypes.DMN_DECISION_EVALUATE, rootDecisionInstance);
        this.setUserId(event);
        final DecisionDefinition decisionDefinition = (DecisionDefinition)evaluationEvent.getDecision();
        String tenantId = decisionDefinition.getTenantId();
        if (tenantId == null) {
            tenantId = this.provideTenantId(decisionDefinition, event);
        }
        event.setTenantId(tenantId);
        return event;
    }
    
    protected HistoricDecisionEvaluationEvent newDecisionEvaluationEvent(final DmnDecisionEvaluationEvent evaluationEvent) {
        return new HistoricDecisionEvaluationEvent();
    }
    
    protected HistoricDecisionInstanceEntity newDecisionInstanceEventEntity(final ExecutionEntity executionEntity, final DmnDecisionLogicEvaluationEvent evaluationEvent) {
        return new HistoricDecisionInstanceEntity();
    }
    
    protected HistoricDecisionInstanceEntity newDecisionInstanceEventEntity(final CaseExecutionEntity executionEntity, final DmnDecisionLogicEvaluationEvent evaluationEvent) {
        return new HistoricDecisionInstanceEntity();
    }
    
    protected HistoricDecisionInstanceEntity newDecisionInstanceEventEntity(final DmnDecisionLogicEvaluationEvent evaluationEvent) {
        return new HistoricDecisionInstanceEntity();
    }
    
    protected void initDecisionInstanceEvent(final HistoricDecisionInstanceEntity event, final DmnDecisionLogicEvaluationEvent evaluationEvent, final HistoryEventTypes eventType) {
        this.initDecisionInstanceEvent(event, evaluationEvent, eventType, null);
    }
    
    protected void initDecisionInstanceEvent(final HistoricDecisionInstanceEntity event, final DmnDecisionLogicEvaluationEvent evaluationEvent, final HistoryEventTypes eventType, final HistoricDecisionInstanceEntity rootDecisionInstance) {
        event.setEventType(eventType.getEventName());
        final DecisionDefinition decision = (DecisionDefinition)evaluationEvent.getDecision();
        event.setDecisionDefinitionId(decision.getId());
        event.setDecisionDefinitionKey(decision.getKey());
        event.setDecisionDefinitionName(decision.getName());
        if (decision.getDecisionRequirementsDefinitionId() != null) {
            event.setDecisionRequirementsDefinitionId(decision.getDecisionRequirementsDefinitionId());
            event.setDecisionRequirementsDefinitionKey(decision.getDecisionRequirementsDefinitionKey());
        }
        event.setEvaluationTime(ClockUtil.getCurrentTime());
        if (event.getRootProcessInstanceId() == null && event.getCaseInstanceId() == null) {
            if (rootDecisionInstance != null) {
                event.setRemovalTime(rootDecisionInstance.getRemovalTime());
            }
            else {
                final Date removalTime = this.calculateRemovalTime(event, decision);
                event.setRemovalTime(removalTime);
            }
        }
        if (evaluationEvent instanceof DmnDecisionTableEvaluationEvent) {
            this.initDecisionInstanceEventForDecisionTable(event, (DmnDecisionTableEvaluationEvent)evaluationEvent);
        }
        else if (evaluationEvent instanceof DmnDecisionLiteralExpressionEvaluationEvent) {
            this.initDecisionInstanceEventForDecisionLiteralExpression(event, (DmnDecisionLiteralExpressionEvaluationEvent)evaluationEvent);
        }
        else {
            event.setInputs(Collections.emptyList());
            event.setOutputs(Collections.emptyList());
        }
    }
    
    protected void initDecisionInstanceEventForDecisionTable(final HistoricDecisionInstanceEntity event, final DmnDecisionTableEvaluationEvent evaluationEvent) {
        if (evaluationEvent.getCollectResultValue() != null) {
            final Double collectResultValue = this.getCollectResultValue(evaluationEvent.getCollectResultValue());
            event.setCollectResultValue(collectResultValue);
        }
        final List<HistoricDecisionInputInstance> historicDecisionInputInstances = this.createHistoricDecisionInputInstances(evaluationEvent, event.getRootProcessInstanceId(), event.getRemovalTime());
        event.setInputs(historicDecisionInputInstances);
        final List<HistoricDecisionOutputInstance> historicDecisionOutputInstances = this.createHistoricDecisionOutputInstances(evaluationEvent, event.getRootProcessInstanceId(), event.getRemovalTime());
        event.setOutputs(historicDecisionOutputInstances);
    }
    
    protected Double getCollectResultValue(final TypedValue collectResultValue) {
        if (collectResultValue instanceof IntegerValue) {
            return (double)((IntegerValue)collectResultValue).getValue();
        }
        if (collectResultValue instanceof LongValue) {
            return (double)((LongValue)collectResultValue).getValue();
        }
        if (collectResultValue instanceof DoubleValue) {
            return (Double)((DoubleValue)collectResultValue).getValue();
        }
        throw DefaultDmnHistoryEventProducer.LOG.collectResultValueOfUnsupportedTypeException(collectResultValue);
    }
    
    protected List<HistoricDecisionInputInstance> createHistoricDecisionInputInstances(final DmnDecisionTableEvaluationEvent evaluationEvent, final String rootProcessInstanceId, final Date removalTime) {
        final List<HistoricDecisionInputInstance> inputInstances = new ArrayList<HistoricDecisionInputInstance>();
        for (final DmnEvaluatedInput inputClause : evaluationEvent.getInputs()) {
            final HistoricDecisionInputInstanceEntity inputInstance = new HistoricDecisionInputInstanceEntity(rootProcessInstanceId, removalTime);
            inputInstance.setClauseId(inputClause.getId());
            inputInstance.setClauseName(inputClause.getName());
            inputInstance.setCreateTime(ClockUtil.getCurrentTime());
            final TypedValue typedValue = Variables.untypedValue((Object)inputClause.getValue());
            inputInstance.setValue(typedValue);
            inputInstances.add(inputInstance);
        }
        return inputInstances;
    }
    
    protected List<HistoricDecisionOutputInstance> createHistoricDecisionOutputInstances(final DmnDecisionTableEvaluationEvent evaluationEvent, final String rootProcessInstanceId, final Date removalTime) {
        final List<HistoricDecisionOutputInstance> outputInstances = new ArrayList<HistoricDecisionOutputInstance>();
        final List<DmnEvaluatedDecisionRule> matchingRules = (List<DmnEvaluatedDecisionRule>)evaluationEvent.getMatchingRules();
        for (int index = 0; index < matchingRules.size(); ++index) {
            final DmnEvaluatedDecisionRule rule = matchingRules.get(index);
            final String ruleId = rule.getId();
            final Integer ruleOrder = index + 1;
            for (final DmnEvaluatedOutput outputClause : rule.getOutputEntries().values()) {
                final HistoricDecisionOutputInstanceEntity outputInstance = new HistoricDecisionOutputInstanceEntity(rootProcessInstanceId, removalTime);
                outputInstance.setClauseId(outputClause.getId());
                outputInstance.setClauseName(outputClause.getName());
                outputInstance.setCreateTime(ClockUtil.getCurrentTime());
                outputInstance.setRuleId(ruleId);
                outputInstance.setRuleOrder(ruleOrder);
                outputInstance.setVariableName(outputClause.getOutputName());
                outputInstance.setValue(outputClause.getValue());
                outputInstances.add(outputInstance);
            }
        }
        return outputInstances;
    }
    
    protected void initDecisionInstanceEventForDecisionLiteralExpression(final HistoricDecisionInstanceEntity event, final DmnDecisionLiteralExpressionEvaluationEvent evaluationEvent) {
        event.setInputs(Collections.emptyList());
        final HistoricDecisionOutputInstanceEntity outputInstance = new HistoricDecisionOutputInstanceEntity(event.getRootProcessInstanceId(), event.getRemovalTime());
        outputInstance.setVariableName(evaluationEvent.getOutputName());
        outputInstance.setValue(evaluationEvent.getOutputValue());
        event.setOutputs((List<HistoricDecisionOutputInstance>)Collections.singletonList(outputInstance));
    }
    
    protected void setReferenceToProcessInstance(final HistoricDecisionInstanceEntity event, final ExecutionEntity execution) {
        event.setProcessDefinitionKey(this.getProcessDefinitionKey(execution));
        event.setProcessDefinitionId(execution.getProcessDefinitionId());
        event.setRootProcessInstanceId(execution.getRootProcessInstanceId());
        event.setProcessInstanceId(execution.getProcessInstanceId());
        event.setExecutionId(execution.getId());
        event.setActivityId(execution.getActivityId());
        event.setActivityInstanceId(execution.getActivityInstanceId());
    }
    
    protected String getProcessDefinitionKey(final ExecutionEntity execution) {
        final ProcessDefinitionEntity definition = execution.getProcessDefinition();
        if (definition != null) {
            return definition.getKey();
        }
        return null;
    }
    
    protected void setReferenceToCaseInstance(final HistoricDecisionInstanceEntity event, final CaseExecutionEntity execution) {
        event.setCaseDefinitionKey(this.getCaseDefinitionKey(execution));
        event.setCaseDefinitionId(execution.getCaseDefinitionId());
        event.setCaseInstanceId(execution.getCaseInstanceId());
        event.setExecutionId(execution.getId());
        event.setActivityId(execution.getActivityId());
        event.setActivityInstanceId(execution.getId());
    }
    
    protected String getCaseDefinitionKey(final CaseExecutionEntity execution) {
        final CaseDefinitionEntity definition = (CaseDefinitionEntity)execution.getCaseDefinition();
        if (definition != null) {
            return definition.getKey();
        }
        return null;
    }
    
    protected void setUserId(final HistoricDecisionInstanceEntity event) {
        event.setUserId(Context.getCommandContext().getAuthenticatedUserId());
    }
    
    protected String provideTenantId(final DecisionDefinition decisionDefinition, final HistoricDecisionInstanceEntity event) {
        final TenantIdProvider tenantIdProvider = Context.getProcessEngineConfiguration().getTenantIdProvider();
        String tenantId = null;
        if (tenantIdProvider != null) {
            TenantIdProviderHistoricDecisionInstanceContext ctx = null;
            if (event.getExecutionId() != null) {
                ctx = new TenantIdProviderHistoricDecisionInstanceContext(decisionDefinition, this.getExecution(event));
            }
            else if (event.getCaseExecutionId() != null) {
                ctx = new TenantIdProviderHistoricDecisionInstanceContext(decisionDefinition, this.getCaseExecution(event));
            }
            else {
                ctx = new TenantIdProviderHistoricDecisionInstanceContext(decisionDefinition);
            }
            tenantId = tenantIdProvider.provideTenantIdForHistoricDecisionInstance(ctx);
        }
        return tenantId;
    }
    
    protected DelegateExecution getExecution(final HistoricDecisionInstanceEntity event) {
        return Context.getCommandContext().getExecutionManager().findExecutionById(event.getExecutionId());
    }
    
    protected DelegateCaseExecution getCaseExecution(final HistoricDecisionInstanceEntity event) {
        return Context.getCommandContext().getCaseExecutionManager().findCaseExecutionById(event.getCaseExecutionId());
    }
    
    protected Date calculateRemovalTime(final HistoricDecisionInstanceEntity historicDecisionInstance, final DecisionDefinition decisionDefinition) {
        return Context.getProcessEngineConfiguration().getHistoryRemovalTimeProvider().calculateRemovalTime(historicDecisionInstance, decisionDefinition);
    }
    
    protected void provideRemovalTime(final HistoryEvent historyEvent) {
        final String rootProcessInstanceId = historyEvent.getRootProcessInstanceId();
        if (rootProcessInstanceId != null) {
            final HistoricProcessInstanceEventEntity historicRootProcessInstance = this.getHistoricRootProcessInstance(rootProcessInstanceId);
            if (historicRootProcessInstance != null) {
                final Date removalTime = historicRootProcessInstance.getRemovalTime();
                historyEvent.setRemovalTime(removalTime);
            }
        }
    }
    
    protected boolean isHistoryRemovalTimeStrategyStart() {
        return "start".equals(this.getHistoryRemovalTimeStrategy());
    }
    
    protected String getHistoryRemovalTimeStrategy() {
        return Context.getProcessEngineConfiguration().getHistoryRemovalTimeStrategy();
    }
    
    protected HistoricProcessInstanceEventEntity getHistoricRootProcessInstance(final String rootProcessInstanceId) {
        return Context.getCommandContext().getDbEntityManager().selectById(HistoricProcessInstanceEventEntity.class, rootProcessInstanceId);
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
    
    protected interface HistoricDecisionInstanceSupplier
    {
        HistoricDecisionInstanceEntity createHistoricDecisionInstance(final DmnDecisionLogicEvaluationEvent p0, final HistoricDecisionInstanceEntity p1);
    }
}
