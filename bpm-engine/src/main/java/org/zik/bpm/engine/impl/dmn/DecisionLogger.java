// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn;

import org.zik.bpm.engine.BadUserRequestException;
import java.util.Collection;
import org.zik.bpm.engine.ProcessEngineException;
import org.camunda.bpm.dmn.engine.DmnEngineException;
import org.zik.bpm.engine.impl.dmn.result.DecisionResultMapper;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class DecisionLogger extends ProcessEngineLogger
{
    public ProcessEngineException decisionResultMappingException(final DmnDecisionResult decisionResult, final DecisionResultMapper resultMapper, final DmnEngineException cause) {
        return new ProcessEngineException(this.exceptionMessage("001", "The decision result mapper '{}' failed to process '{}'", new Object[] { resultMapper, decisionResult }), (Throwable)cause);
    }
    
    public ProcessEngineException decisionResultCollectMappingException(final Collection<String> outputNames, final DmnDecisionResult decisionResult, final DecisionResultMapper resultMapper) {
        return new ProcessEngineException(this.exceptionMessage("002", "The decision result mapper '{}' failed to process '{}'. The decision outputs should only contains values for one output name but found '{}'.", new Object[] { resultMapper, decisionResult, outputNames }));
    }
    
    public BadUserRequestException exceptionEvaluateDecisionDefinitionByIdAndTenantId() {
        return new BadUserRequestException(this.exceptionMessage("003", "Cannot specify a tenant-id when evaluate a decision definition by decision definition id.", new Object[0]));
    }
    
    public ProcessEngineException exceptionParseDmnResource(final String resouceName, final Exception cause) {
        return new ProcessEngineException(this.exceptionMessage("004", "Unable to transform DMN resource '{}'.", new Object[] { resouceName }), cause);
    }
    
    public ProcessEngineException exceptionNoDrdForResource(final String resourceName) {
        return new ProcessEngineException(this.exceptionMessage("005", "Found no decision requirements definition for DMN resource '{}'.", new Object[] { resourceName }));
    }
}
