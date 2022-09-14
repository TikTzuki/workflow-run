// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd.batch;

import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.batch.message.MessageCorrelationBatchConfiguration;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.HistoricProcessInstanceQueryImpl;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.util.ImmutablePair;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import org.zik.bpm.engine.impl.ProcessInstanceQueryImpl;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import org.zik.bpm.engine.impl.batch.BatchElementConfiguration;
import org.zik.bpm.engine.impl.core.variable.VariableUtil;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.BatchPermissions;
import org.zik.bpm.engine.impl.batch.builder.BatchBuilder;
import java.util.Collection;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.MessageCorrelationAsyncBuilderImpl;
import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;
import org.zik.bpm.engine.runtime.ProcessInstanceQuery;
import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.interceptor.Command;

public class CorrelateAllMessageBatchCmd implements Command<Batch>
{
    protected String messageName;
    protected Map<String, Object> variables;
    protected List<String> processInstanceIds;
    protected ProcessInstanceQuery processInstanceQuery;
    protected HistoricProcessInstanceQuery historicProcessInstanceQuery;
    
    public CorrelateAllMessageBatchCmd(final MessageCorrelationAsyncBuilderImpl asyncBuilder) {
        this.messageName = asyncBuilder.getMessageName();
        this.variables = asyncBuilder.getPayloadProcessInstanceVariables();
        this.processInstanceIds = asyncBuilder.getProcessInstanceIds();
        this.processInstanceQuery = asyncBuilder.getProcessInstanceQuery();
        this.historicProcessInstanceQuery = asyncBuilder.getHistoricProcessInstanceQuery();
    }
    
    @Override
    public Batch execute(final CommandContext commandContext) {
        EnsureUtil.ensureAtLeastOneNotNull("No process instances found.", this.processInstanceIds, this.processInstanceQuery, this.historicProcessInstanceQuery);
        final BatchElementConfiguration elementConfiguration = this.collectProcessInstanceIds(commandContext);
        final List<String> ids = elementConfiguration.getIds();
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "Process instance ids cannot be empty", "process instance ids", ids);
        final Batch batch = new BatchBuilder(commandContext).type("correlate-message").config(this.getConfiguration(elementConfiguration)).permission(BatchPermissions.CREATE_BATCH_CORRELATE_MESSAGE).operationLogHandler(this::writeUserOperationLog).build();
        if (this.variables != null) {
            VariableUtil.setVariablesByBatchId(this.variables, batch.getId());
        }
        return batch;
    }
    
    protected BatchElementConfiguration collectProcessInstanceIds(final CommandContext commandContext) {
        final BatchElementConfiguration elementConfiguration = new BatchElementConfiguration();
        if (!CollectionUtil.isEmpty(this.processInstanceIds)) {
            final ProcessInstanceQueryImpl query = new ProcessInstanceQueryImpl();
            query.processInstanceIds(new HashSet<String>(this.processInstanceIds));
            final BatchElementConfiguration batchElementConfiguration = elementConfiguration;
            final ProcessInstanceQueryImpl obj = query;
            Objects.requireNonNull(obj);
            batchElementConfiguration.addDeploymentMappings(commandContext.runWithoutAuthorization((Callable<List<ImmutablePair<String, String>>>)obj::listDeploymentIdMappings), this.processInstanceIds);
        }
        if (this.processInstanceQuery != null) {
            elementConfiguration.addDeploymentMappings(((ProcessInstanceQueryImpl)this.processInstanceQuery).listDeploymentIdMappings());
        }
        if (this.historicProcessInstanceQuery != null) {
            elementConfiguration.addDeploymentMappings(((HistoricProcessInstanceQueryImpl)this.historicProcessInstanceQuery).listDeploymentIdMappings());
        }
        return elementConfiguration;
    }
    
    protected BatchConfiguration getConfiguration(final BatchElementConfiguration elementConfiguration) {
        return new MessageCorrelationBatchConfiguration(elementConfiguration.getIds(), elementConfiguration.getMappings(), this.messageName);
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext, final int instancesCount) {
        final List<PropertyChange> propChanges = new ArrayList<PropertyChange>();
        propChanges.add(new PropertyChange("messageName", null, this.messageName));
        propChanges.add(new PropertyChange("nrOfInstances", null, instancesCount));
        propChanges.add(new PropertyChange("nrOfVariables", null, (this.variables == null) ? 0 : this.variables.size()));
        propChanges.add(new PropertyChange("async", null, true));
        commandContext.getOperationLogManager().logProcessInstanceOperation("CorrelateMessage", propChanges);
    }
}
