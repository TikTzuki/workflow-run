// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.deploy;

import org.zik.bpm.engine.impl.cmd.RegisterProcessApplicationCmd;
import org.zik.bpm.engine.impl.cmd.RegisterDeploymentCmd;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.impl.cfg.TransactionListener;

public class DeleteDeploymentFailListener implements TransactionListener
{
    protected String deploymentId;
    protected ProcessApplicationReference processApplicationReference;
    protected CommandExecutor commandExecutor;
    
    public DeleteDeploymentFailListener(final String deploymentId, final ProcessApplicationReference processApplicationReference, final CommandExecutor commandExecutor) {
        this.deploymentId = deploymentId;
        this.processApplicationReference = processApplicationReference;
        this.commandExecutor = commandExecutor;
    }
    
    @Override
    public void execute(final CommandContext commandContext) {
        this.commandExecutor.execute((Command<Object>)new DeleteDeploymentFailCmd());
    }
    
    protected class DeleteDeploymentFailCmd implements Command<Void>
    {
        @Override
        public Void execute(final CommandContext commandContext) {
            commandContext.runWithoutAuthorization((Command<Object>)new RegisterDeploymentCmd(DeleteDeploymentFailListener.this.deploymentId));
            if (DeleteDeploymentFailListener.this.processApplicationReference != null) {
                commandContext.runWithoutAuthorization((Command<Object>)new RegisterProcessApplicationCmd(DeleteDeploymentFailListener.this.deploymentId, DeleteDeploymentFailListener.this.processApplicationReference));
            }
            return null;
        }
    }
}
