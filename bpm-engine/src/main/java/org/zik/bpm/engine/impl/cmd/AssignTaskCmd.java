// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;

public class AssignTaskCmd extends AddIdentityLinkCmd
{
    private static final long serialVersionUID = 1L;
    
    public AssignTaskCmd(final String taskId, final String userId) {
        super(taskId, userId, null, "assignee");
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        super.execute(commandContext);
        this.task.logUserOperation("Assign");
        return null;
    }
}
