// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class CheckPassword implements Command<Boolean>, Serializable
{
    private static final long serialVersionUID = 1L;
    String userId;
    String password;
    
    public CheckPassword(final String userId, final String password) {
        this.userId = userId;
        this.password = password;
    }
    
    @Override
    public Boolean execute(final CommandContext commandContext) {
        return commandContext.getReadOnlyIdentityProvider().checkPassword(this.userId, this.password);
    }
}
