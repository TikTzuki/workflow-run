// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import org.zik.bpm.engine.impl.cmd.AbstractProcessInstanceModificationCommand;
import java.util.List;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;

public class ModificationBatchConfiguration extends BatchConfiguration
{
    protected List<AbstractProcessInstanceModificationCommand> instructions;
    protected boolean skipCustomListeners;
    protected boolean skipIoMappings;
    protected String processDefinitionId;
    
    public ModificationBatchConfiguration(final List<String> ids, final String processDefinitionId, final List<AbstractProcessInstanceModificationCommand> instructions, final boolean skipCustomListeners, final boolean skipIoMappings) {
        this(ids, null, processDefinitionId, instructions, skipCustomListeners, skipIoMappings);
    }
    
    public ModificationBatchConfiguration(final List<String> ids, final DeploymentMappings mappings, final String processDefinitionId, final List<AbstractProcessInstanceModificationCommand> instructions, final boolean skipCustomListeners, final boolean skipIoMappings) {
        super(ids, mappings);
        this.instructions = instructions;
        this.processDefinitionId = processDefinitionId;
        this.skipCustomListeners = skipCustomListeners;
        this.skipIoMappings = skipIoMappings;
    }
    
    public List<AbstractProcessInstanceModificationCommand> getInstructions() {
        return this.instructions;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public boolean isSkipCustomListeners() {
        return this.skipCustomListeners;
    }
    
    public boolean isSkipIoMappings() {
        return this.skipIoMappings;
    }
}
