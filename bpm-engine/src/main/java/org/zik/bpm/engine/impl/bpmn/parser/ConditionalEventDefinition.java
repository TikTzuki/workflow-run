// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.parser;

import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.core.variable.event.VariableEvent;
import org.zik.bpm.engine.impl.el.Expression;
import org.zik.bpm.engine.impl.event.EventType;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import java.util.Set;
import org.zik.bpm.engine.impl.Condition;
import java.io.Serializable;

public class ConditionalEventDefinition extends EventSubscriptionDeclaration implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected String conditionAsString;
    protected final Condition condition;
    protected boolean interrupting;
    protected String variableName;
    protected Set<String> variableEvents;
    protected ActivityImpl conditionalActivity;
    
    public ConditionalEventDefinition(final Condition condition, final ActivityImpl conditionalActivity) {
        super(null, EventType.CONDITONAL);
        this.activityId = conditionalActivity.getActivityId();
        this.conditionalActivity = conditionalActivity;
        this.condition = condition;
    }
    
    public ActivityImpl getConditionalActivity() {
        return this.conditionalActivity;
    }
    
    public void setConditionalActivity(final ActivityImpl conditionalActivity) {
        this.conditionalActivity = conditionalActivity;
    }
    
    public boolean isInterrupting() {
        return this.interrupting;
    }
    
    public void setInterrupting(final boolean interrupting) {
        this.interrupting = interrupting;
    }
    
    public String getVariableName() {
        return this.variableName;
    }
    
    public void setVariableName(final String variableName) {
        this.variableName = variableName;
    }
    
    public Set<String> getVariableEvents() {
        return this.variableEvents;
    }
    
    public void setVariableEvents(final Set<String> variableEvents) {
        this.variableEvents = variableEvents;
    }
    
    public String getConditionAsString() {
        return this.conditionAsString;
    }
    
    public void setConditionAsString(final String conditionAsString) {
        this.conditionAsString = conditionAsString;
    }
    
    public boolean shouldEvaluateForVariableEvent(final VariableEvent event) {
        return (this.variableName == null || event.getVariableInstance().getName().equals(this.variableName)) && (this.variableEvents == null || this.variableEvents.isEmpty() || this.variableEvents.contains(event.getEventName()));
    }
    
    public boolean evaluate(final DelegateExecution execution) {
        if (this.condition != null) {
            return this.condition.evaluate(execution, execution);
        }
        throw new IllegalStateException("Conditional event must have a condition!");
    }
    
    public boolean tryEvaluate(final DelegateExecution execution) {
        if (this.condition != null) {
            return this.condition.tryEvaluate(execution, execution);
        }
        throw new IllegalStateException("Conditional event must have a condition!");
    }
    
    public boolean tryEvaluate(final VariableEvent variableEvent, final DelegateExecution execution) {
        return (variableEvent == null || this.shouldEvaluateForVariableEvent(variableEvent)) && this.tryEvaluate(execution);
    }
}
