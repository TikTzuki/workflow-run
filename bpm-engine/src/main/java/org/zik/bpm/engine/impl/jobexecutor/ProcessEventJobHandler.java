// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;


public class ProcessEventJobHandler implements JobHandler<ProcessEventJobHandler.EventSubscriptionJobConfiguration> {
    public static final String TYPE = "event";

    public ProcessEventJobHandler() {
    }

    public String getType() {
        return "event";
    }

    public void execute(EventSubscriptionJobConfiguration configuration, ExecutionEntity execution, CommandContext commandContext, String tenantId) {
        String eventSubscriptionId = configuration.getEventSubscriptionId();
        EventSubscriptionEntity eventSubscription = commandContext.getEventSubscriptionManager().findEventSubscriptionById(eventSubscriptionId);
        if (eventSubscription != null) {
            eventSubscription.eventReceived((Object)null, false);
        }

    }

    public EventSubscriptionJobConfiguration newConfiguration(String canonicalString) {
        return new EventSubscriptionJobConfiguration(canonicalString);
    }

    public void onDelete(EventSubscriptionJobConfiguration configuration, JobEntity jobEntity) {
    }

    public static class EventSubscriptionJobConfiguration implements JobHandlerConfiguration {
        protected String eventSubscriptionId;

        public EventSubscriptionJobConfiguration(String eventSubscriptionId) {
            this.eventSubscriptionId = eventSubscriptionId;
        }

        public String getEventSubscriptionId() {
            return this.eventSubscriptionId;
        }

        public String toCanonicalString() {
            return this.eventSubscriptionId;
        }
    }

}
