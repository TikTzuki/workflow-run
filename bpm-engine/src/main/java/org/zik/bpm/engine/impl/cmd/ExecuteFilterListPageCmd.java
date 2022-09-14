// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.AbstractQuery;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.query.Query;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class ExecuteFilterListPageCmd extends AbstractExecuteFilterCmd implements Command<List<?>>
{
    private static final long serialVersionUID = 1L;
    protected int firstResult;
    protected int maxResults;
    
    public ExecuteFilterListPageCmd(final String filterId, final int firstResult, final int maxResults) {
        super(filterId);
        this.firstResult = firstResult;
        this.maxResults = maxResults;
    }
    
    public ExecuteFilterListPageCmd(final String filterId, final Query<?, ?> extendingQuery, final int firstResult, final int maxResults) {
        super(filterId, extendingQuery);
        this.firstResult = firstResult;
        this.maxResults = maxResults;
    }
    
    @Override
    public List<?> execute(final CommandContext commandContext) {
        final Query<?, ?> query = this.getFilterQuery(commandContext);
        ((AbstractQuery)query).enableMaxResultsLimit();
        return query.listPage(this.firstResult, this.maxResults);
    }
}
