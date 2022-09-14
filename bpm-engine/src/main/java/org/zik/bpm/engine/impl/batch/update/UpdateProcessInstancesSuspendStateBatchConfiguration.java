// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.update;

import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import java.util.List;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;

public class UpdateProcessInstancesSuspendStateBatchConfiguration extends BatchConfiguration
{
    protected final boolean suspended;
    
    public UpdateProcessInstancesSuspendStateBatchConfiguration(final List<String> ids, final boolean suspended) {
        this(ids, null, suspended);
    }
    
    public UpdateProcessInstancesSuspendStateBatchConfiguration(final List<String> ids, final DeploymentMappings mappings, final boolean suspended) {
        super(ids, mappings);
        this.suspended = suspended;
    }
    
    boolean getSuspended() {
        return this.suspended;
    }
}
