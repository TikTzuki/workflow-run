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
import org.zik.bpm.engine.impl.ProcessInstanceQueryImpl;
import java.util.HashSet;
import java.util.Collection;
import org.zik.bpm.engine.impl.ModificationBuilderImpl;
import org.zik.bpm.engine.impl.interceptor.Command;

public abstract class AbstractModificationCmd<T> implements Command<T>
{
    protected ModificationBuilderImpl builder;
    
    public AbstractModificationCmd(final ModificationBuilderImpl modificationBuilderImpl) {
        this.builder = modificationBuilderImpl;
    }
    
    protected Collection<String> collectProcessInstanceIds() {
        final Set<String> collectedProcessInstanceIds = new HashSet<String>();
        final List<String> processInstanceIds = this.builder.getProcessInstanceIds();
        if (processInstanceIds != null) {
            collectedProcessInstanceIds.addAll(processInstanceIds);
        }
        final ProcessInstanceQueryImpl processInstanceQuery = (ProcessInstanceQueryImpl)this.builder.getProcessInstanceQuery();
        if (processInstanceQuery != null) {
            collectedProcessInstanceIds.addAll(processInstanceQuery.listIds());
        }
        return collectedProcessInstanceIds;
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext, final ProcessDefinition processDefinition, final int numInstances, final boolean async, final String annotation) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(new PropertyChange("nrOfInstances", null, numInstances));
        propertyChanges.add(new PropertyChange("async", null, async));
        commandContext.getOperationLogManager().logProcessInstanceOperation("ModifyProcessInstance", null, processDefinition.getId(), processDefinition.getKey(), propertyChanges, annotation);
    }
    
    protected ProcessDefinitionEntity getProcessDefinition(final CommandContext commandContext, final String processDefinitionId) {
        return commandContext.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(processDefinitionId);
    }
}
