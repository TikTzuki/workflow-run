// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetUserInfoKeysCmd implements Command<List<String>>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String userId;
    protected String userInfoType;
    
    public GetUserInfoKeysCmd(final String userId, final String userInfoType) {
        this.userId = userId;
        this.userInfoType = userInfoType;
    }
    
    @Override
    public List<String> execute(final CommandContext commandContext) {
        return commandContext.getIdentityInfoManager().findUserInfoKeysByUserIdAndType(this.userId, this.userInfoType);
    }
}
