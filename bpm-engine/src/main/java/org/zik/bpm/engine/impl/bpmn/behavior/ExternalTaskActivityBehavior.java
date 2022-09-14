// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import java.util.Iterator;
import org.zik.bpm.engine.impl.migration.instance.MigratingInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingExternalTaskInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;
import org.zik.bpm.engine.impl.migration.instance.parser.MigratingInstanceParseContext;
import org.zik.bpm.engine.impl.PriorityProvider;
import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ParameterValueProvider;
import org.zik.bpm.engine.impl.pvm.delegate.MigrationObserverBehavior;

public class ExternalTaskActivityBehavior extends AbstractBpmnActivityBehavior implements MigrationObserverBehavior
{
    protected ParameterValueProvider topicNameValueProvider;
    protected ParameterValueProvider priorityValueProvider;
    
    public ExternalTaskActivityBehavior(final ParameterValueProvider topicName, final ParameterValueProvider paramValueProvider) {
        this.topicNameValueProvider = topicName;
        this.priorityValueProvider = paramValueProvider;
    }
    
    @Override
    public void execute(final ActivityExecution execution) throws Exception {
        final ExecutionEntity executionEntity = (ExecutionEntity)execution;
        final PriorityProvider<ExternalTaskActivityBehavior> provider = Context.getProcessEngineConfiguration().getExternalTaskPriorityProvider();
        final long priority = provider.determinePriority(executionEntity, this, null);
        final String topic = (String)this.topicNameValueProvider.getValue(executionEntity);
        ExternalTaskEntity.createAndInsert(executionEntity, topic, priority);
    }
    
    @Override
    public void signal(final ActivityExecution execution, final String signalName, final Object signalData) throws Exception {
        this.leave(execution);
    }
    
    public ParameterValueProvider getPriorityValueProvider() {
        return this.priorityValueProvider;
    }
    
    @Override
    public void migrateScope(final ActivityExecution scopeExecution) {
    }
    
    @Override
    public void onParseMigratingInstance(final MigratingInstanceParseContext parseContext, final MigratingActivityInstance migratingInstance) {
        final ExecutionEntity execution = migratingInstance.resolveRepresentativeExecution();
        for (final ExternalTaskEntity task : execution.getExternalTasks()) {
            final MigratingExternalTaskInstance migratingTask = new MigratingExternalTaskInstance(task, migratingInstance);
            migratingInstance.addMigratingDependentInstance(migratingTask);
            parseContext.consume(task);
            parseContext.submit(migratingTask);
        }
    }
}
