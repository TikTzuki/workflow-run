// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.interceptor;

public abstract class CommandInterceptor implements CommandExecutor
{
    protected CommandExecutor next;
    
    public CommandExecutor getNext() {
        return this.next;
    }
    
    public void setNext(final CommandExecutor next) {
        this.next = next;
    }
}
