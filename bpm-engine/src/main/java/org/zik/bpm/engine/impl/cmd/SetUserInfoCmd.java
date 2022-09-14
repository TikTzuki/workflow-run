// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Map;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SetUserInfoCmd implements Command<Object>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String userId;
    protected String userPassword;
    protected String type;
    protected String key;
    protected String value;
    protected String accountPassword;
    protected Map<String, String> accountDetails;
    
    public SetUserInfoCmd(final String userId, final String key, final String value) {
        this.userId = userId;
        this.type = "userinfo";
        this.key = key;
        this.value = value;
    }
    
    public SetUserInfoCmd(final String userId, final String userPassword, final String accountName, final String accountUsername, final String accountPassword, final Map<String, String> accountDetails) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.type = "account";
        this.key = accountName;
        this.value = accountUsername;
        this.accountPassword = accountPassword;
        this.accountDetails = accountDetails;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        commandContext.getIdentityInfoManager().setUserInfo(this.userId, this.userPassword, this.type, this.key, this.value, this.accountPassword, this.accountDetails);
        return null;
    }
}
