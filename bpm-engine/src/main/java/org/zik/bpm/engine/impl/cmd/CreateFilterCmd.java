// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.filter.Filter;
import org.zik.bpm.engine.impl.interceptor.Command;

public class CreateFilterCmd implements Command<Filter>
{
    protected String resourceType;
    
    public CreateFilterCmd(final String resourceType) {
        this.resourceType = resourceType;
    }
    
    @Override
    public Filter execute(final CommandContext commandContext) {
        return commandContext.getFilterManager().createNewFilter(this.resourceType);
    }
}
