// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.cmd.ActivateJobDefinitionCmd;
import org.zik.bpm.engine.impl.cmd.AbstractSetJobDefinitionStateCmd;

public class TimerActivateJobDefinitionHandler extends TimerChangeJobDefinitionSuspensionStateJobHandler
{
    public static final String TYPE = "activate-job-definition";
    
    @Override
    public String getType() {
        return "activate-job-definition";
    }
    
    @Override
    protected AbstractSetJobDefinitionStateCmd getCommand(final JobDefinitionSuspensionStateConfiguration configuration) {
        return new ActivateJobDefinitionCmd(configuration.createBuilder());
    }
}
