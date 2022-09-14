// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.repository.CaseDefinition;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetDeploymentCaseDefinitionCmd implements Command<CaseDefinition>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String caseDefinitionId;
    
    public GetDeploymentCaseDefinitionCmd(final String caseDefinitionId) {
        this.caseDefinitionId = caseDefinitionId;
    }
    
    @Override
    public CaseDefinition execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("caseDefinitionId", (Object)this.caseDefinitionId);
        final CaseDefinitionEntity caseDefinition = Context.getProcessEngineConfiguration().getDeploymentCache().findDeployedCaseDefinitionById(this.caseDefinitionId);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadCaseDefinition(caseDefinition);
        }
        return caseDefinition;
    }
}
