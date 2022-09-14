// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.authorization.Authorization;
import org.zik.bpm.engine.impl.interceptor.Command;

public class CreateAuthorizationCommand implements Command<Authorization>
{
    protected int type;
    
    public CreateAuthorizationCommand(final int type) {
        this.type = type;
    }
    
    @Override
    public Authorization execute(final CommandContext commandContext) {
        return commandContext.getAuthorizationManager().createNewAuthorization(this.type);
    }
}
