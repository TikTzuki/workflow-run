// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.telemetry.dto;

import org.zik.bpm.engine.telemetry.Command;

public class CommandImpl implements Command
{
    protected long count;
    
    public CommandImpl(final long count) {
        this.count = count;
    }
    
    @Override
    public long getCount() {
        return this.count;
    }
    
    public void setCount(final long count) {
        this.count = count;
    }
}
