// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.result;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.camunda.bpm.dmn.engine.DmnDecisionResultEntries;
import org.camunda.bpm.dmn.engine.DmnEngineException;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.zik.bpm.engine.impl.dmn.DecisionLogger;

public class SingleResultDecisionResultMapper implements DecisionResultMapper
{
    protected static final DecisionLogger LOG;
    
    @Override
    public Object mapDecisionResult(final DmnDecisionResult decisionResult) {
        try {
            final DmnDecisionResultEntries singleResult = decisionResult.getSingleResult();
            if (singleResult != null) {
                return singleResult.getEntryMap();
            }
            return Variables.untypedNullValue();
        }
        catch (DmnEngineException e) {
            throw SingleResultDecisionResultMapper.LOG.decisionResultMappingException(decisionResult, this, e);
        }
    }
    
    @Override
    public String toString() {
        return "SingleResultDecisionResultMapper{}";
    }
    
    static {
        LOG = ProcessEngineLogger.DECISION_LOGGER;
    }
}
