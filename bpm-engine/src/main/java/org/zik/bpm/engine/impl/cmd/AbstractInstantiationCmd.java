// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.pvm.PvmTransition;
import org.zik.bpm.engine.impl.core.delegate.CoreActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.SequentialMultiInstanceActivityBehavior;
import java.util.Set;
import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.bpmn.helper.BpmnProperties;
import org.zik.bpm.engine.impl.pvm.process.ActivityStartBehavior;
import org.zik.bpm.engine.impl.pvm.process.TransitionImpl;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import java.util.List;
import java.util.Collections;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.runtime.ActivityInstance;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.zik.bpm.engine.impl.tree.ReferenceWalker;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.tree.TreeVisitor;
import org.zik.bpm.engine.impl.tree.FlowScopeWalker;
import org.zik.bpm.engine.impl.tree.ActivityStackCollector;
import org.zik.bpm.engine.impl.ActivityExecutionTreeMapping;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Map;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import org.camunda.bpm.engine.variable.VariableMap;

public abstract class AbstractInstantiationCmd extends AbstractProcessInstanceModificationCommand
{
    protected VariableMap variables;
    protected VariableMap variablesLocal;
    protected String ancestorActivityInstanceId;
    
    public AbstractInstantiationCmd(final String processInstanceId, final String ancestorActivityInstanceId) {
        super(processInstanceId);
        this.ancestorActivityInstanceId = ancestorActivityInstanceId;
        this.variables = (VariableMap)new VariableMapImpl();
        this.variablesLocal = (VariableMap)new VariableMapImpl();
    }
    
    public void addVariable(final String name, final Object value) {
        this.variables.put((Object)name, value);
    }
    
    public void addVariableLocal(final String name, final Object value) {
        this.variablesLocal.put((Object)name, value);
    }
    
    public void addVariables(final Map<String, Object> variables) {
        this.variables.putAll((Map)variables);
    }
    
    public void addVariablesLocal(final Map<String, Object> variables) {
        this.variablesLocal.putAll((Map)variables);
    }
    
    public VariableMap getVariables() {
        return this.variables;
    }
    
