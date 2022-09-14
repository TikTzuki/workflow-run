// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch;

import java.util.List;

public class BatchConfiguration
{
    protected List<String> ids;
    protected DeploymentMappings idMappings;
    protected boolean failIfNotExists;
    protected String batchId;
    
    public BatchConfiguration(final List<String> ids) {
        this(ids, true);
    }
    
    public BatchConfiguration(final List<String> ids, final boolean failIfNotExists) {
        this(ids, null, failIfNotExists);
    }
    
    public BatchConfiguration(final List<String> ids, final DeploymentMappings mappings) {
        this(ids, mappings, true);
    }
    
    public BatchConfiguration(final List<String> ids, final DeploymentMappings mappings, final boolean failIfNotExists) {
        this.ids = ids;
        this.idMappings = mappings;
        this.failIfNotExists = failIfNotExists;
    }
    
    public BatchConfiguration(final List<String> ids, final DeploymentMappings mappings, final String batchId) {
        this.ids = ids;
        this.idMappings = mappings;
        this.batchId = batchId;
    }
    
    public List<String> getIds() {
        return this.ids;
    }
    
    public void setIds(final List<String> ids) {
        this.ids = ids;
    }
    
    public DeploymentMappings getIdMappings() {
        return this.idMappings;
    }
    
    public void setIdMappings(final DeploymentMappings idMappings) {
        this.idMappings = idMappings;
    }
    
    public boolean isFailIfNotExists() {
        return this.failIfNotExists;
    }
    
    public void setFailIfNotExists(final boolean failIfNotExists) {
        this.failIfNotExists = failIfNotExists;
    }
    
    public String getBatchId() {
        return this.batchId;
    }
    
    public void setBatchId(final String batchId) {
        this.batchId = batchId;
    }
}
