// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteUserInfoCmd implements Command<Object>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String userId;
    protected String key;
    
    public DeleteUserInfoCmd(final String userId, final String key) {
        this.userId = userId;
        this.key = key;
    }
    
    @Override
    public String execute(final CommandContext commandContext) {
        commandContext.getIdentityInfoManager().deleteUserInfoByUserIdAndKey(this.userId, this.key);
        return null;
    }
}
