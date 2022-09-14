// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.util.ModificationUtil;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;

public abstract class AbstractInstanceCancellationCmd extends AbstractProcessInstanceModificationCommand
{
    protected String cancellationReason;
    
    public AbstractInstanceCancellationCmd(final String processInstanceId) {
        super(processInstanceId);
        this.cancellationReason = "Cancellation due to process instance modifcation";
    }
    
    public AbstractInstanceCancellationCmd(final String processInstanceId, final String cancellationReason) {
        super(processInstanceId);
        this.cancellationReason = cancellationReason;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        ExecutionEntity topmostCancellableExecution;
        for (ExecutionEntity sourceInstanceExecution = topmostCancellableExecution = this.determineSourceInstanceExecution(commandContext), parentScopeExecution = (ExecutionEntity)topmostCancellableExecution.getParentScopeExecution(false); parentScopeExecution != null && parentScopeExecution.getNonEventScopeExecutions().size() <= 1; parentScopeExecution = (ExecutionEntity)topmostCancellableExecution.getParentScopeExecution(false)) {
            topmostCancellableExecution = parentScopeExecution;
        }
        if (topmostCancellableExecution.isPreserveScope()) {
            topmostCancellableExecution.interrupt(this.cancellationReason, this.skipCustomListeners, this.skipIoMappings, this.externallyTerminated);
            topmostCancellableExecution.leaveActivityInstance();
            topmostCancellableExecution.setActivity(null);
        }
        else {
            topmostCancellableExecution.deleteCascade(this.cancellationReason, this.skipCustomListeners, this.skipIoMappings, this.externallyTerminated, false);
            ModificationUtil.handleChildRemovalInScope(topmostCancellableExecution);
        }
        return null;
    }
    
    protected abstract ExecutionEntity determineSourceInstanceExecution(final CommandContext p0);
    
    protected ExecutionEntity findSuperExecution(final ExecutionEntity parentScopeExecution, final ExecutionEntity topmostCancellableExecution) {
        ExecutionEntity superExecution = null;
        if (parentScopeExecution == null) {
            superExecution = topmostCancellableExecution.getSuperExecution();
        }
        return superExecution;
    }
}
