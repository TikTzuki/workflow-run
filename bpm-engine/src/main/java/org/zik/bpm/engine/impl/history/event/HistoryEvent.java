// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

import java.util.Date;
import org.zik.bpm.engine.impl.db.HistoricEntity;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.io.Serializable;

public class HistoryEvent implements Serializable, DbEntity, HistoricEntity
{
    private static final long serialVersionUID = 1L;
    @Deprecated
    public static final String ACTIVITY_EVENT_TYPE_START;
    @Deprecated
    public static final String ACTIVITY_EVENT_TYPE_UPDATE;
    @Deprecated
    public static final String ACTIVITY_EVENT_TYPE_END;
    @Deprecated
    public static final String TASK_EVENT_TYPE_CREATE;
    @Deprecated
    public static final String TASK_EVENT_TYPE_UPDATE;
    @Deprecated
    public static final String TASK_EVENT_TYPE_COMPLETE;
    @Deprecated
    public static final String TASK_EVENT_TYPE_DELETE;
    @Deprecated
    public static final String VARIABLE_EVENT_TYPE_CREATE;
    @Deprecated
    public static final String VARIABLE_EVENT_TYPE_UPDATE;
    @Deprecated
    public static final String VARIABLE_EVENT_TYPE_DELETE;
    @Deprecated
    public static final String FORM_PROPERTY_UPDATE;
    @Deprecated
    public static final String INCIDENT_CREATE;
    @Deprecated
    public static final String INCIDENT_DELETE;
    @Deprecated
    public static final String INCIDENT_RESOLVE;
    public static final String IDENTITY_LINK_ADD;
    public static final String IDENTITY_LINK_DELETE;
    protected String id;
    protected String rootProcessInstanceId;
    protected String processInstanceId;
    protected String executionId;
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected String processDefinitionName;
    protected Integer processDefinitionVersion;
    protected String caseInstanceId;
    protected String caseExecutionId;
    protected String caseDefinitionId;
    protected String caseDefinitionKey;
    protected String caseDefinitionName;
    protected String eventType;
    protected long sequenceCounter;
    protected Date removalTime;
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public void setProcessInstanceId(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    public String getRootProcessInstanceId() {
        return this.rootProcessInstanceId;
    }
    
    public void setRootProcessInstanceId(final String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }
    
    public String getExecutionId() {
        return this.executionId;
    }
    
    public void setExecutionId(final String executionId) {
        this.executionId = executionId;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public void setProcessDefinitionId(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public void setProcessDefinitionKey(final String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }
    
    public String getProcessDefinitionName() {
        return this.processDefinitionName;
    }
    
    public void setProcessDefinitionName(final String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }
    
    public Integer getProcessDefinitionVersion() {
        return this.processDefinitionVersion;
    }
    
    public void setProcessDefinitionVersion(final Integer processDefinitionVersion) {
        this.processDefinitionVersion = processDefinitionVersion;
    }
    
    public String getCaseDefinitionName() {
        return this.caseDefinitionName;
    }
    
    public void setCaseDefinitionName(final String caseDefinitionName) {
        this.caseDefinitionName = caseDefinitionName;
    }
    
    public String getCaseDefinitionKey() {
        return this.caseDefinitionKey;
    }
    
    public void setCaseDefinitionKey(final String caseDefinitionKey) {
        this.caseDefinitionKey = caseDefinitionKey;
    }
    
    public String getCaseDefinitionId() {
        return this.caseDefinitionId;
    }
    
    public void setCaseDefinitionId(final String caseDefinitionId) {
        this.caseDefinitionId = caseDefinitionId;
    }
    
    public String getCaseInstanceId() {
        return this.caseInstanceId;
    }
    
    public void setCaseInstanceId(final String caseInstanceId) {
        this.caseInstanceId = caseInstanceId;
    }
    
    public String getCaseExecutionId() {
        return this.caseExecutionId;
    }
    
    public void setCaseExecutionId(final String caseExecutionId) {
        this.caseExecutionId = caseExecutionId;
    }
    
    @Override
    public void setId(final String id) {
        this.id = id;
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    public String getEventType() {
        return this.eventType;
    }
    
    public void setEventType(final String eventType) {
        this.eventType = eventType;
    }
    
    public long getSequenceCounter() {
        return this.sequenceCounter;
    }
    
    public void setSequenceCounter(final long sequenceCounter) {
        this.sequenceCounter = sequenceCounter;
    }
    
    public Date getRemovalTime() {
        return this.removalTime;
    }
    
    public void setRemovalTime(final Date removalTime) {
        this.removalTime = removalTime;
    }
    
    @Override
    public Object getPersistentState() {
        return HistoryEvent.class;
    }
    
    public boolean isEventOfType(final HistoryEventType type) {
        return type.getEventName().equals(this.eventType);
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.id + ", eventType=" + this.eventType + ", executionId=" + this.executionId + ", processDefinitionId=" + this.processDefinitionId + ", processInstanceId=" + this.processInstanceId + ", rootProcessInstanceId=" + this.rootProcessInstanceId + ", removalTime=" + this.removalTime + "]";
    }
    
    static {
        ACTIVITY_EVENT_TYPE_START = HistoryEventTypes.ACTIVITY_INSTANCE_START.getEventName();
        ACTIVITY_EVENT_TYPE_UPDATE = HistoryEventTypes.ACTIVITY_INSTANCE_END.getEventName();
        ACTIVITY_EVENT_TYPE_END = HistoryEventTypes.ACTIVITY_INSTANCE_END.getEventName();
        TASK_EVENT_TYPE_CREATE = HistoryEventTypes.TASK_INSTANCE_CREATE.getEventName();
        TASK_EVENT_TYPE_UPDATE = HistoryEventTypes.TASK_INSTANCE_UPDATE.getEventName();
        TASK_EVENT_TYPE_COMPLETE = HistoryEventTypes.TASK_INSTANCE_COMPLETE.getEventName();
        TASK_EVENT_TYPE_DELETE = HistoryEventTypes.TASK_INSTANCE_DELETE.getEventName();
        VARIABLE_EVENT_TYPE_CREATE = HistoryEventTypes.VARIABLE_INSTANCE_CREATE.getEventName();
        VARIABLE_EVENT_TYPE_UPDATE = HistoryEventTypes.VARIABLE_INSTANCE_UPDATE.getEventName();
        VARIABLE_EVENT_TYPE_DELETE = HistoryEventTypes.VARIABLE_INSTANCE_DELETE.getEventName();
        FORM_PROPERTY_UPDATE = HistoryEventTypes.FORM_PROPERTY_UPDATE.getEventName();
        INCIDENT_CREATE = HistoryEventTypes.INCIDENT_CREATE.getEventName();
        INCIDENT_DELETE = HistoryEventTypes.INCIDENT_DELETE.getEventName();
        INCIDENT_RESOLVE = HistoryEventTypes.INCIDENT_RESOLVE.getEventName();
        IDENTITY_LINK_ADD = HistoryEventTypes.IDENTITY_LINK_ADD.getEventName();
        IDENTITY_LINK_DELETE = HistoryEventTypes.IDENTITY_LINK_DELETE.getEventName();
    }
}
