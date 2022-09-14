// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.management;

public interface TablePageQuery
{
    TablePageQuery tableName(final String p0);
    
    TablePageQuery orderAsc(final String p0);
    
    TablePageQuery orderDesc(final String p0);
    
    TablePage listPage(final int p0, final int p1);
}
