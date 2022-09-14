// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.impl.cmmn.CaseControlRule;

public class CaseControlRuleImpl implements CaseControlRule
{
    protected static final CmmnBehaviorLogger LOG;
    protected Expression expression;
    
    public CaseControlRuleImpl(final Expression expression) {
        this.expression = expression;
    }
    
    @Override
    public boolean evaluate(final CmmnActivityExecution execution) {
        if (this.expression == null) {
            return true;
        }
        final Object result = this.expression.getValue(execution);
        EnsureUtil.ensureNotNull("rule expression returns null", "result", result);
        if (!(result instanceof Boolean)) {
            throw CaseControlRuleImpl.LOG.ruleExpressionNotBooleanException(result);
        }
        return (boolean)result;
    }
    
    static {
        LOG = ProcessEngineLogger.CMNN_BEHAVIOR_LOGGER;
    }
}
