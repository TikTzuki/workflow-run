// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration;

import org.zik.bpm.engine.impl.migration.validation.instruction.ValidatingMigrationInstructionImpl;
import org.zik.bpm.engine.impl.migration.validation.instruction.UpdateEventTriggersValidator;
import org.zik.bpm.engine.impl.bpmn.behavior.ConditionalEventBehavior;
import org.zik.bpm.engine.impl.migration.validation.instruction.ValidatingMigrationInstruction;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import java.util.Collection;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.migration.validation.instruction.ValidatingMigrationInstructions;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import java.util.Iterator;
import org.zik.bpm.engine.impl.migration.validation.instruction.CannotRemoveMultiInstanceInnerActivityValidator;
import org.zik.bpm.engine.impl.migration.validation.instruction.CannotAddMultiInstanceInnerActivityValidator;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.migration.validation.instruction.MigrationInstructionValidator;
import org.zik.bpm.engine.impl.migration.validation.activity.MigrationActivityValidator;
import java.util.List;

public class DefaultMigrationInstructionGenerator implements MigrationInstructionGenerator
{
    protected List<MigrationActivityValidator> migrationActivityValidators;
    protected List<MigrationInstructionValidator> migrationInstructionValidators;
    protected MigrationActivityMatcher migrationActivityMatcher;
    
    public DefaultMigrationInstructionGenerator(final MigrationActivityMatcher migrationActivityMatcher) {
        this.migrationActivityValidators = new ArrayList<MigrationActivityValidator>();
        this.migrationInstructionValidators = new ArrayList<MigrationInstructionValidator>();
        this.migrationActivityMatcher = migrationActivityMatcher;
    }
    
    @Override
    public MigrationInstructionGenerator migrationActivityValidators(final List<MigrationActivityValidator> migrationActivityValidators) {
        this.migrationActivityValidators = migrationActivityValidators;
        return this;
    }
    
    @Override
    public MigrationInstructionGenerator migrationInstructionValidators(final List<MigrationInstructionValidator> migrationInstructionValidators) {
        this.migrationInstructionValidators = new ArrayList<MigrationInstructionValidator>();
        for (final MigrationInstructionValidator validator : migrationInstructionValidators) {
            if (!(validator instanceof CannotAddMultiInstanceInnerActivityValidator) && !(validator instanceof CannotRemoveMultiInstanceInnerActivityValidator)) {
                this.migrationInstructionValidators.add(validator);
            }
        }
        return this;
    }
    
    @Override
    public ValidatingMigrationInstructions generate(final ProcessDefinitionImpl sourceProcessDefinition, final ProcessDefinitionImpl targetProcessDefinition, final boolean updateEventTriggers) {
        final ValidatingMigrationInstructions migrationInstructions = new ValidatingMigrationInstructions();
        this.generate(sourceProcessDefinition, targetProcessDefinition, sourceProcessDefinition, targetProcessDefinition, migrationInstructions, updateEventTriggers);
        return migrationInstructions;
    }
    
    protected List<ValidatingMigrationInstruction> generateInstructionsForActivities(final Collection<ActivityImpl> sourceActivities, final Collection<ActivityImpl> targetActivities, final boolean updateEventTriggers, final ValidatingMigrationInstructions existingInstructions) {
        final List<ValidatingMigrationInstruction> generatedInstructions = new ArrayList<ValidatingMigrationInstruction>();
        for (final ActivityImpl sourceActivity : sourceActivities) {
            if (!existingInstructions.containsInstructionForSourceScope(sourceActivity)) {
                for (final ActivityImpl targetActivity : targetActivities) {
                    if (this.isValidActivity(sourceActivity) && this.isValidActivity(targetActivity) && this.migrationActivityMatcher.matchActivities(sourceActivity, targetActivity)) {
                        final boolean updateEventTriggersForInstruction = sourceActivity.getActivityBehavior() instanceof ConditionalEventBehavior || (updateEventTriggers && UpdateEventTriggersValidator.definesPersistentEventTrigger(sourceActivity));
                        final ValidatingMigrationInstruction generatedInstruction = new ValidatingMigrationInstructionImpl(sourceActivity, targetActivity, updateEventTriggersForInstruction);
                        generatedInstructions.add(generatedInstruction);
                    }
                }
            }
        }
        return generatedInstructions;
    }
    
    public void generate(final ScopeImpl sourceScope, final ScopeImpl targetScope, final ProcessDefinitionImpl sourceProcessDefinition, final ProcessDefinitionImpl targetProcessDefinition, final ValidatingMigrationInstructions existingInstructions, final boolean updateEventTriggers) {
        final List<ValidatingMigrationInstruction> flowScopeInstructions = this.generateInstructionsForActivities(sourceScope.getActivities(), targetScope.getActivities(), updateEventTriggers, existingInstructions);
        existingInstructions.addAll(flowScopeInstructions);
        final List<ValidatingMigrationInstruction> eventScopeInstructions = this.generateInstructionsForActivities(sourceScope.getEventActivities(), targetScope.getEventActivities(), updateEventTriggers, existingInstructions);
        existingInstructions.addAll(eventScopeInstructions);
        existingInstructions.filterWith(this.migrationInstructionValidators);
        for (final ValidatingMigrationInstruction generatedInstruction : flowScopeInstructions) {
            if (existingInstructions.contains(generatedInstruction)) {
                this.generate(generatedInstruction.getSourceActivity(), generatedInstruction.getTargetActivity(), sourceProcessDefinition, targetProcessDefinition, existingInstructions, updateEventTriggers);
            }
        }
    }
    
    protected boolean isValidActivity(final ActivityImpl activity) {
        for (final MigrationActivityValidator migrationActivityValidator : this.migrationActivityValidators) {
            if (!migrationActivityValidator.valid(activity)) {
                return false;
            }
        }
        return true;
    }
}
