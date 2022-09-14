// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import org.zik.bpm.engine.query.Query;
import java.util.List;
import org.zik.bpm.engine.filter.FilterQuery;
import org.zik.bpm.engine.filter.Filter;

public interface FilterService
{
    Filter newTaskFilter();
    
    Filter newTaskFilter(final String p0);
    
    FilterQuery createFilterQuery();
    
    FilterQuery createTaskFilterQuery();
    
    Filter saveFilter(final Filter p0);
    
    Filter getFilter(final String p0);
    
    void deleteFilter(final String p0);
    
     <T> List<T> list(final String p0);
    
     <T, Q extends Query<?, T>> List<T> list(final String p0, final Q p1);
    
     <T> List<T> listPage(final String p0, final int p1, final int p2);
    
     <T, Q extends Query<?, T>> List<T> listPage(final String p0, final Q p1, final int p2, final int p3);
    
     <T> T singleResult(final String p0);
    
     <T, Q extends Query<?, T>> T singleResult(final String p0, final Q p1);
    
    Long count(final String p0);
    
    Long count(final String p0, final Query<?, ?> p1);
}
