// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd.batch.removaltime;

import org.zik.bpm.engine.query.Query;
import org.zik.bpm.engine.impl.batch.removaltime.SetRemovalTimeBatchConfiguration;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import java.util.Iterator;
import org.zik.bpm.engine.batch.history.HistoricBatchQuery;
import java.util.List;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.BatchPermissions;
import org.zik.bpm.engine.impl.batch.builder.BatchBuilder;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.util.Collection;
import org.zik.bpm.engine.batch.history.HistoricBatch;
import java.util.HashSet;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.history.SetRemovalTimeToHistoricBatchesBuilderImpl;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SetRemovalTimeToHistoricBatchesCmd implements Command<Batch>
{
    protected SetRemovalTimeToHistoricBatchesBuilderImpl builder;
    
    public SetRemovalTimeToHistoricBatchesCmd(final SetRemovalTimeToHistoricBatchesBuilderImpl builder) {
        this.builder = builder;
    }
    
    @Override
    public Batch execute(final CommandContext commandContext) {
        final List<String> instanceIds = this.builder.getIds();
        final HistoricBatchQuery instanceQuery = this.builder.getQuery();
        if (instanceQuery == null && instanceIds == null) {
            throw new BadUserRequestException("Neither query nor ids provided.");
        }
        final Collection<String> collectedInstanceIds = new HashSet<String>();
        if (instanceQuery != null) {
            for (final HistoricBatch historicBatch : ((Query<T, HistoricBatch>)instanceQuery).list()) {
                collectedInstanceIds.add(historicBatch.getId());
            }
        }
        if (instanceIds != null) {
            collectedInstanceIds.addAll(this.findHistoricInstanceIds(instanceIds, commandContext));
        }
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "removalTime", this.builder.getMode());
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "historicBatches", collectedInstanceIds);
        return new BatchBuilder(commandContext).type("batch-set-removal-time").config(this.getConfiguration(collectedInstanceIds)).permission(BatchPermissions.CREATE_BATCH_SET_REMOVAL_TIME).operationLogHandler(this::writeUserOperationLog).build();
    }
    
    protected List<String> findHistoricInstanceIds(final List<String> instanceIds, final CommandContext commandContext) {
        final List<String> ids = new ArrayList<String>();
        for (final String instanceId : instanceIds) {
            final HistoricBatch batch = ((Query<T, HistoricBatch>)this.createHistoricBatchQuery(commandContext).batchId(instanceId)).singleResult();
            if (batch != null) {
                ids.add(batch.getId());
            }
        }
        return ids;
    }
    
    protected HistoricBatchQuery createHistoricBatchQuery(final CommandContext commandContext) {
        return commandContext.getProcessEngineConfiguration().getHistoryService().createHistoricBatchQuery();
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext, final int numInstances) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(new PropertyChange("mode", null, this.builder.getMode()));
        propertyChanges.add(new PropertyChange("removalTime", null, this.builder.getRemovalTime()));
        propertyChanges.add(new PropertyChange("nrOfInstances", null, numInstances));
        propertyChanges.add(new PropertyChange("async", null, true));
        commandContext.getOperationLogManager().logBatchOperation("SetRemovalTime", propertyChanges);
    }
    
    protected boolean hasRemovalTime() {
        return this.builder.getMode() == SetRemovalTimeToHistoricBatchesBuilderImpl.Mode.ABSOLUTE_REMOVAL_TIME || this.builder.getMode() == SetRemovalTimeToHistoricBatchesBuilderImpl.Mode.CLEARED_REMOVAL_TIME;
    }
    
    public BatchConfiguration getConfiguration(final Collection<String> instances) {
        return new SetRemovalTimeBatchConfiguration(new ArrayList<String>(instances)).setHasRemovalTime(this.hasRemovalTime()).setRemovalTime(this.builder.getRemovalTime());
    }
}
