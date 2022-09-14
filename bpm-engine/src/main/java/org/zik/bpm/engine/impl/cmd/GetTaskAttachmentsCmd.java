// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.task.Attachment;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetTaskAttachmentsCmd implements Command<List<Attachment>>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String taskId;
    
    public GetTaskAttachmentsCmd(final String taskId) {
        this.taskId = taskId;
    }
    
    @Override
    public List<Attachment> execute(final CommandContext commandContext) {
        return commandContext.getAttachmentManager().findAttachmentsByTaskId(this.taskId);
    }
}
