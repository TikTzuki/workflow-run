// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.io.Serializable;
import org.zik.bpm.engine.impl.db.HistoricEntity;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.task.Attachment;

public class AttachmentEntity implements Attachment, DbEntity, HasDbRevision, HistoricEntity, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected int revision;
    protected String name;
    protected String description;
    protected String type;
    protected String taskId;
    protected String processInstanceId;
    protected String url;
    protected String contentId;
    protected ByteArrayEntity content;
    protected String tenantId;
    protected Date createTime;
    protected String rootProcessInstanceId;
    protected Date removalTime;
    
    @Override
    public Object getPersistentState() {
        final Map<String, Object> persistentState = new HashMap<String, Object>();
        persistentState.put("name", this.name);
        persistentState.put("description", this.description);
        return persistentState;
    }
    
    @Override
    public int getRevisionNext() {
        return this.revision + 1;
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
    public String getName() {
        return this.name;
    }
    
    @Override
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public String getDescription() {
        return this.description;
    }
    
    @Override
    public void setDescription(final String description) {
        this.description = description;
    }
    
    @Override
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    @Override
    public String getTaskId() {
        return this.taskId;
    }
    
    public void setTaskId(final String taskId) {
        this.taskId = taskId;
    }
    
    @Override
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public void setProcessInstanceId(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    @Override
    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
    
    public String getContentId() {
        return this.contentId;
    }
    
    public void setContentId(final String contentId) {
        this.contentId = contentId;
    }
    
    public ByteArrayEntity getContent() {
        return this.content;
    }
    
    public void setContent(final ByteArrayEntity content) {
        this.content = content;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(final Date createTime) {
        this.createTime = createTime;
    }
    
    @Override
    public String getRootProcessInstanceId() {
        return this.rootProcessInstanceId;
    }
    
    @Override
    public Date getRemovalTime() {
        return this.removalTime;
    }
    
    public void setRemovalTime(final Date removalTime) {
        this.removalTime = removalTime;
    }
    
    public void setRootProcessInstanceId(final String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.id + ", revision=" + this.revision + ", name=" + this.name + ", description=" + this.description + ", type=" + this.type + ", taskId=" + this.taskId + ", processInstanceId=" + this.processInstanceId + ", rootProcessInstanceId=" + this.rootProcessInstanceId + ", removalTime=" + this.removalTime + ", url=" + this.url + ", contentId=" + this.contentId + ", content=" + this.content + ", tenantId=" + this.tenantId + ", createTime=" + this.createTime + "]";
    }
}
