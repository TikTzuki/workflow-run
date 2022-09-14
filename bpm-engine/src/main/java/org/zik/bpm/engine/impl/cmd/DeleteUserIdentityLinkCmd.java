// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.interceptor.CommandContext;

public class DeleteUserIdentityLinkCmd extends DeleteIdentityLinkCmd
{
    private static final long serialVersionUID = 1L;
    
    public DeleteUserIdentityLinkCmd(final String taskId, final String userId, final String type) {
        super(taskId, userId, null, type);
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        super.execute(commandContext);
        final PropertyChange propertyChange = new PropertyChange(this.type, null, this.userId);
        commandContext.getOperationLogManager().logLinkOperation("DeleteUserLink", this.task, propertyChange);
        return null;
    }
}
