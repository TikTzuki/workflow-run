// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch;

import java.util.List;

public class SetRetriesBatchConfiguration extends BatchConfiguration
{
    protected int retries;
    
    public SetRetriesBatchConfiguration(final List<String> ids, final int retries) {
        this(ids, null, retries);
    }
    
    public SetRetriesBatchConfiguration(final List<String> ids, final DeploymentMappings mappings, final int retries) {
        super(ids, mappings);
        this.retries = retries;
    }
    
    public int getRetries() {
        return this.retries;
    }
    
    public void setRetries(final int retries) {
        this.retries = retries;
    }
}
