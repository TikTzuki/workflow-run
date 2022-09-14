// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.handler;

import java.util.List;
import org.zik.bpm.engine.impl.cmmn.behavior.StageActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;
import org.camunda.bpm.model.cmmn.instance.PlanItemDefinition;
import org.camunda.bpm.model.cmmn.instance.Stage;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;

public class StageItemHandler extends ItemHandler
{
    @Override
    protected void initializeAutoComplete(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final PlanItemDefinition definition = this.getDefinition(element);
        if (definition instanceof Stage) {
            final Stage stage = (Stage)definition;
            activity.setProperty("autoComplete", stage.isAutoComplete());
        }
    }
    
    @Override
    protected CmmnActivityBehavior getActivityBehavior() {
        return new StageActivityBehavior();
    }
    
    @Override
    protected List<String> getStandardEvents(final CmmnElement element) {
        return StageItemHandler.TASK_OR_STAGE_EVENTS;
    }
}
