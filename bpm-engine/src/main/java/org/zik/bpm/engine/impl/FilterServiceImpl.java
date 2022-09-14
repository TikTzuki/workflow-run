// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.cmd.ExecuteFilterCountCmd;
import org.zik.bpm.engine.impl.cmd.ExecuteFilterSingleResultCmd;
import org.zik.bpm.engine.impl.cmd.ExecuteFilterListPageCmd;
import org.zik.bpm.engine.query.Query;
import org.zik.bpm.engine.impl.cmd.ExecuteFilterListCmd;
import java.util.List;
import org.zik.bpm.engine.impl.cmd.DeleteFilterCmd;
import org.zik.bpm.engine.impl.cmd.GetFilterCmd;
import org.zik.bpm.engine.impl.cmd.SaveFilterCmd;
import org.zik.bpm.engine.impl.filter.FilterQueryImpl;
import org.zik.bpm.engine.filter.FilterQuery;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.CreateFilterCmd;
import org.zik.bpm.engine.filter.Filter;
import org.zik.bpm.engine.FilterService;

public class FilterServiceImpl extends ServiceImpl implements FilterService
{
    @Override
    public Filter newTaskFilter() {
        return this.commandExecutor.execute((Command<Filter>)new CreateFilterCmd("Task"));
    }
    
    @Override
    public Filter newTaskFilter(final String filterName) {
        return this.newTaskFilter().setName(filterName);
    }
    
    @Override
    public FilterQuery createFilterQuery() {
        return new FilterQueryImpl(this.commandExecutor);
    }
    
    @Override
    public FilterQuery createTaskFilterQuery() {
        return new FilterQueryImpl(this.commandExecutor).filterResourceType("Task");
    }
    
    @Override
    public Filter saveFilter(final Filter filter) {
        return this.commandExecutor.execute((Command<Filter>)new SaveFilterCmd(filter));
    }
    
    @Override
    public Filter getFilter(final String filterId) {
        return this.commandExecutor.execute((Command<Filter>)new GetFilterCmd(filterId));
    }
    
    @Override
    public void deleteFilter(final String filterId) {
        this.commandExecutor.execute((Command<Object>)new DeleteFilterCmd(filterId));
    }
    
    @Override
    public <T> List<T> list(final String filterId) {
        return this.commandExecutor.execute((Command<List<T>>)new ExecuteFilterListCmd(filterId));
    }
    
    @Override
    public <T, Q extends Query<?, T>> List<T> list(final String filterId, final Q extendingQuery) {
        return this.commandExecutor.execute((Command<List<T>>)new ExecuteFilterListCmd(filterId, extendingQuery));
    }
    
    @Override
    public <T> List<T> listPage(final String filterId, final int firstResult, final int maxResults) {
        return this.commandExecutor.execute((Command<List<T>>)new ExecuteFilterListPageCmd(filterId, firstResult, maxResults));
    }
    
    @Override
    public <T, Q extends Query<?, T>> List<T> listPage(final String filterId, final Q extendingQuery, final int firstResult, final int maxResults) {
        return this.commandExecutor.execute((Command<List<T>>)new ExecuteFilterListPageCmd(filterId, extendingQuery, firstResult, maxResults));
    }
    
    @Override
    public <T> T singleResult(final String filterId) {
        return this.commandExecutor.execute((Command<T>)new ExecuteFilterSingleResultCmd(filterId));
    }
    
    @Override
    public <T, Q extends Query<?, T>> T singleResult(final String filterId, final Q extendingQuery) {
        return this.commandExecutor.execute((Command<T>)new ExecuteFilterSingleResultCmd(filterId, extendingQuery));
    }
    
    @Override
    public Long count(final String filterId) {
        return this.commandExecutor.execute((Command<Long>)new ExecuteFilterCountCmd(filterId));
    }
    
    @Override
    public Long count(final String filterId, final Query<?, ?> extendingQuery) {
        return this.commandExecutor.execute((Command<Long>)new ExecuteFilterCountCmd(filterId, extendingQuery));
    }
}
