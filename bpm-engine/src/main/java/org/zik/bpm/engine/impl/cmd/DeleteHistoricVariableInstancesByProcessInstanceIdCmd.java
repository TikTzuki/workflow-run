// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.exception.NullValueException;
import java.util.Arrays;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteHistoricVariableInstancesByProcessInstanceIdCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    private String processInstanceId;
    
    public DeleteHistoricVariableInstancesByProcessInstanceIdCmd(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "processInstanceId", this.processInstanceId);
        final HistoricProcessInstanceEntity instance = commandContext.getHistoricProcessInstanceManager().findHistoricProcessInstance(this.processInstanceId);
        EnsureUtil.ensureNotNull(NotFoundException.class, "No historic process instance found with id: " + this.processInstanceId, "instance", instance);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkDeleteHistoricVariableInstancesByProcessInstance(instance);
        }
        commandContext.getHistoricDetailManager().deleteHistoricDetailsByProcessInstanceIds(Arrays.asList(this.processInstanceId));
        commandContext.getHistoricVariableInstanceManager().deleteHistoricVariableInstanceByProcessInstanceIds(Arrays.asList(this.processInstanceId));
        ResourceDefinitionEntity<?> definition = null;
        try {
            definition = commandContext.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(instance.getProcessDefinitionId());
        }
        catch (NullValueException ex) {}
        commandContext.getOperationLogManager().logHistoricVariableOperation("DeleteHistory", instance, definition, PropertyChange.EMPTY_CHANGE);
        return null;
    }
}
