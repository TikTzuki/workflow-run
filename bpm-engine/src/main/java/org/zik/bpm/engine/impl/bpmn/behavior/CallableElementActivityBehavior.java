// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.core.model.BaseCallableElement;
import org.zik.bpm.engine.ProcessEngineException;
import java.util.Map;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.bpmn.parser.FieldDeclaration;
import java.util.List;
import org.zik.bpm.engine.impl.util.ClassDelegateUtil;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.application.InvocationContext;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.context.ProcessApplicationContextUtil;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.delegate.DelegateVariableMapping;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.impl.core.model.CallableElement;
import org.zik.bpm.engine.impl.pvm.delegate.SubProcessActivityBehavior;

public abstract class CallableElementActivityBehavior extends AbstractBpmnActivityBehavior implements SubProcessActivityBehavior
{
    protected String[] variablesFilter;
    protected CallableElement callableElement;
    protected Expression expression;
    protected String className;
    
    public CallableElementActivityBehavior() {
        this.variablesFilter = new String[] { "nrOfInstances", "nrOfActiveInstances", "nrOfCompletedInstances" };
    }
    
    public CallableElementActivityBehavior(final String className) {
        this.variablesFilter = new String[] { "nrOfInstances", "nrOfActiveInstances", "nrOfCompletedInstances" };
        this.className = className;
    }
    
    public CallableElementActivityBehavior(final Expression expression) {
        this.variablesFilter = new String[] { "nrOfInstances", "nrOfActiveInstances", "nrOfCompletedInstances" };
        this.expression = expression;
    }
    
    protected DelegateVariableMapping getDelegateVariableMapping(final Object instance) {
        if (instance instanceof DelegateVariableMapping) {
            return (DelegateVariableMapping)instance;
        }
        throw CallableElementActivityBehavior.LOG.missingDelegateVariableMappingParentClassException(instance.getClass().getName(), DelegateVariableMapping.class.getName());
    }
    
    protected DelegateVariableMapping resolveDelegation(final ActivityExecution execution) {
        final Object delegate = this.resolveDelegateClass(execution);
        return (delegate != null) ? this.getDelegateVariableMapping(delegate) : null;
    }
    
    public Object resolveDelegateClass(final ActivityExecution execution) {
        final ProcessApplicationReference targetProcessApplication = ProcessApplicationContextUtil.getTargetProcessApplication((ExecutionEntity)execution);
        if (ProcessApplicationContextUtil.requiresContextSwitch(targetProcessApplication)) {
            return Context.executeWithinProcessApplication((Callable<Object>)new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return CallableElementActivityBehavior.this.resolveDelegateClass(execution);
                }
            }, targetProcessApplication, new InvocationContext(execution));
        }
        return this.instantiateDelegateClass(execution);
    }
    
    protected Object instantiateDelegateClass(final ActivityExecution execution) {
        Object delegate = null;
        if (this.expression != null) {
            delegate = this.expression.getValue(execution);
        }
        else if (this.className != null) {
            delegate = ClassDelegateUtil.instantiateDelegate(this.className, null);
        }
        return delegate;
    }
    
    @Override
    public void execute(final ActivityExecution execution) throws Exception {
        final VariableMap variables = this.getInputVariables(execution);
        final DelegateVariableMapping varMapping = this.resolveDelegation(execution);
        if (varMapping != null) {
            this.invokeVarMappingDelegation(new DelegateInvocation(execution, null) {
                @Override
                protected void invoke() throws Exception {
                    varMapping.mapInputVariables(execution, variables);
                }
            });
        }
        final String businessKey = this.getBusinessKey(execution);
        this.startInstance(execution, variables, businessKey);
    }
    
    @Override
    public void passOutputVariables(final ActivityExecution execution, final VariableScope subInstance) {
        final VariableMap variables = this.filterVariables(this.getOutputVariables(subInstance));
        final VariableMap localVariables = this.getOutputVariablesLocal(subInstance);
        execution.setVariables((Map<String, ?>)variables);
        execution.setVariablesLocal((Map<String, ?>)localVariables);
        final DelegateVariableMapping varMapping = this.resolveDelegation(execution);
        if (varMapping != null) {
            this.invokeVarMappingDelegation(new DelegateInvocation(execution, null) {
                @Override
                protected void invoke() throws Exception {
                    varMapping.mapOutputVariables(execution, subInstance);
                }
            });
        }
    }
    
    protected void invokeVarMappingDelegation(final DelegateInvocation delegation) {
        try {
            Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(delegation);
        }
        catch (Exception ex) {
            throw new ProcessEngineException(ex);
        }
    }
    
    protected VariableMap filterVariables(final VariableMap variables) {
        if (variables != null) {
            for (final String key : this.variablesFilter) {
                variables.remove((Object)key);
            }
        }
        return variables;
    }
    
    @Override
    public void completed(final ActivityExecution execution) throws Exception {
        this.leave(execution);
    }
    
    public CallableElement getCallableElement() {
        return this.callableElement;
    }
    
    public void setCallableElement(final CallableElement callableElement) {
        this.callableElement = callableElement;
    }
    
    protected String getBusinessKey(final ActivityExecution execution) {
        return this.getCallableElement().getBusinessKey(execution);
    }
    
    protected VariableMap getInputVariables(final ActivityExecution callingExecution) {
        return this.getCallableElement().getInputVariables(callingExecution);
    }
    
    protected VariableMap getOutputVariables(final VariableScope calledElementScope) {
        return this.getCallableElement().getOutputVariables(calledElementScope);
    }
    
    protected VariableMap getOutputVariablesLocal(final VariableScope calledElementScope) {
        return this.getCallableElement().getOutputVariablesLocal(calledElementScope);
    }
    
    protected Integer getVersion(final ActivityExecution execution) {
        return this.getCallableElement().getVersion(execution);
    }
    
    protected String getDeploymentId(final ActivityExecution execution) {
        return this.getCallableElement().getDeploymentId();
    }
    
    protected BaseCallableElement.CallableElementBinding getBinding() {
        return this.getCallableElement().getBinding();
    }
    
    protected boolean isLatestBinding() {
        return this.getCallableElement().isLatestBinding();
    }
    
    protected boolean isDeploymentBinding() {
        return this.getCallableElement().isDeploymentBinding();
    }
    
    protected boolean isVersionBinding() {
        return this.getCallableElement().isVersionBinding();
    }
    
    protected abstract void startInstance(final ActivityExecution p0, final VariableMap p1, final String p2);
}
