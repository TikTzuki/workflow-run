// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.deploy;

import org.zik.bpm.engine.impl.cmd.UnregisterDeploymentCmd;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Collections;
import java.util.Set;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.cfg.TransactionListener;

public class DeploymentFailListener implements TransactionListener
{
    protected CommandExecutor commandExecutor;
    protected Set<String> deploymentIds;
    
    public DeploymentFailListener(final String deploymentId, final CommandExecutor commandExecutor) {
        this.deploymentIds = Collections.singleton(deploymentId);
        this.commandExecutor = commandExecutor;
    }
    
    public DeploymentFailListener(final Set<String> deploymentIds, final CommandExecutor commandExecutor) {
        this.deploymentIds = deploymentIds;
        this.commandExecutor = commandExecutor;
    }
    
    @Override
    public void execute(final CommandContext commandContext) {
        this.commandExecutor.execute((Command<Object>)new DeleteDeploymentListenerCmd());
    }
    
    protected class DeleteDeploymentListenerCmd implements Command<Void>
    {
        @Override
        public Void execute(final CommandContext commandContext) {
            commandContext.runWithoutAuthorization((Command<Object>)new UnregisterDeploymentCmd(DeploymentFailListener.this.deploymentIds));
            return null;
        }
    }
}
