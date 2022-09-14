// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.result;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Iterator;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.Collection;
import java.util.Collections;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.zik.bpm.engine.impl.dmn.DecisionLogger;

public class CollectEntriesDecisionResultMapper implements DecisionResultMapper
{
    protected static final DecisionLogger LOG;
    
    @Override
    public Object mapDecisionResult(final DmnDecisionResult decisionResult) {
        if (decisionResult.isEmpty()) {
            return Collections.emptyList();
        }
        final Set<String> outputNames = this.collectOutputNames(decisionResult);
        if (outputNames.size() > 1) {
            throw CollectEntriesDecisionResultMapper.LOG.decisionResultCollectMappingException(outputNames, decisionResult, this);
        }
        final String outputName = outputNames.iterator().next();
        return decisionResult.collectEntries(outputName);
    }
    
    protected Set<String> collectOutputNames(final DmnDecisionResult decisionResult) {
        final Set<String> outputNames = new HashSet<String>();
        for (final Map<String, Object> entryMap : decisionResult.getResultList()) {
            outputNames.addAll(entryMap.keySet());
        }
        return outputNames;
    }
    
    @Override
    public String toString() {
        return "CollectEntriesDecisionResultMapper{}";
    }
    
    static {
        LOG = ProcessEngineLogger.DECISION_LOGGER;
    }
}
