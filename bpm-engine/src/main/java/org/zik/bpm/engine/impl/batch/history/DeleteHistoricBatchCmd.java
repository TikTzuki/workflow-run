// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.history;

import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteHistoricBatchCmd implements Command<Object>
{
    protected String batchId;
    
    public DeleteHistoricBatchCmd(final String batchId) {
        this.batchId = batchId;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "Historic batch id must not be null", "historic batch id", this.batchId);
        final HistoricBatchEntity historicBatch = commandContext.getHistoricBatchManager().findHistoricBatchById(this.batchId);
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "Historic batch for id '" + this.batchId + "' cannot be found", "historic batch", historicBatch);
        this.checkAccess(commandContext, historicBatch);
        this.writeUserOperationLog(commandContext);
        historicBatch.delete();
        return null;
    }
    
    protected void checkAccess(final CommandContext commandContext, final HistoricBatchEntity batch) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkDeleteHistoricBatch(batch);
        }
    }
    
    protected void writeUserOperationLog(final CommandContext commandContext) {
        commandContext.getOperationLogManager().logBatchOperation("DeleteHistory", this.batchId, PropertyChange.EMPTY_CHANGE);
    }
}
