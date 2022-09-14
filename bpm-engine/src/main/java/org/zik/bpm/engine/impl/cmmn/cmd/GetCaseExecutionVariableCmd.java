// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.runtime.CaseExecution;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.cmmn.CaseExecutionNotFoundException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetCaseExecutionVariableCmd implements Command<Object>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String caseExecutionId;
    protected String variableName;
    protected boolean isLocal;
    
    public GetCaseExecutionVariableCmd(final String caseExecutionId, final String variableName, final boolean isLocal) {
        this.caseExecutionId = caseExecutionId;
        this.variableName = variableName;
        this.isLocal = isLocal;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("caseExecutionId", (Object)this.caseExecutionId);
        EnsureUtil.ensureNotNull("variableName", (Object)this.variableName);
        final CaseExecutionEntity caseExecution = commandContext.getCaseExecutionManager().findCaseExecutionById(this.caseExecutionId);
        EnsureUtil.ensureNotNull(CaseExecutionNotFoundException.class, "case execution " + this.caseExecutionId + " doesn't exist", "caseExecution", caseExecution);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadCaseInstance(caseExecution);
        }
        Object value;
        if (this.isLocal) {
            value = caseExecution.getVariableLocal(this.variableName);
        }
        else {
            value = caseExecution.getVariable(this.variableName);
        }
        return value;
    }
}
