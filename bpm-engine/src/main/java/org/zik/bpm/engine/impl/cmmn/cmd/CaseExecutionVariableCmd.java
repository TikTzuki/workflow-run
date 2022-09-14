// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.runtime.CaseExecution;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.cmmn.CaseExecutionNotFoundException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.cmmn.CaseExecutionCommandBuilderImpl;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import java.util.Collection;
import java.util.Map;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class CaseExecutionVariableCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String caseExecutionId;
    protected Map<String, Object> variables;
    protected Map<String, Object> variablesLocal;
    protected Collection<String> variablesDeletions;
    protected Collection<String> variablesLocalDeletions;
    protected CaseExecutionEntity caseExecution;
    
    public CaseExecutionVariableCmd(final String caseExecutionId, final Map<String, Object> variables, final Map<String, Object> variablesLocal, final Collection<String> variablesDeletions, final Collection<String> variablesLocalDeletions) {
        this.caseExecutionId = caseExecutionId;
        this.variables = variables;
        this.variablesLocal = variablesLocal;
        this.variablesDeletions = variablesDeletions;
        this.variablesLocalDeletions = variablesLocalDeletions;
    }
    
    public CaseExecutionVariableCmd(final CaseExecutionCommandBuilderImpl builder) {
        this(builder.getCaseExecutionId(), (Map<String, Object>)builder.getVariables(), (Map<String, Object>)builder.getVariablesLocal(), builder.getVariableDeletions(), builder.getVariableLocalDeletions());
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("caseExecutionId", (Object)this.caseExecutionId);
        this.caseExecution = commandContext.getCaseExecutionManager().findCaseExecutionById(this.caseExecutionId);
        EnsureUtil.ensureNotNull(CaseExecutionNotFoundException.class, "There does not exist any case execution with id: '" + this.caseExecutionId + "'", "caseExecution", this.caseExecution);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkUpdateCaseInstance(this.caseExecution);
        }
        if (this.variablesDeletions != null && !this.variablesDeletions.isEmpty()) {
            this.caseExecution.removeVariables(this.variablesDeletions);
        }
        if (this.variablesLocalDeletions != null && !this.variablesLocalDeletions.isEmpty()) {
            this.caseExecution.removeVariablesLocal(this.variablesLocalDeletions);
        }
        if (this.variables != null && !this.variables.isEmpty()) {
            this.caseExecution.setVariables(this.variables);
        }
        if (this.variablesLocal != null && !this.variablesLocal.isEmpty()) {
            this.caseExecution.setVariablesLocal(this.variablesLocal);
        }
        return null;
    }
    
    public CaseExecutionEntity getCaseExecution() {
        return this.caseExecution;
    }
}
