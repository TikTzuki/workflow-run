// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.IdentityInfoEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetUserInfoCmd implements Command<String>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String userId;
    protected String key;
    
    public GetUserInfoCmd(final String userId, final String key) {
        this.userId = userId;
        this.key = key;
    }
    
    @Override
    public String execute(final CommandContext commandContext) {
        final IdentityInfoEntity identityInfo = commandContext.getIdentityInfoManager().findUserInfoByUserIdAndKey(this.userId, this.key);
        return (identityInfo != null) ? identityInfo.getValue() : null;
    }
}
