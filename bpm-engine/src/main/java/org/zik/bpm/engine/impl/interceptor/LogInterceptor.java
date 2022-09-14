// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.interceptor;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cmd.CommandLogger;

public class LogInterceptor extends CommandInterceptor
{
    private static final CommandLogger LOG;
    
    @Override
    public <T> T execute(final Command<T> command) {
        LogInterceptor.LOG.debugStartingCommand(command);
        try {
            return this.next.execute(command);
        }
        finally {
            LogInterceptor.LOG.debugFinishingCommand(command);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
