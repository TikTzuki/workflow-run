// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.filter.Filter;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SaveFilterCmd implements Command<Filter>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected Filter filter;
    
    public SaveFilterCmd(final Filter filter) {
        this.filter = filter;
    }
    
    @Override
    public Filter execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("filter", this.filter);
        final String operation = (this.filter.getId() == null) ? "Create" : "Update";
        final Filter savedFilter = commandContext.getFilterManager().insertOrUpdateFilter(this.filter);
        commandContext.getOperationLogManager().logFilterOperation(operation, this.filter.getId());
        return savedFilter;
    }
}
