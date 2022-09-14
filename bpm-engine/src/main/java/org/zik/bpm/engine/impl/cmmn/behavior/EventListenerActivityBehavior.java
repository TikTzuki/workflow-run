// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;

public class EventListenerActivityBehavior extends EventListenerOrMilestoneActivityBehavior
{
    protected static final CmmnBehaviorLogger LOG;
    
    @Override
    public void created(final CmmnActivityExecution execution) {
    }
    
    @Override
    protected String getTypeName() {
        return "event listener";
    }
    
    @Override
    protected boolean isAtLeastOneEntryCriterionSatisfied(final CmmnActivityExecution execution) {
        return false;
    }
    
    @Override
    public void fireEntryCriteria(final CmmnActivityExecution execution) {
        throw EventListenerActivityBehavior.LOG.criteriaNotAllowedForEventListenerException("entry", execution.getId());
    }
    
    public void repeat(final CmmnActivityExecution execution) {
    }
    
    @Override
    protected boolean evaluateRepetitionRule(final CmmnActivityExecution execution) {
        return false;
    }
    
    static {
        LOG = ProcessEngineLogger.CMNN_BEHAVIOR_LOGGER;
    }
}
