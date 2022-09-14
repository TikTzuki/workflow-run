// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.event.EventType;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import java.util.List;

public class MigrationCompensationInstanceVisitor extends MigratingProcessElementInstanceVisitor
{
    @Override
    protected boolean canMigrate(final MigratingProcessElementInstance instance) {
        return instance instanceof MigratingEventScopeInstance || instance instanceof MigratingCompensationEventSubscriptionInstance;
    }
    
    @Override
    protected void instantiateScopes(final MigratingScopeInstance ancestorScopeInstance, final MigratingScopeInstanceBranch executionBranch, final List<ScopeImpl> scopesToInstantiate) {
        if (scopesToInstantiate.isEmpty()) {
            return;
        }
        ExecutionEntity parentExecution;
        final ExecutionEntity ancestorScopeExecution = parentExecution = ancestorScopeInstance.resolveRepresentativeExecution();
        for (final ScopeImpl scope : scopesToInstantiate) {
            final ExecutionEntity compensationScopeExecution = parentExecution.createExecution();
            compensationScopeExecution.setScope(true);
            compensationScopeExecution.setEventScope(true);
            compensationScopeExecution.setActivity((PvmActivity)scope);
            compensationScopeExecution.setActive(false);
            compensationScopeExecution.activityInstanceStarting();
            compensationScopeExecution.enterActivityInstance();
            final EventSubscriptionEntity eventSubscription = EventSubscriptionEntity.createAndInsert(parentExecution, EventType.COMPENSATE, (ActivityImpl)scope);
            eventSubscription.setConfiguration(compensationScopeExecution.getId());
            executionBranch.visited(new MigratingEventScopeInstance(eventSubscription, compensationScopeExecution, scope));
            parentExecution = compensationScopeExecution;
        }
    }
}
