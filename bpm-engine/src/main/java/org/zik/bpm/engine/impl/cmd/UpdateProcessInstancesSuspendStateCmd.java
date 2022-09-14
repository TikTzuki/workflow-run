// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.runtime.UpdateProcessInstanceSuspensionStateBuilderImpl;
import java.util.Collection;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.UpdateProcessInstancesSuspensionStateBuilderImpl;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;

public class UpdateProcessInstancesSuspendStateCmd extends AbstractUpdateProcessInstancesSuspendStateCmd<Void>
{
    public UpdateProcessInstancesSuspendStateCmd(final CommandExecutor commandExecutor, final UpdateProcessInstancesSuspensionStateBuilderImpl builder, final boolean suspendstate) {
        super(commandExecutor, builder, suspendstate);
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final Collection<String> processInstanceIds = this.collectProcessInstanceIds(commandContext).getIds();
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "No process instance ids given", "Process Instance ids", processInstanceIds);
        EnsureUtil.ensureNotContainsNull(BadUserRequestException.class, "Cannot be null.", "Process Instance ids", processInstanceIds);
        this.writeUserOperationLog(commandContext, processInstanceIds.size(), false);
        final UpdateProcessInstanceSuspensionStateBuilderImpl suspensionStateBuilder = new UpdateProcessInstanceSuspensionStateBuilderImpl(this.commandExecutor);
        if (this.suspending) {
            for (final String processInstanceId : processInstanceIds) {
                suspensionStateBuilder.byProcessInstanceId(processInstanceId).suspend();
            }
        }
        else {
            for (final String processInstanceId : processInstanceIds) {
                suspensionStateBuilder.byProcessInstanceId(processInstanceId).activate();
            }
        }
        return null;
    }
}
