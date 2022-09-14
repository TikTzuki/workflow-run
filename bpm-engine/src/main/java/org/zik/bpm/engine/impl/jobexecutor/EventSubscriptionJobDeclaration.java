// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import java.util.Iterator;
import java.util.Collections;
import java.util.List;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.commons.utils.EnsureUtil;
import org.zik.bpm.engine.impl.bpmn.parser.EventSubscriptionDeclaration;
import org.zik.bpm.engine.impl.persistence.entity.MessageEntity;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;

public class EventSubscriptionJobDeclaration extends JobDeclaration<EventSubscriptionEntity, MessageEntity>
{
    private static final long serialVersionUID = 1L;
    protected EventSubscriptionDeclaration eventSubscriptionDeclaration;
    
    public EventSubscriptionJobDeclaration(final EventSubscriptionDeclaration eventSubscriptionDeclaration) {
        super("event");
        EnsureUtil.ensureNotNull("eventSubscriptionDeclaration", (Object)eventSubscriptionDeclaration);
        this.eventSubscriptionDeclaration = eventSubscriptionDeclaration;
    }
    
    @Override
    protected MessageEntity newJobInstance(final EventSubscriptionEntity eventSubscription) {
        final MessageEntity message = new MessageEntity();
        message.setActivityId(eventSubscription.getActivityId());
        message.setExecutionId(eventSubscription.getExecutionId());
        message.setProcessInstanceId(eventSubscription.getProcessInstanceId());
        final ProcessDefinitionEntity processDefinition = eventSubscription.getProcessDefinition();
        if (processDefinition != null) {
            message.setProcessDefinitionId(processDefinition.getId());
            message.setProcessDefinitionKey(processDefinition.getKey());
        }
        return message;
    }
    
    public String getEventType() {
        return this.eventSubscriptionDeclaration.getEventType();
    }
    
    public String getEventName() {
        return this.eventSubscriptionDeclaration.getUnresolvedEventName();
    }
    
    @Override
    public String getActivityId() {
        return this.eventSubscriptionDeclaration.getActivityId();
    }
    
    @Override
    protected ExecutionEntity resolveExecution(final EventSubscriptionEntity context) {
        return context.getExecution();
    }
    
    @Override
    protected JobHandlerConfiguration resolveJobHandlerConfiguration(final EventSubscriptionEntity context) {
        return new ProcessEventJobHandler.EventSubscriptionJobConfiguration(context.getId());
    }
    
    public static List<EventSubscriptionJobDeclaration> getDeclarationsForActivity(final PvmActivity activity) {
        final Object result = activity.getProperty("eventJobDeclarations");
        if (result != null) {
            return (List<EventSubscriptionJobDeclaration>)result;
        }
        return Collections.emptyList();
    }
    
    public static EventSubscriptionJobDeclaration findDeclarationForSubscription(final EventSubscriptionEntity eventSubscription) {
        final List<EventSubscriptionJobDeclaration> declarations = getDeclarationsForActivity(eventSubscription.getActivity());
        for (final EventSubscriptionJobDeclaration declaration : declarations) {
            if (declaration.getEventType().equals(eventSubscription.getEventType())) {
                return declaration;
            }
        }
        return null;
    }
}
