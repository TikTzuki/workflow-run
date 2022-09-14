// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.camunda.commons.utils.EnsureUtil;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.runtime.EventSubscription;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.EventSubscriptionQueryImpl;
import java.util.Iterator;
import org.zik.bpm.engine.impl.event.EventType;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.ArrayList;
import java.util.List;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class EventSubscriptionManager extends AbstractManager
{
    protected static final EnginePersistenceLogger LOG;
    protected List<EventSubscriptionEntity> createdSignalSubscriptions;
    
    public EventSubscriptionManager() {
        this.createdSignalSubscriptions = new ArrayList<EventSubscriptionEntity>();
    }
    
    public void insert(final EventSubscriptionEntity persistentObject) {
        super.insert(persistentObject);
        if (persistentObject.isSubscriptionForEventType(EventType.SIGNAL)) {
            this.createdSignalSubscriptions.add(persistentObject);
        }
    }
    
    public void deleteEventSubscription(final EventSubscriptionEntity persistentObject) {
        this.getDbEntityManager().delete(persistentObject);
        if (persistentObject.isSubscriptionForEventType(EventType.SIGNAL)) {
            this.createdSignalSubscriptions.remove(persistentObject);
        }
        final List<JobEntity> asyncJobs = this.getJobManager().findJobsByConfiguration("event", persistentObject.getId(), persistentObject.getTenantId());
        for (final JobEntity asyncJob : asyncJobs) {
            asyncJob.delete();
        }
    }
    
    public void deleteAndFlushEventSubscription(final EventSubscriptionEntity persistentObject) {
        this.deleteEventSubscription(persistentObject);
        this.getDbEntityManager().flushEntity(persistentObject);
    }
    
    public EventSubscriptionEntity findEventSubscriptionById(final String id) {
        return (EventSubscriptionEntity)this.getDbEntityManager().selectOne("selectEventSubscription", id);
    }
    
    public long findEventSubscriptionCountByQueryCriteria(final EventSubscriptionQueryImpl eventSubscriptionQueryImpl) {
        this.configureQuery(eventSubscriptionQueryImpl);
        return (long)this.getDbEntityManager().selectOne("selectEventSubscriptionCountByQueryCriteria", eventSubscriptionQueryImpl);
    }
    
    public List<EventSubscription> findEventSubscriptionsByQueryCriteria(final EventSubscriptionQueryImpl eventSubscriptionQueryImpl, final Page page) {
        this.configureQuery(eventSubscriptionQueryImpl);
        return (List<EventSubscription>)this.getDbEntityManager().selectList("selectEventSubscriptionByQueryCriteria", eventSubscriptionQueryImpl, page);
    }
    
    public List<EventSubscriptionEntity> findSignalEventSubscriptionsByEventName(final String eventName) {
        final String query = "selectSignalEventSubscriptionsByEventName";
        final Set<EventSubscriptionEntity> eventSubscriptions = new HashSet<EventSubscriptionEntity>(this.getDbEntityManager().selectList("selectSignalEventSubscriptionsByEventName", this.configureParameterizedQuery(eventName)));
        for (final EventSubscriptionEntity entity : this.createdSignalSubscriptions) {
            if (eventName.equals(entity.getEventName())) {
                eventSubscriptions.add(entity);
            }
        }
        return new ArrayList<EventSubscriptionEntity>(eventSubscriptions);
    }
    
    public List<EventSubscriptionEntity> findSignalEventSubscriptionsByEventNameAndTenantId(final String eventName, final String tenantId) {
        final String query = "selectSignalEventSubscriptionsByEventNameAndTenantId";
        final Map<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("eventName", eventName);
        parameter.put("tenantId", tenantId);
        final Set<EventSubscriptionEntity> eventSubscriptions = new HashSet<EventSubscriptionEntity>(this.getDbEntityManager().selectList("selectSignalEventSubscriptionsByEventNameAndTenantId", parameter));
        for (final EventSubscriptionEntity entity : this.createdSignalSubscriptions) {
            if (eventName.equals(entity.getEventName()) && this.hasTenantId(entity, tenantId)) {
                eventSubscriptions.add(entity);
            }
        }
        return new ArrayList<EventSubscriptionEntity>(eventSubscriptions);
    }
    
    public List<EventSubscriptionEntity> findSignalEventSubscriptionsByEventNameAndTenantIdIncludeWithoutTenantId(final String eventName, final String tenantId) {
        final String query = "selectSignalEventSubscriptionsByEventNameAndTenantIdIncludeWithoutTenantId";
        final Map<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("eventName", eventName);
        parameter.put("tenantId", tenantId);
        final Set<EventSubscriptionEntity> eventSubscriptions = new HashSet<EventSubscriptionEntity>(this.getDbEntityManager().selectList("selectSignalEventSubscriptionsByEventNameAndTenantIdIncludeWithoutTenantId", parameter));
        for (final EventSubscriptionEntity entity : this.createdSignalSubscriptions) {
            if (eventName.equals(entity.getEventName()) && (entity.getTenantId() == null || this.hasTenantId(entity, tenantId))) {
                eventSubscriptions.add(entity);
            }
        }
        return new ArrayList<EventSubscriptionEntity>(eventSubscriptions);
    }
    
    protected boolean hasTenantId(final EventSubscriptionEntity entity, final String tenantId) {
        if (tenantId == null) {
            return entity.getTenantId() == null;
        }
        return tenantId.equals(entity.getTenantId());
    }
    
    public List<EventSubscriptionEntity> findSignalEventSubscriptionsByExecution(final String executionId) {
        final String query = "selectSignalEventSubscriptionsByExecution";
        final Set<EventSubscriptionEntity> selectList = new HashSet<EventSubscriptionEntity>(this.getDbEntityManager().selectList("selectSignalEventSubscriptionsByExecution", executionId));
        for (final EventSubscriptionEntity entity : this.createdSignalSubscriptions) {
            if (executionId.equals(entity.getExecutionId())) {
                selectList.add(entity);
            }
        }
        return new ArrayList<EventSubscriptionEntity>(selectList);
    }
    
    public List<EventSubscriptionEntity> findSignalEventSubscriptionsByNameAndExecution(final String name, final String executionId) {
        final String query = "selectSignalEventSubscriptionsByNameAndExecution";
        final Map<String, String> params = new HashMap<String, String>();
        params.put("executionId", executionId);
        params.put("eventName", name);
        final Set<EventSubscriptionEntity> selectList = new HashSet<EventSubscriptionEntity>(this.getDbEntityManager().selectList("selectSignalEventSubscriptionsByNameAndExecution", params));
        for (final EventSubscriptionEntity entity : this.createdSignalSubscriptions) {
            if (executionId.equals(entity.getExecutionId()) && name.equals(entity.getEventName())) {
                selectList.add(entity);
            }
        }
        return new ArrayList<EventSubscriptionEntity>(selectList);
    }
    
    public List<EventSubscriptionEntity> findEventSubscriptionsByExecutionAndType(final String executionId, final String type, final boolean lockResult) {
        final String query = "selectEventSubscriptionsByExecutionAndType";
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("executionId", executionId);
        params.put("eventType", type);
        params.put("lockResult", lockResult);
        return (List<EventSubscriptionEntity>)this.getDbEntityManager().selectList("selectEventSubscriptionsByExecutionAndType", params);
    }
    
    public List<EventSubscriptionEntity> findEventSubscriptionsByExecution(final String executionId) {
        final String query = "selectEventSubscriptionsByExecution";
        return (List<EventSubscriptionEntity>)this.getDbEntityManager().selectList("selectEventSubscriptionsByExecution", executionId);
    }
    
    public List<EventSubscriptionEntity> findEventSubscriptions(final String executionId, final String type, final String activityId) {
        final String query = "selectEventSubscriptionsByExecutionTypeAndActivity";
        final Map<String, String> params = new HashMap<String, String>();
        params.put("executionId", executionId);
        params.put("eventType", type);
        params.put("activityId", activityId);
        return (List<EventSubscriptionEntity>)this.getDbEntityManager().selectList("selectEventSubscriptionsByExecutionTypeAndActivity", params);
    }
    
    public List<EventSubscriptionEntity> findEventSubscriptionsByConfiguration(final String type, final String configuration) {
        final String query = "selectEventSubscriptionsByConfiguration";
        final Map<String, String> params = new HashMap<String, String>();
        params.put("eventType", type);
        params.put("configuration", configuration);
        return (List<EventSubscriptionEntity>)this.getDbEntityManager().selectList("selectEventSubscriptionsByConfiguration", params);
    }
    
    public List<EventSubscriptionEntity> findEventSubscriptionsByNameAndTenantId(final String type, final String eventName, final String tenantId) {
        final String query = "selectEventSubscriptionsByNameAndTenantId";
        final Map<String, String> params = new HashMap<String, String>();
        params.put("eventType", type);
        params.put("eventName", eventName);
        params.put("tenantId", tenantId);
        return (List<EventSubscriptionEntity>)this.getDbEntityManager().selectList("selectEventSubscriptionsByNameAndTenantId", params);
    }
    
    public List<EventSubscriptionEntity> findEventSubscriptionsByNameAndExecution(final String type, final String eventName, final String executionId, final boolean lockResult) {
        final ExecutionEntity cachedExecution = this.getDbEntityManager().getCachedEntity(ExecutionEntity.class, executionId);
        if (cachedExecution != null && !lockResult) {
            final List<EventSubscriptionEntity> eventSubscriptions = cachedExecution.getEventSubscriptions();
            final List<EventSubscriptionEntity> result = new ArrayList<EventSubscriptionEntity>();
            for (final EventSubscriptionEntity subscription : eventSubscriptions) {
                if (this.matchesSubscription(subscription, type, eventName)) {
                    result.add(subscription);
                }
            }
            return result;
        }
        final String query = "selectEventSubscriptionsByNameAndExecution";
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("eventType", type);
        params.put("eventName", eventName);
        params.put("executionId", executionId);
        params.put("lockResult", lockResult);
        return (List<EventSubscriptionEntity>)this.getDbEntityManager().selectList("selectEventSubscriptionsByNameAndExecution", params);
    }
    
    public List<EventSubscriptionEntity> findEventSubscriptionsByProcessInstanceId(final String processInstanceId) {
        return (List<EventSubscriptionEntity>)this.getDbEntityManager().selectList("selectEventSubscriptionsByProcessInstanceId", processInstanceId);
    }
    
    public List<EventSubscriptionEntity> findMessageStartEventSubscriptionByName(final String messageName) {
        return (List<EventSubscriptionEntity>)this.getDbEntityManager().selectList("selectMessageStartEventSubscriptionByName", this.configureParameterizedQuery(messageName));
    }
    
    public EventSubscriptionEntity findMessageStartEventSubscriptionByNameAndTenantId(final String messageName, final String tenantId) {
        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("messageName", messageName);
        parameters.put("tenantId", tenantId);
        return (EventSubscriptionEntity)this.getDbEntityManager().selectOne("selectMessageStartEventSubscriptionByNameAndTenantId", parameters);
    }
    
    public List<EventSubscriptionEntity> findConditionalStartEventSubscriptionByTenantId(final String tenantId) {
        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("tenantId", tenantId);
        this.configureParameterizedQuery(parameters);
        return (List<EventSubscriptionEntity>)this.getDbEntityManager().selectList("selectConditionalStartEventSubscriptionByTenantId", parameters);
    }
    
    public List<EventSubscriptionEntity> findConditionalStartEventSubscription() {
        final ListQueryParameterObject parameter = new ListQueryParameterObject();
        this.configurParameterObject(parameter);
        return (List<EventSubscriptionEntity>)this.getDbEntityManager().selectList("selectConditionalStartEventSubscription", parameter);
    }
    
    protected void configurParameterObject(final ListQueryParameterObject parameter) {
        this.getAuthorizationManager().configureConditionalEventSubscriptionQuery(parameter);
        this.getTenantManager().configureQuery(parameter);
    }
    
    protected void configureQuery(final EventSubscriptionQueryImpl query) {
        this.getAuthorizationManager().configureEventSubscriptionQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    protected ListQueryParameterObject configureParameterizedQuery(final Object parameter) {
        return this.getTenantManager().configureQuery(parameter);
    }
    
    protected boolean matchesSubscription(final EventSubscriptionEntity subscription, final String type, final String eventName) {
        EnsureUtil.ensureNotNull("event type", (Object)type);
        final String subscriptionEventName = subscription.getEventName();
        return type.equals(subscription.getEventType()) && ((eventName == null && subscriptionEventName == null) || (eventName != null && eventName.equals(subscriptionEventName)));
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
