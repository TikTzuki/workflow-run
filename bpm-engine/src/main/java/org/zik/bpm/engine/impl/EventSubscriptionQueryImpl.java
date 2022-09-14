// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.io.Serializable;
import org.zik.bpm.engine.runtime.EventSubscription;
import org.zik.bpm.engine.runtime.EventSubscriptionQuery;

public class EventSubscriptionQueryImpl extends AbstractQuery<EventSubscriptionQuery, EventSubscription> implements Serializable, EventSubscriptionQuery
{
    private static final long serialVersionUID = 1L;
    protected String eventSubscriptionId;
    protected String eventName;
    protected String eventType;
    protected String executionId;
    protected String processInstanceId;
    protected String activityId;
    protected boolean isTenantIdSet;
    protected String[] tenantIds;
    protected boolean includeEventSubscriptionsWithoutTenantId;
    
    public EventSubscriptionQueryImpl() {
        this.isTenantIdSet = false;
        this.includeEventSubscriptionsWithoutTenantId = false;
    }
    
    public EventSubscriptionQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.isTenantIdSet = false;
        this.includeEventSubscriptionsWithoutTenantId = false;
    }
    
    @Override
    public EventSubscriptionQuery eventSubscriptionId(final String id) {
        EnsureUtil.ensureNotNull("event subscription id", (Object)id);
        this.eventSubscriptionId = id;
        return this;
    }
    
    @Override
    public EventSubscriptionQuery eventName(final String eventName) {
        EnsureUtil.ensureNotNull("event name", (Object)eventName);
        this.eventName = eventName;
        return this;
    }
    
    @Override
    public EventSubscriptionQueryImpl executionId(final String executionId) {
        EnsureUtil.ensureNotNull("execution id", (Object)executionId);
        this.executionId = executionId;
        return this;
    }
    
    @Override
    public EventSubscriptionQuery processInstanceId(final String processInstanceId) {
        EnsureUtil.ensureNotNull("process instance id", (Object)processInstanceId);
        this.processInstanceId = processInstanceId;
        return this;
    }
    
    @Override
    public EventSubscriptionQueryImpl activityId(final String activityId) {
        EnsureUtil.ensureNotNull("activity id", (Object)activityId);
        this.activityId = activityId;
        return this;
    }
    
    @Override
    public EventSubscriptionQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public EventSubscriptionQuery withoutTenantId() {
        this.isTenantIdSet = true;
        this.tenantIds = null;
        return this;
    }
    
    @Override
    public EventSubscriptionQuery includeEventSubscriptionsWithoutTenantId() {
        this.includeEventSubscriptionsWithoutTenantId = true;
        return this;
    }
    
    @Override
    public EventSubscriptionQueryImpl eventType(final String eventType) {
        EnsureUtil.ensureNotNull("event type", (Object)eventType);
        this.eventType = eventType;
        return this;
    }
    
    @Override
    public EventSubscriptionQuery orderByCreated() {
        return ((AbstractQuery<EventSubscriptionQuery, U>)this).orderBy(EventSubscriptionQueryProperty.CREATED);
    }
    
    @Override
    public EventSubscriptionQuery orderByTenantId() {
        return ((AbstractQuery<EventSubscriptionQuery, U>)this).orderBy(EventSubscriptionQueryProperty.TENANT_ID);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getEventSubscriptionManager().findEventSubscriptionCountByQueryCriteria(this);
    }
    
    @Override
    public List<EventSubscription> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getEventSubscriptionManager().findEventSubscriptionsByQueryCriteria(this, page);
    }
    
    public String getEventSubscriptionId() {
        return this.eventSubscriptionId;
    }
    
    public String getEventName() {
        return this.eventName;
    }
    
    public String getEventType() {
        return this.eventType;
    }
    
    public String getExecutionId() {
        return this.executionId;
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public String getActivityId() {
        return this.activityId;
    }
}
