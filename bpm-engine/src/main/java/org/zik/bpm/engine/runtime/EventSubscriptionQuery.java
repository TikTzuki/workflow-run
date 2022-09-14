// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import org.zik.bpm.engine.query.Query;

public interface EventSubscriptionQuery extends Query<EventSubscriptionQuery, EventSubscription>
{
    EventSubscriptionQuery eventSubscriptionId(final String p0);
    
    EventSubscriptionQuery eventName(final String p0);
    
    EventSubscriptionQuery eventType(final String p0);
    
    EventSubscriptionQuery executionId(final String p0);
    
    EventSubscriptionQuery processInstanceId(final String p0);
    
    EventSubscriptionQuery activityId(final String p0);
    
    EventSubscriptionQuery tenantIdIn(final String... p0);
    
    EventSubscriptionQuery withoutTenantId();
    
    EventSubscriptionQuery includeEventSubscriptionsWithoutTenantId();
    
    EventSubscriptionQuery orderByCreated();
    
    EventSubscriptionQuery orderByTenantId();
}
