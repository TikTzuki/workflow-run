// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm;

import org.zik.bpm.engine.delegate.ExecutionListener;
import java.util.Iterator;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.zik.bpm.engine.impl.pvm.process.ActivityStartBehavior;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.List;
import org.zik.bpm.engine.impl.pvm.process.TransitionImpl;
import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import java.util.Deque;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;

public class ProcessDefinitionBuilder
{
    protected ProcessDefinitionImpl processDefinition;
    protected Deque<ScopeImpl> scopeStack;
    protected CoreModelElement processElement;
    protected TransitionImpl transition;
    protected List<Object[]> unresolvedTransitions;
    
    public ProcessDefinitionBuilder() {
        this(null);
    }
    
    public ProcessDefinitionBuilder(final String processDefinitionId) {
        this.scopeStack = new ArrayDeque<ScopeImpl>();
        this.processElement = this.processDefinition;
        this.unresolvedTransitions = new ArrayList<Object[]>();
        this.processDefinition = new ProcessDefinitionImpl(processDefinitionId);
        this.scopeStack.push(this.processDefinition);
    }
    
    public ProcessDefinitionBuilder createActivity(final String id) {
        final ActivityImpl activity = this.scopeStack.peek().createActivity(id);
        this.scopeStack.push(activity);
        this.processElement = activity;
        this.transition = null;
        return this;
    }
    
    public ProcessDefinitionBuilder attachedTo(final String id, final boolean isInterrupting) {
        final ActivityImpl activity = this.getActivity();
        activity.setEventScope(this.processDefinition.findActivity(id));
        if (isInterrupting) {
            activity.setActivityStartBehavior(ActivityStartBehavior.INTERRUPT_EVENT_SCOPE);
        }
        else {
            activity.setActivityStartBehavior(ActivityStartBehavior.CONCURRENT_IN_FLOW_SCOPE);
        }
        return this;
    }
    
    public ProcessDefinitionBuilder endActivity() {
        this.scopeStack.pop();
        this.processElement = this.scopeStack.peek();
        this.transition = null;
        return this;
    }
    
    public ProcessDefinitionBuilder initial() {
        this.processDefinition.setInitial(this.getActivity());
        return this;
    }
    
    public ProcessDefinitionBuilder startTransition(final String destinationActivityId) {
        return this.startTransition(destinationActivityId, null);
    }
    
    public ProcessDefinitionBuilder startTransition(final String destinationActivityId, final String transitionId) {
        if (destinationActivityId == null) {
            throw new PvmException("destinationActivityId is null");
        }
        final ActivityImpl activity = this.getActivity();
        this.transition = activity.createOutgoingTransition(transitionId);
        this.unresolvedTransitions.add(new Object[] { this.transition, destinationActivityId });
        this.processElement = this.transition;
        return this;
    }
    
    public ProcessDefinitionBuilder endTransition() {
        this.processElement = this.scopeStack.peek();
        this.transition = null;
        return this;
    }
    
    public ProcessDefinitionBuilder transition(final String destinationActivityId) {
        return this.transition(destinationActivityId, null);
    }
    
    public ProcessDefinitionBuilder transition(final String destinationActivityId, final String transitionId) {
        this.startTransition(destinationActivityId, transitionId);
        this.endTransition();
        return this;
    }
    
    public ProcessDefinitionBuilder behavior(final ActivityBehavior activityBehaviour) {
        this.getActivity().setActivityBehavior(activityBehaviour);
        return this;
    }
    
    public ProcessDefinitionBuilder property(final String name, final Object value) {
        this.processElement.setProperty(name, value);
        return this;
    }
    
    public PvmProcessDefinition buildProcessDefinition() {
        for (final Object[] unresolvedTransition : this.unresolvedTransitions) {
            final TransitionImpl transition = (TransitionImpl)unresolvedTransition[0];
            final String destinationActivityName = (String)unresolvedTransition[1];
            final ActivityImpl destination = this.processDefinition.findActivity(destinationActivityName);
            if (destination == null) {
                throw new RuntimeException("destination '" + destinationActivityName + "' not found.  (referenced from transition in '" + transition.getSource().getId() + "')");
            }
            transition.setDestination(destination);
        }
        return this.processDefinition;
    }
    
    protected ActivityImpl getActivity() {
        return this.scopeStack.peek();
    }
    
    public ProcessDefinitionBuilder scope() {
        this.getActivity().setScope(true);
        return this;
    }
    
    public ProcessDefinitionBuilder executionListener(final ExecutionListener executionListener) {
        if (this.transition != null) {
            this.transition.addExecutionListener(executionListener);
            return this;
        }
        throw new PvmException("not in a transition scope");
    }
    
    public ProcessDefinitionBuilder executionListener(final String eventName, final ExecutionListener executionListener) {
        if (this.transition == null) {
            this.scopeStack.peek().addExecutionListener(eventName, executionListener);
        }
        else {
            this.transition.addExecutionListener(executionListener);
        }
        return this;
    }
}
