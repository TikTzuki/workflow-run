// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.task.Comment;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetTaskCommentCmd implements Command<Comment>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String taskId;
    protected String commentId;
    
    public GetTaskCommentCmd(final String taskId, final String commentId) {
        this.taskId = taskId;
        this.commentId = commentId;
    }
    
    @Override
    public Comment execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("taskId", (Object)this.taskId);
        EnsureUtil.ensureNotNull("commentId", (Object)this.commentId);
        return commandContext.getCommentManager().findCommentByTaskIdAndCommentId(this.taskId, this.commentId);
    }
}
