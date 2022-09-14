// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.task.TaskQuery;
import org.zik.bpm.engine.impl.persistence.entity.FilterEntity;
import org.zik.bpm.engine.impl.AbstractQuery;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.filter.Filter;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.query.Query;
import java.io.Serializable;

public abstract class AbstractExecuteFilterCmd implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected String filterId;
    protected Query<?, ?> extendingQuery;
    
    public AbstractExecuteFilterCmd(final String filterId) {
        this.filterId = filterId;
    }
    
    public AbstractExecuteFilterCmd(final String filterId, final Query<?, ?> extendingQuery) {
        this.filterId = filterId;
        this.extendingQuery = extendingQuery;
    }
    
    protected Filter getFilter(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("No filter id given to execute", "filterId", this.filterId);
        FilterEntity filter = commandContext.getFilterManager().findFilterById(this.filterId);
        EnsureUtil.ensureNotNull("No filter found for id '" + this.filterId + "'", "filter", filter);
        if (this.extendingQuery != null) {
            ((AbstractQuery)this.extendingQuery).validate();
            filter = (FilterEntity)filter.extend(this.extendingQuery);
        }
        return filter;
    }
    
    protected Query<?, ?> getFilterQuery(final CommandContext commandContext) {
        final Filter filter = this.getFilter(commandContext);
        final Query<?, ?> query = filter.getQuery();
        if (query instanceof TaskQuery) {
            ((TaskQuery)query).initializeFormKeys();
        }
        return query;
    }
}
