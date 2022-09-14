// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.runtime.UpdateProcessInstanceSuspensionStateBuilderImpl;
import org.zik.bpm.engine.impl.management.UpdateJobDefinitionSuspensionStateBuilderImpl;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import org.zik.bpm.engine.impl.repository.UpdateProcessDefinitionSuspensionStateBuilderImpl;

public class SuspendProcessDefinitionCmd extends AbstractSetProcessDefinitionStateCmd
{
    public SuspendProcessDefinitionCmd(final UpdateProcessDefinitionSuspensionStateBuilderImpl builder) {
        super(builder);
    }
    
    @Override
    protected SuspensionState getNewSuspensionState() {
        return SuspensionState.SUSPENDED;
    }
    
    @Override
    protected String getDelayedExecutionJobHandlerType() {
        return "suspend-processdefinition";
    }
    
    @Override
    protected AbstractSetJobDefinitionStateCmd getSetJobDefinitionStateCmd(final UpdateJobDefinitionSuspensionStateBuilderImpl jobDefinitionSuspensionStateBuilder) {
        return new SuspendJobDefinitionCmd(jobDefinitionSuspensionStateBuilder);
    }
    
    @Override
    protected SuspendProcessInstanceCmd getNextCommand(final UpdateProcessInstanceSuspensionStateBuilderImpl processInstanceCommandBuilder) {
        return new SuspendProcessInstanceCmd(processInstanceCommandBuilder);
    }
    
    @Override
    protected String getLogEntryOperation() {
        return "SuspendProcessDefinition";
    }
}
