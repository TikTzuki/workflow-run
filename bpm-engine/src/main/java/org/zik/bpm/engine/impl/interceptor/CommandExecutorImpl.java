// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.interceptor;

import org.zik.bpm.engine.impl.context.Context;

public class CommandExecutorImpl extends CommandInterceptor
{
    @Override
    public <T> T execute(final Command<T> command) {
        return command.execute(Context.getCommandContext());
    }
}
