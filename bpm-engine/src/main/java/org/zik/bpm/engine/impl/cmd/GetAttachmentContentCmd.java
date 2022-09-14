// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.db.entitymanager.DbEntityManager;
import java.io.ByteArrayInputStream;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.impl.persistence.entity.AttachmentEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import java.io.InputStream;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetAttachmentContentCmd implements Command<InputStream>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String attachmentId;
    
    public GetAttachmentContentCmd(final String attachmentId) {
        this.attachmentId = attachmentId;
    }
    
    @Override
    public InputStream execute(final CommandContext commandContext) {
        final DbEntityManager dbEntityManger = commandContext.getDbEntityManager();
        final AttachmentEntity attachment = dbEntityManger.selectById(AttachmentEntity.class, this.attachmentId);
        final String contentId = attachment.getContentId();
        if (contentId == null) {
            return null;
        }
        final ByteArrayEntity byteArray = dbEntityManger.selectById(ByteArrayEntity.class, contentId);
        final byte[] bytes = byteArray.getBytes();
        return new ByteArrayInputStream(bytes);
    }
}
