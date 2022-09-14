// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.management.UpdateJobSuspensionStateBuilderImpl;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import org.zik.bpm.engine.impl.management.UpdateJobDefinitionSuspensionStateBuilderImpl;

public class SuspendJobDefinitionCmd extends AbstractSetJobDefinitionStateCmd
{
    public SuspendJobDefinitionCmd(final UpdateJobDefinitionSuspensionStateBuilderImpl builder) {
        super(builder);
    }
    
    @Override
    protected SuspensionState getNewSuspensionState() {
        return SuspensionState.SUSPENDED;
    }
    
    @Override
    protected String getDelayedExecutionJobHandlerType() {
        return "suspend-job-definition";
    }
    
    @Override
    protected SuspendJobCmd getNextCommand(final UpdateJobSuspensionStateBuilderImpl jobCommandBuilder) {
        return new SuspendJobCmd(jobCommandBuilder);
    }
    
    @Override
    protected String getLogEntryOperation() {
        return "SuspendJobDefinition";
    }
}
