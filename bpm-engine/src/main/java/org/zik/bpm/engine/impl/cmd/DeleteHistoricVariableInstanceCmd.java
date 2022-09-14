// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.exception.NullValueException;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteHistoricVariableInstanceCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    private String variableInstanceId;
    
    public DeleteHistoricVariableInstanceCmd(final String variableInstanceId) {
        this.variableInstanceId = variableInstanceId;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "variableInstanceId", this.variableInstanceId);
        final HistoricVariableInstanceEntity variable = commandContext.getHistoricVariableInstanceManager().findHistoricVariableInstanceByVariableInstanceId(this.variableInstanceId);
        EnsureUtil.ensureNotNull(NotFoundException.class, "No historic variable instance found with id: " + this.variableInstanceId, "variable", variable);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkDeleteHistoricVariableInstance(variable);
        }
        commandContext.getHistoricDetailManager().deleteHistoricDetailsByVariableInstanceId(this.variableInstanceId);
        commandContext.getHistoricVariableInstanceManager().deleteHistoricVariableInstanceByVariableInstanceId(this.variableInstanceId);
        ResourceDefinitionEntity<?> definition = null;
        try {
            if (variable.getProcessDefinitionId() != null) {
                definition = commandContext.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(variable.getProcessDefinitionId());
            }
            else if (variable.getCaseDefinitionId() != null) {
                definition = commandContext.getProcessEngineConfiguration().getDeploymentCache().findDeployedCaseDefinitionById(variable.getCaseDefinitionId());
            }
        }
        catch (NullValueException ex) {}
        commandContext.getOperationLogManager().logHistoricVariableOperation("DeleteHistory", variable, definition, new PropertyChange("name", null, variable.getName()));
        return null;
    }
}
