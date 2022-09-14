// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Collection;
import java.io.Serializable;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetTaskVariablesCmd implements Command<VariableMap>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String taskId;
    protected Collection<String> variableNames;
    protected boolean isLocal;
    protected boolean deserializeValues;
    
    public GetTaskVariablesCmd(final String taskId, final Collection<String> variableNames, final boolean isLocal, final boolean deserializeValues) {
        this.taskId = taskId;
        this.variableNames = variableNames;
        this.isLocal = isLocal;
        this.deserializeValues = deserializeValues;
    }
    
    @Override
    public VariableMap execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("taskId", (Object)this.taskId);
        final TaskEntity task = Context.getCommandContext().getTaskManager().findTaskById(this.taskId);
        EnsureUtil.ensureNotNull("task " + this.taskId + " doesn't exist", "task", task);
        this.checkGetTaskVariables(task, commandContext);
        final VariableMapImpl variables = new VariableMapImpl();
        task.collectVariables(variables, this.variableNames, this.isLocal, this.deserializeValues);
        return (VariableMap)variables;
    }
    
    protected void checkGetTaskVariables(final TaskEntity task, final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadTaskVariable(task);
        }
    }
}
