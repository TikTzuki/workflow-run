// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.application.ProcessApplicationReference;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.UserOperationLogManager;
import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.TransactionListener;
import org.zik.bpm.engine.impl.cfg.TransactionState;
import java.util.Collections;
import org.zik.bpm.engine.impl.persistence.deploy.DeleteDeploymentFailListener;
import org.zik.bpm.engine.impl.context.Context;
import java.util.Arrays;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.cfg.TransactionLogger;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteDeploymentCmd implements Command<Void>, Serializable
{
    private static final TransactionLogger TX_LOG;
    private static final long serialVersionUID = 1L;
    protected String deploymentId;
    protected boolean cascade;
    protected boolean skipCustomListeners;
    protected boolean skipIoMappings;
    
    public DeleteDeploymentCmd(final String deploymentId, final boolean cascade, final boolean skipCustomListeners, final boolean skipIoMappings) {
        this.deploymentId = deploymentId;
        this.cascade = cascade;
        this.skipCustomListeners = skipCustomListeners;
        this.skipIoMappings = skipIoMappings;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("deploymentId", (Object)this.deploymentId);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkDeleteDeployment(this.deploymentId);
        }
        final UserOperationLogManager logManager = commandContext.getOperationLogManager();
        final List<PropertyChange> propertyChanges = Arrays.asList(new PropertyChange("cascade", null, this.cascade));
        logManager.logDeploymentOperation("Delete", this.deploymentId, propertyChanges);
        commandContext.getDeploymentManager().deleteDeployment(this.deploymentId, this.cascade, this.skipCustomListeners, this.skipIoMappings);
        final ProcessApplicationReference processApplicationReference = Context.getProcessEngineConfiguration().getProcessApplicationManager().getProcessApplicationForDeployment(this.deploymentId);
        final DeleteDeploymentFailListener listener = new DeleteDeploymentFailListener(this.deploymentId, processApplicationReference, Context.getProcessEngineConfiguration().getCommandExecutorTxRequiresNew());
        try {
            commandContext.runWithoutAuthorization((Command<Object>)new UnregisterProcessApplicationCmd(this.deploymentId, false));
            commandContext.runWithoutAuthorization((Command<Object>)new UnregisterDeploymentCmd(Collections.singleton(this.deploymentId)));
        }
        finally {
            try {
                commandContext.getTransactionContext().addTransactionListener(TransactionState.ROLLED_BACK, listener);
            }
            catch (Exception e) {
                DeleteDeploymentCmd.TX_LOG.debugTransactionOperation("Could not register transaction synchronization. Probably the TX has already been rolled back by application code.");
                listener.execute(commandContext);
            }
        }
        return null;
    }
    
    static {
        TX_LOG = ProcessEngineLogger.TX_LOGGER;
    }
}
