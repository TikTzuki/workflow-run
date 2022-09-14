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
import java.util.Map;

public class SetExecutionVariablesCmd extends AbstractSetVariableCmd
{
    private static final long serialVersionUID = 1L;
    
    public SetExecutionVariablesCmd(final String executionId, final Map<String, ?> variables, final boolean isLocal, final boolean skipJavaSerializationFormatCheck) {
        super(executionId, variables, isLocal, skipJavaSerializationFormatCheck);
    }
    
    public SetExecutionVariablesCmd(final String executionId, final Map<String, ?> variables, final boolean isLocal) {
        super(executionId, variables, isLocal, false);
    }
    
    @Override
    protected ExecutionEntity getEntity() {
        EnsureUtil.ensureNotNull("executionId", (Object)this.entityId);
        final ExecutionEntity execution = this.commandContext.getExecutionManager().findExecutionById(this.entityId);
        EnsureUtil.ensureNotNull("execution " + this.entityId + " doesn't exist", "execution", execution);
        this.checkSetExecutionVariables(execution);
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
    
    protected void checkSetExecutionVariables(final ExecutionEntity execution) {
        for (final CommandChecker checker : this.commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkUpdateProcessInstanceVariables(execution);
        }
    }
}
