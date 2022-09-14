// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.filter;

import java.util.Map;
import org.zik.bpm.engine.query.Query;

public interface Filter
{
    String getId();
    
    String getResourceType();
    
    String getName();
    
    Filter setName(final String p0);
    
    String getOwner();
    
    Filter setOwner(final String p0);
    
     <T extends Query<?, ?>> T getQuery();
    
     <T extends Query<?, ?>> Filter setQuery(final T p0);
    
     <T extends Query<?, ?>> Filter extend(final T p0);
    
    Map<String, Object> getProperties();
    
    Filter setProperties(final Map<String, Object> p0);
}
