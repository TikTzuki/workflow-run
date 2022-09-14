// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.cmd;

import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Collection;
import java.util.Map;
import org.zik.bpm.engine.impl.cmmn.CaseExecutionCommandBuilderImpl;

public class TerminateCaseExecutionCmd extends StateTransitionCaseExecutionCmd
{
    private static final long serialVersionUID = 1L;
    
    public TerminateCaseExecutionCmd(final CaseExecutionCommandBuilderImpl builder) {
        super(builder);
    }
    
    public TerminateCaseExecutionCmd(final String caseExecutionId, final Map<String, Object> variables, final Map<String, Object> variablesLocal, final Collection<String> variableDeletions, final Collection<String> variableLocalDeletions) {
        super(caseExecutionId, variables, variablesLocal, variableDeletions, variableLocalDeletions);
    }
    
    @Override
    protected void performStateTransition(final CommandContext commandContext, final CaseExecutionEntity caseExecution) {
        caseExecution.terminate();
    }
}
