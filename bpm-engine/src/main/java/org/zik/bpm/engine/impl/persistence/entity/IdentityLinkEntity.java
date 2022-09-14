// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.impl.history.event.HistoryEventProcessor;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.db.HasDbReferences;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.task.IdentityLink;
import java.io.Serializable;

public class IdentityLinkEntity implements Serializable, IdentityLink, DbEntity, HasDbReferences
{
    private static final long serialVersionUID = 1L;
    protected static final EnginePersistenceLogger LOG;
    protected String id;
    protected String type;
    protected String userId;
    protected String groupId;
    protected String taskId;
    protected String processDefId;
    protected String tenantId;
    protected TaskEntity task;
    protected ProcessDefinitionEntity processDef;
    
    @Override
    public Object getPersistentState() {
        return this.type;
    }
    
    public static IdentityLinkEntity createAndInsert() {
        final IdentityLinkEntity identityLinkEntity = new IdentityLinkEntity();
        identityLinkEntity.insert();
        return identityLinkEntity;
    }
    
    public static IdentityLinkEntity newIdentityLink() {
        final IdentityLinkEntity identityLinkEntity = new IdentityLinkEntity();
        return identityLinkEntity;
    }
    
    public void insert() {
        Context.getCommandContext().getDbEntityManager().insert(this);
        this.fireHistoricIdentityLinkEvent(HistoryEventTypes.IDENTITY_LINK_ADD);
    }
    
    public void delete() {
        this.delete(true);
    }
    
    public void delete(final boolean withHistory) {
        Context.getCommandContext().getDbEntityManager().delete(this);
        if (withHistory) {
            this.fireHistoricIdentityLinkEvent(HistoryEventTypes.IDENTITY_LINK_DELETE);
        }
    }
    
    public boolean isUser() {
        return this.userId != null;
    }
    
    public boolean isGroup() {
        return this.groupId != null;
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
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    @Override
    public String getUserId() {
        return this.userId;
    }
    
    public void setUserId(final String userId) {
        if (this.groupId != null && userId != null) {
            throw IdentityLinkEntity.LOG.taskIsAlreadyAssignedException("userId", "groupId");
        }
        this.userId = userId;
    }
    
    @Override
    public String getGroupId() {
        return this.groupId;
    }
    
    public void setGroupId(final String groupId) {
        if (this.userId != null && groupId != null) {
            throw IdentityLinkEntity.LOG.taskIsAlreadyAssignedException("groupId", "userId");
        }
        this.groupId = groupId;
    }
    
    @Override
    public String getTaskId() {
        return this.taskId;
    }
    
    void setTaskId(final String taskId) {
        this.taskId = taskId;
    }
    
    @Override
    public String getProcessDefId() {
        return this.processDefId;
    }
    
    public void setProcessDefId(final String processDefId) {
        this.processDefId = processDefId;
    }
    
    @Override
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    public TaskEntity getTask() {
        if (this.task == null && this.taskId != null) {
            this.task = Context.getCommandContext().getTaskManager().findTaskById(this.taskId);
        }
        return this.task;
    }
    
    public void setTask(final TaskEntity task) {
        this.task = task;
        this.taskId = task.getId();
    }
    
    public ProcessDefinitionEntity getProcessDef() {
        if (this.processDef == null && this.processDefId != null) {
            this.processDef = Context.getCommandContext().getProcessDefinitionManager().findLatestProcessDefinitionById(this.processDefId);
        }
        return this.processDef;
    }
    
    public void setProcessDef(final ProcessDefinitionEntity processDef) {
        this.processDef = processDef;
        this.processDefId = processDef.getId();
    }
    
    public void fireHistoricIdentityLinkEvent(final HistoryEventType eventType) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final HistoryLevel historyLevel = processEngineConfiguration.getHistoryLevel();
        if (historyLevel.isHistoryEventProduced(eventType, this)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    HistoryEvent event = null;
                    if (HistoryEvent.IDENTITY_LINK_ADD.equals(eventType.getEventName())) {
                        event = producer.createHistoricIdentityLinkAddEvent(IdentityLinkEntity.this);
                    }
                    else if (HistoryEvent.IDENTITY_LINK_DELETE.equals(eventType.getEventName())) {
                        event = producer.createHistoricIdentityLinkDeleteEvent(IdentityLinkEntity.this);
                    }
                    return event;
                }
            });
        }
    }
    
    @Override
    public Set<String> getReferencedEntityIds() {
        final Set<String> referencedEntityIds = new HashSet<String>();
        return referencedEntityIds;
    }
    
    @Override
    public Map<String, Class> getReferencedEntitiesIdAndClass() {
        final Map<String, Class> referenceIdAndClass = new HashMap<String, Class>();
        if (this.processDefId != null) {
            referenceIdAndClass.put(this.processDefId, ProcessDefinitionEntity.class);
        }
        if (this.taskId != null) {
            referenceIdAndClass.put(this.taskId, TaskEntity.class);
        }
        return referenceIdAndClass;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.id + ", type=" + this.type + ", userId=" + this.userId + ", groupId=" + this.groupId + ", taskId=" + this.taskId + ", processDefId=" + this.processDefId + ", task=" + this.task + ", processDef=" + this.processDef + ", tenantId=" + this.tenantId + "]";
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
