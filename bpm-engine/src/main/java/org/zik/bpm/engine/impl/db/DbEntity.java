// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db;

public interface DbEntity
{
    String getId();
    
    void setId(final String p0);
    
    Object getPersistentState();
}
