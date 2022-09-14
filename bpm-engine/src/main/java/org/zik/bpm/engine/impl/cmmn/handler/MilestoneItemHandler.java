// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.handler;

import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.zik.bpm.engine.impl.cmmn.behavior.MilestoneActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;
import java.util.List;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;

public class MilestoneItemHandler extends ItemHandler
{
    @Override
    protected List<String> getStandardEvents(final CmmnElement element) {
        return MilestoneItemHandler.EVENT_LISTENER_OR_MILESTONE_EVENTS;
    }
    
    @Override
    protected CmmnActivityBehavior getActivityBehavior() {
        return new MilestoneActivityBehavior();
    }
    
    @Override
    protected void initializeManualActivationRule(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
    }
    
    @Override
    protected void initializeExitCriterias(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
    }
}
