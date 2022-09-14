// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.message;

import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import java.util.List;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;

public class MessageCorrelationBatchConfiguration extends BatchConfiguration
{
    protected String messageName;
    
    public MessageCorrelationBatchConfiguration(final List<String> ids, final String messageName, final String batchId) {
        this(ids, null, messageName, batchId);
    }
    
    public MessageCorrelationBatchConfiguration(final List<String> ids, final DeploymentMappings mappings, final String messageName, final String batchId) {
        super(ids, mappings);
        this.messageName = messageName;
        this.batchId = batchId;
    }
    
    public MessageCorrelationBatchConfiguration(final List<String> ids, final DeploymentMappings mappings, final String messageName) {
        this(ids, mappings, messageName, null);
    }
    
    public String getMessageName() {
        return this.messageName;
    }
    
    public void setMessageName(final String messageName) {
        this.messageName = messageName;
    }
}
