// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import org.camunda.bpm.engine.variable.context.VariableContext;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.dmn.invocation.VariableScopeContext;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.context.Context;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionTableResultImpl;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.zik.bpm.engine.impl.dmn.invocation.DecisionInvocation;
import org.zik.bpm.engine.repository.DecisionDefinition;
import org.camunda.bpm.engine.variable.Variables;
import org.zik.bpm.engine.impl.core.model.BaseCallableElement;
import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.zik.bpm.engine.impl.dmn.result.ResultListDecisionTableResultMapper;
import org.zik.bpm.engine.impl.dmn.result.CollectEntriesDecisionResultMapper;
import org.zik.bpm.engine.impl.dmn.result.SingleResultDecisionResultMapper;
import org.zik.bpm.engine.impl.dmn.result.SingleEntryDecisionResultMapper;
import org.zik.bpm.engine.impl.dmn.result.DecisionResultMapper;

public class DecisionEvaluationUtil
{
    public static final String DECISION_RESULT_VARIABLE = "decisionResult";
    
    public static DecisionResultMapper getDecisionResultMapperForName(final String mapDecisionResult) {
        if ("singleEntry".equals(mapDecisionResult)) {
            return new SingleEntryDecisionResultMapper();
        }
        if ("singleResult".equals(mapDecisionResult)) {
            return new SingleResultDecisionResultMapper();
        }
        if ("collectEntries".equals(mapDecisionResult)) {
            return new CollectEntriesDecisionResultMapper();
        }
        if ("resultList".equals(mapDecisionResult) || mapDecisionResult == null) {
            return new ResultListDecisionTableResultMapper();
        }
        return null;
    }
    
    public static void evaluateDecision(final AbstractVariableScope execution, final String defaultTenantId, final BaseCallableElement callableElement, final String resultVariable, final DecisionResultMapper decisionResultMapper) throws Exception {
        final DecisionDefinition decisionDefinition = resolveDecisionDefinition(callableElement, execution, defaultTenantId);
        final DecisionInvocation invocation = createInvocation(decisionDefinition, execution);
        invoke(invocation);
        final DmnDecisionResult result = invocation.getInvocationResult();
        if (result != null) {
            final TypedValue typedValue = Variables.untypedValue((Object)result, true);
            execution.setVariableLocal("decisionResult", typedValue);
            if (resultVariable != null && decisionResultMapper != null) {
                final Object mappedDecisionResult = decisionResultMapper.mapDecisionResult(result);
                execution.setVariable(resultVariable, mappedDecisionResult);
            }
        }
    }
    
    public static DmnDecisionResult evaluateDecision(final DecisionDefinition decisionDefinition, final VariableMap variables) throws Exception {
        final DecisionInvocation invocation = createInvocation(decisionDefinition, variables);
        invoke(invocation);
        return invocation.getInvocationResult();
    }
    
    public static DmnDecisionTableResult evaluateDecisionTable(final DecisionDefinition decisionDefinition, final VariableMap variables) throws Exception {
        final DmnDecisionResult decisionResult = evaluateDecision(decisionDefinition, variables);
        return (DmnDecisionTableResult)DmnDecisionTableResultImpl.wrap(decisionResult);
    }
    
    protected static void invoke(final DecisionInvocation invocation) throws Exception {
        Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(invocation);
    }
    
    protected static DecisionInvocation createInvocation(final DecisionDefinition decisionDefinition, final VariableMap variables) {
        return createInvocation(decisionDefinition, variables.asVariableContext());
    }
    
    protected static DecisionInvocation createInvocation(final DecisionDefinition decisionDefinition, final AbstractVariableScope variableScope) {
        return createInvocation(decisionDefinition, (VariableContext)VariableScopeContext.wrap(variableScope));
    }
    
    protected static DecisionInvocation createInvocation(final DecisionDefinition decisionDefinition, final VariableContext variableContext) {
        return new DecisionInvocation(decisionDefinition, variableContext);
    }
    
    protected static DecisionDefinition resolveDecisionDefinition(final BaseCallableElement callableElement, final AbstractVariableScope execution, final String defaultTenantId) {
        return CallableElementUtil.getDecisionDefinitionToCall(execution, defaultTenantId, callableElement);
    }
}
