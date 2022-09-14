// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.removaltime;

import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import java.util.List;
import java.util.Date;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;

public class SetRemovalTimeBatchConfiguration extends BatchConfiguration
{
    protected Date removalTime;
    protected boolean hasRemovalTime;
    protected boolean isHierarchical;
    
    public SetRemovalTimeBatchConfiguration(final List<String> ids) {
        this(ids, null);
    }
    
    public SetRemovalTimeBatchConfiguration(final List<String> ids, final DeploymentMappings mappings) {
        super(ids, mappings);
    }
    
    public Date getRemovalTime() {
        return this.removalTime;
    }
    
    public SetRemovalTimeBatchConfiguration setRemovalTime(final Date removalTime) {
        this.removalTime = removalTime;
        return this;
    }
    
    public boolean hasRemovalTime() {
        return this.hasRemovalTime;
    }
    
    public SetRemovalTimeBatchConfiguration setHasRemovalTime(final boolean hasRemovalTime) {
        this.hasRemovalTime = hasRemovalTime;
        return this;
    }
    
    public boolean isHierarchical() {
        return this.isHierarchical;
    }
    
    public SetRemovalTimeBatchConfiguration setHierarchical(final boolean hierarchical) {
        this.isHierarchical = hierarchical;
        return this;
    }
}
