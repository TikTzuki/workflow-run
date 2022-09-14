// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.parser;

import org.zik.bpm.engine.repository.DecisionDefinition;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.context.CoreExecutionContext;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.zik.bpm.engine.delegate.DelegateCaseExecution;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.context.Context;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationEvent;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.history.producer.DmnHistoryEventProducer;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationListener;

public class HistoryDecisionEvaluationListener implements DmnDecisionEvaluationListener
{
    protected DmnHistoryEventProducer eventProducer;
    protected HistoryLevel historyLevel;
    
    public HistoryDecisionEvaluationListener(final DmnHistoryEventProducer historyEventProducer) {
        this.eventProducer = historyEventProducer;
    }
    
    public void notify(final DmnDecisionEvaluationEvent evaluationEvent) {
        final HistoryEvent historyEvent = this.createHistoryEvent(evaluationEvent);
        if (historyEvent != null) {
            Context.getProcessEngineConfiguration().getHistoryEventHandler().handleEvent(historyEvent);
        }
    }
    
    protected HistoryEvent createHistoryEvent(final DmnDecisionEvaluationEvent evaluationEvent) {
        if (this.historyLevel == null) {
            this.historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
        }
        final DmnDecision decisionTable = evaluationEvent.getDecisionResult().getDecision();
        if (this.isDeployedDecisionTable(decisionTable) && this.historyLevel.isHistoryEventProduced(HistoryEventTypes.DMN_DECISION_EVALUATE, decisionTable)) {
            final CoreExecutionContext<? extends CoreExecution> executionContext = Context.getCoreExecutionContext();
            if (executionContext != null) {
                final CoreExecution coreExecution = (CoreExecution)executionContext.getExecution();
                if (coreExecution instanceof ExecutionEntity) {
                    final ExecutionEntity execution = (ExecutionEntity)coreExecution;
                    return this.eventProducer.createDecisionEvaluatedEvt(execution, evaluationEvent);
                }
                if (coreExecution instanceof CaseExecutionEntity) {
                    final CaseExecutionEntity caseExecution = (CaseExecutionEntity)coreExecution;
                    return this.eventProducer.createDecisionEvaluatedEvt(caseExecution, evaluationEvent);
                }
            }
            return this.eventProducer.createDecisionEvaluatedEvt(evaluationEvent);
        }
        return null;
    }
    
    protected boolean isDeployedDecisionTable(final DmnDecision decision) {
        return decision instanceof DecisionDefinition && ((DecisionDefinition)decision).getId() != null;
    }
}
