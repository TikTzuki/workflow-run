// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db;

public interface HasDbRevision
{
    void setRevision(final int p0);
    
    int getRevision();
    
    int getRevisionNext();
}
