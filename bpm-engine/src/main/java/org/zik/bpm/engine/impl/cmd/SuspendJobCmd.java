// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import org.zik.bpm.engine.impl.management.UpdateJobSuspensionStateBuilderImpl;

public class SuspendJobCmd extends AbstractSetJobStateCmd
{
    public SuspendJobCmd(final UpdateJobSuspensionStateBuilderImpl builder) {
        super(builder);
    }
    
    @Override
    protected SuspensionState getNewSuspensionState() {
        return SuspensionState.SUSPENDED;
    }
    
    @Override
    protected String getLogEntryOperation() {
        return "SuspendJob";
    }
}
