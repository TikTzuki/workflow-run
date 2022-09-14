// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

import java.io.Serializable;

public class DiagramEdgeWaypoint implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Double x;
    private Double y;
    
    public DiagramEdgeWaypoint() {
        this.x = null;
        this.y = null;
    }
    
    public Double getX() {
        return this.x;
    }
    
    public void setX(final Double x) {
        this.x = x;
    }
    
    public Double getY() {
        return this.y;
    }
    
    public void setY(final Double y) {
        this.y = y;
    }
}
