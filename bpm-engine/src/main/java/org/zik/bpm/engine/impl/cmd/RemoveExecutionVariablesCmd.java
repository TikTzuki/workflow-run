// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import java.util.Collection;

public class RemoveExecutionVariablesCmd extends AbstractRemoveVariableCmd
{
    private static final long serialVersionUID = 1L;
    
    public RemoveExecutionVariablesCmd(final String executionId, final Collection<String> variableNames, final boolean isLocal) {
        super(executionId, variableNames, isLocal);
    }
    
    @Override
    protected ExecutionEntity getEntity() {
        EnsureUtil.ensureNotNull("executionId", (Object)this.entityId);
        final ExecutionEntity execution = this.commandContext.getExecutionManager().findExecutionById(this.entityId);
        EnsureUtil.ensureNotNull("execution " + this.entityId + " doesn't exist", "execution", execution);
        this.checkRemoveExecutionVariables(execution);
        return execution;
    }
    
    @Override
    protected ExecutionEntity getContextExecution() {
        return this.getEntity();
    }
    
    @Override
    protected void logVariableOperation(final AbstractVariableScope scope) {
        final ExecutionEntity execution = (ExecutionEntity)scope;
        this.commandContext.getOperationLogManager().logVariableOperation(this.getLogEntryOperation(), execution.getId(), null, PropertyChange.EMPTY_CHANGE);
    }
    
    protected void checkRemoveExecutionVariables(final ExecutionEntity execution) {
        for (final CommandChecker checker : this.commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkUpdateProcessInstanceVariables(execution);
        }
    }
}
