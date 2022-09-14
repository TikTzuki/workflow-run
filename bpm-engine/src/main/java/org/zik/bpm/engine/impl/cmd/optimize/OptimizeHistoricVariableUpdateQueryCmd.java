// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd.optimize;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Iterator;
import java.util.ArrayList;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.zik.bpm.engine.impl.variable.serializer.AbstractTypedValueSerializer;
import org.zik.bpm.engine.impl.persistence.entity.HistoricDetailVariableInstanceUpdateEntity;
import java.util.Collection;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Date;
import org.zik.bpm.engine.impl.cmd.CommandLogger;
import org.zik.bpm.engine.history.HistoricVariableUpdate;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class OptimizeHistoricVariableUpdateQueryCmd implements Command<List<HistoricVariableUpdate>>
{
    private static final CommandLogger LOG;
    protected Date occurredAfter;
    protected Date occurredAt;
    protected boolean excludeObjectValues;
    protected int maxResults;
    
    public OptimizeHistoricVariableUpdateQueryCmd(final Date occurredAfter, final Date occurredAt, final boolean excludeObjectValues, final int maxResults) {
        this.occurredAfter = occurredAfter;
        this.occurredAt = occurredAt;
        this.excludeObjectValues = excludeObjectValues;
        this.maxResults = maxResults;
    }
    
    @Override
    public List<HistoricVariableUpdate> execute(final CommandContext commandContext) {
        final List<HistoricVariableUpdate> historicVariableUpdates = commandContext.getOptimizeManager().getHistoricVariableUpdates(this.occurredAfter, this.occurredAt, this.maxResults);
        this.fetchVariableValues(historicVariableUpdates, commandContext);
        return historicVariableUpdates;
    }
    
    private void fetchVariableValues(final List<HistoricVariableUpdate> historicVariableUpdates, final CommandContext commandContext) {
        if (!CollectionUtil.isEmpty(historicVariableUpdates)) {
            final List<String> byteArrayIds = this.getByteArrayIds(historicVariableUpdates);
            if (!byteArrayIds.isEmpty()) {
                commandContext.getOptimizeManager().fetchHistoricVariableUpdateByteArrays(byteArrayIds);
            }
            this.resolveTypedValues(historicVariableUpdates);
        }
    }
    
    protected boolean shouldFetchValue(final HistoricDetailVariableInstanceUpdateEntity entity) {
        final ValueType entityType = entity.getSerializer().getType();
        return !AbstractTypedValueSerializer.BINARY_VALUE_TYPES.contains(entityType.getName()) && (!ValueType.OBJECT.equals(entityType) || !this.excludeObjectValues);
    }
    
    protected boolean isHistoricDetailVariableInstanceUpdateEntity(final HistoricVariableUpdate variableUpdate) {
        return variableUpdate instanceof HistoricDetailVariableInstanceUpdateEntity;
    }
    
    protected List<String> getByteArrayIds(final List<HistoricVariableUpdate> variableUpdates) {
        final List<String> byteArrayIds = new ArrayList<String>();
        for (final HistoricVariableUpdate variableUpdate : variableUpdates) {
            if (this.isHistoricDetailVariableInstanceUpdateEntity(variableUpdate)) {
                final HistoricDetailVariableInstanceUpdateEntity entity = (HistoricDetailVariableInstanceUpdateEntity)variableUpdate;
                if (!this.shouldFetchValue(entity)) {
                    continue;
                }
                final String byteArrayId = entity.getByteArrayValueId();
                if (byteArrayId == null) {
                    continue;
                }
                byteArrayIds.add(byteArrayId);
            }
        }
        return byteArrayIds;
    }
    
    protected void resolveTypedValues(final List<HistoricVariableUpdate> variableUpdates) {
        for (final HistoricVariableUpdate variableUpdate : variableUpdates) {
            if (this.isHistoricDetailVariableInstanceUpdateEntity(variableUpdate)) {
                final HistoricDetailVariableInstanceUpdateEntity entity = (HistoricDetailVariableInstanceUpdateEntity)variableUpdate;
                if (!this.shouldFetchValue(entity)) {
                    continue;
                }
                try {
                    entity.getTypedValue(false);
                }
                catch (Exception t) {
                    OptimizeHistoricVariableUpdateQueryCmd.LOG.exceptionWhileGettingValueForVariable(t);
                }
            }
        }
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
