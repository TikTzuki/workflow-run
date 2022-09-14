// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.cmd.ActivateProcessDefinitionCmd;
import org.zik.bpm.engine.impl.cmd.AbstractSetProcessDefinitionStateCmd;

public class TimerActivateProcessDefinitionHandler extends TimerChangeProcessDefinitionSuspensionStateJobHandler
{
    public static final String TYPE = "activate-processdefinition";
    
    @Override
    public String getType() {
        return "activate-processdefinition";
    }
    
    @Override
    protected AbstractSetProcessDefinitionStateCmd getCommand(final ProcessDefinitionSuspensionStateConfiguration configuration) {
        return new ActivateProcessDefinitionCmd(configuration.createBuilder());
    }
}
