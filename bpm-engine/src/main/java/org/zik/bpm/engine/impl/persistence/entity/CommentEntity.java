// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;
import org.zik.bpm.engine.impl.util.StringUtil;
import java.util.Date;
import java.io.Serializable;
import org.zik.bpm.engine.impl.db.HistoricEntity;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.task.Event;
import org.zik.bpm.engine.task.Comment;

public class CommentEntity implements Comment, Event, DbEntity, HistoricEntity, Serializable
{
    private static final long serialVersionUID = 1L;
    public static final String TYPE_EVENT = "event";
    public static final String TYPE_COMMENT = "comment";
    protected String id;
    protected String type;
    protected String userId;
    protected Date time;
    protected String taskId;
    protected String processInstanceId;
    protected String action;
    protected String message;
    protected String fullMessage;
    protected String tenantId;
    protected String rootProcessInstanceId;
    protected Date removalTime;
    public static String MESSAGE_PARTS_MARKER;
    
    @Override
    public Object getPersistentState() {
        return CommentEntity.class;
    }
    
    public byte[] getFullMessageBytes() {
        return (byte[])((this.fullMessage != null) ? StringUtil.toByteArray(this.fullMessage) : null);
    }
    
    public void setFullMessageBytes(final byte[] fullMessageBytes) {
        this.fullMessage = ((fullMessageBytes != null) ? StringUtil.fromBytes(fullMessageBytes) : null);
    }
    
    public void setMessage(final String[] messageParts) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final String part : messageParts) {
            if (part != null) {
                stringBuilder.append(part.replace(CommentEntity.MESSAGE_PARTS_MARKER, " | "));
                stringBuilder.append(CommentEntity.MESSAGE_PARTS_MARKER);
            }
            else {
                stringBuilder.append("null");
                stringBuilder.append(CommentEntity.MESSAGE_PARTS_MARKER);
            }
        }
        for (int i = 0; i < CommentEntity.MESSAGE_PARTS_MARKER.length(); ++i) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        this.message = stringBuilder.toString();
    }
    
    @Override
    public List<String> getMessageParts() {
        if (this.message == null) {
            return null;
        }
        final List<String> messageParts = new ArrayList<String>();
        final StringTokenizer tokenizer = new StringTokenizer(this.message, CommentEntity.MESSAGE_PARTS_MARKER);
        while (tokenizer.hasMoreTokens()) {
            final String nextToken = tokenizer.nextToken();
            if ("null".equals(nextToken)) {
                messageParts.add(null);
            }
            else {
                messageParts.add(nextToken);
            }
        }
        return messageParts;
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
    public String getUserId() {
        return this.userId;
    }
    
    public void setUserId(final String userId) {
        this.userId = userId;
    }
    
    @Override
    public String getTaskId() {
        return this.taskId;
    }
    
    public void setTaskId(final String taskId) {
        this.taskId = taskId;
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
    
    @Override
    public Date getTime() {
        return this.time;
    }
    
    public void setTime(final Date time) {
        this.time = time;
    }
    
    @Override
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public void setProcessInstanceId(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    @Override
    public String getFullMessage() {
        return this.fullMessage;
    }
    
    public void setFullMessage(final String fullMessage) {
        this.fullMessage = fullMessage;
    }
    
    @Override
    public String getAction() {
        return this.action;
    }
    
    public void setAction(final String action) {
        this.action = action;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public String getRootProcessInstanceId() {
        return this.rootProcessInstanceId;
    }
    
    public void setRootProcessInstanceId(final String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }
    
    @Override
    public Date getRemovalTime() {
        return this.removalTime;
    }
    
    public void setRemovalTime(final Date removalTime) {
        this.removalTime = removalTime;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.id + ", type=" + this.type + ", userId=" + this.userId + ", time=" + this.time + ", taskId=" + this.taskId + ", processInstanceId=" + this.processInstanceId + ", rootProcessInstanceId=" + this.rootProcessInstanceId + ", removalTime=" + this.removalTime + ", action=" + this.action + ", message=" + this.message + ", fullMessage=" + this.fullMessage + ", tenantId=" + this.tenantId + "]";
    }
    
    static {
        CommentEntity.MESSAGE_PARTS_MARKER = "_|_";
    }
}
