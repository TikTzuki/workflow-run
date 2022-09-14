// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.AttachmentEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.task.Attachment;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetAttachmentCmd implements Command<Attachment>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String attachmentId;
    
    public GetAttachmentCmd(final String attachmentId) {
        this.attachmentId = attachmentId;
    }
    
    @Override
    public Attachment execute(final CommandContext commandContext) {
        return commandContext.getDbEntityManager().selectById(AttachmentEntity.class, this.attachmentId);
    }
}
