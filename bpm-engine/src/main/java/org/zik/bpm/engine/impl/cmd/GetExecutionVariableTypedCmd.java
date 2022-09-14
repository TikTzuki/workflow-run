// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.variable.value.TypedValue;

public class GetExecutionVariableTypedCmd<T extends TypedValue> implements Command<T>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String executionId;
    protected String variableName;
    protected boolean isLocal;
    protected boolean deserializeValue;
    
    public GetExecutionVariableTypedCmd(final String executionId, final String variableName, final boolean isLocal, final boolean deserializeValue) {
        this.executionId = executionId;
        this.variableName = variableName;
        this.isLocal = isLocal;
        this.deserializeValue = deserializeValue;
    }
    
    @Override
    public T execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("executionId", (Object)this.executionId);
        EnsureUtil.ensureNotNull("variableName", (Object)this.variableName);
        final ExecutionEntity execution = commandContext.getExecutionManager().findExecutionById(this.executionId);
        EnsureUtil.ensureNotNull("execution " + this.executionId + " doesn't exist", "execution", execution);
        this.checkGetExecutionVariableTyped(execution, commandContext);
        T value;
        if (this.isLocal) {
            value = execution.getVariableLocalTyped(this.variableName, this.deserializeValue);
        }
        else {
            value = execution.getVariableTyped(this.variableName, this.deserializeValue);
        }
        return value;
    }
    
    public void checkGetExecutionVariableTyped(final ExecutionEntity execution, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadProcessInstanceVariable(execution);
        }
    }
}
