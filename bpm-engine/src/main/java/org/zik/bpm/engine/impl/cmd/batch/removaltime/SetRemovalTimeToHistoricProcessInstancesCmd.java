// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd.batch.removaltime;

import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.batch.removaltime.SetRemovalTimeBatchConfiguration;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.util.ImmutablePair;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import org.zik.bpm.engine.impl.HistoricProcessInstanceQueryImpl;
import org.zik.bpm.engine.impl.batch.BatchElementConfiguration;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.BatchPermissions;
import org.zik.bpm.engine.impl.batch.builder.BatchBuilder;
import java.util.Collection;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.history.SetRemovalTimeToHistoricProcessInstancesBuilderImpl;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SetRemovalTimeToHistoricProcessInstancesCmd implements Command<Batch>
{
    protected SetRemovalTimeToHistoricProcessInstancesBuilderImpl builder;
    
    public SetRemovalTimeToHistoricProcessInstancesCmd(final SetRemovalTimeToHistoricProcessInstancesBuilderImpl builder) {
        this.builder = builder;
    }
    
    @Override
    public Batch execute(final CommandContext commandContext) {
        if (this.builder.getQuery() == null && this.builder.getIds() == null) {
            throw new BadUserRequestException("Neither query nor ids provided.");
        }
        final BatchElementConfiguration elementConfiguration = this.collectInstances(commandContext);
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "removalTime", this.builder.getMode());
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "historicProcessInstances", elementConfiguration.getIds());
        return new BatchBuilder(commandContext).type("process-set-removal-time").config(this.getConfiguration(elementConfiguration)).permission(BatchPermissions.CREATE_BATCH_SET_REMOVAL_TIME).operationLogHandler(this::writeUserOperationLog).build();
    }
    
    protected BatchElementConfiguration collectInstances(final CommandContext commandContext) {
        final BatchElementConfiguration elementConfiguration = new BatchElementConfiguration();
        final HistoricProcessInstanceQuery instanceQuery = this.builder.getQuery();
        if (instanceQuery != null) {
            elementConfiguration.addDeploymentMappings(((HistoricProcessInstanceQueryImpl)instanceQuery).listDeploymentIdMappings());
        }
        final List<String> instanceIds = this.builder.getIds();
        if (!CollectionUtil.isEmpty(instanceIds)) {
            final HistoricProcessInstanceQueryImpl query = new HistoricProcessInstanceQueryImpl();
            query.processInstanceIds(new HashSet<String>(instanceIds));
            final BatchElementConfiguration batchElementConfiguration = elementConfiguration;
            final HistoricProcessInstanceQueryImpl obj = query;
            Objects.requireNonNull(obj);
            batchElementConfiguration.addDeploymentMappings(commandContext.runWithoutAuthorization((Callable<List<ImmutablePair<String, String>>>)obj::listDeploymentIdMappings));
        }
        return elementConfiguration;
    }
    
    protected BatchConfiguration getConfiguration(final BatchElementConfiguration elementConfiguration) {
        return new SetRemovalTimeBatchConfiguration(elementConfiguration.getIds(), elementConfiguration.getMappings()).setHierarchical(this.builder.isHierarchical()).setHasRemovalTime(this.hasRemovalTime()).setRemovalTime(this.builder.getRemovalTime());
    }
    
    protected boolean hasRemovalTime() {
        return this.builder.getMode() == SetRemovalTimeToHistoricProcessInstancesBuilderImpl.Mode.ABSOLUTE_REMOVAL_TIME || this.builder.getMode() == SetRemovalTimeToHistoricProcessInstancesBuilderImpl.Mode.CLEARED_REMOVAL_TIME;
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext, final int numInstances) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(new PropertyChange("mode", null, this.builder.getMode()));
        propertyChanges.add(new PropertyChange("removalTime", null, this.builder.getRemovalTime()));
        propertyChanges.add(new PropertyChange("hierarchical", null, this.builder.isHierarchical()));
        propertyChanges.add(new PropertyChange("nrOfInstances", null, numInstances));
        propertyChanges.add(new PropertyChange("async", null, true));
        commandContext.getOperationLogManager().logProcessInstanceOperation("SetRemovalTime", propertyChanges);
    }
}
