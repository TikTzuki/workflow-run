// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;

public class SetTaskOwnerCmd extends AddIdentityLinkCmd
{
    private static final long serialVersionUID = 1L;
    
    public SetTaskOwnerCmd(final String taskId, final String userId) {
        super(taskId, userId, null, "owner");
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        super.execute(commandContext);
        this.task.logUserOperation("SetOwner");
        return null;
    }
}
