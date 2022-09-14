// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

import java.util.List;

public class DiagramEdge extends DiagramElement
{
    private static final long serialVersionUID = 1L;
    private List<DiagramEdgeWaypoint> waypoints;
    
    public DiagramEdge() {
    }
    
    public DiagramEdge(final String id, final List<DiagramEdgeWaypoint> waypoints) {
        super(id);
        this.waypoints = waypoints;
    }
    
    @Override
    public boolean isNode() {
        return false;
    }
    
    @Override
    public boolean isEdge() {
        return true;
    }
    
    public List<DiagramEdgeWaypoint> getWaypoints() {
        return this.waypoints;
    }
    
    public void setWaypoints(final List<DiagramEdgeWaypoint> waypoints) {
        this.waypoints = waypoints;
    }
}
