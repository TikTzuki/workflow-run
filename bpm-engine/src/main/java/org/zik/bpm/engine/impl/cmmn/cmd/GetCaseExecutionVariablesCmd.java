// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import org.zik.bpm.engine.runtime.CaseExecution;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.cmmn.CaseExecutionNotFoundException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Collection;
import java.io.Serializable;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetCaseExecutionVariablesCmd implements Command<VariableMap>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String caseExecutionId;
    protected Collection<String> variableNames;
    protected boolean isLocal;
    protected boolean deserializeValues;
    
    public GetCaseExecutionVariablesCmd(final String caseExecutionId, final Collection<String> variableNames, final boolean isLocal, final boolean deserializeValues) {
        this.caseExecutionId = caseExecutionId;
        this.variableNames = variableNames;
        this.isLocal = isLocal;
        this.deserializeValues = deserializeValues;
    }
    
    @Override
    public VariableMap execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("caseExecutionId", (Object)this.caseExecutionId);
        final CaseExecutionEntity caseExecution = commandContext.getCaseExecutionManager().findCaseExecutionById(this.caseExecutionId);
        EnsureUtil.ensureNotNull(CaseExecutionNotFoundException.class, "case execution " + this.caseExecutionId + " doesn't exist", "caseExecution", caseExecution);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadCaseInstance(caseExecution);
        }
        final VariableMapImpl result = new VariableMapImpl();
        caseExecution.collectVariables(result, this.variableNames, this.isLocal, this.deserializeValues);
        return (VariableMap)result;
    }
}
