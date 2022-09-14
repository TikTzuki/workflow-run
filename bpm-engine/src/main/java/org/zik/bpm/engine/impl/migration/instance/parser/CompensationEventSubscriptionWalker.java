// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance.parser;

import java.util.Collections;
import org.zik.bpm.engine.impl.bpmn.helper.CompensationUtil;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;
import java.util.Collection;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.zik.bpm.engine.impl.tree.ReferenceWalker;

public class CompensationEventSubscriptionWalker extends ReferenceWalker<EventSubscriptionEntity>
{
    public CompensationEventSubscriptionWalker(final Collection<MigratingActivityInstance> collection) {
        super(collectCompensationEventSubscriptions(collection));
    }
    
    protected static List<EventSubscriptionEntity> collectCompensationEventSubscriptions(final Collection<MigratingActivityInstance> activityInstances) {
        final List<EventSubscriptionEntity> eventSubscriptions = new ArrayList<EventSubscriptionEntity>();
        for (final MigratingActivityInstance activityInstance : activityInstances) {
            if (activityInstance.getSourceScope().isScope()) {
                final ExecutionEntity scopeExecution = activityInstance.resolveRepresentativeExecution();
                eventSubscriptions.addAll(scopeExecution.getCompensateEventSubscriptions());
            }
        }
        return eventSubscriptions;
    }
    
    @Override
    protected Collection<EventSubscriptionEntity> nextElements() {
        final EventSubscriptionEntity eventSubscriptionEntity = this.getCurrentElement();
        final ExecutionEntity compensatingExecution = CompensationUtil.getCompensatingExecution(eventSubscriptionEntity);
        if (compensatingExecution != null) {
            return compensatingExecution.getCompensateEventSubscriptions();
        }
        return (Collection<EventSubscriptionEntity>)Collections.emptyList();
    }
}
