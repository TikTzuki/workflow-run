// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.management.UpdateJobSuspensionStateBuilderImpl;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import org.zik.bpm.engine.impl.runtime.UpdateProcessInstanceSuspensionStateBuilderImpl;

public class ActivateProcessInstanceCmd extends AbstractSetProcessInstanceStateCmd
{
    public ActivateProcessInstanceCmd(final UpdateProcessInstanceSuspensionStateBuilderImpl builder) {
        super(builder);
    }
    
    @Override
    protected SuspensionState getNewSuspensionState() {
        return SuspensionState.ACTIVE;
    }
    
    @Override
    protected ActivateJobCmd getNextCommand(final UpdateJobSuspensionStateBuilderImpl jobCommandBuilder) {
        return new ActivateJobCmd(jobCommandBuilder);
    }
    
    @Override
    protected String getLogEntryOperation() {
        return "Activate";
    }
}
