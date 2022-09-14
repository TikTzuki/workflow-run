// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

import java.util.ArrayList;
import java.util.Collection;

public class HistoricDecisionEvaluationEvent extends HistoryEvent
{
    private static final long serialVersionUID = 1L;
    protected HistoricDecisionInstanceEntity rootHistoricDecisionInstance;
    protected Collection<HistoricDecisionInstanceEntity> requiredHistoricDecisionInstances;
    
    public HistoricDecisionEvaluationEvent() {
        this.requiredHistoricDecisionInstances = new ArrayList<HistoricDecisionInstanceEntity>();
    }
    
    public HistoricDecisionInstanceEntity getRootHistoricDecisionInstance() {
        return this.rootHistoricDecisionInstance;
    }
    
    public void setRootHistoricDecisionInstance(final HistoricDecisionInstanceEntity rootHistoricDecisionInstance) {
        this.rootHistoricDecisionInstance = rootHistoricDecisionInstance;
    }
    
    public Collection<HistoricDecisionInstanceEntity> getRequiredHistoricDecisionInstances() {
        return this.requiredHistoricDecisionInstances;
    }
    
    public void setRequiredHistoricDecisionInstances(final Collection<HistoricDecisionInstanceEntity> requiredHistoricDecisionInstances) {
        this.requiredHistoricDecisionInstances = requiredHistoricDecisionInstances;
    }
}
