// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.batch.update.UpdateProcessInstancesSuspendStateBatchConfiguration;
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
import org.zik.bpm.engine.impl.UpdateProcessInstancesSuspensionStateBuilderImpl;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.batch.Batch;

public class UpdateProcessInstancesSuspendStateBatchCmd extends AbstractUpdateProcessInstancesSuspendStateCmd<Batch>
{
    public UpdateProcessInstancesSuspendStateBatchCmd(final CommandExecutor commandExecutor, final UpdateProcessInstancesSuspensionStateBuilderImpl builder, final boolean suspending) {
        super(commandExecutor, builder, suspending);
    }
    
    @Override
    public Batch execute(final CommandContext commandContext) {
        final BatchElementConfiguration elementConfiguration = this.collectProcessInstanceIds(commandContext);
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "No process instance ids given", "process Instance Ids", elementConfiguration.getIds());
        EnsureUtil.ensureNotContainsNull(BadUserRequestException.class, "Cannot be null.", "Process Instance ids", elementConfiguration.getIds());
        return new BatchBuilder(commandContext).type("instance-update-suspension-state").config(this.getConfiguration(elementConfiguration)).permission(BatchPermissions.CREATE_BATCH_UPDATE_PROCESS_INSTANCES_SUSPEND).operationLogHandler(this::writeUserOperationLogAsync).build();
    }
    
    public BatchConfiguration getConfiguration(final BatchElementConfiguration elementConfiguration) {
        return new UpdateProcessInstancesSuspendStateBatchConfiguration(elementConfiguration.getIds(), elementConfiguration.getMappings(), this.suspending);
    }
}
