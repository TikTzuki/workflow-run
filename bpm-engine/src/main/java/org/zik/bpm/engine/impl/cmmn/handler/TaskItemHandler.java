// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.handler;

import java.util.List;
import org.zik.bpm.engine.impl.cmmn.behavior.TaskActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;
import org.camunda.bpm.model.cmmn.instance.Task;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;

public class TaskItemHandler extends ItemHandler
{
    @Override
    protected void initializeActivity(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        super.initializeActivity(element, activity, context);
        this.initializeBlocking(element, activity, context);
    }
    
    protected void initializeBlocking(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final Task task = (Task)this.getDefinition(element);
        activity.setProperty("isBlocking", task.isBlocking());
    }
    
    @Override
    protected CmmnActivityBehavior getActivityBehavior() {
        return new TaskActivityBehavior();
    }
    
    @Override
    protected List<String> getStandardEvents(final CmmnElement element) {
        return TaskItemHandler.TASK_OR_STAGE_EVENTS;
    }
}
