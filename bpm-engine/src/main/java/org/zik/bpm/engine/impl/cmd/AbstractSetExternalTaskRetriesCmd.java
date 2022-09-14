// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.util.ImmutablePair;
import java.util.Objects;
import org.zik.bpm.engine.impl.ExternalTaskQueryImpl;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import org.zik.bpm.engine.impl.batch.BatchElementConfiguration;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Set;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.HistoricProcessInstanceQueryImpl;
import org.zik.bpm.engine.impl.ProcessInstanceQueryImpl;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public abstract class AbstractSetExternalTaskRetriesCmd<T> implements Command<T>
{
    protected UpdateExternalTaskRetriesBuilderImpl builder;
    
    public AbstractSetExternalTaskRetriesCmd(final UpdateExternalTaskRetriesBuilderImpl builder) {
        this.builder = builder;
    }
    
    protected List<String> collectProcessInstanceIds() {
        final Set<String> collectedProcessInstanceIds = new HashSet<String>();
        final List<String> processInstanceIds = this.builder.getProcessInstanceIds();
        if (processInstanceIds != null && !processInstanceIds.isEmpty()) {
            collectedProcessInstanceIds.addAll(processInstanceIds);
        }
        final ProcessInstanceQueryImpl processInstanceQuery = (ProcessInstanceQueryImpl)this.builder.getProcessInstanceQuery();
        if (processInstanceQuery != null) {
            collectedProcessInstanceIds.addAll(processInstanceQuery.listIds());
        }
        final HistoricProcessInstanceQueryImpl historicProcessInstanceQuery = (HistoricProcessInstanceQueryImpl)this.builder.getHistoricProcessInstanceQuery();
        if (historicProcessInstanceQuery != null) {
            collectedProcessInstanceIds.addAll(historicProcessInstanceQuery.listIds());
        }
        return new ArrayList<String>(collectedProcessInstanceIds);
    }
    
    protected BatchElementConfiguration collectExternalTaskIds(final CommandContext commandContext) {
        final BatchElementConfiguration elementConfiguration = new BatchElementConfiguration();
        final List<String> externalTaskIds = this.builder.getExternalTaskIds();
        if (!CollectionUtil.isEmpty(externalTaskIds)) {
            EnsureUtil.ensureNotContainsNull(BadUserRequestException.class, "External task id cannot be null", "externalTaskIds", externalTaskIds);
            final ExternalTaskQueryImpl taskQuery = new ExternalTaskQueryImpl();
            taskQuery.externalTaskIdIn(new HashSet<String>(externalTaskIds));
            final BatchElementConfiguration batchElementConfiguration = elementConfiguration;
            final ExternalTaskQueryImpl obj = taskQuery;
            Objects.requireNonNull(obj);
            batchElementConfiguration.addDeploymentMappings(commandContext.runWithoutAuthorization((Callable<List<ImmutablePair<String, String>>>)obj::listDeploymentIdMappings), externalTaskIds);
        }
        final ExternalTaskQueryImpl externalTaskQuery = (ExternalTaskQueryImpl)this.builder.getExternalTaskQuery();
        if (externalTaskQuery != null) {
            elementConfiguration.addDeploymentMappings(externalTaskQuery.listDeploymentIdMappings());
        }
        final List<String> collectedProcessInstanceIds = this.collectProcessInstanceIds();
        if (!collectedProcessInstanceIds.isEmpty()) {
            final ExternalTaskQueryImpl query = new ExternalTaskQueryImpl();
            query.processInstanceIdIn((String[])collectedProcessInstanceIds.toArray(new String[0]));
            final BatchElementConfiguration batchElementConfiguration2 = elementConfiguration;
            final ExternalTaskQueryImpl obj2 = query;
            Objects.requireNonNull(obj2);
            batchElementConfiguration2.addDeploymentMappings(commandContext.runWithoutAuthorization((Callable<List<ImmutablePair<String, String>>>)obj2::listDeploymentIdMappings));
        }
        return elementConfiguration;
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext, final int numInstances, final boolean async) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(new PropertyChange("nrOfInstances", null, numInstances));
        propertyChanges.add(new PropertyChange("async", null, async));
        propertyChanges.add(new PropertyChange("retries", null, this.builder.getRetries()));
        commandContext.getOperationLogManager().logExternalTaskOperation("SetExternalTaskRetries", null, propertyChanges);
    }
    
    protected void writeUserOperationLogAsync(final CommandContext commandContext, final int numInstances) {
        this.writeUserOperationLog(commandContext, numInstances, true);
    }
}
