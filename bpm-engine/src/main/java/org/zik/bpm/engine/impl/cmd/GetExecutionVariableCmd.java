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

public class GetExecutionVariableCmd implements Command<Object>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String executionId;
    protected String variableName;
    protected boolean isLocal;
    
    public GetExecutionVariableCmd(final String executionId, final String variableName, final boolean isLocal) {
        this.executionId = executionId;
        this.variableName = variableName;
        this.isLocal = isLocal;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("executionId", (Object)this.executionId);
        EnsureUtil.ensureNotNull("variableName", (Object)this.variableName);
        final ExecutionEntity execution = commandContext.getExecutionManager().findExecutionById(this.executionId);
        EnsureUtil.ensureNotNull("execution " + this.executionId + " doesn't exist", "execution", execution);
        this.checkGetExecutionVariable(execution, commandContext);
        Object value;
        if (this.isLocal) {
            value = execution.getVariableLocal(this.variableName, true);
        }
        else {
            value = execution.getVariable(this.variableName, true);
        }
        return value;
    }
    
    protected void checkGetExecutionVariable(final ExecutionEntity execution, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadProcessInstanceVariable(execution);
        }
    }
}
