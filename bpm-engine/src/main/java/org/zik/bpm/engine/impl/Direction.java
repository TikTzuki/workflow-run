// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.HashMap;
import java.util.Map;

public class Direction
{
    private static final Map<String, Direction> directions;
    public static final Direction ASCENDING;
    public static final Direction DESCENDING;
    private String name;
    
    public Direction(final String name) {
        this.name = name;
        Direction.directions.put(name, this);
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return "Direction[name=" + this.name + "]";
    }
    
    public static Direction findByName(final String directionName) {
        return Direction.directions.get(directionName);
    }
    
    static {
        directions = new HashMap<String, Direction>();
        ASCENDING = new Direction("asc");
        DESCENDING = new Direction("desc");
    }
}
