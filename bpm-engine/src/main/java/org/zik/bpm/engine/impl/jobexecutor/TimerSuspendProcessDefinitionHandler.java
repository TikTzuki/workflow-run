// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.cmd.SuspendProcessDefinitionCmd;
import org.zik.bpm.engine.impl.cmd.AbstractSetProcessDefinitionStateCmd;

public class TimerSuspendProcessDefinitionHandler extends TimerChangeProcessDefinitionSuspensionStateJobHandler
{
    public static final String TYPE = "suspend-processdefinition";
    
    @Override
    public String getType() {
        return "suspend-processdefinition";
    }
    
    @Override
    protected AbstractSetProcessDefinitionStateCmd getCommand(final ProcessDefinitionSuspensionStateConfiguration configuration) {
        return new SuspendProcessDefinitionCmd(configuration.createBuilder());
    }
}
