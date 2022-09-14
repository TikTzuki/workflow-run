// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteProcessInstanceCmd extends AbstractDeleteProcessInstanceCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String processInstanceId;
    protected boolean skipIoMappings;
    protected boolean skipSubprocesses;
    
    public DeleteProcessInstanceCmd(final String processInstanceId, final String deleteReason, final boolean skipCustomListeners, final boolean externallyTerminated, final boolean skipIoMappings, final boolean skipSubprocesses, final boolean failIfNotExists) {
        this.processInstanceId = processInstanceId;
        this.deleteReason = deleteReason;
        this.skipCustomListeners = skipCustomListeners;
        this.externallyTerminated = externallyTerminated;
        this.skipIoMappings = skipIoMappings;
        this.skipSubprocesses = skipSubprocesses;
        this.failIfNotExists = failIfNotExists;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        this.deleteProcessInstance(commandContext, this.processInstanceId, this.deleteReason, this.skipCustomListeners, this.externallyTerminated, this.skipIoMappings, this.skipSubprocesses);
        return null;
    }
}
