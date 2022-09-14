// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import java.util.Iterator;
import java.util.Map;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.HashMap;
import java.util.Date;
import org.zik.bpm.engine.task.Attachment;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.AbstractHistoricManager;

public class AttachmentManager extends AbstractHistoricManager
{
    public List<Attachment> findAttachmentsByProcessInstanceId(final String processInstanceId) {
        this.checkHistoryEnabled();
        return (List<Attachment>)this.getDbEntityManager().selectList("selectAttachmentsByProcessInstanceId", processInstanceId);
    }
    
    public List<Attachment> findAttachmentsByTaskId(final String taskId) {
        this.checkHistoryEnabled();
        return (List<Attachment>)this.getDbEntityManager().selectList("selectAttachmentsByTaskId", taskId);
    }
    
    public void addRemovalTimeToAttachmentsByRootProcessInstanceId(final String rootProcessInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("rootProcessInstanceId", rootProcessInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(AttachmentEntity.class, "updateAttachmentsByRootProcessInstanceId", parameters);
    }
    
    public void addRemovalTimeToAttachmentsByProcessInstanceId(final String processInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceId", processInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(AttachmentEntity.class, "updateAttachmentsByProcessInstanceId", parameters);
    }
    
    public void deleteAttachmentsByTaskId(final String taskId) {
        this.checkHistoryEnabled();
        final List<AttachmentEntity> attachments = (List<AttachmentEntity>)this.getDbEntityManager().selectList("selectAttachmentsByTaskId", taskId);
        for (final AttachmentEntity attachment : attachments) {
            final String contentId = attachment.getContentId();
            if (contentId != null) {
                this.getByteArrayManager().deleteByteArrayById(contentId);
            }
            this.getDbEntityManager().delete(attachment);
        }
    }
    
    public void deleteAttachmentsByProcessInstanceIds(final List<String> processInstanceIds) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceIds", processInstanceIds);
        this.deleteAttachments(parameters);
    }
    
    public void deleteAttachmentsByTaskProcessInstanceIds(final List<String> processInstanceIds) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("taskProcessInstanceIds", processInstanceIds);
        this.deleteAttachments(parameters);
    }
    
    public void deleteAttachmentsByTaskCaseInstanceIds(final List<String> caseInstanceIds) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("caseInstanceIds", caseInstanceIds);
        this.deleteAttachments(parameters);
    }
    
    protected void deleteAttachments(final Map<String, Object> parameters) {
        this.getDbEntityManager().deletePreserveOrder(ByteArrayEntity.class, "deleteAttachmentByteArraysByIds", parameters);
        this.getDbEntityManager().deletePreserveOrder(AttachmentEntity.class, "deleteAttachmentByIds", parameters);
    }
    
    public Attachment findAttachmentByTaskIdAndAttachmentId(final String taskId, final String attachmentId) {
        this.checkHistoryEnabled();
        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("taskId", taskId);
        parameters.put("id", attachmentId);
        return (AttachmentEntity)this.getDbEntityManager().selectOne("selectAttachmentByTaskIdAndAttachmentId", parameters);
    }
    
    public DbOperation deleteAttachmentsByRemovalTime(final Date removalTime, final int minuteFrom, final int minuteTo, final int batchSize) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("removalTime", removalTime);
        if (minuteTo - minuteFrom + 1 < 60) {
            parameters.put("minuteFrom", minuteFrom);
            parameters.put("minuteTo", minuteTo);
        }
        parameters.put("batchSize", batchSize);
        return this.getDbEntityManager().deletePreserveOrder(AttachmentEntity.class, "deleteAttachmentsByRemovalTime", new ListQueryParameterObject(parameters, 0, batchSize));
    }
}
