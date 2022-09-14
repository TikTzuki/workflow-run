// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.identity.User;

public class CreateUserCmd extends AbstractWritableIdentityServiceCmd<User> implements Command<User>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String userId;
    
    public CreateUserCmd(final String userId) {
        EnsureUtil.ensureNotNull("userId", (Object)userId);
        this.userId = userId;
    }
    
    @Override
    protected User executeCmd(final CommandContext commandContext) {
        return commandContext.getWritableIdentityProvider().createNewUser(this.userId);
    }
}
