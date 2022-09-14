// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.bpmn.diagram.ProcessDiagramLayoutFactory;
import java.io.InputStream;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.ProcessEngineException;
import java.io.Serializable;
import org.zik.bpm.engine.repository.DiagramLayout;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetDeploymentProcessDiagramLayoutCmd implements Command<DiagramLayout>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String processDefinitionId;
    
    public GetDeploymentProcessDiagramLayoutCmd(final String processDefinitionId) {
        if (processDefinitionId == null || processDefinitionId.length() < 1) {
            throw new ProcessEngineException("The process definition id is mandatory, but '" + processDefinitionId + "' has been provided.");
        }
        this.processDefinitionId = processDefinitionId;
    }
    
    @Override
    public DiagramLayout execute(final CommandContext commandContext) {
        final ProcessDefinitionEntity processDefinition = Context.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(this.processDefinitionId);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadProcessDefinition(processDefinition);
        }
        final InputStream processModelStream = commandContext.runWithoutAuthorization((Command<InputStream>)new GetDeploymentProcessModelCmd(this.processDefinitionId));
        final InputStream processDiagramStream = commandContext.runWithoutAuthorization((Command<InputStream>)new GetDeploymentProcessDiagramCmd(this.processDefinitionId));
        return new ProcessDiagramLayoutFactory().getProcessDiagramLayout(processModelStream, processDiagramStream);
    }
}
