// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.entitymanager.cache;

public enum DbEntityState
{
    TRANSIENT, 
    PERSISTENT, 
    MERGED, 
    DELETED_TRANSIENT, 
    DELETED_PERSISTENT, 
    DELETED_MERGED;
}
