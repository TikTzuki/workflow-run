// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.util.ImmutablePair;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import org.zik.bpm.engine.impl.JobQueryImpl;
import java.util.Collection;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import org.zik.bpm.engine.impl.batch.BatchElementConfiguration;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.runtime.JobQuery;
import java.util.List;

public class SetJobsRetriesBatchCmd extends AbstractSetJobsRetriesBatchCmd
{
    protected List<String> ids;
    protected JobQuery jobQuery;
    
    public SetJobsRetriesBatchCmd(final List<String> ids, final JobQuery jobQuery, final int retries) {
        this.jobQuery = jobQuery;
        this.ids = ids;
        this.retries = retries;
    }
    
    @Override
    protected BatchElementConfiguration collectJobIds(final CommandContext commandContext) {
        final BatchElementConfiguration elementConfiguration = new BatchElementConfiguration();
        if (!CollectionUtil.isEmpty(this.ids)) {
            final JobQueryImpl query = new JobQueryImpl();
            query.jobIds(new HashSet<String>(this.ids));
            final BatchElementConfiguration batchElementConfiguration = elementConfiguration;
            final JobQueryImpl obj = query;
            Objects.requireNonNull(obj);
            batchElementConfiguration.addDeploymentMappings(commandContext.runWithoutAuthorization((Callable<List<ImmutablePair<String, String>>>)obj::listDeploymentIdMappings), this.ids);
        }
        if (this.jobQuery != null) {
            elementConfiguration.addDeploymentMappings(((JobQueryImpl)this.jobQuery).listDeploymentIdMappings());
        }
        return elementConfiguration;
    }
}
