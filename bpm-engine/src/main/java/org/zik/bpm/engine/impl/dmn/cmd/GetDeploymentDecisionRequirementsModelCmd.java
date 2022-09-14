// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.cmd;

import org.zik.bpm.engine.repository.DecisionRequirementsDefinition;
import org.zik.bpm.engine.impl.cmd.GetDeploymentResourceCmd;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import java.io.InputStream;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetDeploymentDecisionRequirementsModelCmd implements Command<InputStream>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String decisionRequirementsDefinitionId;
    
    public GetDeploymentDecisionRequirementsModelCmd(final String decisionRequirementsDefinitionId) {
        this.decisionRequirementsDefinitionId = decisionRequirementsDefinitionId;
    }
    
    @Override
    public InputStream execute(final CommandContext commandContext) {
        final DecisionRequirementsDefinition decisionRequirementsDefinition = new GetDeploymentDecisionRequirementsDefinitionCmd(this.decisionRequirementsDefinitionId).execute(commandContext);
        final String deploymentId = decisionRequirementsDefinition.getDeploymentId();
        final String resourceName = decisionRequirementsDefinition.getResourceName();
        return commandContext.runWithoutAuthorization((Command<InputStream>)new GetDeploymentResourceCmd(deploymentId, resourceName));
    }
}
