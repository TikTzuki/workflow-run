// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.handler;

import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.zik.bpm.engine.impl.cmmn.behavior.EventListenerActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;
import java.util.List;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;

public class EventListenerItemHandler extends ItemHandler
{
    @Override
    protected List<String> getStandardEvents(final CmmnElement element) {
        return EventListenerItemHandler.EVENT_LISTENER_OR_MILESTONE_EVENTS;
    }
    
    @Override
    protected CmmnActivityBehavior getActivityBehavior() {
        return new EventListenerActivityBehavior();
    }
    
    @Override
    protected void initializeEntryCriterias(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
    }
    
    @Override
    protected void initializeExitCriterias(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
    }
    
    @Override
    protected void initializeRepetitionRule(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
    }
    
    @Override
    protected void initializeRequiredRule(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
    }
    
    @Override
    protected void initializeManualActivationRule(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
    }
}
