// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.management.UpdateJobDefinitionSuspensionStateBuilderImpl;
import org.zik.bpm.engine.impl.batch.BatchEntity;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;

public class SuspendBatchCmd extends AbstractSetBatchStateCmd
{
    public SuspendBatchCmd(final String batchId) {
        super(batchId);
    }
    
    @Override
    protected SuspensionState getNewSuspensionState() {
        return SuspensionState.SUSPENDED;
    }
    
    @Override
    protected void checkAccess(final CommandChecker checker, final BatchEntity batch) {
        checker.checkSuspendBatch(batch);
    }
    
    @Override
    protected AbstractSetJobDefinitionStateCmd createSetJobDefinitionStateCommand(final UpdateJobDefinitionSuspensionStateBuilderImpl builder) {
        return new SuspendJobDefinitionCmd(builder);
    }
    
    @Override
    protected String getUserOperationType() {
        return "SuspendBatch";
    }
}
