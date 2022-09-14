// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.List;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteProcessInstancesCmd extends AbstractDeleteProcessInstanceCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected List<String> processInstanceIds;
    
    public DeleteProcessInstancesCmd(final List<String> processInstanceIds, final String deleteReason, final boolean skipCustomListeners, final boolean externallyTerminated, final boolean skipSubprocesses, final boolean failIfNotExists) {
        this.processInstanceIds = processInstanceIds;
        this.deleteReason = deleteReason;
        this.skipCustomListeners = skipCustomListeners;
        this.externallyTerminated = externallyTerminated;
        this.skipSubprocesses = skipSubprocesses;
        this.failIfNotExists = failIfNotExists;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        for (final String processInstanceId : this.processInstanceIds) {
            this.deleteProcessInstance(commandContext, processInstanceId, this.deleteReason, this.skipCustomListeners, this.externallyTerminated, false, this.skipSubprocesses);
        }
        return null;
    }
}
