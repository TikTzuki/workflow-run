// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.externaltask;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.FetchExternalTasksCmd;
import org.zik.bpm.engine.externaltask.LockedExternalTask;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.externaltask.ExternalTaskQueryTopicBuilder;

public class ExternalTaskQueryTopicBuilderImpl implements ExternalTaskQueryTopicBuilder
{
    protected CommandExecutor commandExecutor;
    protected String workerId;
    protected int maxTasks;
    protected boolean usePriority;
    protected Map<String, TopicFetchInstruction> instructions;
    protected TopicFetchInstruction currentInstruction;
    
    public ExternalTaskQueryTopicBuilderImpl(final CommandExecutor commandExecutor, final String workerId, final int maxTasks, final boolean usePriority) {
        this.commandExecutor = commandExecutor;
        this.workerId = workerId;
        this.maxTasks = maxTasks;
        this.usePriority = usePriority;
        this.instructions = new HashMap<String, TopicFetchInstruction>();
    }
    
    @Override
    public List<LockedExternalTask> execute() {
        this.submitCurrentInstruction();
        return this.commandExecutor.execute((Command<List<LockedExternalTask>>)new FetchExternalTasksCmd(this.workerId, this.maxTasks, this.instructions, this.usePriority));
    }
    
    @Override
    public ExternalTaskQueryTopicBuilder topic(final String topicName, final long lockDuration) {
        this.submitCurrentInstruction();
        this.currentInstruction = new TopicFetchInstruction(topicName, lockDuration);
        return this;
    }
    
    @Override
    public ExternalTaskQueryTopicBuilder variables(final String... variables) {
        if (variables != null) {
            this.currentInstruction.setVariablesToFetch(new ArrayList<String>(Arrays.asList(variables)));
        }
        return this;
    }
    
    @Override
    public ExternalTaskQueryTopicBuilder variables(final List<String> variables) {
        this.currentInstruction.setVariablesToFetch(variables);
        return this;
    }
    
    @Override
    public ExternalTaskQueryTopicBuilder processInstanceVariableEquals(final Map<String, Object> variables) {
        this.currentInstruction.setFilterVariables(variables);
        return this;
    }
    
    @Override
    public ExternalTaskQueryTopicBuilder processInstanceVariableEquals(final String name, final Object value) {
        this.currentInstruction.addFilterVariable(name, value);
        return this;
    }
    
    @Override
    public ExternalTaskQueryTopicBuilder businessKey(final String businessKey) {
        this.currentInstruction.setBusinessKey(businessKey);
        return this;
    }
    
    @Override
    public ExternalTaskQueryTopicBuilder processDefinitionId(final String processDefinitionId) {
        this.currentInstruction.setProcessDefinitionId(processDefinitionId);
        return this;
    }
    
    @Override
    public ExternalTaskQueryTopicBuilder processDefinitionIdIn(final String... processDefinitionIds) {
        this.currentInstruction.setProcessDefinitionIds(processDefinitionIds);
        return this;
    }
    
    @Override
    public ExternalTaskQueryTopicBuilder processDefinitionKey(final String processDefinitionKey) {
        this.currentInstruction.setProcessDefinitionKey(processDefinitionKey);
        return this;
    }
    
    @Override
    public ExternalTaskQueryTopicBuilder processDefinitionKeyIn(final String... processDefinitionKeys) {
        this.currentInstruction.setProcessDefinitionKeys(processDefinitionKeys);
        return this;
    }
    
    @Override
    public ExternalTaskQueryTopicBuilder processDefinitionVersionTag(final String processDefinitionVersionTag) {
        this.currentInstruction.setProcessDefinitionVersionTag(processDefinitionVersionTag);
        return this;
    }
    
    @Override
    public ExternalTaskQueryTopicBuilder withoutTenantId() {
        this.currentInstruction.setTenantIds(null);
        return this;
    }
    
    @Override
    public ExternalTaskQueryTopicBuilder tenantIdIn(final String... tenantIds) {
        this.currentInstruction.setTenantIds(tenantIds);
        return this;
    }
    
    protected void submitCurrentInstruction() {
        if (this.currentInstruction != null) {
            this.instructions.put(this.currentInstruction.getTopicName(), this.currentInstruction);
        }
    }
    
    @Override
    public ExternalTaskQueryTopicBuilder enableCustomObjectDeserialization() {
        this.currentInstruction.setDeserializeVariables(true);
        return this;
    }
    
    @Override
    public ExternalTaskQueryTopicBuilder localVariables() {
        this.currentInstruction.setLocalVariables(true);
        return this;
    }
    
    @Override
    public ExternalTaskQueryTopicBuilder includeExtensionProperties() {
        this.currentInstruction.setIncludeExtensionProperties(true);
        return this;
    }
}
