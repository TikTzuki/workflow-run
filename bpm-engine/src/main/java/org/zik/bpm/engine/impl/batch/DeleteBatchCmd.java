// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch;

import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteBatchCmd implements Command<Void>
{
    protected boolean cascadeToHistory;
    protected String batchId;
    
    public DeleteBatchCmd(final String batchId, final boolean cascadeToHistory) {
        this.batchId = batchId;
        this.cascadeToHistory = cascadeToHistory;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "Batch id must not be null", "batch id", this.batchId);
        final BatchEntity batchEntity = commandContext.getBatchManager().findBatchById(this.batchId);
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "Batch for id '" + this.batchId + "' cannot be found", "batch", batchEntity);
        this.checkAccess(commandContext, batchEntity);
        this.writeUserOperationLog(commandContext);
        batchEntity.delete(this.cascadeToHistory, true);
        return null;
    }
    
    protected void checkAccess(final CommandContext commandContext, final BatchEntity batch) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkDeleteBatch(batch);
        }
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext) {
        commandContext.getOperationLogManager().logBatchOperation("Delete", this.batchId, new PropertyChange("cascadeToHistory", null, this.cascadeToHistory));
    }
}
