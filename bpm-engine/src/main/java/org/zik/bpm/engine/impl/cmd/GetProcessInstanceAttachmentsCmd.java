// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.task.Attachment;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetProcessInstanceAttachmentsCmd implements Command<List<Attachment>>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String processInstanceId;
    
    public GetProcessInstanceAttachmentsCmd(final String taskId) {
        this.processInstanceId = taskId;
    }
    
    @Override
    public List<Attachment> execute(final CommandContext commandContext) {
        return commandContext.getAttachmentManager().findAttachmentsByProcessInstanceId(this.processInstanceId);
    }
}
