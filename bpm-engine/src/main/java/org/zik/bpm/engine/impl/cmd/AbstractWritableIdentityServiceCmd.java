// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.identity.WritableIdentityProvider;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public abstract class AbstractWritableIdentityServiceCmd<T> implements Command<T>, Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Override
    public final T execute(final CommandContext commandContext) {
        if (!commandContext.getSessionFactories().containsKey(WritableIdentityProvider.class)) {
            throw new UnsupportedOperationException("This identity service implementation is read-only.");
        }
        final T result = this.executeCmd(commandContext);
        return result;
    }
    
    protected abstract T executeCmd(final CommandContext p0);
}
