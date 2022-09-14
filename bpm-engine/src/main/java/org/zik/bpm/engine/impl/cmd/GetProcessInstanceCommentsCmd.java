// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.task.Comment;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetProcessInstanceCommentsCmd implements Command<List<Comment>>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String processInstanceId;
    
    public GetProcessInstanceCommentsCmd(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    @Override
    public List<Comment> execute(final CommandContext commandContext) {
        return commandContext.getCommentManager().findCommentsByProcessInstanceId(this.processInstanceId);
    }
}
