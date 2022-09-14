// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.cmd;

import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.cmmn.CaseExecutionCommandBuilderImpl;
import java.util.Collection;
import java.util.Map;

public class ManualStartCaseExecutionCmd extends StateTransitionCaseExecutionCmd
{
    private static final long serialVersionUID = 1L;
    
    public ManualStartCaseExecutionCmd(final String caseExecutionId, final Map<String, Object> variables, final Map<String, Object> variablesLocal, final Collection<String> variableDeletions, final Collection<String> variableLocalDeletions) {
        super(caseExecutionId, variables, variablesLocal, variableDeletions, variableLocalDeletions);
    }
    
    public ManualStartCaseExecutionCmd(final CaseExecutionCommandBuilderImpl builder) {
        super(builder);
    }
    
    @Override
    protected void performStateTransition(final CommandContext commandContext, final CaseExecutionEntity caseExecution) {
        caseExecution.manualStart();
    }
}
