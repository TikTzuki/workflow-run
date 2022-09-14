// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteFilterCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String filterId;
    
    public DeleteFilterCmd(final String filterId) {
        this.filterId = filterId;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        commandContext.getOperationLogManager().logFilterOperation("Delete", this.filterId);
        commandContext.getFilterManager().deleteFilter(this.filterId);
        return null;
    }
}
