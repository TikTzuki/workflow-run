// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnCaseInstance;
import org.zik.bpm.engine.impl.cmmn.model.CmmnCaseDefinition;
import org.zik.bpm.engine.impl.core.model.BaseCallableElement;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.util.CallableElementUtil;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import java.util.Map;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;

public class CaseTaskActivityBehavior extends ProcessOrCaseTaskActivityBehavior
{
    protected static final CmmnBehaviorLogger LOG;
    
    @Override
    protected void triggerCallableElement(final CmmnActivityExecution execution, final Map<String, Object> variables, final String businessKey) {
        final CaseExecutionEntity executionEntity = (CaseExecutionEntity)execution;
        final CmmnCaseDefinition definition = CallableElementUtil.getCaseDefinitionToCall(executionEntity, executionEntity.getCaseDefinitionTenantId(), this.getCallableElement());
        final CmmnCaseInstance caseInstance = execution.createSubCaseInstance(definition, businessKey);
        caseInstance.create(variables);
    }
    
    @Override
    protected String getTypeName() {
        return "case task";
    }
    
    static {
        LOG = ProcessEngineLogger.CMNN_BEHAVIOR_LOGGER;
    }
}
