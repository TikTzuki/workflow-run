// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.zik.bpm.engine.impl.util.DecisionEvaluationUtil;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.dmn.result.DecisionResultMapper;
import org.zik.bpm.engine.impl.core.model.BaseCallableElement;

public class DmnBusinessRuleTaskActivityBehavior extends AbstractBpmnActivityBehavior
{
    protected final BaseCallableElement callableElement;
    protected final String resultVariable;
    protected final DecisionResultMapper decisionResultMapper;
    
    public DmnBusinessRuleTaskActivityBehavior(final BaseCallableElement callableElement, final String resultVariableName, final DecisionResultMapper decisionResultMapper) {
        this.callableElement = callableElement;
        this.resultVariable = resultVariableName;
        this.decisionResultMapper = decisionResultMapper;
    }
    
    @Override
    public void execute(final ActivityExecution execution) throws Exception {
        this.executeWithErrorPropagation(execution, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                final ExecutionEntity executionEntity = (ExecutionEntity)execution;
                DecisionEvaluationUtil.evaluateDecision(executionEntity, executionEntity.getProcessDefinitionTenantId(), DmnBusinessRuleTaskActivityBehavior.this.callableElement, DmnBusinessRuleTaskActivityBehavior.this.resultVariable, DmnBusinessRuleTaskActivityBehavior.this.decisionResultMapper);
                DmnBusinessRuleTaskActivityBehavior.this.leave(execution);
                return null;
            }
        });
    }
}
