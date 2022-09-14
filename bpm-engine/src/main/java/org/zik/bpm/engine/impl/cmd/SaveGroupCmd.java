// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.identity.IdentityOperationResult;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.identity.Group;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SaveGroupCmd extends AbstractWritableIdentityServiceCmd<Void> implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected Group group;
    
    public SaveGroupCmd(final Group group) {
        this.group = group;
    }
    
    @Override
    protected Void executeCmd(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("group", this.group);
        EnsureUtil.ensureWhitelistedResourceId(commandContext, "Group", this.group.getId());
        final IdentityOperationResult operationResult = commandContext.getWritableIdentityProvider().saveGroup(this.group);
        commandContext.getOperationLogManager().logGroupOperation(operationResult, this.group.getId());
        return null;
    }
}
