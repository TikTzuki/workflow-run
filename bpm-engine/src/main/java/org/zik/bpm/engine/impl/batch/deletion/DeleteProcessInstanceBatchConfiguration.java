// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.deletion;

import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import java.util.List;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;

public class DeleteProcessInstanceBatchConfiguration extends BatchConfiguration
{
    protected String deleteReason;
    protected boolean skipCustomListeners;
    protected boolean skipSubprocesses;
    
    public DeleteProcessInstanceBatchConfiguration(final List<String> ids, final DeploymentMappings mappings, final boolean skipCustomListeners, final boolean skipSubprocesses) {
        this(ids, mappings, null, skipCustomListeners, skipSubprocesses, true);
    }
    
    public DeleteProcessInstanceBatchConfiguration(final List<String> ids, final DeploymentMappings mappings, final String deleteReason, final boolean skipCustomListeners) {
        this(ids, mappings, deleteReason, skipCustomListeners, true, true);
    }
    
    public DeleteProcessInstanceBatchConfiguration(final List<String> ids, final DeploymentMappings mappings, final String deleteReason, final boolean skipCustomListeners, final boolean skipSubprocesses, final boolean failIfNotExists) {
        super(ids, mappings);
        this.deleteReason = deleteReason;
        this.skipCustomListeners = skipCustomListeners;
        this.skipSubprocesses = skipSubprocesses;
        this.failIfNotExists = failIfNotExists;
    }
    
    public String getDeleteReason() {
        return this.deleteReason;
    }
    
    public void setDeleteReason(final String deleteReason) {
        this.deleteReason = deleteReason;
    }
    
    public boolean isSkipCustomListeners() {
        return this.skipCustomListeners;
    }
    
    public boolean isSkipSubprocesses() {
        return this.skipSubprocesses;
    }
    
    public void setSkipSubprocesses(final boolean skipSubprocesses) {
        this.skipSubprocesses = skipSubprocesses;
    }
}
