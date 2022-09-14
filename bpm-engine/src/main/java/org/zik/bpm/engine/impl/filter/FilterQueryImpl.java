// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.filter;

import java.util.List;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.filter.Filter;
import org.zik.bpm.engine.filter.FilterQuery;
import org.zik.bpm.engine.impl.AbstractQuery;

public class FilterQueryImpl extends AbstractQuery<FilterQuery, Filter> implements FilterQuery
{
    private static final long serialVersionUID = 1L;
    protected String filterId;
    protected String resourceType;
    protected String name;
    protected String nameLike;
    protected String owner;
    
    public FilterQueryImpl() {
    }
    
    public FilterQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public FilterQuery filterId(final String filterId) {
        EnsureUtil.ensureNotNull("filterId", (Object)filterId);
        this.filterId = filterId;
        return this;
    }
    
    @Override
    public FilterQuery filterResourceType(final String resourceType) {
        EnsureUtil.ensureNotNull("resourceType", (Object)resourceType);
        this.resourceType = resourceType;
        return this;
    }
    
    @Override
    public FilterQuery filterName(final String name) {
        EnsureUtil.ensureNotNull("name", (Object)name);
        this.name = name;
        return this;
    }
    
    @Override
    public FilterQuery filterNameLike(final String nameLike) {
        EnsureUtil.ensureNotNull("nameLike", (Object)nameLike);
        this.nameLike = nameLike;
        return this;
    }
    
    @Override
    public FilterQuery filterOwner(final String owner) {
        EnsureUtil.ensureNotNull("owner", (Object)owner);
        this.owner = owner;
        return this;
    }
    
    @Override
    public FilterQuery orderByFilterId() {
        return ((AbstractQuery<FilterQuery, U>)this).orderBy(FilterQueryProperty.FILTER_ID);
    }
    
    @Override
    public FilterQuery orderByFilterResourceType() {
        return ((AbstractQuery<FilterQuery, U>)this).orderBy(FilterQueryProperty.RESOURCE_TYPE);
    }
    
    @Override
    public FilterQuery orderByFilterName() {
        return ((AbstractQuery<FilterQuery, U>)this).orderBy(FilterQueryProperty.NAME);
    }
    
    @Override
    public FilterQuery orderByFilterOwner() {
        return ((AbstractQuery<FilterQuery, U>)this).orderBy(FilterQueryProperty.OWNER);
    }
    
    @Override
    public List<Filter> executeList(final CommandContext commandContext, final Page page) {
        return commandContext.getFilterManager().findFiltersByQueryCriteria(this);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        return commandContext.getFilterManager().findFilterCountByQueryCriteria(this);
    }
}
