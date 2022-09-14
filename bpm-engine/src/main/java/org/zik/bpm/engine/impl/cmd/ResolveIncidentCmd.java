// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.runtime.Incident;
import java.util.Collections;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.Command;

public class ResolveIncidentCmd implements Command<Void>
{
    protected String incidentId;
    
    public ResolveIncidentCmd(final String incidentId) {
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "", "incidentId", incidentId);
        this.incidentId = incidentId;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final Incident incident = commandContext.getIncidentManager().findIncidentById(this.incidentId);
        EnsureUtil.ensureNotNull(NotFoundException.class, "Cannot find an incident with id '" + this.incidentId + "'", "incident", incident);
        if (incident.getIncidentType().equals("failedJob") || incident.getIncidentType().equals("failedExternalTask")) {
            throw new BadUserRequestException("Cannot resolve an incident of type " + incident.getIncidentType());
        }
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "", "executionId", incident.getExecutionId());
        final ExecutionEntity execution = commandContext.getExecutionManager().findExecutionById(incident.getExecutionId());
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "Cannot find an execution for an incident with id '" + this.incidentId + "'", "execution", execution);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkUpdateProcessInstance(execution);
        }
        commandContext.getOperationLogManager().logProcessInstanceOperation("Resolve", execution.getProcessInstanceId(), execution.getProcessDefinitionId(), null, Collections.singletonList(new PropertyChange("incidentId", null, this.incidentId)));
        execution.resolveIncident(this.incidentId);
        return null;
    }
}
