// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.persistence.entity.AttachmentEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteAttachmentCmd implements Command<Object>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String attachmentId;
    
    public DeleteAttachmentCmd(final String attachmentId) {
        this.attachmentId = attachmentId;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        final AttachmentEntity attachment = commandContext.getDbEntityManager().selectById(AttachmentEntity.class, this.attachmentId);
        commandContext.getDbEntityManager().delete(attachment);
        if (attachment.getContentId() != null) {
            commandContext.getByteArrayManager().deleteByteArrayById(attachment.getContentId());
        }
        if (attachment.getTaskId() != null) {
            final TaskEntity task = commandContext.getTaskManager().findTaskById(attachment.getTaskId());
            final PropertyChange propertyChange = new PropertyChange("name", null, attachment.getName());
            commandContext.getOperationLogManager().logAttachmentOperation("DeleteAttachment", task, propertyChange);
            task.triggerUpdateEvent();
        }
        return null;
    }
}
