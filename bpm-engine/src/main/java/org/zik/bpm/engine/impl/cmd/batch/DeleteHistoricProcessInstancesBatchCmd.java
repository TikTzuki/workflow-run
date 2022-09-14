// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd.batch;

import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.util.ImmutablePair;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import org.zik.bpm.engine.impl.HistoricProcessInstanceQueryImpl;
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
import java.util.List;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteHistoricProcessInstancesBatchCmd implements Command<Batch>
{
    protected final String deleteReason;
    protected List<String> historicProcessInstanceIds;
    protected HistoricProcessInstanceQuery historicProcessInstanceQuery;
    
    public DeleteHistoricProcessInstancesBatchCmd(final List<String> historicProcessInstanceIds, final HistoricProcessInstanceQuery historicProcessInstanceQuery, final String deleteReason) {
        this.historicProcessInstanceIds = historicProcessInstanceIds;
        this.historicProcessInstanceQuery = historicProcessInstanceQuery;
        this.deleteReason = deleteReason;
    }
    
    @Override
    public Batch execute(final CommandContext commandContext) {
        final BatchElementConfiguration elementConfiguration = this.collectHistoricProcessInstanceIds(commandContext);
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "historicProcessInstanceIds", elementConfiguration.getIds());
        return new BatchBuilder(commandContext).type("historic-instance-deletion").config(this.getConfiguration(elementConfiguration)).permission(BatchPermissions.CREATE_BATCH_DELETE_FINISHED_PROCESS_INSTANCES).operationLogHandler(this::writeUserOperationLog).build();
    }
    
    protected BatchElementConfiguration collectHistoricProcessInstanceIds(final CommandContext commandContext) {
        final BatchElementConfiguration elementConfiguration = new BatchElementConfiguration();
        final List<String> processInstanceIds = this.getHistoricProcessInstanceIds();
        if (!CollectionUtil.isEmpty(processInstanceIds)) {
            final HistoricProcessInstanceQueryImpl query = new HistoricProcessInstanceQueryImpl();
            query.processInstanceIds(new HashSet<String>(processInstanceIds));
            final BatchElementConfiguration batchElementConfiguration = elementConfiguration;
            final HistoricProcessInstanceQueryImpl obj = query;
            Objects.requireNonNull(obj);
            batchElementConfiguration.addDeploymentMappings(commandContext.runWithoutAuthorization((Callable<List<ImmutablePair<String, String>>>)obj::listDeploymentIdMappings), processInstanceIds);
        }
        final HistoricProcessInstanceQueryImpl processInstanceQuery = (HistoricProcessInstanceQueryImpl)this.historicProcessInstanceQuery;
        if (processInstanceQuery != null) {
            elementConfiguration.addDeploymentMappings(processInstanceQuery.listDeploymentIdMappings());
        }
        return elementConfiguration;
    }
    
    public List<String> getHistoricProcessInstanceIds() {
        return this.historicProcessInstanceIds;
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext, final int numInstances) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(new PropertyChange("nrOfInstances", null, numInstances));
        propertyChanges.add(new PropertyChange("async", null, true));
        propertyChanges.add(new PropertyChange("deleteReason", null, this.deleteReason));
        commandContext.getOperationLogManager().logProcessInstanceOperation("DeleteHistory", null, null, null, propertyChanges);
    }
    
    public BatchConfiguration getConfiguration(final BatchElementConfiguration elementConfiguration) {
        return new BatchConfiguration(elementConfiguration.getIds(), elementConfiguration.getMappings(), false);
    }
}
