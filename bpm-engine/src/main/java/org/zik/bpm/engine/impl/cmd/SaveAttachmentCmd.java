// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.AttachmentEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.task.Attachment;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SaveAttachmentCmd implements Command<Object>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected Attachment attachment;
    
    public SaveAttachmentCmd(final Attachment attachment) {
        this.attachment = attachment;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        final AttachmentEntity updateAttachment = commandContext.getDbEntityManager().selectById(AttachmentEntity.class, this.attachment.getId());
        updateAttachment.setName(this.attachment.getName());
        updateAttachment.setDescription(this.attachment.getDescription());
        final String taskId = this.attachment.getTaskId();
        if (taskId != null) {
            final TaskEntity task = commandContext.getTaskManager().findTaskById(taskId);
            if (task != null) {
                task.triggerUpdateEvent();
            }
        }
        return null;
    }
}
