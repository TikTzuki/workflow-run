// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.cmd.SuspendJobDefinitionCmd;
import org.zik.bpm.engine.impl.cmd.AbstractSetJobDefinitionStateCmd;

public class TimerSuspendJobDefinitionHandler extends TimerChangeJobDefinitionSuspensionStateJobHandler
{
    public static final String TYPE = "suspend-job-definition";
    
    @Override
    public String getType() {
        return "suspend-job-definition";
    }
    
    @Override
    protected AbstractSetJobDefinitionStateCmd getCommand(final JobDefinitionSuspensionStateConfiguration configuration) {
        return new SuspendJobDefinitionCmd(configuration.createBuilder());
    }
}
