// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

public class DiagramNode extends DiagramElement
{
    private static final long serialVersionUID = 1L;
    private Double x;
    private Double y;
    private Double width;
    private Double height;
    
    public DiagramNode() {
        this.x = null;
        this.y = null;
        this.width = null;
        this.height = null;
    }
    
    public DiagramNode(final String id) {
        super(id);
        this.x = null;
        this.y = null;
        this.width = null;
        this.height = null;
    }
    
    public DiagramNode(final String id, final Double x, final Double y, final Double width, final Double height) {
        super(id);
        this.x = null;
        this.y = null;
        this.width = null;
        this.height = null;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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
    
    public Double getWidth() {
        return this.width;
    }
    
    public void setWidth(final Double width) {
        this.width = width;
    }
    
    public Double getHeight() {
        return this.height;
    }
    
    public void setHeight(final Double height) {
        this.height = height;
    }
    
    @Override
    public String toString() {
        return super.toString() + ", x=" + this.getX() + ", y=" + this.getY() + ", width=" + this.getWidth() + ", height=" + this.getHeight();
    }
    
    @Override
    public boolean isNode() {
        return true;
    }
    
    @Override
    public boolean isEdge() {
        return false;
    }
}
