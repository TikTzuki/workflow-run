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
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetCaseExecutionVariableTypedCmd implements Command<TypedValue>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String caseExecutionId;
    protected String variableName;
    protected boolean isLocal;
    protected boolean deserializeValue;
    
    public GetCaseExecutionVariableTypedCmd(final String caseExecutionId, final String variableName, final boolean isLocal, final boolean deserializeValue) {
        this.caseExecutionId = caseExecutionId;
        this.variableName = variableName;
        this.isLocal = isLocal;
        this.deserializeValue = deserializeValue;
    }
    
    @Override
    public TypedValue execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("caseExecutionId", (Object)this.caseExecutionId);
        EnsureUtil.ensureNotNull("variableName", (Object)this.variableName);
        final CaseExecutionEntity caseExecution = commandContext.getCaseExecutionManager().findCaseExecutionById(this.caseExecutionId);
        EnsureUtil.ensureNotNull(CaseExecutionNotFoundException.class, "case execution " + this.caseExecutionId + " doesn't exist", "caseExecution", caseExecution);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadCaseInstance(caseExecution);
        }
        TypedValue value;
        if (this.isLocal) {
            value = caseExecution.getVariableLocalTyped(this.variableName, this.deserializeValue);
        }
        else {
            value = caseExecution.getVariableTyped(this.variableName, this.deserializeValue);
        }
        return value;
    }
}
