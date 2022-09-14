// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.cmd.EvaluateStartConditionCmd;
import org.zik.bpm.engine.runtime.ProcessInstance;
import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.runtime.ConditionEvaluationBuilder;

public class ConditionEvaluationBuilderImpl implements ConditionEvaluationBuilder
{
    protected CommandExecutor commandExecutor;
    protected String businessKey;
    protected String processDefinitionId;
    protected VariableMap variables;
    protected String tenantId;
    protected boolean isTenantIdSet;
    
    public ConditionEvaluationBuilderImpl(final CommandExecutor commandExecutor) {
        this.variables = (VariableMap)new VariableMapImpl();
        this.tenantId = null;
        this.isTenantIdSet = false;
        EnsureUtil.ensureNotNull("commandExecutor", commandExecutor);
        this.commandExecutor = commandExecutor;
    }
    
    public CommandExecutor getCommandExecutor() {
        return this.commandExecutor;
    }
    
    public String getBusinessKey() {
        return this.businessKey;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public VariableMap getVariables() {
        return this.variables;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
    
    protected <T> T execute(final Command<T> command) {
        return this.commandExecutor.execute(command);
    }
    
    @Override
    public ConditionEvaluationBuilder processInstanceBusinessKey(final String businessKey) {
        EnsureUtil.ensureNotNull("businessKey", (Object)businessKey);
        this.businessKey = businessKey;
        return this;
    }
    
    @Override
    public ConditionEvaluationBuilder processDefinitionId(final String processDefinitionId) {
        EnsureUtil.ensureNotNull("processDefinitionId", (Object)processDefinitionId);
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public ConditionEvaluationBuilder setVariable(final String variableName, final Object variableValue) {
        EnsureUtil.ensureNotNull("variableName", (Object)variableName);
        this.variables.put((Object)variableName, variableValue);
        return this;
    }
    
    @Override
    public ConditionEvaluationBuilder setVariables(final Map<String, Object> variables) {
        EnsureUtil.ensureNotNull("variables", variables);
        if (variables != null) {
            this.variables.putAll((Map)variables);
        }
        return this;
    }
    
    @Override
    public ConditionEvaluationBuilder tenantId(final String tenantId) {
        EnsureUtil.ensureNotNull("The tenant-id cannot be null. Use 'withoutTenantId()' if you want to evaluate conditional start event with a process definition which has no tenant-id.", "tenantId", tenantId);
        this.isTenantIdSet = true;
        this.tenantId = tenantId;
        return this;
    }
    
    @Override
    public ConditionEvaluationBuilder withoutTenantId() {
        this.isTenantIdSet = true;
        this.tenantId = null;
        return this;
    }
    
    @Override
    public List<ProcessInstance> evaluateStartConditions() {
        return this.execute((Command<List<ProcessInstance>>)new EvaluateStartConditionCmd(this));
    }
}
