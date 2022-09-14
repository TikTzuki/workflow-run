// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import org.zik.bpm.engine.impl.management.UpdateJobSuspensionStateBuilderImpl;

public class ActivateJobCmd extends AbstractSetJobStateCmd
{
    public ActivateJobCmd(final UpdateJobSuspensionStateBuilderImpl builder) {
        super(builder);
    }
    
    @Override
    protected SuspensionState getNewSuspensionState() {
        return SuspensionState.ACTIVE;
    }
    
    @Override
    protected String getLogEntryOperation() {
        return "ActivateJob";
    }
}
