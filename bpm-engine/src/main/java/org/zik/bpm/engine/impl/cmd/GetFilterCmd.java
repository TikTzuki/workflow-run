// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.filter.Filter;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetFilterCmd implements Command<Filter>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String filterId;
    
    public GetFilterCmd(final String filterId) {
        this.filterId = filterId;
    }
    
    @Override
    public Filter execute(final CommandContext commandContext) {
        return commandContext.getFilterManager().findFilterById(this.filterId);
    }
}
