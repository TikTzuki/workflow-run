// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.repository.DecisionRequirementsDefinition;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetDeploymentDecisionRequirementsDefinitionCmd implements Command<DecisionRequirementsDefinition>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String decisionRequirementsDefinitionId;
    
    public GetDeploymentDecisionRequirementsDefinitionCmd(final String decisionRequirementsDefinitionId) {
        this.decisionRequirementsDefinitionId = decisionRequirementsDefinitionId;
    }
    
    @Override
    public DecisionRequirementsDefinition execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("decisionRequirementsDefinitionId", (Object)this.decisionRequirementsDefinitionId);
        final DeploymentCache deploymentCache = Context.getProcessEngineConfiguration().getDeploymentCache();
        final DecisionRequirementsDefinitionEntity decisionRequirementsDefinition = deploymentCache.findDeployedDecisionRequirementsDefinitionById(this.decisionRequirementsDefinitionId);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadDecisionRequirementsDefinition(decisionRequirementsDefinition);
        }
        return decisionRequirementsDefinition;
    }
}
