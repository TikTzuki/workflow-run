// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.filter;

import org.zik.bpm.engine.query.Query;

public interface FilterQuery extends Query<FilterQuery, Filter>
{
    FilterQuery filterId(final String p0);
    
    FilterQuery filterResourceType(final String p0);
    
    FilterQuery filterName(final String p0);
    
    FilterQuery filterNameLike(final String p0);
    
    FilterQuery filterOwner(final String p0);
    
    FilterQuery orderByFilterId();
    
    FilterQuery orderByFilterResourceType();
    
    FilterQuery orderByFilterName();
    
    FilterQuery orderByFilterOwner();
}
