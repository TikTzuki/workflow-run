// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.producer;

import org.zik.bpm.engine.delegate.DelegateCaseExecution;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationEvent;
import org.zik.bpm.engine.delegate.DelegateExecution;

public interface DmnHistoryEventProducer
{
    HistoryEvent createDecisionEvaluatedEvt(final DelegateExecution p0, final DmnDecisionEvaluationEvent p1);
    
    HistoryEvent createDecisionEvaluatedEvt(final DelegateCaseExecution p0, final DmnDecisionEvaluationEvent p1);
    
    HistoryEvent createDecisionEvaluatedEvt(final DmnDecisionEvaluationEvent p0);
}
