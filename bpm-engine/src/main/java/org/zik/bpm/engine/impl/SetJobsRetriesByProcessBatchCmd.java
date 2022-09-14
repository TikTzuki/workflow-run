// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.Set;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.util.ImmutablePair;
import java.util.Objects;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import java.util.Collection;
import java.util.HashSet;
import org.zik.bpm.engine.impl.batch.BatchElementConfiguration;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;
import org.zik.bpm.engine.runtime.ProcessInstanceQuery;
import java.util.List;
import org.zik.bpm.engine.impl.cmd.AbstractSetJobsRetriesBatchCmd;

public class SetJobsRetriesByProcessBatchCmd extends AbstractSetJobsRetriesBatchCmd
{
    protected final List<String> processInstanceIds;
    protected final ProcessInstanceQuery query;
    protected HistoricProcessInstanceQuery historicProcessInstanceQuery;
    
    public SetJobsRetriesByProcessBatchCmd(final List<String> processInstanceIds, final ProcessInstanceQuery query, final HistoricProcessInstanceQuery historicProcessInstanceQuery, final int retries) {
        this.processInstanceIds = processInstanceIds;
        this.query = query;
        this.historicProcessInstanceQuery = historicProcessInstanceQuery;
        this.retries = retries;
    }
    
    @Override
    protected BatchElementConfiguration collectJobIds(final CommandContext commandContext) {
        final Set<String> collectedProcessInstanceIds = new HashSet<String>();
        if (this.query != null) {
            collectedProcessInstanceIds.addAll(((ProcessInstanceQueryImpl)this.query).listIds());
        }
        if (this.historicProcessInstanceQuery != null) {
            final List<String> ids = ((HistoricProcessInstanceQueryImpl)this.historicProcessInstanceQuery).listIds();
            collectedProcessInstanceIds.addAll(ids);
        }
        if (this.processInstanceIds != null) {
            collectedProcessInstanceIds.addAll(this.processInstanceIds);
        }
        final BatchElementConfiguration elementConfiguration = new BatchElementConfiguration();
        if (!CollectionUtil.isEmpty(collectedProcessInstanceIds)) {
            final JobQueryImpl jobQuery = new JobQueryImpl();
            jobQuery.processInstanceIds(collectedProcessInstanceIds);
            final BatchElementConfiguration batchElementConfiguration = elementConfiguration;
            final JobQueryImpl obj = jobQuery;
            Objects.requireNonNull(obj);
            batchElementConfiguration.addDeploymentMappings(commandContext.runWithoutAuthorization((Callable<List<ImmutablePair<String, String>>>)obj::listDeploymentIdMappings));
        }
        return elementConfiguration;
    }
}
