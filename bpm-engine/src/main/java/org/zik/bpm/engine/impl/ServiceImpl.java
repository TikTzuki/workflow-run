// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.interceptor.CommandExecutor;

public class ServiceImpl
{
    protected CommandExecutor commandExecutor;
    
    public CommandExecutor getCommandExecutor() {
        return this.commandExecutor;
    }
    
    public void setCommandExecutor(final CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }
}
