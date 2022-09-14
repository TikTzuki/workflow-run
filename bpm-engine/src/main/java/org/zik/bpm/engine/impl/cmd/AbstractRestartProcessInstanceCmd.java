// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.List;
import java.util.Set;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.HistoricProcessInstanceQueryImpl;
import java.util.HashSet;
import java.util.Collection;
import org.zik.bpm.engine.impl.RestartProcessInstanceBuilderImpl;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.interceptor.Command;

public abstract class AbstractRestartProcessInstanceCmd<T> implements Command<T>
{
    protected CommandExecutor commandExecutor;
    protected RestartProcessInstanceBuilderImpl builder;
    
    public AbstractRestartProcessInstanceCmd(final CommandExecutor commandExecutor, final RestartProcessInstanceBuilderImpl builder) {
        this.commandExecutor = commandExecutor;
        this.builder = builder;
    }
    
    protected Collection<String> collectProcessInstanceIds() {
        final Set<String> collectedProcessInstanceIds = new HashSet<String>();
        final List<String> processInstanceIds = this.builder.getProcessInstanceIds();
        if (processInstanceIds != null) {
            collectedProcessInstanceIds.addAll(processInstanceIds);
        }
        final HistoricProcessInstanceQueryImpl historicProcessInstanceQuery = (HistoricProcessInstanceQueryImpl)this.builder.getHistoricProcessInstanceQuery();
        if (historicProcessInstanceQuery != null) {
            collectedProcessInstanceIds.addAll(historicProcessInstanceQuery.listIds());
        }
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "processInstanceIds", collectedProcessInstanceIds);
        return collectedProcessInstanceIds;
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext, final ProcessDefinition processDefinition, final int numInstances, final boolean async) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(new PropertyChange("nrOfInstances", null, numInstances));
        propertyChanges.add(new PropertyChange("async", null, async));
        commandContext.getOperationLogManager().logProcessInstanceOperation("RestartProcessInstance", null, processDefinition.getId(), processDefinition.getKey(), propertyChanges);
    }
    
    protected ProcessDefinitionEntity getProcessDefinition(final CommandContext commandContext, final String processDefinitionId) {
        return commandContext.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(processDefinitionId);
    }
}
