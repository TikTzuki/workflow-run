// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.identity.WritableIdentityProvider;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class IsIdentityServiceReadOnlyCmd implements Command<Boolean>
{
    @Override
    public Boolean execute(final CommandContext commandContext) {
        return !commandContext.getSessionFactories().containsKey(WritableIdentityProvider.class);
    }
}
