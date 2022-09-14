// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.runtime.InstantiationBuilder;
import org.zik.bpm.engine.runtime.ActivityInstantiationBuilder;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.StartProcessInstanceAtActivitiesCmd;
import org.zik.bpm.engine.impl.cmd.StartProcessInstanceCmd;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.zik.bpm.engine.runtime.ProcessInstance;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.cmd.CommandLogger;
import org.zik.bpm.engine.runtime.ProcessInstantiationBuilder;

public class ProcessInstantiationBuilderImpl implements ProcessInstantiationBuilder
{
    private static final CommandLogger LOG;
    protected CommandExecutor commandExecutor;
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected String businessKey;
    protected String caseInstanceId;
    protected String tenantId;
    protected String processDefinitionTenantId;
    protected boolean isProcessDefinitionTenantIdSet;
    protected ProcessInstanceModificationBuilderImpl modificationBuilder;
    
    protected ProcessInstantiationBuilderImpl(final CommandExecutor commandExecutor) {
        this.isProcessDefinitionTenantIdSet = false;
        this.modificationBuilder = new ProcessInstanceModificationBuilderImpl();
        this.commandExecutor = commandExecutor;
    }
    
    @Override
    public ProcessInstantiationBuilder startBeforeActivity(final String activityId) {
        this.modificationBuilder.startBeforeActivity(activityId);
        return this;
    }
    
    @Override
    public ProcessInstantiationBuilder startAfterActivity(final String activityId) {
        this.modificationBuilder.startAfterActivity(activityId);
        return this;
    }
    
    @Override
    public ProcessInstantiationBuilder startTransition(final String transitionId) {
        this.modificationBuilder.startTransition(transitionId);
        return this;
    }
    
    @Override
    public ProcessInstantiationBuilder setVariable(final String name, final Object value) {
        this.modificationBuilder.setVariable(name, value);
        return this;
    }
    
    @Override
    public ProcessInstantiationBuilder setVariableLocal(final String name, final Object value) {
        this.modificationBuilder.setVariableLocal(name, value);
        return this;
    }
    
    @Override
    public ProcessInstantiationBuilder setVariables(final Map<String, Object> variables) {
        if (variables != null) {
            this.modificationBuilder.setVariables(variables);
        }
        return this;
    }
    
    @Override
    public ProcessInstantiationBuilder setVariablesLocal(final Map<String, Object> variables) {
        if (variables != null) {
            this.modificationBuilder.setVariablesLocal(variables);
        }
        return this;
    }
    
    @Override
    public ProcessInstantiationBuilder businessKey(final String businessKey) {
        this.businessKey = businessKey;
        return this;
    }
    
    @Override
    public ProcessInstantiationBuilder caseInstanceId(final String caseInstanceId) {
        this.caseInstanceId = caseInstanceId;
        return this;
    }
    
    public ProcessInstantiationBuilder tenantId(final String tenantId) {
        this.tenantId = tenantId;
        return this;
    }
    
    @Override
    public ProcessInstantiationBuilder processDefinitionTenantId(final String tenantId) {
        this.processDefinitionTenantId = tenantId;
        this.isProcessDefinitionTenantIdSet = true;
        return this;
    }
    
    @Override
    public ProcessInstantiationBuilder processDefinitionWithoutTenantId() {
        this.processDefinitionTenantId = null;
        this.isProcessDefinitionTenantIdSet = true;
        return this;
    }
    
    @Override
    public ProcessInstance execute() {
        return this.execute(false, false);
    }
    
    @Override
    public ProcessInstance execute(final boolean skipCustomListeners, final boolean skipIoMappings) {
        return this.executeWithVariablesInReturn(skipCustomListeners, skipIoMappings);
    }
    
    @Override
    public ProcessInstanceWithVariables executeWithVariablesInReturn() {
        return this.executeWithVariablesInReturn(false, false);
    }
    
    @Override
    public ProcessInstanceWithVariables executeWithVariablesInReturn(final boolean skipCustomListeners, final boolean skipIoMappings) {
        EnsureUtil.ensureOnlyOneNotNull("either process definition id or key must be set", this.processDefinitionId, this.processDefinitionKey);
        if (this.isProcessDefinitionTenantIdSet && this.processDefinitionId != null) {
            throw ProcessInstantiationBuilderImpl.LOG.exceptionStartProcessInstanceByIdAndTenantId();
        }
        Command<ProcessInstanceWithVariables> command;
        if (this.modificationBuilder.getModificationOperations().isEmpty()) {
            if (skipCustomListeners || skipIoMappings) {
                throw ProcessInstantiationBuilderImpl.LOG.exceptionStartProcessInstanceAtStartActivityAndSkipListenersOrMapping();
            }
            command = new StartProcessInstanceCmd(this);
        }
        else {
            this.modificationBuilder.setSkipCustomListeners(skipCustomListeners);
            this.modificationBuilder.setSkipIoMappings(skipIoMappings);
            command = new StartProcessInstanceAtActivitiesCmd(this);
        }
        return this.commandExecutor.execute(command);
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public ProcessInstanceModificationBuilderImpl getModificationBuilder() {
        return this.modificationBuilder;
    }
    
    public String getBusinessKey() {
        return this.businessKey;
    }
    
    public String getCaseInstanceId() {
        return this.caseInstanceId;
    }
    
    public Map<String, Object> getVariables() {
        return (Map<String, Object>)this.modificationBuilder.getProcessVariables();
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public String getProcessDefinitionTenantId() {
        return this.processDefinitionTenantId;
    }
    
    public boolean isProcessDefinitionTenantIdSet() {
        return this.isProcessDefinitionTenantIdSet;
    }
    
    public void setModificationBuilder(final ProcessInstanceModificationBuilderImpl modificationBuilder) {
        this.modificationBuilder = modificationBuilder;
    }
    
    public static ProcessInstantiationBuilder createProcessInstanceById(final CommandExecutor commandExecutor, final String processDefinitionId) {
        final ProcessInstantiationBuilderImpl builder = new ProcessInstantiationBuilderImpl(commandExecutor);
        builder.processDefinitionId = processDefinitionId;
        return builder;
    }
    
    public static ProcessInstantiationBuilder createProcessInstanceByKey(final CommandExecutor commandExecutor, final String processDefinitionKey) {
        final ProcessInstantiationBuilderImpl builder = new ProcessInstantiationBuilderImpl(commandExecutor);
        builder.processDefinitionKey = processDefinitionKey;
        return builder;
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
