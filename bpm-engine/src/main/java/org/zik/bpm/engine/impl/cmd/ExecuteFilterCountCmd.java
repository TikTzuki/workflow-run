// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.filter.Filter;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.query.Query;
import org.zik.bpm.engine.impl.interceptor.Command;

public class ExecuteFilterCountCmd extends AbstractExecuteFilterCmd implements Command<Long>
{
    private static final long serialVersionUID = 1L;
    
    public ExecuteFilterCountCmd(final String filterId) {
        super(filterId);
    }
    
    public ExecuteFilterCountCmd(final String filterId, final Query<?, ?> extendingQuery) {
        super(filterId, extendingQuery);
    }
    
    @Override
    public Long execute(final CommandContext commandContext) {
        final Filter filter = this.getFilter(commandContext);
        return filter.getQuery().count();
    }
}
