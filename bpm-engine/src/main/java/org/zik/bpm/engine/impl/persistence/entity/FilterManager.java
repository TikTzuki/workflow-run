// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import java.util.List;
import org.zik.bpm.engine.impl.filter.FilterQueryImpl;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.Validator;
import org.zik.bpm.engine.impl.QueryValidators;
import org.zik.bpm.engine.impl.AbstractQuery;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.authorization.Permissions;
import org.zik.bpm.engine.filter.Filter;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class FilterManager extends AbstractManager
{
    public Filter createNewFilter(final String resourceType) {
        this.checkAuthorization(Permissions.CREATE, Resources.FILTER, "*");
        return new FilterEntity(resourceType);
    }
    
    public Filter insertOrUpdateFilter(final Filter filter) {
        final AbstractQuery<?, ?> query = filter.getQuery();
        query.validate((Validator<AbstractQuery<?, ?>>)QueryValidators.StoredQueryValidator.get());
        if (filter.getId() == null) {
            this.checkAuthorization(Permissions.CREATE, Resources.FILTER, "*");
            this.getDbEntityManager().insert((DbEntity)filter);
            this.createDefaultAuthorizations(filter);
        }
        else {
            this.checkAuthorization(Permissions.UPDATE, Resources.FILTER, filter.getId());
            this.getDbEntityManager().merge((DbEntity)filter);
        }
        return filter;
    }
    
    public void deleteFilter(final String filterId) {
        this.checkAuthorization(Permissions.DELETE, Resources.FILTER, filterId);
        final FilterEntity filter = this.findFilterByIdInternal(filterId);
        EnsureUtil.ensureNotNull("No filter found for filter id '" + filterId + "'", "filter", filter);
        this.deleteAuthorizations(Resources.FILTER, filterId);
        this.getDbEntityManager().delete(filter);
    }
    
    public FilterEntity findFilterById(final String filterId) {
        EnsureUtil.ensureNotNull("Invalid filter id", "filterId", filterId);
        this.checkAuthorization(Permissions.READ, Resources.FILTER, filterId);
        return this.findFilterByIdInternal(filterId);
    }
    
    protected FilterEntity findFilterByIdInternal(final String filterId) {
        return this.getDbEntityManager().selectById(FilterEntity.class, filterId);
    }
    
    public List<Filter> findFiltersByQueryCriteria(final FilterQueryImpl filterQuery) {
        this.configureQuery(filterQuery, Resources.FILTER);
        return (List<Filter>)this.getDbEntityManager().selectList("selectFilterByQueryCriteria", filterQuery);
    }
    
    public long findFilterCountByQueryCriteria(final FilterQueryImpl filterQuery) {
        this.configureQuery(filterQuery, Resources.FILTER);
        return (long)this.getDbEntityManager().selectOne("selectFilterCountByQueryCriteria", filterQuery);
    }
    
    protected void createDefaultAuthorizations(final Filter filter) {
        if (this.isAuthorizationEnabled()) {
            this.saveDefaultAuthorizations(this.getResourceAuthorizationProvider().newFilter(filter));
        }
    }
}
