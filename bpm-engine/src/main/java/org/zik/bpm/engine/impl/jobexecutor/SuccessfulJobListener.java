// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SuccessfulJobListener implements Command<Void>
{
    @Override
    public Void execute(final CommandContext commandContext) {
        this.logJobSuccess(commandContext);
        return null;
    }
    
    protected void logJobSuccess(final CommandContext commandContext) {
        if (commandContext.getProcessEngineConfiguration().isMetricsEnabled()) {
            commandContext.getProcessEngineConfiguration().getMetricsRegistry().markOccurrence("job-successful");
        }
    }
}
