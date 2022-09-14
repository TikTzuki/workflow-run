// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.cmd;

import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.cmmn.CaseExecutionCommandBuilderImpl;
import java.util.Collection;
import java.util.Map;

public abstract class StateTransitionCaseExecutionCmd extends CaseExecutionVariableCmd
{
    private static final long serialVersionUID = 1L;
    
    public StateTransitionCaseExecutionCmd(final String caseExecutionId, final Map<String, Object> variables, final Map<String, Object> variablesLocal, final Collection<String> variableDeletions, final Collection<String> variableLocalDeletions) {
        super(caseExecutionId, variables, variablesLocal, variableDeletions, variableLocalDeletions);
    }
    
    public StateTransitionCaseExecutionCmd(final CaseExecutionCommandBuilderImpl builder) {
        super(builder);
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        super.execute(commandContext);
        final CaseExecutionEntity caseExecution = this.getCaseExecution();
        this.performStateTransition(commandContext, caseExecution);
        return null;
    }
    
    protected abstract void performStateTransition(final CommandContext p0, final CaseExecutionEntity p1);
}
