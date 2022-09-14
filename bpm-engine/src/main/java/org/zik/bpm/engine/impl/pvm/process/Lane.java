// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.process;

import java.util.ArrayList;
import java.util.List;

public class Lane implements HasDIBounds
{
    protected String id;
    protected String name;
    protected List<String> flowNodeIds;
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    
    public Lane() {
        this.x = -1;
        this.y = -1;
        this.width = -1;
        this.height = -1;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public int getX() {
        return this.x;
    }
    
    @Override
    public void setX(final int x) {
        this.x = x;
    }
    
    @Override
    public int getY() {
        return this.y;
    }
    
    @Override
    public void setY(final int y) {
        this.y = y;
    }
    
    @Override
    public int getWidth() {
        return this.width;
    }
    
    @Override
    public void setWidth(final int width) {
        this.width = width;
    }
    
    @Override
    public int getHeight() {
        return this.height;
    }
    
    @Override
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public List<String> getFlowNodeIds() {
        if (this.flowNodeIds == null) {
            this.flowNodeIds = new ArrayList<String>();
        }
        return this.flowNodeIds;
    }
}
