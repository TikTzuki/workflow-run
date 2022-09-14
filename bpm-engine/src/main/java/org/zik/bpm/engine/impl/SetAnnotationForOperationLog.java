// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.query.Query;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.history.UserOperationLogEntry;
import java.util.Collection;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SetAnnotationForOperationLog implements Command<Void>
{
    protected String operationId;
    protected String annotation;
    
    public SetAnnotationForOperationLog(final String operationId, final String annotation) {
        this.operationId = operationId;
        this.annotation = annotation;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull(NotValidException.class, "operation id", (Object)this.operationId);
        commandContext.disableAuthorizationCheck();
        final List<UserOperationLogEntry> operationLogEntries = ((Query<T, UserOperationLogEntry>)commandContext.getProcessEngineConfiguration().getHistoryService().createUserOperationLogQuery().operationId(this.operationId)).list();
        commandContext.enableAuthorizationCheck();
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "operations", operationLogEntries);
        final UserOperationLogEntry operationLogEntry = operationLogEntries.get(0);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkUpdateUserOperationLog(operationLogEntry);
        }
        commandContext.getOperationLogManager().updateOperationLogAnnotationByOperationId(this.operationId, this.annotation);
        if (this.annotation == null) {
            commandContext.getOperationLogManager().logClearAnnotationOperation(this.operationId);
        }
        else {
            commandContext.getOperationLogManager().logSetAnnotationOperation(this.operationId);
        }
        return null;
    }
}
