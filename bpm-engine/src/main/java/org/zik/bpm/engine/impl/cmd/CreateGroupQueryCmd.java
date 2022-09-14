// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.identity.GroupQuery;
import org.zik.bpm.engine.impl.interceptor.Command;

public class CreateGroupQueryCmd implements Command<GroupQuery>, Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Override
    public GroupQuery execute(final CommandContext commandContext) {
        return commandContext.getReadOnlyIdentityProvider().createGroupQuery();
    }
}