    public VariableMap getVariablesLocal() {
        return this.variablesLocal;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final ExecutionEntity processInstance = commandContext.getExecutionManager().findExecutionById(this.processInstanceId);
        final ProcessDefinitionImpl processDefinition = processInstance.getProcessDefinition();
        final CoreModelElement elementToInstantiate = this.getTargetElement(processDefinition);
        EnsureUtil.ensureNotNull(NotValidException.class, this.describeFailure("Element '" + this.getTargetElementId() + "' does not exist in process '" + processDefinition.getId() + "'"), "element", elementToInstantiate);
        final ActivityExecutionTreeMapping mapping = new ActivityExecutionTreeMapping(commandContext, this.processInstanceId);
        final ScopeImpl targetFlowScope = this.getTargetFlowScope(processDefinition);
        final ActivityStackCollector stackCollector = new ActivityStackCollector();
        final FlowScopeWalker walker = new FlowScopeWalker(targetFlowScope);
        walker.addPreVisitor(stackCollector);
        ExecutionEntity scopeExecution = null;
        if (this.ancestorActivityInstanceId == null) {
            walker.walkWhile(new ReferenceWalker.WalkCondition<ScopeImpl>() {
                @Override
                public boolean isFulfilled(final ScopeImpl element) {
                    return !mapping.getExecutions(element).isEmpty() || element == processDefinition;
                }
            });
            final Set<ExecutionEntity> flowScopeExecutions = mapping.getExecutions(walker.getCurrentElement());
            if (flowScopeExecutions.size() > 1) {
                throw new ProcessEngineException("Ancestor activity execution is ambiguous for activity " + targetFlowScope);
            }
            scopeExecution = flowScopeExecutions.iterator().next();
        }
        else {
            final ActivityInstance tree = commandContext.runWithoutAuthorization((Command<ActivityInstance>)new GetActivityInstanceCmd(this.processInstanceId));
            final ActivityInstance ancestorInstance = this.findActivityInstance(tree, this.ancestorActivityInstanceId);
            EnsureUtil.ensureNotNull(NotValidException.class, this.describeFailure("Ancestor activity instance '" + this.ancestorActivityInstanceId + "' does not exist"), "ancestorInstance", ancestorInstance);
            final ExecutionEntity ancestorScopeExecution = this.getScopeExecutionForActivityInstance(processInstance, mapping, ancestorInstance);
            final PvmScope ancestorScope = this.getScopeForActivityInstance(processDefinition, ancestorInstance);
            walker.walkWhile(new ReferenceWalker.WalkCondition<ScopeImpl>() {
                @Override
                public boolean isFulfilled(final ScopeImpl element) {
                    return (mapping.getExecutions(element).contains(ancestorScopeExecution) && element == ancestorScope) || element == processDefinition;
                }
            });
            final Set<ExecutionEntity> flowScopeExecutions2 = mapping.getExecutions(walker.getCurrentElement());
            if (!flowScopeExecutions2.contains(ancestorScopeExecution)) {
                throw new NotValidException(this.describeFailure("Scope execution for '" + this.ancestorActivityInstanceId + "' cannot be found in parent hierarchy of flow element '" + elementToInstantiate.getId() + "'"));
            }
            scopeExecution = ancestorScopeExecution;
        }
        final List<PvmActivity> activitiesToInstantiate = stackCollector.getActivityStack();
        Collections.reverse(activitiesToInstantiate);
        ActivityImpl topMostActivity = null;
        ScopeImpl flowScope = null;
        if (!activitiesToInstantiate.isEmpty()) {
            topMostActivity = activitiesToInstantiate.get(0);
            flowScope = topMostActivity.getFlowScope();
        }
        else if (ActivityImpl.class.isAssignableFrom(elementToInstantiate.getClass())) {
            topMostActivity = (ActivityImpl)elementToInstantiate;
            flowScope = topMostActivity.getFlowScope();
        }
        else if (TransitionImpl.class.isAssignableFrom(elementToInstantiate.getClass())) {
            final TransitionImpl transitionToInstantiate = (TransitionImpl)elementToInstantiate;
            flowScope = transitionToInstantiate.getSource().getFlowScope();
        }
        if (!this.supportsConcurrentChildInstantiation(flowScope)) {
            throw new ProcessEngineException("Concurrent instantiation not possible for activities in scope " + flowScope.getId());
        }
        ActivityStartBehavior startBehavior = ActivityStartBehavior.CONCURRENT_IN_FLOW_SCOPE;
        if (topMostActivity != null) {
            startBehavior = topMostActivity.getActivityStartBehavior();
            if (!activitiesToInstantiate.isEmpty()) {
                final PvmActivity initialActivity = topMostActivity.getProperties().get(BpmnProperties.INITIAL_ACTIVITY);
                PvmActivity secondTopMostActivity = null;
                if (activitiesToInstantiate.size() > 1) {
                    secondTopMostActivity = activitiesToInstantiate.get(1);
                }
                else if (ActivityImpl.class.isAssignableFrom(elementToInstantiate.getClass())) {
                    secondTopMostActivity = (PvmActivity)elementToInstantiate;
                }
                if (initialActivity != secondTopMostActivity) {
                    startBehavior = ActivityStartBehavior.CONCURRENT_IN_FLOW_SCOPE;
                }
            }
        }
        switch (startBehavior) {
            case CANCEL_EVENT_SCOPE: {
                final ScopeImpl scopeToCancel = topMostActivity.getEventScope();
                final ExecutionEntity executionToCancel = this.getSingleExecutionForScope(mapping, scopeToCancel);
                if (executionToCancel != null) {
                    executionToCancel.deleteCascade("Cancelling activity " + topMostActivity + " executed.", this.skipCustomListeners, this.skipIoMappings);
                    this.instantiate(executionToCancel.getParent(), activitiesToInstantiate, elementToInstantiate);
                    break;
                }
                final ExecutionEntity flowScopeExecution = this.getSingleExecutionForScope(mapping, topMostActivity.getFlowScope());
                this.instantiateConcurrent(flowScopeExecution, activitiesToInstantiate, elementToInstantiate);
                break;
            }
            case INTERRUPT_EVENT_SCOPE: {
                final ScopeImpl scopeToCancel = topMostActivity.getEventScope();
                final ExecutionEntity executionToCancel = this.getSingleExecutionForScope(mapping, scopeToCancel);
                executionToCancel.interrupt("Interrupting activity " + topMostActivity + " executed.", this.skipCustomListeners, this.skipIoMappings, false);
                executionToCancel.setActivity(null);
                executionToCancel.leaveActivityInstance();
                this.instantiate(executionToCancel, activitiesToInstantiate, elementToInstantiate);
                break;
            }
            case INTERRUPT_FLOW_SCOPE: {
                final ScopeImpl scopeToCancel = topMostActivity.getFlowScope();
                final ExecutionEntity executionToCancel = this.getSingleExecutionForScope(mapping, scopeToCancel);
                executionToCancel.interrupt("Interrupting activity " + topMostActivity + " executed.", this.skipCustomListeners, this.skipIoMappings, false);
                executionToCancel.setActivity(null);
                executionToCancel.leaveActivityInstance();
                this.instantiate(executionToCancel, activitiesToInstantiate, elementToInstantiate);
                break;
            }
            default: {
                if (!scopeExecution.hasChildren() && (scopeExecution.getActivity() == null || scopeExecution.isEnded())) {
                    this.instantiate(scopeExecution, activitiesToInstantiate, elementToInstantiate);
                    break;
                }
                this.instantiateConcurrent(scopeExecution, activitiesToInstantiate, elementToInstantiate);
                break;
            }
        }
        return null;
    }
    
