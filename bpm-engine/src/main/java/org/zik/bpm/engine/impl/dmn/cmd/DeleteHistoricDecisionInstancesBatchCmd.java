// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.cmd;

import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.util.ImmutablePair;
import java.util.Objects;
import org.zik.bpm.engine.impl.HistoricDecisionInstanceQueryImpl;
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
import org.zik.bpm.engine.history.HistoricDecisionInstanceQuery;
import java.util.List;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteHistoricDecisionInstancesBatchCmd implements Command<Batch>
{
    protected List<String> historicDecisionInstanceIds;
    protected HistoricDecisionInstanceQuery historicDecisionInstanceQuery;
    protected String deleteReason;
    
    public DeleteHistoricDecisionInstancesBatchCmd(final List<String> ids, final HistoricDecisionInstanceQuery query, final String deleteReason) {
        this.historicDecisionInstanceIds = ids;
        this.historicDecisionInstanceQuery = query;
        this.deleteReason = deleteReason;
    }
    
    @Override
    public Batch execute(final CommandContext commandContext) {
        final BatchElementConfiguration elementConfiguration = this.collectHistoricDecisionInstanceIds(commandContext);
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "historicDecisionInstanceIds", elementConfiguration.getIds());
        return new BatchBuilder(commandContext).type("historic-decision-instance-deletion").config(this.getConfiguration(elementConfiguration)).permission(BatchPermissions.CREATE_BATCH_DELETE_DECISION_INSTANCES).operationLogHandler(this::writeUserOperationLog).build();
    }
    
    protected BatchElementConfiguration collectHistoricDecisionInstanceIds(final CommandContext commandContext) {
        final BatchElementConfiguration elementConfiguration = new BatchElementConfiguration();
        if (!CollectionUtil.isEmpty(this.historicDecisionInstanceIds)) {
            final HistoricDecisionInstanceQueryImpl query = new HistoricDecisionInstanceQueryImpl();
            query.decisionInstanceIdIn((String[])this.historicDecisionInstanceIds.toArray(new String[0]));
            final BatchElementConfiguration batchElementConfiguration = elementConfiguration;
            final HistoricDecisionInstanceQueryImpl obj = query;
            Objects.requireNonNull(obj);
            batchElementConfiguration.addDeploymentMappings(commandContext.runWithoutAuthorization((Callable<List<ImmutablePair<String, String>>>)obj::listDeploymentIdMappings), this.historicDecisionInstanceIds);
        }
        final HistoricDecisionInstanceQueryImpl decisionInstanceQuery = (HistoricDecisionInstanceQueryImpl)this.historicDecisionInstanceQuery;
        if (decisionInstanceQuery != null) {
            elementConfiguration.addDeploymentMappings(decisionInstanceQuery.listDeploymentIdMappings());
        }
        return elementConfiguration;
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext, final int numInstances) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(new PropertyChange("nrOfInstances", null, numInstances));
        propertyChanges.add(new PropertyChange("async", null, true));
        propertyChanges.add(new PropertyChange("deleteReason", null, this.deleteReason));
        commandContext.getOperationLogManager().logDecisionInstanceOperation("DeleteHistory", propertyChanges);
    }
    
    public BatchConfiguration getConfiguration(final BatchElementConfiguration elementConfiguration) {
        return new BatchConfiguration(elementConfiguration.getIds(), elementConfiguration.getMappings());
    }
}
