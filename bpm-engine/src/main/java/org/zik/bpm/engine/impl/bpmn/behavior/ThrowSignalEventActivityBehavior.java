// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionManager;
import org.zik.bpm.engine.impl.context.Context;
import java.util.Iterator;
import java.util.List;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.bpmn.parser.EventSubscriptionDeclaration;

public class ThrowSignalEventActivityBehavior extends AbstractBpmnActivityBehavior
{
    protected static final BpmnBehaviorLogger LOG;
    protected final EventSubscriptionDeclaration signalDefinition;
    
    public ThrowSignalEventActivityBehavior(final EventSubscriptionDeclaration signalDefinition) {
        this.signalDefinition = signalDefinition;
    }
    
    @Override
    public void execute(final ActivityExecution execution) throws Exception {
        final String businessKey = this.signalDefinition.getEventPayload().getBusinessKey(execution);
        final VariableMap variableMap = this.signalDefinition.getEventPayload().getInputVariables(execution);
        final String eventName = this.signalDefinition.resolveExpressionOfEventName(execution);
        final List<EventSubscriptionEntity> signalEventSubscriptions = this.findSignalEventSubscriptions(eventName, execution.getTenantId());
        for (final EventSubscriptionEntity signalEventSubscription : signalEventSubscriptions) {
            if (this.isActiveEventSubscription(signalEventSubscription)) {
                signalEventSubscription.eventReceived(variableMap, null, businessKey, this.signalDefinition.isAsync());
            }
        }
        this.leave(execution);
    }
    
    protected List<EventSubscriptionEntity> findSignalEventSubscriptions(final String signalName, final String tenantId) {
        final EventSubscriptionManager eventSubscriptionManager = Context.getCommandContext().getEventSubscriptionManager();
        if (tenantId != null) {
            return eventSubscriptionManager.findSignalEventSubscriptionsByEventNameAndTenantIdIncludeWithoutTenantId(signalName, tenantId);
        }
        return eventSubscriptionManager.findSignalEventSubscriptionsByEventNameAndTenantId(signalName, null);
    }
    
    protected boolean isActiveEventSubscription(final EventSubscriptionEntity signalEventSubscriptionEntity) {
        return this.isStartEventSubscription(signalEventSubscriptionEntity) || this.isActiveIntermediateEventSubscription(signalEventSubscriptionEntity);
    }
    
    protected boolean isStartEventSubscription(final EventSubscriptionEntity signalEventSubscriptionEntity) {
        return signalEventSubscriptionEntity.getExecutionId() == null;
    }
    
    protected boolean isActiveIntermediateEventSubscription(final EventSubscriptionEntity signalEventSubscriptionEntity) {
        final ExecutionEntity execution = signalEventSubscriptionEntity.getExecution();
        return execution != null && !execution.isEnded() && !execution.isCanceled();
    }
    
    static {
        LOG = ProcessEngineLogger.BPMN_BEHAVIOR_LOGGER;
    }
}
