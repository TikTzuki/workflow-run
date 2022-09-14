// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.identity.Account;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetUserAccountCmd implements Command<Account>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String userId;
    protected String userPassword;
    protected String accountName;
    
    public GetUserAccountCmd(final String userId, final String userPassword, final String accountName) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.accountName = accountName;
    }
    
    @Override
    public Account execute(final CommandContext commandContext) {
        return commandContext.getIdentityInfoManager().findUserAccountByUserIdAndKey(this.userId, this.userPassword, this.accountName);
    }
}
