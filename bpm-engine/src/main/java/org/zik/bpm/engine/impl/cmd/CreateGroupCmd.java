// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.identity.Group;

public class CreateGroupCmd extends AbstractWritableIdentityServiceCmd<Group> implements Command<Group>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String groupId;
    
    public CreateGroupCmd(final String groupId) {
        EnsureUtil.ensureNotNull("groupId", (Object)groupId);
        this.groupId = groupId;
    }
    
    @Override
    protected Group executeCmd(final CommandContext commandContext) {
        return commandContext.getWritableIdentityProvider().createNewGroup(this.groupId);
    }
}
