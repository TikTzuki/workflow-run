// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import java.util.HashMap;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.event.EventHandler;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.event.EventType;
import org.zik.bpm.engine.impl.jobexecutor.EventSubscriptionJobDeclaration;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import java.util.Date;
import java.io.Serializable;
import org.zik.bpm.engine.impl.db.HasDbReferences;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.runtime.EventSubscription;

public class EventSubscriptionEntity implements EventSubscription, DbEntity, HasDbRevision, HasDbReferences, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected int revision;
    protected String eventType;
    protected String eventName;
    protected String executionId;
    protected String processInstanceId;
    protected String activityId;
    protected String configuration;
    protected Date created;
    protected String tenantId;
    protected ExecutionEntity execution;
    protected ActivityImpl activity;
    protected EventSubscriptionJobDeclaration jobDeclaration;
    
    public EventSubscriptionEntity() {
        this.revision = 1;
    }
    
    public EventSubscriptionEntity(final EventType eventType) {
        this.revision = 1;
        this.created = ClockUtil.getCurrentTime();
        this.eventType = eventType.name();
    }
    
    public EventSubscriptionEntity(final ExecutionEntity executionEntity, final EventType eventType) {
        this(eventType);
        this.setExecution(executionEntity);
        this.setActivity(this.execution.getActivity());
        this.processInstanceId = executionEntity.getProcessInstanceId();
        this.tenantId = executionEntity.getTenantId();
    }
    
    public void eventReceived(final Object payload, final boolean processASync) {
        this.eventReceived(payload, null, null, processASync);
    }
    
    public void eventReceived(final Object payload, final Object payloadLocal, final String businessKey, final boolean processASync) {
        if (processASync) {
            this.scheduleEventAsync(payload, payloadLocal, businessKey);
        }
        else {
            this.processEventSync(payload, payloadLocal, businessKey);
        }
    }
    
    protected void processEventSync(final Object payload) {
        this.processEventSync(payload, null, null);
    }
    
    protected void processEventSync(final Object payload, final Object payloadLocal, final String businessKey) {
        final EventHandler eventHandler = Context.getProcessEngineConfiguration().getEventHandler(this.eventType);
        EnsureUtil.ensureNotNull("Could not find eventhandler for event of type '" + this.eventType + "'", "eventHandler", eventHandler);
        eventHandler.handleEvent(this, payload, payloadLocal, businessKey, Context.getCommandContext());
    }
    
    protected void scheduleEventAsync(final Object payload, final Object payloadLocal, final String businessKey) {
        final EventSubscriptionJobDeclaration asyncDeclaration = this.getJobDeclaration();
        if (asyncDeclaration == null) {
            this.processEventSync(payload, payloadLocal, businessKey);
        }
        else {
            final MessageEntity message = asyncDeclaration.createJobInstance(this);
            final CommandContext commandContext = Context.getCommandContext();
            commandContext.getJobManager().send(message);
        }
    }
    
    public void delete() {
        Context.getCommandContext().getEventSubscriptionManager().deleteEventSubscription(this);
        this.removeFromExecution();
    }
    
    public void insert() {
        Context.getCommandContext().getEventSubscriptionManager().insert(this);
        this.addToExecution();
    }
    
    public static EventSubscriptionEntity createAndInsert(final ExecutionEntity executionEntity, final EventType eventType, final ActivityImpl activity) {
        return createAndInsert(executionEntity, eventType, activity, null);
    }
    
    public static EventSubscriptionEntity createAndInsert(final ExecutionEntity executionEntity, final EventType eventType, final ActivityImpl activity, final String configuration) {
        final EventSubscriptionEntity eventSubscription = new EventSubscriptionEntity(executionEntity, eventType);
        eventSubscription.setActivity(activity);
        eventSubscription.setTenantId(executionEntity.getTenantId());
        eventSubscription.setConfiguration(configuration);
        eventSubscription.insert();
        return eventSubscription;
    }
    
    protected void addToExecution() {
        final ExecutionEntity execution = this.getExecution();
        if (execution != null) {
            execution.addEventSubscription(this);
        }
    }
    
    protected void removeFromExecution() {
        final ExecutionEntity execution = this.getExecution();
        if (execution != null) {
            execution.removeEventSubscription(this);
        }
    }
    
    @Override
    public Object getPersistentState() {
        final HashMap<String, Object> persistentState = new HashMap<String, Object>();
        persistentState.put("executionId", this.executionId);
        persistentState.put("configuration", this.configuration);
        persistentState.put("activityId", this.activityId);
        persistentState.put("eventName", this.eventName);
        return persistentState;
    }
    
    public ExecutionEntity getExecution() {
        if (this.execution == null && this.executionId != null) {
            this.execution = Context.getCommandContext().getExecutionManager().findExecutionById(this.executionId);
        }
        return this.execution;
    }
    
    public void setExecution(final ExecutionEntity execution) {
        if (execution != null) {
            this.execution = execution;
            this.executionId = execution.getId();
            this.addToExecution();
        }
        else {
            this.removeFromExecution();
            this.executionId = null;
            this.execution = null;
        }
    }
    
    public ActivityImpl getActivity() {
        if (this.activity == null && this.activityId != null) {
            final ProcessDefinitionImpl processDefinition = this.getProcessDefinition();
            this.activity = processDefinition.findActivity(this.activityId);
        }
        return this.activity;
    }
    
    public ProcessDefinitionEntity getProcessDefinition() {
        if (this.executionId != null) {
            final ExecutionEntity execution = this.getExecution();
            return execution.getProcessDefinition();
        }
        final String processDefinitionId = this.getConfiguration();
        return Context.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(processDefinitionId);
    }
    
    public void setActivity(final ActivityImpl activity) {
        this.activity = activity;
        if (activity != null) {
            this.activityId = activity.getId();
        }
    }
    
    public EventSubscriptionJobDeclaration getJobDeclaration() {
        if (this.jobDeclaration == null) {
            this.jobDeclaration = EventSubscriptionJobDeclaration.findDeclarationForSubscription(this);
        }
        return this.jobDeclaration;
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public void setId(final String id) {
        this.id = id;
    }
    
    @Override
    public int getRevision() {
        return this.revision;
    }
    
    @Override
    public void setRevision(final int revision) {
        this.revision = revision;
    }
    
    @Override
    public int getRevisionNext() {
        return this.revision + 1;
    }
    
    public boolean isSubscriptionForEventType(final EventType eventType) {
        return this.eventType.equals(eventType.name());
    }
    
    @Override
    public String getEventType() {
        return this.eventType;
    }
    
    public void setEventType(final String eventType) {
        this.eventType = eventType;
    }
    
    @Override
    public String getEventName() {
        return this.eventName;
    }
    
    public void setEventName(final String eventName) {
        this.eventName = eventName;
    }
    
    @Override
    public String getExecutionId() {
        return this.executionId;
    }
    
    public void setExecutionId(final String executionId) {
        this.executionId = executionId;
    }
    
    @Override
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public void setProcessInstanceId(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    public String getConfiguration() {
        return this.configuration;
    }
    
    public void setConfiguration(final String configuration) {
        this.configuration = configuration;
    }
    
    @Override
    public String getActivityId() {
        return this.activityId;
    }
    
    public void setActivityId(final String activityId) {
        this.activityId = activityId;
        this.activity = null;
    }
    
    @Override
    public Date getCreated() {
        return this.created;
    }
    
    public void setCreated(final Date created) {
        this.created = created;
    }
    
    @Override
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final EventSubscriptionEntity other = (EventSubscriptionEntity)obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        }
        else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }
    
    @Override
    public Set<String> getReferencedEntityIds() {
        final Set<String> referencedEntityIds = new HashSet<String>();
        return referencedEntityIds;
    }
    
    @Override
    public Map<String, Class> getReferencedEntitiesIdAndClass() {
        final Map<String, Class> referenceIdAndClass = new HashMap<String, Class>();
        if (this.executionId != null) {
            referenceIdAndClass.put(this.executionId, ExecutionEntity.class);
        }
        return referenceIdAndClass;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.id + ", eventType=" + this.eventType + ", eventName=" + this.eventName + ", executionId=" + this.executionId + ", processInstanceId=" + this.processInstanceId + ", activityId=" + this.activityId + ", tenantId=" + this.tenantId + ", configuration=" + this.configuration + ", revision=" + this.revision + ", created=" + this.created + "]";
    }
}
