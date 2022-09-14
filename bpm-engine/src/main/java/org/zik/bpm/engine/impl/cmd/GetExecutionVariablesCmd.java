// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Collection;
import java.io.Serializable;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetExecutionVariablesCmd implements Command<VariableMap>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String executionId;
    protected Collection<String> variableNames;
    protected boolean isLocal;
    protected boolean deserializeValues;
    
    public GetExecutionVariablesCmd(final String executionId, final Collection<String> variableNames, final boolean isLocal, final boolean deserializeValues) {
        this.executionId = executionId;
        this.variableNames = variableNames;
        this.isLocal = isLocal;
        this.deserializeValues = deserializeValues;
    }
    
    @Override
    public VariableMap execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("executionId", (Object)this.executionId);
        final ExecutionEntity execution = commandContext.getExecutionManager().findExecutionById(this.executionId);
        EnsureUtil.ensureNotNull("execution " + this.executionId + " doesn't exist", "execution", execution);
        this.checkGetExecutionVariables(execution, commandContext);
        final VariableMapImpl executionVariables = new VariableMapImpl();
        execution.collectVariables(executionVariables, this.variableNames, this.isLocal, this.deserializeValues);
        return (VariableMap)executionVariables;
    }
    
    protected void checkGetExecutionVariables(final ExecutionEntity execution, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadProcessInstanceVariable(execution);
        }
    }
}
