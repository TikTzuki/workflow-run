// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.bpmn.parser.ConditionalEventDefinition;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class EventBasedGatewayActivityBehavior extends FlowNodeActivityBehavior
{
    @Override
    public void execute(final ActivityExecution execution) throws Exception {
        final ActivityImpl eventBasedGateway = (ActivityImpl)execution.getActivity();
        for (final ActivityImpl act : eventBasedGateway.getEventActivities()) {
            final ActivityBehavior activityBehavior = act.getActivityBehavior();
            if (activityBehavior instanceof ConditionalEventBehavior) {
                final ConditionalEventBehavior conditionalEventBehavior = (ConditionalEventBehavior)activityBehavior;
                final ConditionalEventDefinition conditionalEventDefinition = conditionalEventBehavior.getConditionalEventDefinition();
                if (conditionalEventDefinition.tryEvaluate(execution)) {
                    ((ExecutionEntity)execution).executeEventHandlerActivity(conditionalEventDefinition.getConditionalActivity());
                    return;
                }
                continue;
            }
        }
    }
}
