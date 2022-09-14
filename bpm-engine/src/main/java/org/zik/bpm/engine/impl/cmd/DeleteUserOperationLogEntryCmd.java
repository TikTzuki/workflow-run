// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.history.UserOperationLogEntry;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteUserOperationLogEntryCmd implements Command<Void>
{
    protected String entryId;
    
    public DeleteUserOperationLogEntryCmd(final String entryId) {
        this.entryId = entryId;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull(NotValidException.class, "entryId", (Object)this.entryId);
        final UserOperationLogEntry entry = commandContext.getOperationLogManager().findOperationLogById(this.entryId);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkDeleteUserOperationLog(entry);
        }
        commandContext.getOperationLogManager().deleteOperationLogEntryById(this.entryId);
        return null;
    }
}
