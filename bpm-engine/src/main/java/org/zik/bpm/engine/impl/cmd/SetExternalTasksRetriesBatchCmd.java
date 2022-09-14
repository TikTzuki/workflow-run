// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.batch.SetRetriesBatchConfiguration;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
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

public class SetExternalTasksRetriesBatchCmd extends AbstractSetExternalTaskRetriesCmd<Batch>
{
    public SetExternalTasksRetriesBatchCmd(final UpdateExternalTaskRetriesBuilderImpl builder) {
        super(builder);
    }
    
    @Override
    public Batch execute(final CommandContext commandContext) {
        final BatchElementConfiguration elementConfiguration = this.collectExternalTaskIds(commandContext);
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "externalTaskIds", elementConfiguration.getIds());
        return new BatchBuilder(commandContext).type("set-external-task-retries").config(this.getConfiguration(elementConfiguration)).permission(BatchPermissions.CREATE_BATCH_SET_EXTERNAL_TASK_RETRIES).operationLogHandler(this::writeUserOperationLogAsync).build();
    }
    
    public BatchConfiguration getConfiguration(final BatchElementConfiguration elementConfiguration) {
        return new SetRetriesBatchConfiguration(elementConfiguration.getIds(), elementConfiguration.getMappings(), this.builder.getRetries());
    }
}
