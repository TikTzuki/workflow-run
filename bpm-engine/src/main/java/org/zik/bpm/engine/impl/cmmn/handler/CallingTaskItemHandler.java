// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.handler;

import org.zik.bpm.engine.impl.el.Expression;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ConstantValueProvider;
import org.zik.bpm.engine.impl.el.ElValueProvider;
import org.zik.bpm.engine.impl.util.StringUtil;
import org.zik.bpm.engine.impl.core.variable.mapping.value.NullValueProvider;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ParameterValueProvider;
import org.zik.bpm.engine.impl.el.ExpressionManager;
import org.zik.bpm.engine.impl.core.model.BaseCallableElement;
import org.zik.bpm.engine.repository.Deployment;
import org.zik.bpm.engine.impl.cmmn.behavior.CallingTaskActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;

public abstract class CallingTaskItemHandler extends TaskItemHandler
{
    @Override
    protected void initializeActivity(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        super.initializeActivity(element, activity, context);
        this.initializeCallableElement(element, activity, context);
    }
    
    protected void initializeCallableElement(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final Deployment deployment = context.getDeployment();
        String deploymentId = null;
        if (deployment != null) {
            deploymentId = deployment.getId();
        }
        final BaseCallableElement callableElement = this.createCallableElement();
        callableElement.setDeploymentId(deploymentId);
        final CallingTaskActivityBehavior behavior = (CallingTaskActivityBehavior)activity.getActivityBehavior();
        behavior.setCallableElement(callableElement);
        this.initializeDefinitionKey(element, activity, context, callableElement);
        this.initializeBinding(element, activity, context, callableElement);
        this.initializeVersion(element, activity, context, callableElement);
        this.initializeTenantId(element, activity, context, callableElement);
    }
    
    protected void initializeDefinitionKey(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context, final BaseCallableElement callableElement) {
        final ExpressionManager expressionManager = context.getExpressionManager();
        final String definitionKey = this.getDefinitionKey(element, activity, context);
        final ParameterValueProvider definitionKeyProvider = this.createParameterValueProvider(definitionKey, expressionManager);
        callableElement.setDefinitionKeyValueProvider(definitionKeyProvider);
    }
    
    protected void initializeBinding(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context, final BaseCallableElement callableElement) {
        final String binding = this.getBinding(element, activity, context);
        if (BaseCallableElement.CallableElementBinding.DEPLOYMENT.getValue().equals(binding)) {
            callableElement.setBinding(BaseCallableElement.CallableElementBinding.DEPLOYMENT);
        }
        else if (BaseCallableElement.CallableElementBinding.LATEST.getValue().equals(binding)) {
            callableElement.setBinding(BaseCallableElement.CallableElementBinding.LATEST);
        }
        else if (BaseCallableElement.CallableElementBinding.VERSION.getValue().equals(binding)) {
            callableElement.setBinding(BaseCallableElement.CallableElementBinding.VERSION);
        }
    }
    
    protected void initializeVersion(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context, final BaseCallableElement callableElement) {
        final ExpressionManager expressionManager = context.getExpressionManager();
        final String version = this.getVersion(element, activity, context);
        final ParameterValueProvider versionProvider = this.createParameterValueProvider(version, expressionManager);
        callableElement.setVersionValueProvider(versionProvider);
    }
    
    protected void initializeTenantId(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context, final BaseCallableElement callableElement) {
        ParameterValueProvider tenantIdProvider = null;
        final ExpressionManager expressionManager = context.getExpressionManager();
        final String tenantId = this.getTenantId(element, activity, context);
        if (tenantId != null && tenantId.length() > 0) {
            tenantIdProvider = this.createParameterValueProvider(tenantId, expressionManager);
        }
        callableElement.setTenantIdProvider(tenantIdProvider);
    }
    
    protected ParameterValueProvider createParameterValueProvider(final String value, final ExpressionManager expressionManager) {
        if (value == null) {
            return new NullValueProvider();
        }
        if (StringUtil.isCompositeExpression(value, expressionManager)) {
            final Expression expression = expressionManager.createExpression(value);
            return new ElValueProvider(expression);
        }
        return new ConstantValueProvider(value);
    }
    
    protected abstract BaseCallableElement createCallableElement();
    
    protected abstract String getDefinitionKey(final CmmnElement p0, final CmmnActivity p1, final CmmnHandlerContext p2);
    
    protected abstract String getBinding(final CmmnElement p0, final CmmnActivity p1, final CmmnHandlerContext p2);
    
    protected abstract String getVersion(final CmmnElement p0, final CmmnActivity p1, final CmmnHandlerContext p2);
    
    protected abstract String getTenantId(final CmmnElement p0, final CmmnActivity p1, final CmmnHandlerContext p2);
}
