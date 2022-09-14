// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.engine;

import org.zik.bpm.engine.impl.persistence.entity.ResourceEntity;
import java.io.UnsupportedEncodingException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.scripting.ExecutableScript;
import org.zik.bpm.engine.impl.scripting.ScriptFactory;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.delegate.ScriptInvocation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.form.TaskFormData;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.form.FormData;
import org.zik.bpm.engine.form.StartFormData;

public class JuelFormEngine implements FormEngine
{
    @Override
    public String getName() {
        return "juel";
    }
    
    @Override
    public Object renderStartForm(final StartFormData startForm) {
        if (startForm.getFormKey() == null) {
            return null;
        }
        final String formTemplateString = this.getFormTemplateString(startForm, startForm.getFormKey());
        return this.executeScript(formTemplateString, null);
    }
    
    @Override
    public Object renderTaskForm(final TaskFormData taskForm) {
        if (taskForm.getFormKey() == null) {
            return null;
        }
        final String formTemplateString = this.getFormTemplateString(taskForm, taskForm.getFormKey());
        final TaskEntity task = (TaskEntity)taskForm.getTask();
        return this.executeScript(formTemplateString, task.getExecution());
    }
    
    protected Object executeScript(final String scriptSrc, final VariableScope scope) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final ScriptFactory scriptFactory = processEngineConfiguration.getScriptFactory();
        final ExecutableScript script = scriptFactory.createScriptFromSource("juel", scriptSrc);
        final ScriptInvocation invocation = new ScriptInvocation(script, scope);
        try {
            processEngineConfiguration.getDelegateInterceptor().handleInvocation(invocation);
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e2) {
            throw new ProcessEngineException(e2);
        }
        return invocation.getInvocationResult();
    }
    
    protected String getFormTemplateString(final FormData formInstance, final String formKey) {
        final String deploymentId = formInstance.getDeploymentId();
        final ResourceEntity resourceStream = Context.getCommandContext().getResourceManager().findResourceByDeploymentIdAndResourceName(deploymentId, formKey);
        EnsureUtil.ensureNotNull("Form with formKey '" + formKey + "' does not exist", "resourceStream", resourceStream);
        final byte[] resourceBytes = resourceStream.getBytes();
        final String encoding = "UTF-8";
        String formTemplateString = "";
        try {
            formTemplateString = new String(resourceBytes, encoding);
        }
        catch (UnsupportedEncodingException e) {
            throw new ProcessEngineException("Unsupported encoding of :" + encoding, e);
        }
        return formTemplateString;
    }
}