    protected boolean supportsConcurrentChildInstantiation(final ScopeImpl flowScope) {
        final CoreActivityBehavior<?> behavior = flowScope.getActivityBehavior();
        return behavior == null || !(behavior instanceof SequentialMultiInstanceActivityBehavior);
    }
    
    protected ExecutionEntity getSingleExecutionForScope(final ActivityExecutionTreeMapping mapping, final ScopeImpl scope) {
        final Set<ExecutionEntity> executions = mapping.getExecutions(scope);
        if (executions.isEmpty()) {
            return null;
        }
        if (executions.size() > 1) {
            throw new ProcessEngineException("Executions for activity " + scope + " ambiguous");
        }
        return executions.iterator().next();
    }
    
    protected boolean isConcurrentStart(final ActivityStartBehavior startBehavior) {
        return startBehavior == ActivityStartBehavior.DEFAULT || startBehavior == ActivityStartBehavior.CONCURRENT_IN_FLOW_SCOPE;
    }
    
    protected void instantiate(final ExecutionEntity ancestorScopeExecution, final List<PvmActivity> parentFlowScopes, final CoreModelElement targetElement) {
        if (PvmTransition.class.isAssignableFrom(targetElement.getClass())) {
            ancestorScopeExecution.executeActivities(parentFlowScopes, null, (PvmTransition)targetElement, (Map<String, Object>)this.variables, (Map<String, Object>)this.variablesLocal, this.skipCustomListeners, this.skipIoMappings);
        }
        else {
            if (!PvmActivity.class.isAssignableFrom(targetElement.getClass())) {
                throw new ProcessEngineException("Cannot instantiate element " + targetElement);
            }
            ancestorScopeExecution.executeActivities(parentFlowScopes, (PvmActivity)targetElement, null, (Map<String, Object>)this.variables, (Map<String, Object>)this.variablesLocal, this.skipCustomListeners, this.skipIoMappings);
        }
    }
    
    protected void instantiateConcurrent(final ExecutionEntity ancestorScopeExecution, final List<PvmActivity> parentFlowScopes, final CoreModelElement targetElement) {
        if (PvmTransition.class.isAssignableFrom(targetElement.getClass())) {
            ancestorScopeExecution.executeActivitiesConcurrent(parentFlowScopes, null, (PvmTransition)targetElement, (Map<String, Object>)this.variables, (Map<String, Object>)this.variablesLocal, this.skipCustomListeners, this.skipIoMappings);
        }
        else {
            if (!PvmActivity.class.isAssignableFrom(targetElement.getClass())) {
                throw new ProcessEngineException("Cannot instantiate element " + targetElement);
            }
            ancestorScopeExecution.executeActivitiesConcurrent(parentFlowScopes, (PvmActivity)targetElement, null, (Map<String, Object>)this.variables, (Map<String, Object>)this.variablesLocal, this.skipCustomListeners, this.skipIoMappings);
        }
    }
    
    protected abstract ScopeImpl getTargetFlowScope(final ProcessDefinitionImpl p0);
    
    protected abstract CoreModelElement getTargetElement(final ProcessDefinitionImpl p0);
    
    protected abstract String getTargetElementId();
}
