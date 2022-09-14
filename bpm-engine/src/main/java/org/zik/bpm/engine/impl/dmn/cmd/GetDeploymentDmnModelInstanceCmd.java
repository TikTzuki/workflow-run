// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.dmn.DmnModelInstanceNotFoundException;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetDeploymentDmnModelInstanceCmd implements Command<DmnModelInstance>
{
    protected String decisionDefinitionId;
    
    public GetDeploymentDmnModelInstanceCmd(final String decisionDefinitionId) {
        this.decisionDefinitionId = decisionDefinitionId;
    }
    
    @Override
    public DmnModelInstance execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("decisionDefinitionId", (Object)this.decisionDefinitionId);
        final DeploymentCache deploymentCache = Context.getProcessEngineConfiguration().getDeploymentCache();
        final DecisionDefinitionEntity decisionDefinition = deploymentCache.findDeployedDecisionDefinitionById(this.decisionDefinitionId);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadDecisionDefinition(decisionDefinition);
        }
        final DmnModelInstance modelInstance = deploymentCache.findDmnModelInstanceForDecisionDefinition(this.decisionDefinitionId);
        EnsureUtil.ensureNotNull(DmnModelInstanceNotFoundException.class, "No DMN model instance found for decision definition id " + this.decisionDefinitionId, "modelInstance", modelInstance);
        return modelInstance;
    }
}
