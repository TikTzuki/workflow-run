// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd.batch;

import org.zik.bpm.engine.impl.batch.deletion.DeleteProcessInstanceBatchConfiguration;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.HistoricProcessInstanceQueryImpl;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.util.ImmutablePair;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import org.zik.bpm.engine.impl.ProcessInstanceQueryImpl;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import org.zik.bpm.engine.impl.batch.BatchElementConfiguration;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.BatchPermissions;
import org.zik.bpm.engine.impl.batch.builder.BatchBuilder;
import java.util.Collection;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;
import org.zik.bpm.engine.runtime.ProcessInstanceQuery;
import java.util.List;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteProcessInstanceBatchCmd implements Command<Batch>
{
    protected final String deleteReason;
    protected List<String> processInstanceIds;
    protected ProcessInstanceQuery processInstanceQuery;
    protected HistoricProcessInstanceQuery historicProcessInstanceQuery;
    protected boolean skipCustomListeners;
    protected boolean skipSubprocesses;
    
    public DeleteProcessInstanceBatchCmd(final List<String> processInstances, final ProcessInstanceQuery processInstanceQuery, final HistoricProcessInstanceQuery historicProcessInstanceQuery, final String deleteReason, final boolean skipCustomListeners, final boolean skipSubprocesses) {
        this.processInstanceIds = processInstances;
        this.processInstanceQuery = processInstanceQuery;
        this.historicProcessInstanceQuery = historicProcessInstanceQuery;
        this.deleteReason = deleteReason;
        this.skipCustomListeners = skipCustomListeners;
        this.skipSubprocesses = skipSubprocesses;
    }
    
    @Override
    public Batch execute(final CommandContext commandContext) {
        final BatchElementConfiguration elementConfiguration = this.collectProcessInstanceIds(commandContext);
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "processInstanceIds", elementConfiguration.getIds());
        return new BatchBuilder(commandContext).type("instance-deletion").config(this.getConfiguration(elementConfiguration)).permission(BatchPermissions.CREATE_BATCH_DELETE_RUNNING_PROCESS_INSTANCES).operationLogHandler(this::writeUserOperationLog).build();
    }
    
    protected BatchElementConfiguration collectProcessInstanceIds(final CommandContext commandContext) {
        final BatchElementConfiguration elementConfiguration = new BatchElementConfiguration();
        final List<String> processInstanceIds = this.getProcessInstanceIds();
        if (!CollectionUtil.isEmpty(processInstanceIds)) {
            final ProcessInstanceQueryImpl query = new ProcessInstanceQueryImpl();
            query.processInstanceIds(new HashSet<String>(processInstanceIds));
            final BatchElementConfiguration batchElementConfiguration = elementConfiguration;
            final ProcessInstanceQueryImpl obj = query;
            Objects.requireNonNull(obj);
            batchElementConfiguration.addDeploymentMappings(commandContext.runWithoutAuthorization((Callable<List<ImmutablePair<String, String>>>)obj::listDeploymentIdMappings), processInstanceIds);
        }
        final ProcessInstanceQueryImpl processInstanceQuery = (ProcessInstanceQueryImpl)this.processInstanceQuery;
        if (processInstanceQuery != null) {
            elementConfiguration.addDeploymentMappings(processInstanceQuery.listDeploymentIdMappings());
        }
        final HistoricProcessInstanceQueryImpl historicProcessInstanceQuery = (HistoricProcessInstanceQueryImpl)this.historicProcessInstanceQuery;
        if (historicProcessInstanceQuery != null) {
            elementConfiguration.addDeploymentMappings(historicProcessInstanceQuery.listDeploymentIdMappings());
        }
        return elementConfiguration;
    }
    
    public List<String> getProcessInstanceIds() {
        return this.processInstanceIds;
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext, final int numInstances) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(new PropertyChange("nrOfInstances", null, numInstances));
        propertyChanges.add(new PropertyChange("async", null, true));
        propertyChanges.add(new PropertyChange("deleteReason", null, this.deleteReason));
        commandContext.getOperationLogManager().logProcessInstanceOperation("Delete", null, null, null, propertyChanges);
    }
    
    public BatchConfiguration getConfiguration(final BatchElementConfiguration elementConfiguration) {
        return new DeleteProcessInstanceBatchConfiguration(elementConfiguration.getIds(), elementConfiguration.getMappings(), this.deleteReason, this.skipCustomListeners, this.skipSubprocesses, false);
    }
}
