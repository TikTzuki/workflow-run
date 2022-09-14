// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.activity;

import org.zik.bpm.engine.impl.bpmn.behavior.EventSubProcessStartConditionalEventActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.BoundaryConditionalEventActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.IntermediateConditionalEventBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.InclusiveGatewayActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.ParallelGatewayActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.ExternalTaskActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.EventSubProcessStartEventActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.EventSubProcessActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.EventBasedGatewayActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.IntermediateCatchEventActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.CaseCallActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.CallActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.ReceiveTaskActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.SequentialMultiInstanceActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.ParallelMultiInstanceActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.BoundaryEventActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.SubProcessActivityBehavior;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import java.util.List;

public class SupportedActivityValidator implements MigrationActivityValidator
{
    public static SupportedActivityValidator INSTANCE;
    public static List<Class<? extends ActivityBehavior>> SUPPORTED_ACTIVITY_BEHAVIORS;
    
    @Override
    public boolean valid(final ActivityImpl activity) {
        return activity != null && (this.isSupportedActivity(activity) || this.isAsync(activity));
    }
    
    public boolean isSupportedActivity(final ActivityImpl activity) {
        return SupportedActivityValidator.SUPPORTED_ACTIVITY_BEHAVIORS.contains(activity.getActivityBehavior().getClass());
    }
    
    protected boolean isAsync(final ActivityImpl activity) {
        return activity.isAsyncBefore() || activity.isAsyncAfter();
    }
    
    static {
        SupportedActivityValidator.INSTANCE = new SupportedActivityValidator();
        (SupportedActivityValidator.SUPPORTED_ACTIVITY_BEHAVIORS = new ArrayList<Class<? extends ActivityBehavior>>()).add(SubProcessActivityBehavior.class);
        SupportedActivityValidator.SUPPORTED_ACTIVITY_BEHAVIORS.add(UserTaskActivityBehavior.class);
        SupportedActivityValidator.SUPPORTED_ACTIVITY_BEHAVIORS.add(BoundaryEventActivityBehavior.class);
        SupportedActivityValidator.SUPPORTED_ACTIVITY_BEHAVIORS.add(ParallelMultiInstanceActivityBehavior.class);
        SupportedActivityValidator.SUPPORTED_ACTIVITY_BEHAVIORS.add(SequentialMultiInstanceActivityBehavior.class);
        SupportedActivityValidator.SUPPORTED_ACTIVITY_BEHAVIORS.add(ReceiveTaskActivityBehavior.class);
        SupportedActivityValidator.SUPPORTED_ACTIVITY_BEHAVIORS.add(CallActivityBehavior.class);
        SupportedActivityValidator.SUPPORTED_ACTIVITY_BEHAVIORS.add(CaseCallActivityBehavior.class);
        SupportedActivityValidator.SUPPORTED_ACTIVITY_BEHAVIORS.add(IntermediateCatchEventActivityBehavior.class);
        SupportedActivityValidator.SUPPORTED_ACTIVITY_BEHAVIORS.add(EventBasedGatewayActivityBehavior.class);
        SupportedActivityValidator.SUPPORTED_ACTIVITY_BEHAVIORS.add(EventSubProcessActivityBehavior.class);
        SupportedActivityValidator.SUPPORTED_ACTIVITY_BEHAVIORS.add(EventSubProcessStartEventActivityBehavior.class);
        SupportedActivityValidator.SUPPORTED_ACTIVITY_BEHAVIORS.add(ExternalTaskActivityBehavior.class);
        SupportedActivityValidator.SUPPORTED_ACTIVITY_BEHAVIORS.add(ParallelGatewayActivityBehavior.class);
        SupportedActivityValidator.SUPPORTED_ACTIVITY_BEHAVIORS.add(InclusiveGatewayActivityBehavior.class);
        SupportedActivityValidator.SUPPORTED_ACTIVITY_BEHAVIORS.add(IntermediateConditionalEventBehavior.class);
        SupportedActivityValidator.SUPPORTED_ACTIVITY_BEHAVIORS.add(BoundaryConditionalEventActivityBehavior.class);
        SupportedActivityValidator.SUPPORTED_ACTIVITY_BEHAVIORS.add(EventSubProcessStartConditionalEventActivityBehavior.class);
    }
}
