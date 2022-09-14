// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch;

import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;

public class BatchJobConfiguration implements JobHandlerConfiguration
{
    protected String configurationByteArrayId;
    
    public BatchJobConfiguration(final String configurationByteArrayId) {
        this.configurationByteArrayId = configurationByteArrayId;
    }
    
    public String getConfigurationByteArrayId() {
        return this.configurationByteArrayId;
    }
    
    @Override
    public String toCanonicalString() {
        return this.configurationByteArrayId;
    }
}
