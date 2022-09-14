// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.interceptor;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.CrdbTransactionRetryException;
import org.zik.bpm.engine.impl.cmd.CommandLogger;

public class CrdbTransactionRetryInterceptor extends CommandInterceptor
{
    private static final CommandLogger LOG;
    protected int retries;
    
    public CrdbTransactionRetryInterceptor(final int retries) {
        this.retries = retries;
    }
    
    @Override
    public <T> T execute(final Command<T> command) {
        int remainingTries = this.retries + 1;
        while (remainingTries > 0) {
            try {
                return this.next.execute(command);
            }
            catch (CrdbTransactionRetryException e) {
                --remainingTries;
                if (!this.isRetryable(command) || remainingTries == 0) {
                    throw e;
                }
                CrdbTransactionRetryInterceptor.LOG.crdbTransactionRetryAttempt(e);
                continue;
            }
        }
        return null;
    }
    
    protected boolean isRetryable(final Command<?> command) {
        return command.isRetryable();
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
