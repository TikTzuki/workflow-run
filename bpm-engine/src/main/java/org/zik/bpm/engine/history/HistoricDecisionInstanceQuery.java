// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;
import org.zik.bpm.engine.query.Query;

public interface HistoricDecisionInstanceQuery extends Query<HistoricDecisionInstanceQuery, HistoricDecisionInstance>
{
    HistoricDecisionInstanceQuery decisionInstanceId(final String p0);
    
    HistoricDecisionInstanceQuery decisionInstanceIdIn(final String... p0);
    
    HistoricDecisionInstanceQuery decisionDefinitionId(final String p0);
    
    HistoricDecisionInstanceQuery decisionDefinitionIdIn(final String... p0);
    
    HistoricDecisionInstanceQuery decisionDefinitionKey(final String p0);
    
    HistoricDecisionInstanceQuery decisionDefinitionKeyIn(final String... p0);
    
    HistoricDecisionInstanceQuery decisionDefinitionName(final String p0);
    
    HistoricDecisionInstanceQuery decisionDefinitionNameLike(final String p0);
    
    HistoricDecisionInstanceQuery processDefinitionKey(final String p0);
    
    HistoricDecisionInstanceQuery processDefinitionId(final String p0);
    
    HistoricDecisionInstanceQuery processInstanceId(final String p0);
    
    HistoricDecisionInstanceQuery caseDefinitionKey(final String p0);
    
    HistoricDecisionInstanceQuery caseDefinitionId(final String p0);
    
    HistoricDecisionInstanceQuery caseInstanceId(final String p0);
    
    HistoricDecisionInstanceQuery activityIdIn(final String... p0);
    
    HistoricDecisionInstanceQuery activityInstanceIdIn(final String... p0);
    
    HistoricDecisionInstanceQuery evaluatedBefore(final Date p0);
    
    HistoricDecisionInstanceQuery evaluatedAfter(final Date p0);
    
    HistoricDecisionInstanceQuery userId(final String p0);
    
    HistoricDecisionInstanceQuery includeInputs();
    
    HistoricDecisionInstanceQuery includeOutputs();
    
    HistoricDecisionInstanceQuery disableBinaryFetching();
    
    HistoricDecisionInstanceQuery disableCustomObjectDeserialization();
    
    HistoricDecisionInstanceQuery rootDecisionInstanceId(final String p0);
    
    HistoricDecisionInstanceQuery rootDecisionInstancesOnly();
    
    HistoricDecisionInstanceQuery decisionRequirementsDefinitionId(final String p0);
    
    HistoricDecisionInstanceQuery decisionRequirementsDefinitionKey(final String p0);
    
    HistoricDecisionInstanceQuery tenantIdIn(final String... p0);
    
    HistoricDecisionInstanceQuery withoutTenantId();
    
    HistoricDecisionInstanceQuery orderByEvaluationTime();
    
    HistoricDecisionInstanceQuery orderByTenantId();
}
