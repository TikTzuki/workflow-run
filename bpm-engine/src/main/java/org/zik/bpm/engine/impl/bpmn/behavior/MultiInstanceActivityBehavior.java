// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.camunda.bpm.engine.variable.value.IntegerValue;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.bpmn.helper.CompensationUtil;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import java.util.Iterator;
import org.zik.bpm.engine.delegate.VariableScope;
import java.util.Collection;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.impl.pvm.delegate.ModificationObserverBehavior;
import org.zik.bpm.engine.impl.pvm.delegate.CompositeActivityBehavior;

public abstract class MultiInstanceActivityBehavior extends AbstractBpmnActivityBehavior implements CompositeActivityBehavior, ModificationObserverBehavior
{
    protected static final BpmnBehaviorLogger LOG;
    public static final String NUMBER_OF_INSTANCES = "nrOfInstances";
    public static final String NUMBER_OF_ACTIVE_INSTANCES = "nrOfActiveInstances";
    public static final String NUMBER_OF_COMPLETED_INSTANCES = "nrOfCompletedInstances";
    public static final String LOOP_COUNTER = "loopCounter";
    protected Expression loopCardinalityExpression;
    protected Expression completionConditionExpression;
    protected Expression collectionExpression;
    protected String collectionVariable;
    protected String collectionElementVariable;
    
    @Override
    public void execute(final ActivityExecution execution) throws Exception {
        final int nrOfInstances = this.resolveNrOfInstances(execution);
        if (nrOfInstances == 0) {
            this.leave(execution);
        }
        else {
            if (nrOfInstances < 0) {
                throw MultiInstanceActivityBehavior.LOG.invalidAmountException("instances", nrOfInstances);
            }
            this.createInstances(execution, nrOfInstances);
        }
    }
    
    protected void performInstance(final ActivityExecution execution, final PvmActivity activity, final int loopCounter) {
        this.setLoopVariable(execution, "loopCounter", loopCounter);
        this.evaluateCollectionVariable(execution, loopCounter);
        execution.setEnded(false);
        execution.setActive(true);
        execution.executeActivity(activity);
    }
    
    protected void evaluateCollectionVariable(final ActivityExecution execution, final int loopCounter) {
        if (this.usesCollection() && this.collectionElementVariable != null) {
            Collection<?> collection = null;
            if (this.collectionExpression != null) {
                collection = (Collection<?>)this.collectionExpression.getValue(execution);
            }
            else if (this.collectionVariable != null) {
                collection = (Collection<?>)execution.getVariable(this.collectionVariable);
            }
            final Object value = this.getElementAtIndex(loopCounter, collection);
            this.setLoopVariable(execution, this.collectionElementVariable, value);
        }
    }
    
    protected abstract void createInstances(final ActivityExecution p0, final int p1) throws Exception;
    
    protected int resolveNrOfInstances(final ActivityExecution execution) {
        int nrOfInstances = -1;
        if (this.loopCardinalityExpression != null) {
            nrOfInstances = this.resolveLoopCardinality(execution);
        }
        else if (this.collectionExpression != null) {
            final Object obj = this.collectionExpression.getValue(execution);
            if (!(obj instanceof Collection)) {
                throw MultiInstanceActivityBehavior.LOG.unresolvableExpressionException(this.collectionExpression.getExpressionText(), "Collection");
            }
            nrOfInstances = ((Collection)obj).size();
        }
        else {
            if (this.collectionVariable == null) {
                throw MultiInstanceActivityBehavior.LOG.resolveCollectionExpressionOrVariableReferenceException();
            }
            final Object obj = execution.getVariable(this.collectionVariable);
            if (!(obj instanceof Collection)) {
                throw MultiInstanceActivityBehavior.LOG.invalidVariableTypeException(this.collectionVariable, "Collection");
            }
            nrOfInstances = ((Collection)obj).size();
        }
        return nrOfInstances;
    }
    
    protected Object getElementAtIndex(final int i, final Collection<?> collection) {
        Object value = null;
        int index = 0;
        final Iterator<?> it = collection.iterator();
        while (index <= i) {
            value = it.next();
            ++index;
        }
        return value;
    }
    
