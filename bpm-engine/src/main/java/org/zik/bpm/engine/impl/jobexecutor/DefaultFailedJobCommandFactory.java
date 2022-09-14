// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.cmd.DefaultJobRetryCmd;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DefaultFailedJobCommandFactory implements FailedJobCommandFactory
{
    @Override
    public Command<Object> getCommand(final String jobId, final Throwable exception) {
        return new DefaultJobRetryCmd(jobId, exception);
    }
}
