// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.management.UpdateJobDefinitionSuspensionStateBuilderImpl;
import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import org.zik.bpm.engine.impl.batch.BatchEntity;
import org.zik.bpm.engine.impl.persistence.entity.BatchManager;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public abstract class AbstractSetBatchStateCmd implements Command<Void>
{
    public static final String SUSPENSION_STATE_PROPERTY = "suspensionState";
    protected String batchId;
    
    public AbstractSetBatchStateCmd(final String batchId) {
        this.batchId = batchId;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "Batch id must not be null", "batch id", this.batchId);
        final BatchManager batchManager = commandContext.getBatchManager();
        final BatchEntity batch = batchManager.findBatchById(this.batchId);
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "Batch for id '" + this.batchId + "' cannot be found", "batch", batch);
        this.checkAccess(commandContext, batch);
        this.setJobDefinitionState(commandContext, batch.getSeedJobDefinitionId());
        this.setJobDefinitionState(commandContext, batch.getMonitorJobDefinitionId());
        this.setJobDefinitionState(commandContext, batch.getBatchJobDefinitionId());
        batchManager.updateBatchSuspensionStateById(this.batchId, this.getNewSuspensionState());
        this.logUserOperation(commandContext);
        return null;
    }
    
    protected abstract SuspensionState getNewSuspensionState();
    
    protected void checkAccess(final CommandContext commandContext, final BatchEntity batch) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            this.checkAccess(checker, batch);
        }
    }
    
    protected abstract void checkAccess(final CommandChecker p0, final BatchEntity p1);
    
    protected void setJobDefinitionState(final CommandContext commandContext, final String jobDefinitionId) {
        this.createSetJobDefinitionStateCommand(jobDefinitionId).execute(commandContext);
    }
    
    protected AbstractSetJobDefinitionStateCmd createSetJobDefinitionStateCommand(final String jobDefinitionId) {
        final AbstractSetJobDefinitionStateCmd suspendJobDefinitionCmd = this.createSetJobDefinitionStateCommand(new UpdateJobDefinitionSuspensionStateBuilderImpl().byJobDefinitionId(jobDefinitionId).includeJobs(true));
        suspendJobDefinitionCmd.disableLogUserOperation();
        return suspendJobDefinitionCmd;
    }
    
    protected abstract AbstractSetJobDefinitionStateCmd createSetJobDefinitionStateCommand(final UpdateJobDefinitionSuspensionStateBuilderImpl p0);
    
    protected void logUserOperation(final CommandContext commandContext) {
        final PropertyChange propertyChange = new PropertyChange("suspensionState", null, this.getNewSuspensionState().getName());
        commandContext.getOperationLogManager().logBatchOperation(this.getUserOperationType(), this.batchId, propertyChange);
    }
    
    protected abstract String getUserOperationType();
}
