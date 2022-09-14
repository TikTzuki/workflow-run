// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instance;

import org.zik.bpm.engine.impl.migration.instance.MigratingProcessElementInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingEventScopeInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingCompensationEventSubscriptionInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingTransitionInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingProcessInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;

public class NoUnmappedLeafInstanceValidator implements MigratingActivityInstanceValidator, MigratingTransitionInstanceValidator, MigratingCompensationInstanceValidator
{
    @Override
    public void validate(final MigratingActivityInstance migratingInstance, final MigratingProcessInstance migratingProcessInstance, final MigratingActivityInstanceValidationReportImpl instanceReport) {
        if (this.isInvalid(migratingInstance)) {
            instanceReport.addFailure("There is no migration instruction for this instance's activity");
        }
    }
    
    @Override
    public void validate(final MigratingTransitionInstance migratingInstance, final MigratingProcessInstance migratingProcessInstance, final MigratingTransitionInstanceValidationReportImpl instanceReport) {
        if (this.isInvalid(migratingInstance)) {
            instanceReport.addFailure("There is no migration instruction for this instance's activity");
        }
    }
    
    @Override
    public void validate(final MigratingCompensationEventSubscriptionInstance migratingInstance, final MigratingProcessInstance migratingProcessInstance, final MigratingActivityInstanceValidationReportImpl ancestorInstanceReport) {
        if (this.isInvalid(migratingInstance)) {
            ancestorInstanceReport.addFailure("Cannot migrate subscription for compensation handler '" + migratingInstance.getSourceScope().getId() + "'. There is no migration instruction for the compensation boundary event");
        }
    }
    
    @Override
    public void validate(final MigratingEventScopeInstance migratingInstance, final MigratingProcessInstance migratingProcessInstance, final MigratingActivityInstanceValidationReportImpl ancestorInstanceReport) {
        if (this.isInvalid(migratingInstance)) {
            ancestorInstanceReport.addFailure("Cannot migrate subscription for compensation handler '" + migratingInstance.getEventSubscription().getSourceScope().getId() + "'. There is no migration instruction for the compensation start event");
        }
    }
    
    protected boolean isInvalid(final MigratingActivityInstance migratingInstance) {
        return this.hasNoInstruction(migratingInstance) && migratingInstance.getChildren().isEmpty();
    }
    
    protected boolean isInvalid(final MigratingEventScopeInstance migratingInstance) {
        return this.hasNoInstruction(migratingInstance.getEventSubscription()) && migratingInstance.getChildren().isEmpty();
    }
    
    protected boolean isInvalid(final MigratingTransitionInstance migratingInstance) {
        return this.hasNoInstruction(migratingInstance);
    }
    
    protected boolean isInvalid(final MigratingCompensationEventSubscriptionInstance migratingInstance) {
        return this.hasNoInstruction(migratingInstance);
    }
    
    protected boolean hasNoInstruction(final MigratingProcessElementInstance migratingInstance) {
        return migratingInstance.getMigrationInstruction() == null;
    }
}
