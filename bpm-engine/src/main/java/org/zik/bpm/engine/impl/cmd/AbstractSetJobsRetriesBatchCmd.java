// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.batch.SetRetriesBatchConfiguration;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.batch.BatchElementConfiguration;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.BatchPermissions;
import org.zik.bpm.engine.impl.batch.builder.BatchBuilder;
import java.util.Collection;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.interceptor.Command;

public abstract class AbstractSetJobsRetriesBatchCmd implements Command<Batch>
{
    protected int retries;
    
    @Override
    public Batch execute(final CommandContext commandContext) {
        final BatchElementConfiguration elementConfiguration = this.collectJobIds(commandContext);
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "jobIds", elementConfiguration.getIds());
        EnsureUtil.ensureGreaterThanOrEqual("Retries count", this.retries, 0L);
        return new BatchBuilder(commandContext).config(this.getConfiguration(elementConfiguration)).type("set-job-retries").permission(BatchPermissions.CREATE_BATCH_SET_JOB_RETRIES).operationLogHandler(this::writeUserOperationLog).build();
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext, final int numInstances) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(new PropertyChange("nrOfInstances", null, numInstances));
        propertyChanges.add(new PropertyChange("async", null, true));
        propertyChanges.add(new PropertyChange("retries", null, this.retries));
        commandContext.getOperationLogManager().logJobOperation("SetJobRetries", null, null, null, null, null, propertyChanges);
    }
    
    protected abstract BatchElementConfiguration collectJobIds(final CommandContext p0);
    
    public BatchConfiguration getConfiguration(final BatchElementConfiguration elementConfiguration) {
        return new SetRetriesBatchConfiguration(elementConfiguration.getIds(), elementConfiguration.getMappings(), this.retries);
    }
}
