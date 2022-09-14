// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.application.ProcessApplicationManager;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Collections;
import java.util.Set;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.application.ProcessApplicationRegistration;
import org.zik.bpm.engine.impl.interceptor.Command;

public class RegisterProcessApplicationCmd implements Command<ProcessApplicationRegistration>
{
    protected ProcessApplicationReference reference;
    protected Set<String> deploymentsToRegister;
    
    public RegisterProcessApplicationCmd(final String deploymentId, final ProcessApplicationReference reference) {
        this(Collections.singleton(deploymentId), reference);
    }
    
    public RegisterProcessApplicationCmd(final Set<String> deploymentsToRegister, final ProcessApplicationReference appReference) {
        this.deploymentsToRegister = deploymentsToRegister;
        this.reference = appReference;
    }
    
    @Override
    public ProcessApplicationRegistration execute(final CommandContext commandContext) {
        commandContext.getAuthorizationManager().checkCamundaAdminOrPermission(CommandChecker::checkRegisterProcessApplication);
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final ProcessApplicationManager processApplicationManager = processEngineConfiguration.getProcessApplicationManager();
        return processApplicationManager.registerProcessApplicationForDeployments(this.deploymentsToRegister, this.reference);
    }
}
