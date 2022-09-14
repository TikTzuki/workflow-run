// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instance;

import org.zik.bpm.engine.impl.migration.instance.MigratingCompensationEventSubscriptionInstance;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.migration.instance.MigratingProcessInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingEventScopeInstance;

public class NoUnmappedCompensationStartEventValidator implements MigratingCompensationInstanceValidator
{
    @Override
    public void validate(final MigratingEventScopeInstance migratingInstance, final MigratingProcessInstance migratingProcessInstance, final MigratingActivityInstanceValidationReportImpl ancestorInstanceReport) {
        final MigratingCompensationEventSubscriptionInstance eventSubscription = migratingInstance.getEventSubscription();
        final ActivityImpl eventHandlerActivity = (ActivityImpl)eventSubscription.getSourceScope();
        if (eventHandlerActivity.isTriggeredByEvent() && eventSubscription.getTargetScope() == null && !migratingInstance.getChildren().isEmpty()) {
            ancestorInstanceReport.addFailure("Cannot migrate subscription for compensation handler '" + eventSubscription.getSourceScope().getId() + "'. There is no migration instruction for the compensation start event");
        }
    }
    
    @Override
    public void validate(final MigratingCompensationEventSubscriptionInstance migratingInstance, final MigratingProcessInstance migratingProcessInstance, final MigratingActivityInstanceValidationReportImpl ancestorInstanceReport) {
    }
}
