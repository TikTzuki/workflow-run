// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.handler;

import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.context.ProcessApplicationContextUtil;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.bpmn.parser.BpmnParse;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.zik.bpm.engine.impl.util.xml.Element;

public abstract class DelegateFormHandler
{
    protected String deploymentId;
    protected FormHandler formHandler;
    
    public DelegateFormHandler(final FormHandler formHandler, final String deploymentId) {
        this.formHandler = formHandler;
        this.deploymentId = deploymentId;
    }
    
    public void parseConfiguration(final Element activityElement, final DeploymentEntity deployment, final ProcessDefinitionEntity processDefinition, final BpmnParse bpmnParse) {
    }
    
    protected <T> T performContextSwitch(final Callable<T> callable) {
        final ProcessApplicationReference targetProcessApplication = ProcessApplicationContextUtil.getTargetProcessApplication(this.deploymentId);
        if (targetProcessApplication != null) {
            return Context.executeWithinProcessApplication((Callable<T>)new Callable<T>() {
                @Override
                public T call() throws Exception {
                    return (T)DelegateFormHandler.this.doCall((Callable<Object>)callable);
                }
            }, targetProcessApplication);
        }
        return (T)this.doCall((Callable<Object>)callable);
    }
    
    protected <T> T doCall(final Callable<T> callable) {
        try {
            return callable.call();
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e2) {
            throw new ProcessEngineException(e2);
        }
    }
    
    public void submitFormVariables(final VariableMap properties, final VariableScope variableScope) {
        this.performContextSwitch((Callable<Object>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new SubmitFormVariablesInvocation(DelegateFormHandler.this.formHandler, properties, variableScope));
                return null;
            }
        });
    }
    
    public abstract FormHandler getFormHandler();
}
