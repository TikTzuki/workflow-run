// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch;

import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;

public class BatchJobContext
{
    protected BatchEntity batch;
    protected ByteArrayEntity configuration;
    
    public BatchJobContext(final BatchEntity batchEntity, final ByteArrayEntity configuration) {
        this.batch = batchEntity;
        this.configuration = configuration;
    }
    
    public BatchEntity getBatch() {
        return this.batch;
    }
    
    public void setBatch(final BatchEntity batch) {
        this.batch = batch;
    }
    
    public ByteArrayEntity getConfiguration() {
        return this.configuration;
    }
    
    public void setConfiguration(final ByteArrayEntity configuration) {
        this.configuration = configuration;
    }
}
