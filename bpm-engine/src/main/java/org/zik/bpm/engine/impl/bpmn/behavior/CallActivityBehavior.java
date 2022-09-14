// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.migration.instance.MigratingInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingCalledProcessInstance;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;
import org.zik.bpm.engine.impl.migration.instance.parser.MigratingInstanceParseContext;
import org.zik.bpm.engine.impl.pvm.PvmProcessInstance;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import java.util.Map;
import org.zik.bpm.engine.impl.pvm.PvmProcessDefinition;
import org.zik.bpm.engine.impl.core.model.BaseCallableElement;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.util.CallableElementUtil;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.impl.pvm.delegate.MigrationObserverBehavior;

public class CallActivityBehavior extends CallableElementActivityBehavior implements MigrationObserverBehavior
{
    public CallActivityBehavior() {
    }
    
    public CallActivityBehavior(final String className) {
        super(className);
    }
    
    public CallActivityBehavior(final Expression expression) {
        super(expression);
    }
    
    @Override
    protected void startInstance(final ActivityExecution execution, final VariableMap variables, final String businessKey) {
        final ExecutionEntity executionEntity = (ExecutionEntity)execution;
        final ProcessDefinitionImpl definition = CallableElementUtil.getProcessDefinitionToCall(executionEntity, executionEntity.getProcessDefinitionTenantId(), this.getCallableElement());
        final PvmProcessInstance processInstance = execution.createSubProcessInstance(definition, businessKey);
        processInstance.start((Map<String, Object>)variables);
    }
    
    @Override
    public void migrateScope(final ActivityExecution scopeExecution) {
    }
    
    @Override
    public void onParseMigratingInstance(final MigratingInstanceParseContext parseContext, final MigratingActivityInstance migratingInstance) {
        final ActivityImpl callActivity = (ActivityImpl)migratingInstance.getSourceScope();
        if (!callActivity.isScope()) {
            final ExecutionEntity callActivityExecution = migratingInstance.resolveRepresentativeExecution();
            final ExecutionEntity calledProcessInstance = callActivityExecution.getSubProcessInstance();
            migratingInstance.addMigratingDependentInstance(new MigratingCalledProcessInstance(calledProcessInstance));
        }
    }
}
