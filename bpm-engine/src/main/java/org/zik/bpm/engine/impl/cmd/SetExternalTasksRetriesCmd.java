// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.batch.BatchElementConfiguration;
import java.util.Collection;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;

public class SetExternalTasksRetriesCmd extends AbstractSetExternalTaskRetriesCmd<Void>
{
    public SetExternalTasksRetriesCmd(final UpdateExternalTaskRetriesBuilderImpl builder) {
        super(builder);
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final BatchElementConfiguration elementConfiguration = this.collectExternalTaskIds(commandContext);
        final List<String> collectedIds = elementConfiguration.getIds();
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "externalTaskIds", collectedIds);
        final int instanceCount = collectedIds.size();
        this.writeUserOperationLog(commandContext, instanceCount, false);
        final int retries = this.builder.getRetries();
        for (final String externalTaskId : collectedIds) {
            new SetExternalTaskRetriesCmd(externalTaskId, retries, false).execute(commandContext);
        }
        return null;
    }
}
