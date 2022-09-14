// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

import java.io.Serializable;

public abstract class DiagramElement implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected String id;
    
    public DiagramElement() {
        this.id = null;
    }
    
    public DiagramElement(final String id) {
        this.id = null;
        this.id = id;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return "id=" + this.getId();
    }
    
    public abstract boolean isNode();
    
    public abstract boolean isEdge();
}
