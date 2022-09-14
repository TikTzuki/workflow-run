// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db;

public class IdBlock
{
    long nextId;
    long lastId;
    
    public IdBlock(final long nextId, final long lastId) {
        this.nextId = nextId;
        this.lastId = lastId;
    }
    
    public long getNextId() {
        return this.nextId;
    }
    
    public long getLastId() {
        return this.lastId;
    }
}
