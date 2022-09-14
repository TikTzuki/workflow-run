// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.interceptor.Command;

public interface FailedJobCommandFactory
{
    Command<Object> getCommand(final String p0, final Throwable p1);
}
