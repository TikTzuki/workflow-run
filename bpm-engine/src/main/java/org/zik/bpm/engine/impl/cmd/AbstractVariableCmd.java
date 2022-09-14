// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.pvm.runtime.Callback;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public abstract class AbstractVariableCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected CommandContext commandContext;
    protected String entityId;
    protected boolean isLocal;
    protected boolean preventLogUserOperation;
    
    public AbstractVariableCmd(final String entityId, final boolean isLocal) {
        this.preventLogUserOperation = false;
        this.entityId = entityId;
        this.isLocal = isLocal;
    }
    
    public AbstractVariableCmd disableLogUserOperation() {
        this.preventLogUserOperation = true;
        return this;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        this.commandContext = commandContext;
        final AbstractVariableScope scope = this.getEntity();
        this.executeOperation(scope);
        this.onSuccess(scope);
        if (!this.preventLogUserOperation) {
            this.logVariableOperation(scope);
        }
        return null;
    }
    
    protected abstract AbstractVariableScope getEntity();
    
    protected abstract ExecutionEntity getContextExecution();
    
    protected abstract void logVariableOperation(final AbstractVariableScope p0);
    
    protected abstract void executeOperation(final AbstractVariableScope p0);
    
    protected abstract String getLogEntryOperation();
    
    protected void onSuccess(final AbstractVariableScope scope) {
        final ExecutionEntity contextExecution = this.getContextExecution();
        if (contextExecution != null) {
            contextExecution.dispatchDelayedEventsAndPerformOperation((Callback<PvmExecutionImpl, Void>)null);
        }
    }
}
