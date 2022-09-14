// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.cmmn.CmmnModelInstanceNotFoundException;
import org.zik.bpm.engine.repository.CaseDefinition;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.model.cmmn.CmmnModelInstance;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetDeploymentCmmnModelInstanceCmd implements Command<CmmnModelInstance>
{
    protected String caseDefinitionId;
    
    public GetDeploymentCmmnModelInstanceCmd(final String caseDefinitionId) {
        this.caseDefinitionId = caseDefinitionId;
    }
    
    @Override
    public CmmnModelInstance execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("caseDefinitionId", (Object)this.caseDefinitionId);
        final ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
        final DeploymentCache deploymentCache = configuration.getDeploymentCache();
        final CaseDefinitionEntity caseDefinition = deploymentCache.findDeployedCaseDefinitionById(this.caseDefinitionId);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadCaseDefinition(caseDefinition);
        }
        final CmmnModelInstance modelInstance = Context.getProcessEngineConfiguration().getDeploymentCache().findCmmnModelInstanceForCaseDefinition(this.caseDefinitionId);
        EnsureUtil.ensureNotNull(CmmnModelInstanceNotFoundException.class, "No CMMN model instance found for case definition id " + this.caseDefinitionId, "modelInstance", modelInstance);
        return modelInstance;
    }
}
