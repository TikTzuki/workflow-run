// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.AbstractQuery;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.query.Query;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class ExecuteFilterListCmd extends AbstractExecuteFilterCmd implements Command<List<?>>
{
    private static final long serialVersionUID = 1L;
    
    public ExecuteFilterListCmd(final String filterId) {
        super(filterId);
    }
    
    public ExecuteFilterListCmd(final String filterId, final Query<?, ?> extendingQuery) {
        super(filterId, extendingQuery);
    }
    
    @Override
    public List<?> execute(final CommandContext commandContext) {
        final Query<?, ?> query = this.getFilterQuery(commandContext);
        ((AbstractQuery)query).enableMaxResultsLimit();
        return query.list();
    }
}
