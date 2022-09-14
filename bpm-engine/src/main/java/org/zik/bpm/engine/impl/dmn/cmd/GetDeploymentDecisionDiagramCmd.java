// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.cmd;

import org.zik.bpm.engine.repository.DecisionDefinition;
import org.zik.bpm.engine.impl.cmd.GetDeploymentResourceCmd;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import java.io.InputStream;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetDeploymentDecisionDiagramCmd implements Command<InputStream>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String decisionDefinitionId;
    
    public GetDeploymentDecisionDiagramCmd(final String decisionDefinitionId) {
        this.decisionDefinitionId = decisionDefinitionId;
    }
    
    @Override
    public InputStream execute(final CommandContext commandContext) {
        final DecisionDefinition decisionDefinition = new GetDeploymentDecisionDefinitionCmd(this.decisionDefinitionId).execute(commandContext);
        final String deploymentId = decisionDefinition.getDeploymentId();
        final String resourceName = decisionDefinition.getDiagramResourceName();
        if (resourceName != null) {
            return commandContext.runWithoutAuthorization((Command<InputStream>)new GetDeploymentResourceCmd(deploymentId, resourceName));
        }
        return null;
    }
}
