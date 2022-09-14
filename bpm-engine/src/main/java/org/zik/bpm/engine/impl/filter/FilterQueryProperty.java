// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.filter;

import org.zik.bpm.engine.impl.QueryPropertyImpl;
import org.zik.bpm.engine.query.QueryProperty;

public interface FilterQueryProperty
{
    public static final QueryProperty FILTER_ID = new QueryPropertyImpl("ID_");
    public static final QueryProperty RESOURCE_TYPE = new QueryPropertyImpl("RESOURCE_TYPE_");
    public static final QueryProperty NAME = new QueryPropertyImpl("NAME_");
    public static final QueryProperty OWNER = new QueryPropertyImpl("OWNER_");
    public static final QueryProperty QUERY = new QueryPropertyImpl("QUERY_");
    public static final QueryProperty PROPERTIES = new QueryPropertyImpl("PROPERTIES_");
}
