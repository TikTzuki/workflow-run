// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.identity.IdentityOperationResult;
import org.zik.bpm.engine.impl.persistence.entity.UserEntity;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.identity.User;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SaveUserCmd extends AbstractWritableIdentityServiceCmd<Void> implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected User user;
    protected boolean skipPasswordPolicy;
    
    public SaveUserCmd(final User user) {
        this(user, false);
    }
    
    public SaveUserCmd(final User user, final boolean skipPasswordPolicy) {
        this.user = user;
        this.skipPasswordPolicy = skipPasswordPolicy;
    }
    
    @Override
    protected Void executeCmd(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("user", this.user);
        EnsureUtil.ensureWhitelistedResourceId(commandContext, "User", this.user.getId());
        if (this.user instanceof UserEntity) {
            this.validateUserEntity(commandContext);
        }
        final IdentityOperationResult operationResult = commandContext.getWritableIdentityProvider().saveUser(this.user);
        commandContext.getOperationLogManager().logUserOperation(operationResult, this.user.getId());
        return null;
    }
    
    private void validateUserEntity(final CommandContext commandContext) {
        if (this.shouldCheckPasswordPolicy(commandContext) && !((UserEntity)this.user).checkPasswordAgainstPolicy()) {
            throw new ProcessEngineException("Password does not match policy");
        }
    }
    
    protected boolean shouldCheckPasswordPolicy(final CommandContext commandContext) {
        return ((UserEntity)this.user).hasNewPassword() && !this.skipPasswordPolicy && commandContext.getProcessEngineConfiguration().isEnablePasswordPolicy();
    }
}
