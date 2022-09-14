// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.persistence.entity.AttachmentEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteTaskAttachmentCmd implements Command<Object>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String attachmentId;
    protected String taskId;
    
    public DeleteTaskAttachmentCmd(final String taskId, final String attachmentId) {
        this.attachmentId = attachmentId;
        this.taskId = taskId;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        final AttachmentEntity attachment = (AttachmentEntity)commandContext.getAttachmentManager().findAttachmentByTaskIdAndAttachmentId(this.taskId, this.attachmentId);
        EnsureUtil.ensureNotNull("No attachment exist for task id '" + this.taskId + " and attachmentId '" + this.attachmentId + "'.", "attachment", attachment);
        commandContext.getDbEntityManager().delete(attachment);
        if (attachment.getContentId() != null) {
            commandContext.getByteArrayManager().deleteByteArrayById(attachment.getContentId());
        }
        if (attachment.getTaskId() != null) {
            final TaskEntity task = commandContext.getTaskManager().findTaskById(attachment.getTaskId());
            final PropertyChange propertyChange = new PropertyChange("name", null, attachment.getName());
            commandContext.getOperationLogManager().logAttachmentOperation("DeleteAttachment", task, propertyChange);
        }
        return null;
    }
}
