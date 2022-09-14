// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance.parser;

import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import org.zik.bpm.engine.impl.event.EventType;
import org.zik.bpm.engine.impl.migration.instance.EmergingInstance;
import java.util.HashMap;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.migration.MigrationInstruction;
import java.util.Iterator;
import java.util.Map;
import org.zik.bpm.engine.impl.migration.instance.RemovingInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingInstance;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.migration.instance.MigratingEventSubscriptionInstance;
import org.zik.bpm.engine.impl.bpmn.parser.EventSubscriptionDeclaration;
import java.util.Set;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import java.util.List;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;

public class EventSubscriptionInstanceHandler implements MigratingDependentInstanceParseHandler<MigratingActivityInstance, List<EventSubscriptionEntity>>
{
    public static final Set<String> SUPPORTED_EVENT_TYPES;
    
    @Override
    public void handle(final MigratingInstanceParseContext parseContext, final MigratingActivityInstance owningInstance, final List<EventSubscriptionEntity> elements) {
        final Map<String, EventSubscriptionDeclaration> targetDeclarations = this.getDeclarationsByTriggeringActivity(owningInstance.getTargetScope());
        for (final EventSubscriptionEntity eventSubscription : elements) {
            if (!this.getSupportedEventTypes().contains(eventSubscription.getEventType())) {
                continue;
            }
            final MigrationInstruction migrationInstruction = parseContext.findSingleMigrationInstruction(eventSubscription.getActivityId());
            final ActivityImpl targetActivity = parseContext.getTargetActivity(migrationInstruction);
            if (targetActivity != null && owningInstance.migratesTo(targetActivity.getEventScope())) {
                final EventSubscriptionDeclaration targetDeclaration = targetDeclarations.remove(targetActivity.getId());
                owningInstance.addMigratingDependentInstance(new MigratingEventSubscriptionInstance(eventSubscription, targetActivity, migrationInstruction.isUpdateEventTrigger(), targetDeclaration));
            }
            else {
                owningInstance.addRemovingDependentInstance(new MigratingEventSubscriptionInstance(eventSubscription));
            }
            parseContext.consume(eventSubscription);
        }
        if (owningInstance.migrates()) {
            this.addEmergingEventSubscriptions(owningInstance, targetDeclarations);
        }
    }
    
    protected Set<String> getSupportedEventTypes() {
        return EventSubscriptionInstanceHandler.SUPPORTED_EVENT_TYPES;
    }
    
    protected Map<String, EventSubscriptionDeclaration> getDeclarationsByTriggeringActivity(final ScopeImpl eventScope) {
        final Map<String, EventSubscriptionDeclaration> declarations = EventSubscriptionDeclaration.getDeclarationsForScope(eventScope);
        return new HashMap<String, EventSubscriptionDeclaration>(declarations);
    }
    
    protected void addEmergingEventSubscriptions(final MigratingActivityInstance owningInstance, final Map<String, EventSubscriptionDeclaration> targetDeclarations) {
        for (final String key : targetDeclarations.keySet()) {
            final EventSubscriptionDeclaration declaration = targetDeclarations.get(key);
            if (!declaration.isStartEvent()) {
                owningInstance.addEmergingDependentInstance(new MigratingEventSubscriptionInstance(declaration));
            }
        }
    }
    
    static {
        SUPPORTED_EVENT_TYPES = new HashSet<String>(Arrays.asList(EventType.MESSAGE.name(), EventType.SIGNAL.name(), EventType.CONDITONAL.name()));
    }
}
