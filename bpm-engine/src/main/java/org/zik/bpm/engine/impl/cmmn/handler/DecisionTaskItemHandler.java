// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.handler;

import org.camunda.bpm.model.cmmn.instance.PlanItemDefinition;
import org.camunda.bpm.model.cmmn.instance.DecisionRefExpression;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;
import org.zik.bpm.engine.impl.core.model.BaseCallableElement;
import org.zik.bpm.engine.impl.dmn.result.DecisionResultMapper;
import org.zik.bpm.engine.impl.util.DecisionEvaluationUtil;
import org.zik.bpm.engine.impl.cmmn.behavior.DmnDecisionTaskActivityBehavior;
import org.camunda.bpm.model.cmmn.instance.DecisionTask;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;

public class DecisionTaskItemHandler extends CallingTaskItemHandler
{
    @Override
    protected void initializeActivity(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        super.initializeActivity(element, activity, context);
        this.initializeResultVariable(element, activity, context);
        this.initializeDecisionTableResultMapper(element, activity, context);
    }
    
    protected void initializeResultVariable(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final DecisionTask decisionTask = this.getDefinition(element);
        final DmnDecisionTaskActivityBehavior behavior = this.getActivityBehavior(activity);
        final String resultVariable = decisionTask.getCamundaResultVariable();
        behavior.setResultVariable(resultVariable);
    }
    
    protected void initializeDecisionTableResultMapper(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final DecisionTask decisionTask = this.getDefinition(element);
        final DmnDecisionTaskActivityBehavior behavior = this.getActivityBehavior(activity);
        final String mapper = decisionTask.getCamundaMapDecisionResult();
        final DecisionResultMapper decisionResultMapper = DecisionEvaluationUtil.getDecisionResultMapperForName(mapper);
        behavior.setDecisionTableResultMapper(decisionResultMapper);
    }
    
    @Override
    protected BaseCallableElement createCallableElement() {
        return new BaseCallableElement();
    }
    
    @Override
    protected CmmnActivityBehavior getActivityBehavior() {
        return new DmnDecisionTaskActivityBehavior();
    }
    
    protected DmnDecisionTaskActivityBehavior getActivityBehavior(final CmmnActivity activity) {
        return (DmnDecisionTaskActivityBehavior)activity.getActivityBehavior();
    }
    
    @Override
    protected String getDefinitionKey(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final DecisionTask definition = this.getDefinition(element);
        String decision = definition.getDecision();
        if (decision == null) {
            final DecisionRefExpression decisionExpression = definition.getDecisionExpression();
            if (decisionExpression != null) {
                decision = decisionExpression.getText();
            }
        }
        return decision;
    }
    
    @Override
    protected String getBinding(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final DecisionTask definition = this.getDefinition(element);
        return definition.getCamundaDecisionBinding();
    }
    
    @Override
    protected String getVersion(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final DecisionTask definition = this.getDefinition(element);
        return definition.getCamundaDecisionVersion();
    }
    
    @Override
    protected String getTenantId(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final DecisionTask definition = this.getDefinition(element);
        return definition.getCamundaDecisionTenantId();
    }
    
    protected DecisionTask getDefinition(final CmmnElement element) {
        return (DecisionTask)super.getDefinition(element);
    }
}
