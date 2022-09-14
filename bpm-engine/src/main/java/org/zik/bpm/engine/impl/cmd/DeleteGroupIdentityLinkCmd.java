// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.interceptor.CommandContext;

public class DeleteGroupIdentityLinkCmd extends DeleteIdentityLinkCmd
{
    private static final long serialVersionUID = 1L;
    
    public DeleteGroupIdentityLinkCmd(final String taskId, final String groupId, final String type) {
        super(taskId, null, groupId, type);
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        super.execute(commandContext);
        final PropertyChange propertyChange = new PropertyChange(this.type, null, this.groupId);
        commandContext.getOperationLogManager().logLinkOperation("DeleteGroupLink", this.task, propertyChange);
        return null;
    }
}
