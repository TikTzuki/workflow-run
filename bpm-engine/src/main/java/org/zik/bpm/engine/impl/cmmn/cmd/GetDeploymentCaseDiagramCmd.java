// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.zik.bpm.engine.impl.cmd.GetDeploymentResourceCmd;
import org.zik.bpm.engine.repository.CaseDefinition;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.ProcessEngineException;
import java.io.Serializable;
import java.io.InputStream;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetDeploymentCaseDiagramCmd implements Command<InputStream>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String caseDefinitionId;
    
    public GetDeploymentCaseDiagramCmd(final String caseDefinitionId) {
        if (caseDefinitionId == null || caseDefinitionId.length() < 1) {
            throw new ProcessEngineException("The case definition id is mandatory, but '" + caseDefinitionId + "' has been provided.");
        }
        this.caseDefinitionId = caseDefinitionId;
    }
    
    @Override
    public InputStream execute(final CommandContext commandContext) {
        final CaseDefinitionEntity caseDefinition = Context.getProcessEngineConfiguration().getDeploymentCache().findDeployedCaseDefinitionById(this.caseDefinitionId);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadCaseDefinition(caseDefinition);
        }
        final String deploymentId = caseDefinition.getDeploymentId();
        final String resourceName = caseDefinition.getDiagramResourceName();
        InputStream caseDiagramStream = null;
        if (resourceName != null) {
            caseDiagramStream = commandContext.runWithoutAuthorization((Command<InputStream>)new GetDeploymentResourceCmd(deploymentId, resourceName));
        }
        return caseDiagramStream;
    }
}