    protected boolean usesCollection() {
        return this.collectionExpression != null || this.collectionVariable != null;
    }
    
    protected int resolveLoopCardinality(final ActivityExecution execution) {
        final Object value = this.loopCardinalityExpression.getValue(execution);
        if (value instanceof Number) {
            return ((Number)value).intValue();
        }
        if (value instanceof String) {
            return Integer.valueOf((String)value);
        }
        throw MultiInstanceActivityBehavior.LOG.expressionNotANumberException("loopCardinality", this.loopCardinalityExpression.getExpressionText());
    }
    
    protected boolean completionConditionSatisfied(final ActivityExecution execution) {
        if (this.completionConditionExpression == null) {
            return false;
        }
        final Object value = this.completionConditionExpression.getValue(execution);
        if (!(value instanceof Boolean)) {
            throw MultiInstanceActivityBehavior.LOG.expressionNotBooleanException("completionCondition", this.completionConditionExpression.getExpressionText());
        }
        final Boolean booleanValue = (Boolean)value;
        MultiInstanceActivityBehavior.LOG.multiInstanceCompletionConditionState(booleanValue);
        return booleanValue;
    }
    
    @Override
    public void doLeave(final ActivityExecution execution) {
        CompensationUtil.createEventScopeExecution((ExecutionEntity)execution);
        super.doLeave(execution);
    }
    
    public ActivityImpl getInnerActivity(final PvmActivity miBodyActivity) {
        for (final PvmActivity activity : miBodyActivity.getActivities()) {
            final ActivityImpl innerActivity = (ActivityImpl)activity;
            if (!innerActivity.isCompensationHandler()) {
                return innerActivity;
            }
        }
        throw new ProcessEngineException("inner activity of multi instance body activity '" + miBodyActivity.getId() + "' not found");
    }
    
    protected void setLoopVariable(final ActivityExecution execution, final String variableName, final Object value) {
        execution.setVariableLocal(variableName, value);
    }
    
    protected Integer getLoopVariable(final ActivityExecution execution, final String variableName) {
        final IntegerValue value = execution.getVariableLocalTyped(variableName);
        EnsureUtil.ensureNotNull("The variable \"" + variableName + "\" could not be found in execution with id " + execution.getId(), "value", value);
        return (Integer)value.getValue();
    }
    
    protected Integer getLocalLoopVariable(final ActivityExecution execution, final String variableName) {
        return (Integer)execution.getVariableLocal(variableName);
    }
    
    public boolean hasLoopVariable(final ActivityExecution execution, final String variableName) {
        return execution.hasVariableLocal(variableName);
    }
    
    public void removeLoopVariable(final ActivityExecution execution, final String variableName) {
        execution.removeVariableLocal(variableName);
    }
    
    public Expression getLoopCardinalityExpression() {
        return this.loopCardinalityExpression;
    }
    
    public void setLoopCardinalityExpression(final Expression loopCardinalityExpression) {
        this.loopCardinalityExpression = loopCardinalityExpression;
    }
    
    public Expression getCompletionConditionExpression() {
        return this.completionConditionExpression;
    }
    
    public void setCompletionConditionExpression(final Expression completionConditionExpression) {
        this.completionConditionExpression = completionConditionExpression;
    }
    
    public Expression getCollectionExpression() {
        return this.collectionExpression;
    }
    
    public void setCollectionExpression(final Expression collectionExpression) {
        this.collectionExpression = collectionExpression;
    }
    
    public String getCollectionVariable() {
        return this.collectionVariable;
    }
    
    public void setCollectionVariable(final String collectionVariable) {
        this.collectionVariable = collectionVariable;
    }
    
    public String getCollectionElementVariable() {
        return this.collectionElementVariable;
    }
    
    public void setCollectionElementVariable(final String collectionElementVariable) {
        this.collectionElementVariable = collectionElementVariable;
    }
    
    static {
        LOG = ProcessEngineLogger.BPMN_BEHAVIOR_LOGGER;
    }
}
