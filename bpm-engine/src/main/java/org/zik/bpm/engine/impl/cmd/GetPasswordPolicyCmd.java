// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.identity.PasswordPolicy;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetPasswordPolicyCmd implements Command<PasswordPolicy>, Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Override
    public PasswordPolicy execute(final CommandContext commandContext) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = commandContext.getProcessEngineConfiguration();
        if (processEngineConfiguration.isEnablePasswordPolicy()) {
            return processEngineConfiguration.getPasswordPolicy();
        }
        return null;
    }
}
