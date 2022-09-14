// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.io.ByteArrayInputStream;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.impl.persistence.entity.AttachmentEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import java.io.InputStream;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetTaskAttachmentContentCmd implements Command<InputStream>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String attachmentId;
    protected String taskId;
    
    public GetTaskAttachmentContentCmd(final String taskId, final String attachmentId) {
        this.attachmentId = attachmentId;
        this.taskId = taskId;
    }
    
    @Override
    public InputStream execute(final CommandContext commandContext) {
        final AttachmentEntity attachment = (AttachmentEntity)commandContext.getAttachmentManager().findAttachmentByTaskIdAndAttachmentId(this.taskId, this.attachmentId);
        if (attachment == null) {
            return null;
        }
        final String contentId = attachment.getContentId();
        if (contentId == null) {
            return null;
        }
        final ByteArrayEntity byteArray = commandContext.getDbEntityManager().selectById(ByteArrayEntity.class, contentId);
        final byte[] bytes = byteArray.getBytes();
        return new ByteArrayInputStream(bytes);
    }
}
