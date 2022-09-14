// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.event;

import java.util.List;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.pvm.runtime.AtomicOperation;
import org.zik.bpm.engine.impl.pvm.runtime.operation.PvmAtomicOperation;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.bpmn.helper.CompensationUtil;
import org.zik.bpm.engine.impl.pvm.delegate.CompositeActivityBehavior;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;

public class CompensationEventHandler implements EventHandler
{
    @Override
    public String getEventHandlerType() {
        return EventType.COMPENSATE.name();
    }
    
    @Override
    public void handleEvent(final EventSubscriptionEntity eventSubscription, final Object payload, final Object localPayload, final String businessKey, final CommandContext commandContext) {
        eventSubscription.delete();
        final String configuration = eventSubscription.getConfiguration();
        EnsureUtil.ensureNotNull("Compensating execution not set for compensate event subscription with id " + eventSubscription.getId(), "configuration", configuration);
        final ExecutionEntity compensatingExecution = commandContext.getExecutionManager().findExecutionById(configuration);
        final ActivityImpl compensationHandler = eventSubscription.getActivity();
        compensatingExecution.setActive(true);
        if (compensatingExecution.getActivity().getActivityBehavior() instanceof CompositeActivityBehavior) {
            compensatingExecution.getParent().setActivityInstanceId(compensatingExecution.getActivityInstanceId());
        }
        if (compensationHandler.isScope() && !compensationHandler.isCompensationHandler()) {
            final List<EventSubscriptionEntity> eventsForThisScope = compensatingExecution.getCompensateEventSubscriptions();
            CompensationUtil.throwCompensationEvent(eventsForThisScope, compensatingExecution, false);
        }
        else {
            try {
                if (compensationHandler.isSubProcessScope() && compensationHandler.isTriggeredByEvent()) {
                    compensatingExecution.executeActivity(compensationHandler);
                }
                else {
                    compensatingExecution.setActivity(compensationHandler);
                    compensatingExecution.performOperation(PvmAtomicOperation.ACTIVITY_START);
                }
            }
            catch (Exception e) {
                throw new ProcessEngineException("Error while handling compensation event " + eventSubscription, e);
            }
        }
    }
}
