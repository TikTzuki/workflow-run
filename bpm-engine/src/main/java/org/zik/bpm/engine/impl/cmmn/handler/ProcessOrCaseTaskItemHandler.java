// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.handler;

import org.zik.bpm.engine.impl.core.model.BaseCallableElement;
import org.camunda.bpm.model.cmmn.instance.PlanItemDefinition;
import org.camunda.bpm.model.cmmn.instance.camunda.CamundaOut;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ParameterValueProvider;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.el.ExpressionManager;
import org.zik.bpm.engine.impl.core.model.CallableElementParameter;
import org.camunda.bpm.model.cmmn.instance.camunda.CamundaIn;
import org.zik.bpm.engine.impl.cmmn.behavior.ProcessOrCaseTaskActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;
import org.zik.bpm.engine.impl.core.model.CallableElement;

public abstract class ProcessOrCaseTaskItemHandler extends CallingTaskItemHandler
{
    @Override
    protected CallableElement createCallableElement() {
        return new CallableElement();
    }
    
    @Override
    protected void initializeCallableElement(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        super.initializeCallableElement(element, activity, context);
        final ProcessOrCaseTaskActivityBehavior behavior = (ProcessOrCaseTaskActivityBehavior)activity.getActivityBehavior();
        final CallableElement callableElement = behavior.getCallableElement();
        this.initializeInputParameter(element, activity, context, callableElement);
        this.initializeOutputParameter(element, activity, context, callableElement);
    }
    
    protected void initializeInputParameter(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context, final CallableElement callableElement) {
        final ExpressionManager expressionManager = context.getExpressionManager();
        final List<CamundaIn> inputs = this.getInputs(element);
        for (final CamundaIn input : inputs) {
            final String businessKey = input.getCamundaBusinessKey();
            if (businessKey != null && !businessKey.isEmpty()) {
                final ParameterValueProvider businessKeyValueProvider = this.createParameterValueProvider(businessKey, expressionManager);
                callableElement.setBusinessKeyValueProvider(businessKeyValueProvider);
            }
            else {
                final CallableElementParameter parameter = new CallableElementParameter();
                callableElement.addInput(parameter);
                if (input.getCamundaLocal()) {
                    parameter.setReadLocal(true);
                }
                final String variables = input.getCamundaVariables();
                if ("all".equals(variables)) {
                    parameter.setAllVariables(true);
                }
                else {
                    String source = input.getCamundaSource();
                    if (source == null || source.isEmpty()) {
                        source = input.getCamundaSourceExpression();
                    }
                    final ParameterValueProvider sourceValueProvider = this.createParameterValueProvider(source, expressionManager);
                    parameter.setSourceValueProvider(sourceValueProvider);
                    final String target = input.getCamundaTarget();
                    parameter.setTarget(target);
                }
            }
        }
    }
    
    protected void initializeOutputParameter(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context, final CallableElement callableElement) {
        final ExpressionManager expressionManager = context.getExpressionManager();
        final List<CamundaOut> outputs = this.getOutputs(element);
        for (final CamundaOut output : outputs) {
            final CallableElementParameter parameter = new CallableElementParameter();
            callableElement.addOutput(parameter);
            final String variables = output.getCamundaVariables();
            if ("all".equals(variables)) {
                parameter.setAllVariables(true);
            }
            else {
                String source = output.getCamundaSource();
                if (source == null || source.isEmpty()) {
                    source = output.getCamundaSourceExpression();
                }
                final ParameterValueProvider sourceValueProvider = this.createParameterValueProvider(source, expressionManager);
                parameter.setSourceValueProvider(sourceValueProvider);
                final String target = output.getCamundaTarget();
                parameter.setTarget(target);
            }
        }
    }
    
    protected List<CamundaIn> getInputs(final CmmnElement element) {
        final PlanItemDefinition definition = this.getDefinition(element);
        return this.queryExtensionElementsByClass((CmmnElement)definition, CamundaIn.class);
    }
    
    protected List<CamundaOut> getOutputs(final CmmnElement element) {
        final PlanItemDefinition definition = this.getDefinition(element);
        return this.queryExtensionElementsByClass((CmmnElement)definition, CamundaOut.class);
    }
}
