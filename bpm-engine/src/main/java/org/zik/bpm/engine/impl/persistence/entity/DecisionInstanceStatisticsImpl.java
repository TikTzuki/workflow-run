// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.history.HistoricDecisionInstanceStatistics;

public class DecisionInstanceStatisticsImpl implements HistoricDecisionInstanceStatistics
{
    protected int evaluations;
    protected String decisionDefinitionKey;
    
    @Override
    public int getEvaluations() {
        return this.evaluations;
    }
    
    public void setEvaluations(final int evaluations) {
        this.evaluations = evaluations;
    }
    
    @Override
    public String getDecisionDefinitionKey() {
        return this.decisionDefinitionKey;
    }
    
    public void setDecisionDefinitionKey(final String decisionDefinitionKey) {
        this.decisionDefinitionKey = decisionDefinitionKey;
    }
}
