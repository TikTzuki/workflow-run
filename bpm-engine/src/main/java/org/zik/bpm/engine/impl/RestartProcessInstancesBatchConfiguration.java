// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import org.zik.bpm.engine.impl.cmd.AbstractProcessInstanceModificationCommand;
import java.util.List;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;

public class RestartProcessInstancesBatchConfiguration extends BatchConfiguration
{
    protected List<AbstractProcessInstanceModificationCommand> instructions;
    protected String processDefinitionId;
    protected boolean initialVariables;
    protected boolean skipCustomListeners;
    protected boolean skipIoMappings;
    protected boolean withoutBusinessKey;
    
    public RestartProcessInstancesBatchConfiguration(final List<String> processInstanceIds, final List<AbstractProcessInstanceModificationCommand> instructions, final String processDefinitionId, final boolean initialVariables, final boolean skipCustomListeners, final boolean skipIoMappings, final boolean withoutBusinessKey) {
        this(processInstanceIds, null, instructions, processDefinitionId, initialVariables, skipCustomListeners, skipIoMappings, withoutBusinessKey);
    }
    
    public RestartProcessInstancesBatchConfiguration(final List<String> processInstanceIds, final DeploymentMappings mappings, final List<AbstractProcessInstanceModificationCommand> instructions, final String processDefinitionId, final boolean initialVariables, final boolean skipCustomListeners, final boolean skipIoMappings, final boolean withoutBusinessKey) {
        super(processInstanceIds, mappings);
        this.instructions = instructions;
        this.processDefinitionId = processDefinitionId;
        this.initialVariables = initialVariables;
        this.skipCustomListeners = skipCustomListeners;
        this.skipIoMappings = skipIoMappings;
        this.withoutBusinessKey = withoutBusinessKey;
    }
    
    public List<AbstractProcessInstanceModificationCommand> getInstructions() {
        return this.instructions;
    }
    
    public void setInstructions(final List<AbstractProcessInstanceModificationCommand> instructions) {
        this.instructions = instructions;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public void setProcessDefinitionId(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    public boolean isInitialVariables() {
        return this.initialVariables;
    }
    
    public void setInitialVariables(final boolean initialVariables) {
        this.initialVariables = initialVariables;
    }
    
    public boolean isSkipCustomListeners() {
        return this.skipCustomListeners;
    }
    
    public void setSkipCustomListeners(final boolean skipCustomListeners) {
        this.skipCustomListeners = skipCustomListeners;
    }
    
    public boolean isSkipIoMappings() {
        return this.skipIoMappings;
    }
    
    public void setSkipIoMappings(final boolean skipIoMappings) {
        this.skipIoMappings = skipIoMappings;
    }
    
    public boolean isWithoutBusinessKey() {
        return this.withoutBusinessKey;
    }
    
    public void setWithoutBusinessKey(final boolean withoutBusinessKey) {
        this.withoutBusinessKey = withoutBusinessKey;
    }
}
