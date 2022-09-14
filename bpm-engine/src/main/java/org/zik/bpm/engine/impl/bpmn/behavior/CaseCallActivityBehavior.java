// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.migration.instance.MigratingInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingCalledCaseInstance;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;
import org.zik.bpm.engine.impl.migration.instance.parser.MigratingInstanceParseContext;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnCaseInstance;
import org.zik.bpm.engine.impl.cmmn.model.CmmnCaseDefinition;
import java.util.Map;
import org.zik.bpm.engine.impl.core.model.BaseCallableElement;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.util.CallableElementUtil;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.pvm.delegate.MigrationObserverBehavior;

public class CaseCallActivityBehavior extends CallableElementActivityBehavior implements MigrationObserverBehavior
{
    @Override
    protected void startInstance(final ActivityExecution execution, final VariableMap variables, final String businessKey) {
        final ExecutionEntity executionEntity = (ExecutionEntity)execution;
        final CmmnCaseDefinition definition = CallableElementUtil.getCaseDefinitionToCall(executionEntity, executionEntity.getProcessDefinitionTenantId(), this.getCallableElement());
        final CmmnCaseInstance caseInstance = execution.createSubCaseInstance(definition, businessKey);
        caseInstance.create((Map<String, Object>)variables);
    }
    
    @Override
    public void migrateScope(final ActivityExecution scopeExecution) {
    }
    
    @Override
    public void onParseMigratingInstance(final MigratingInstanceParseContext parseContext, final MigratingActivityInstance migratingInstance) {
        final ActivityImpl callActivity = (ActivityImpl)migratingInstance.getSourceScope();
        if (!callActivity.isScope()) {
            final ExecutionEntity callActivityExecution = migratingInstance.resolveRepresentativeExecution();
            final CaseExecutionEntity calledCaseInstance = callActivityExecution.getSubCaseInstance();
            migratingInstance.addMigratingDependentInstance(new MigratingCalledCaseInstance(calledCaseInstance));
        }
    }
}
