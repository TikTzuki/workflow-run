// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.core.variable.event.VariableEvent;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.bpmn.parser.ConditionalEventDefinition;

public class IntermediateConditionalEventBehavior extends IntermediateCatchEventActivityBehavior implements ConditionalEventBehavior
{
    protected final ConditionalEventDefinition conditionalEvent;
    
    public IntermediateConditionalEventBehavior(final ConditionalEventDefinition conditionalEvent, final boolean isAfterEventBasedGateway) {
        super(isAfterEventBasedGateway);
        this.conditionalEvent = conditionalEvent;
    }
    
    @Override
    public ConditionalEventDefinition getConditionalEventDefinition() {
        return this.conditionalEvent;
    }
    
    @Override
    public void execute(final ActivityExecution execution) throws Exception {
        if (this.isAfterEventBasedGateway || this.conditionalEvent.tryEvaluate(execution)) {
            this.leave(execution);
        }
    }
    
    @Override
    public void leaveOnSatisfiedCondition(final EventSubscriptionEntity eventSubscription, final VariableEvent variableEvent) {
        final PvmExecutionImpl execution = eventSubscription.getExecution();
        if (execution != null && !execution.isEnded() && variableEvent != null && this.conditionalEvent.tryEvaluate(variableEvent, execution) && execution.isActive() && execution.isScope()) {
            if (this.isAfterEventBasedGateway) {
                final ActivityImpl activity = eventSubscription.getActivity();
                execution.executeEventHandlerActivity(activity);
            }
            else {
                this.leave(execution);
            }
        }
    }
}
