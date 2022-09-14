// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch;

import java.util.ArrayList;

public class DeploymentMappings extends ArrayList<DeploymentMapping>
{
    private static final long serialVersionUID = -868922966819588407L;
    protected int overallIdCount;
    
    public static DeploymentMappings of(final DeploymentMapping mapping) {
        final DeploymentMappings mappings = new DeploymentMappings();
        mappings.add(mapping);
        return mappings;
    }
    
    @Override
    public boolean add(final DeploymentMapping mapping) {
        this.overallIdCount += mapping.getCount();
        return super.add(mapping);
    }
    
    @Override
    public DeploymentMapping remove(final int mappingIndex) {
        this.overallIdCount -= this.get(mappingIndex).getCount();
        return super.remove(mappingIndex);
    }
    
    @Override
    public boolean remove(final Object mapping) {
        if (super.remove(mapping)) {
            this.overallIdCount -= ((DeploymentMapping)mapping).getCount();
            return true;
        }
        return false;
    }
    
    public int getOverallIdCount() {
        return this.overallIdCount;
    }
}
