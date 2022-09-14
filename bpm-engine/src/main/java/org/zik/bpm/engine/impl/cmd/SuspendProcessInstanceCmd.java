// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.management.UpdateJobSuspensionStateBuilderImpl;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import org.zik.bpm.engine.impl.runtime.UpdateProcessInstanceSuspensionStateBuilderImpl;

public class SuspendProcessInstanceCmd extends AbstractSetProcessInstanceStateCmd
{
    public SuspendProcessInstanceCmd(final UpdateProcessInstanceSuspensionStateBuilderImpl builder) {
        super(builder);
    }
    
    @Override
    protected SuspensionState getNewSuspensionState() {
        return SuspensionState.SUSPENDED;
    }
    
    @Override
    protected SuspendJobCmd getNextCommand(final UpdateJobSuspensionStateBuilderImpl jobCommandBuilder) {
        return new SuspendJobCmd(jobCommandBuilder);
    }
    
    @Override
    protected String getLogEntryOperation() {
        return "Suspend";
    }
}
