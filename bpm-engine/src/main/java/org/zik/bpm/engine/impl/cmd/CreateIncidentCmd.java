// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.List;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.runtime.Incident;
import org.zik.bpm.engine.impl.interceptor.Command;

public class CreateIncidentCmd implements Command<Incident>
{
    protected String incidentType;
    protected String executionId;
    protected String configuration;
    protected String message;
    
    public CreateIncidentCmd(final String incidentType, final String executionId, final String configuration, final String message) {
        this.incidentType = incidentType;
        this.executionId = executionId;
        this.configuration = configuration;
        this.message = message;
    }
    
    @Override
    public Incident execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "Execution id cannot be null", "executionId", this.executionId);
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "incidentType", (Object)this.incidentType);
        final ExecutionEntity execution = commandContext.getExecutionManager().findExecutionById(this.executionId);
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "Cannot find an execution with executionId '" + this.executionId + "'", "execution", execution);
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "Execution must be related to an activity", "activity", execution.getActivity());
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkUpdateProcessInstance(execution);
        }
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(new PropertyChange("incidentType", null, this.incidentType));
        propertyChanges.add(new PropertyChange("configuration", null, this.configuration));
        commandContext.getOperationLogManager().logProcessInstanceOperation("CreateIncident", execution.getProcessInstanceId(), execution.getProcessDefinitionId(), null, propertyChanges);
        return execution.createIncident(this.incidentType, this.configuration, this.message);
    }
}
