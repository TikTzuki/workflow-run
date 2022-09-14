// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.cmd.GetDeployedStartFormCmd;
import java.io.InputStream;
import org.zik.bpm.engine.impl.cmd.GetTaskFormVariablesCmd;
import org.zik.bpm.engine.impl.cmd.GetStartFormVariablesCmd;
import java.util.Collection;
import org.zik.bpm.engine.impl.cmd.GetFormKeyCmd;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.cmd.SubmitTaskFormCmd;
import org.zik.bpm.engine.impl.cmd.SubmitStartFormCmd;
import org.zik.bpm.engine.runtime.ProcessInstance;
import java.util.Map;
import org.zik.bpm.engine.impl.cmd.GetTaskFormCmd;
import org.zik.bpm.engine.form.TaskFormData;
import org.zik.bpm.engine.impl.cmd.GetStartFormCmd;
import org.zik.bpm.engine.form.StartFormData;
import org.zik.bpm.engine.impl.cmd.GetRenderedTaskFormCmd;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.GetRenderedStartFormCmd;
import org.zik.bpm.engine.FormService;

public class FormServiceImpl extends ServiceImpl implements FormService
{
    @Override
    public Object getRenderedStartForm(final String processDefinitionId) {
        return this.commandExecutor.execute((Command<Object>)new GetRenderedStartFormCmd(processDefinitionId, null));
    }
    
    @Override
    public Object getRenderedStartForm(final String processDefinitionId, final String engineName) {
        return this.commandExecutor.execute((Command<Object>)new GetRenderedStartFormCmd(processDefinitionId, engineName));
    }
    
    @Override
    public Object getRenderedTaskForm(final String taskId) {
        return this.commandExecutor.execute((Command<Object>)new GetRenderedTaskFormCmd(taskId, null));
    }
    
    @Override
    public Object getRenderedTaskForm(final String taskId, final String engineName) {
        return this.commandExecutor.execute((Command<Object>)new GetRenderedTaskFormCmd(taskId, engineName));
    }
    
    @Override
    public StartFormData getStartFormData(final String processDefinitionId) {
        return this.commandExecutor.execute((Command<StartFormData>)new GetStartFormCmd(processDefinitionId));
    }
    
    @Override
    public TaskFormData getTaskFormData(final String taskId) {
        return this.commandExecutor.execute((Command<TaskFormData>)new GetTaskFormCmd(taskId));
    }
    
    @Override
    public ProcessInstance submitStartFormData(final String processDefinitionId, final Map<String, String> properties) {
        return this.commandExecutor.execute((Command<ProcessInstance>)new SubmitStartFormCmd(processDefinitionId, null, (Map<String, Object>)properties));
    }
    
    @Override
    public ProcessInstance submitStartFormData(final String processDefinitionId, final String businessKey, final Map<String, String> properties) {
        return this.commandExecutor.execute((Command<ProcessInstance>)new SubmitStartFormCmd(processDefinitionId, businessKey, (Map<String, Object>)properties));
    }
    
    @Override
    public ProcessInstance submitStartForm(final String processDefinitionId, final Map<String, Object> properties) {
        return this.commandExecutor.execute((Command<ProcessInstance>)new SubmitStartFormCmd(processDefinitionId, null, properties));
    }
    
    @Override
    public ProcessInstance submitStartForm(final String processDefinitionId, final String businessKey, final Map<String, Object> properties) {
        return this.commandExecutor.execute((Command<ProcessInstance>)new SubmitStartFormCmd(processDefinitionId, businessKey, properties));
    }
    
    @Override
    public void submitTaskFormData(final String taskId, final Map<String, String> properties) {
        this.submitTaskForm(taskId, (Map<String, Object>)properties);
    }
    
    @Override
    public void submitTaskForm(final String taskId, final Map<String, Object> properties) {
        this.commandExecutor.execute((Command<Object>)new SubmitTaskFormCmd(taskId, properties, false, false));
    }
    
    @Override
    public VariableMap submitTaskFormWithVariablesInReturn(final String taskId, final Map<String, Object> properties, final boolean deserializeValues) {
        return this.commandExecutor.execute((Command<VariableMap>)new SubmitTaskFormCmd(taskId, properties, true, deserializeValues));
    }
    
    @Override
    public String getStartFormKey(final String processDefinitionId) {
        return this.commandExecutor.execute((Command<String>)new GetFormKeyCmd(processDefinitionId));
    }
    
    @Override
    public String getTaskFormKey(final String processDefinitionId, final String taskDefinitionKey) {
        return this.commandExecutor.execute((Command<String>)new GetFormKeyCmd(processDefinitionId, taskDefinitionKey));
    }
    
    @Override
    public VariableMap getStartFormVariables(final String processDefinitionId) {
        return this.getStartFormVariables(processDefinitionId, null, true);
    }
    
    @Override
    public VariableMap getStartFormVariables(final String processDefinitionId, final Collection<String> formVariables, final boolean deserializeObjectValues) {
        return this.commandExecutor.execute((Command<VariableMap>)new GetStartFormVariablesCmd(processDefinitionId, formVariables, deserializeObjectValues));
    }
    
    @Override
    public VariableMap getTaskFormVariables(final String taskId) {
        return this.getTaskFormVariables(taskId, null, true);
    }
    
    @Override
    public VariableMap getTaskFormVariables(final String taskId, final Collection<String> formVariables, final boolean deserializeObjectValues) {
        return this.commandExecutor.execute((Command<VariableMap>)new GetTaskFormVariablesCmd(taskId, formVariables, deserializeObjectValues));
    }
    
    @Override
    public InputStream getDeployedStartForm(final String processDefinitionId) {
        return this.commandExecutor.execute((Command<InputStream>)new GetDeployedStartFormCmd(processDefinitionId));
    }
    
    @Override
    public InputStream getDeployedTaskForm(final String taskId) {
        return this.commandExecutor.execute((Command<InputStream>)new GetDeployedTaskFormCmd(taskId));
    }
}
