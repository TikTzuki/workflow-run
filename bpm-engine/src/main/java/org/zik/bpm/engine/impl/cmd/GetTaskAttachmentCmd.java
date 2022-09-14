// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.task.Attachment;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetTaskAttachmentCmd implements Command<Attachment>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String attachmentId;
    protected String taskId;
    
    public GetTaskAttachmentCmd(final String taskId, final String attachmentId) {
        this.attachmentId = attachmentId;
        this.taskId = taskId;
    }
    
    @Override
    public Attachment execute(final CommandContext commandContext) {
        return commandContext.getAttachmentManager().findAttachmentByTaskIdAndAttachmentId(this.taskId, this.attachmentId);
    }
}
