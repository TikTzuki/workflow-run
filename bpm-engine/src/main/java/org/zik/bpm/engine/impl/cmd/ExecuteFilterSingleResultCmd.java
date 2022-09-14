// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.query.Query;
import org.zik.bpm.engine.impl.interceptor.Command;

public class ExecuteFilterSingleResultCmd extends AbstractExecuteFilterCmd implements Command<Object>
{
    private static final long serialVersionUID = 1L;
    
    public ExecuteFilterSingleResultCmd(final String filterId) {
        super(filterId);
    }
    
    public ExecuteFilterSingleResultCmd(final String filterId, final Query<?, ?> extendingQuery) {
        super(filterId, extendingQuery);
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        final Query<?, ?> query = this.getFilterQuery(commandContext);
        return query.singleResult();
    }
}
