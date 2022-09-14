// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.parser;

import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.runtime.LegacyBehavior;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.el.StartProcessVariableScope;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.bpmn.helper.BpmnProperties;
import java.util.Collections;
import java.util.Map;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.jobexecutor.EventSubscriptionJobDeclaration;
import org.zik.bpm.engine.impl.core.model.CallableElement;
import org.zik.bpm.engine.impl.el.Expression;
import org.zik.bpm.engine.impl.event.EventType;
import java.io.Serializable;

public class EventSubscriptionDeclaration implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected final EventType eventType;
    protected final Expression eventName;
    protected final CallableElement eventPayload;
    protected boolean async;
    protected String activityId;
    protected String eventScopeActivityId;
    protected boolean isStartEvent;
    protected EventSubscriptionJobDeclaration jobDeclaration;
    
    public EventSubscriptionDeclaration(final Expression eventExpression, final EventType eventType) {
        this.activityId = null;
        this.eventScopeActivityId = null;
        this.jobDeclaration = null;
        this.eventName = eventExpression;
        this.eventType = eventType;
        this.eventPayload = null;
    }
    
    public EventSubscriptionDeclaration(final Expression eventExpression, final EventType eventType, final CallableElement eventPayload) {
        this.activityId = null;
        this.eventScopeActivityId = null;
        this.jobDeclaration = null;
        this.eventType = eventType;
        this.eventName = eventExpression;
        this.eventPayload = eventPayload;
    }
    
    public static Map<String, EventSubscriptionDeclaration> getDeclarationsForScope(final PvmScope scope) {
        if (scope == null) {
            return Collections.emptyMap();
        }
        return scope.getProperties().get(BpmnProperties.EVENT_SUBSCRIPTION_DECLARATIONS);
    }
    
    public String getUnresolvedEventName() {
        return this.eventName.getExpressionText();
    }
    
    public boolean hasEventName() {
        return this.eventName != null && !"".equalsIgnoreCase(this.getUnresolvedEventName().trim());
    }
    
    public boolean isEventNameLiteralText() {
        return this.eventName.isLiteralText();
    }
    
    public boolean isAsync() {
        return this.async;
    }
    
    public void setAsync(final boolean async) {
        this.async = async;
    }
    
    public String getActivityId() {
        return this.activityId;
    }
    
    public void setActivityId(final String activityId) {
        this.activityId = activityId;
    }
    
    public String getEventScopeActivityId() {
        return this.eventScopeActivityId;
    }
    
    public void setEventScopeActivityId(final String eventScopeActivityId) {
        this.eventScopeActivityId = eventScopeActivityId;
    }
    
    public boolean isStartEvent() {
        return this.isStartEvent;
    }
    
    public void setStartEvent(final boolean isStartEvent) {
        this.isStartEvent = isStartEvent;
    }
    
    public String getEventType() {
        return this.eventType.name();
    }
    
    public CallableElement getEventPayload() {
        return this.eventPayload;
    }
    
    public void setJobDeclaration(final EventSubscriptionJobDeclaration jobDeclaration) {
        this.jobDeclaration = jobDeclaration;
    }
    
    public EventSubscriptionEntity createSubscriptionForStartEvent(final ProcessDefinitionEntity processDefinition) {
        final EventSubscriptionEntity eventSubscriptionEntity = new EventSubscriptionEntity(this.eventType);
        final VariableScope scopeForExpression = StartProcessVariableScope.getSharedInstance();
        final String eventName = this.resolveExpressionOfEventName(scopeForExpression);
        eventSubscriptionEntity.setEventName(eventName);
        eventSubscriptionEntity.setActivityId(this.activityId);
        eventSubscriptionEntity.setConfiguration(processDefinition.getId());
        eventSubscriptionEntity.setTenantId(processDefinition.getTenantId());
        return eventSubscriptionEntity;
    }
    
    public EventSubscriptionEntity createSubscriptionForExecution(final ExecutionEntity execution) {
        final EventSubscriptionEntity eventSubscriptionEntity = new EventSubscriptionEntity(execution, this.eventType);
        final String eventName = this.resolveExpressionOfEventName(execution);
        eventSubscriptionEntity.setEventName(eventName);
        if (this.activityId != null) {
            final ActivityImpl activity = execution.getProcessDefinition().findActivity(this.activityId);
            eventSubscriptionEntity.setActivity(activity);
        }
        eventSubscriptionEntity.insert();
        LegacyBehavior.removeLegacySubscriptionOnParent(execution, eventSubscriptionEntity);
        return eventSubscriptionEntity;
    }
    
    public String resolveExpressionOfEventName(final VariableScope scope) {
        if (this.isExpressionAvailable()) {
            return (String)this.eventName.getValue(scope);
        }
        return null;
    }
    
    protected boolean isExpressionAvailable() {
        return this.eventName != null;
    }
    
    public void updateSubscription(final EventSubscriptionEntity eventSubscription) {
        final String eventName = this.resolveExpressionOfEventName(eventSubscription.getExecution());
        eventSubscription.setEventName(eventName);
        eventSubscription.setActivityId(this.activityId);
    }
}
