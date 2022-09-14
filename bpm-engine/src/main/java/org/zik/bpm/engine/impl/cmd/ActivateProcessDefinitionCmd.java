// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.runtime.UpdateProcessInstanceSuspensionStateBuilderImpl;
import org.zik.bpm.engine.impl.management.UpdateJobDefinitionSuspensionStateBuilderImpl;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import org.zik.bpm.engine.impl.repository.UpdateProcessDefinitionSuspensionStateBuilderImpl;

public class ActivateProcessDefinitionCmd extends AbstractSetProcessDefinitionStateCmd
{
    public ActivateProcessDefinitionCmd(final UpdateProcessDefinitionSuspensionStateBuilderImpl builder) {
        super(builder);
    }
    
    @Override
    protected SuspensionState getNewSuspensionState() {
        return SuspensionState.ACTIVE;
    }
    
    @Override
    protected String getDelayedExecutionJobHandlerType() {
        return "activate-processdefinition";
    }
    
    @Override
    protected AbstractSetJobDefinitionStateCmd getSetJobDefinitionStateCmd(final UpdateJobDefinitionSuspensionStateBuilderImpl jobDefinitionSuspensionStateBuilder) {
        return new ActivateJobDefinitionCmd(jobDefinitionSuspensionStateBuilder);
    }
    
    @Override
    protected ActivateProcessInstanceCmd getNextCommand(final UpdateProcessInstanceSuspensionStateBuilderImpl processInstanceCommandBuilder) {
        return new ActivateProcessInstanceCmd(processInstanceCommandBuilder);
    }
    
    @Override
    protected String getLogEntryOperation() {
        return "ActivateProcessDefinition";
    }
}
