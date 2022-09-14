// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.behavior;

import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.zik.bpm.engine.impl.util.DecisionEvaluationUtil;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;
import org.zik.bpm.engine.impl.dmn.result.DecisionResultMapper;

public class DmnDecisionTaskActivityBehavior extends DecisionTaskActivityBehavior
{
    protected DecisionResultMapper decisionResultMapper;
    
    @Override
    protected void performStart(final CmmnActivityExecution execution) {
        try {
            final CaseExecutionEntity executionEntity = (CaseExecutionEntity)execution;
            DecisionEvaluationUtil.evaluateDecision(executionEntity, executionEntity.getCaseDefinitionTenantId(), this.callableElement, this.resultVariable, this.decisionResultMapper);
            if (execution.isActive()) {
                execution.complete();
            }
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e2) {
            throw DmnDecisionTaskActivityBehavior.LOG.decisionDefinitionEvaluationFailed(execution, e2);
        }
    }
    
    public DecisionResultMapper getDecisionTableResultMapper() {
        return this.decisionResultMapper;
    }
    
    public void setDecisionTableResultMapper(final DecisionResultMapper decisionResultMapper) {
        this.decisionResultMapper = decisionResultMapper;
    }
}
