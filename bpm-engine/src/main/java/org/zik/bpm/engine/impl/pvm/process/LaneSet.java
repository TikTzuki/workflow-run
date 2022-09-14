// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.process;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class LaneSet
{
    protected String id;
    protected List<Lane> lanes;
    protected String name;
    
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
    
    public List<Lane> getLanes() {
        if (this.lanes == null) {
            this.lanes = new ArrayList<Lane>();
        }
        return this.lanes;
    }
    
    public void addLane(final Lane laneToAdd) {
        this.getLanes().add(laneToAdd);
    }
    
    public Lane getLaneForId(final String id) {
        if (this.lanes != null && this.lanes.size() > 0) {
            for (final Lane lane : this.lanes) {
                if (id.equals(lane.getId())) {
                    return lane;
                }
            }
        }
        return null;
    }
}
