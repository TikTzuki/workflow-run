// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd.batch.variables;

import org.zik.bpm.engine.impl.HistoricProcessInstanceQueryImpl;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.util.ImmutablePair;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import org.zik.bpm.engine.impl.ProcessInstanceQueryImpl;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
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
import java.util.Map;
import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;
import org.zik.bpm.engine.runtime.ProcessInstanceQuery;
import java.util.List;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SetVariablesToProcessInstancesBatchCmd implements Command<Batch>
{
    protected List<String> processInstanceIds;
    protected ProcessInstanceQuery processInstanceQuery;
    protected HistoricProcessInstanceQuery historicProcessInstanceQuery;
    protected Map<String, ?> variables;
    
    public SetVariablesToProcessInstancesBatchCmd(final List<String> processInstanceIds, final ProcessInstanceQuery processInstanceQuery, final HistoricProcessInstanceQuery historicProcessInstanceQuery, final Map<String, ?> variables) {
        this.processInstanceIds = processInstanceIds;
        this.processInstanceQuery = processInstanceQuery;
        this.historicProcessInstanceQuery = historicProcessInstanceQuery;
        this.variables = variables;
    }
    
    @Override
    public Batch execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("variables", this.variables);
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "variables", this.variables);
        EnsureUtil.ensureAtLeastOneNotNull("No process instances found.", this.processInstanceIds, this.processInstanceQuery, this.historicProcessInstanceQuery);
        final BatchElementConfiguration elementConfiguration = this.collectProcessInstanceIds(commandContext);
        final List<String> ids = elementConfiguration.getIds();
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "processInstanceIds", ids);
        final BatchConfiguration configuration = this.getConfiguration(elementConfiguration);
        final Batch batch = new BatchBuilder(commandContext).type("set-variables").config(configuration).permission(BatchPermissions.CREATE_BATCH_SET_VARIABLES).operationLogHandler(this::writeUserOperationLog).build();
        final String batchId = batch.getId();
        VariableUtil.setVariablesByBatchId(this.variables, batchId);
        return batch;
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext, final int instancesCount) {
        final List<PropertyChange> propChanges = new ArrayList<PropertyChange>();
        final int variablesCount = this.variables.size();
        propChanges.add(new PropertyChange("nrOfInstances", null, instancesCount));
        propChanges.add(new PropertyChange("nrOfVariables", null, variablesCount));
        propChanges.add(new PropertyChange("async", null, true));
        commandContext.getOperationLogManager().logProcessInstanceOperation("SetVariables", propChanges);
    }
    
    public BatchConfiguration getConfiguration(final BatchElementConfiguration elementConfiguration) {
        final DeploymentMappings mappings = elementConfiguration.getMappings();
        final List<String> ids = elementConfiguration.getIds();
        return new BatchConfiguration(ids, mappings);
    }
    
    protected BatchElementConfiguration collectProcessInstanceIds(final CommandContext commandContext) {
        final BatchElementConfiguration elementConfiguration = new BatchElementConfiguration();
        if (!CollectionUtil.isEmpty(this.processInstanceIds)) {
            final ProcessInstanceQueryImpl query = new ProcessInstanceQueryImpl();
            query.processInstanceIds(new HashSet<String>(this.processInstanceIds));
            final ProcessInstanceQueryImpl obj = query;
            Objects.requireNonNull(obj);
            final List<ImmutablePair<String, String>> mappings = commandContext.runWithoutAuthorization((Callable<List<ImmutablePair<String, String>>>)obj::listDeploymentIdMappings);
            elementConfiguration.addDeploymentMappings(mappings);
        }
        final ProcessInstanceQueryImpl processInstanceQuery = (ProcessInstanceQueryImpl)this.processInstanceQuery;
        if (processInstanceQuery != null) {
            final List<ImmutablePair<String, String>> mappings = processInstanceQuery.listDeploymentIdMappings();
            elementConfiguration.addDeploymentMappings(mappings);
        }
        final HistoricProcessInstanceQueryImpl historicProcessInstanceQuery = (HistoricProcessInstanceQueryImpl)this.historicProcessInstanceQuery;
        if (historicProcessInstanceQuery != null) {
            historicProcessInstanceQuery.unfinished();
            final List<ImmutablePair<String, String>> mappings2 = historicProcessInstanceQuery.listDeploymentIdMappings();
            elementConfiguration.addDeploymentMappings(mappings2);
        }
        return elementConfiguration;
    }
}
