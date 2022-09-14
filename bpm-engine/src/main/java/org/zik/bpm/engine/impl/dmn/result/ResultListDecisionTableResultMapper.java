// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.result;

import org.camunda.bpm.dmn.engine.DmnDecisionResult;

public class ResultListDecisionTableResultMapper implements DecisionResultMapper
{
    @Override
    public Object mapDecisionResult(final DmnDecisionResult decisionResult) {
        return decisionResult.getResultList();
    }
}
