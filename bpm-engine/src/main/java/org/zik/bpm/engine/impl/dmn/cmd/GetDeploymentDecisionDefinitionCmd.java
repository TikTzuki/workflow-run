// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.repository.DecisionDefinition;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetDeploymentDecisionDefinitionCmd implements Command<DecisionDefinition>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String decisionDefinitionId;
    
    public GetDeploymentDecisionDefinitionCmd(final String decisionDefinitionId) {
        this.decisionDefinitionId = decisionDefinitionId;
    }
    
    @Override
    public DecisionDefinition execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("decisionDefinitionId", (Object)this.decisionDefinitionId);
        final DeploymentCache deploymentCache = Context.getProcessEngineConfiguration().getDeploymentCache();
        final DecisionDefinitionEntity decisionDefinition = deploymentCache.findDeployedDecisionDefinitionById(this.decisionDefinitionId);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadDecisionDefinition(decisionDefinition);
        }
        return decisionDefinition;
    }
}
