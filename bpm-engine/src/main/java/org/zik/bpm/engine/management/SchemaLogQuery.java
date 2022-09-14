// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.management;

import org.zik.bpm.engine.query.Query;

public interface SchemaLogQuery extends Query<SchemaLogQuery, SchemaLogEntry>
{
    SchemaLogQuery version(final String p0);
    
    SchemaLogQuery orderByTimestamp();
}
