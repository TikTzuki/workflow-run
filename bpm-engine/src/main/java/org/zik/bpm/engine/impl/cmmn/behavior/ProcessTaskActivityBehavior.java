// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.pvm.PvmProcessInstance;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.zik.bpm.engine.impl.pvm.PvmProcessDefinition;
import org.zik.bpm.engine.impl.core.model.BaseCallableElement;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.util.CallableElementUtil;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import java.util.Map;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;

public class ProcessTaskActivityBehavior extends ProcessOrCaseTaskActivityBehavior
{
    protected static final CmmnBehaviorLogger LOG;
    
    @Override
    protected void triggerCallableElement(final CmmnActivityExecution execution, final Map<String, Object> variables, final String businessKey) {
        final CaseExecutionEntity executionEntity = (CaseExecutionEntity)execution;
        final ProcessDefinitionImpl definition = CallableElementUtil.getProcessDefinitionToCall(execution, executionEntity.getCaseDefinitionTenantId(), this.getCallableElement());
        final PvmProcessInstance processInstance = execution.createSubProcessInstance(definition, businessKey);
        processInstance.start(variables);
    }
    
    @Override
    protected String getTypeName() {
        return "process task";
    }
    
    static {
        LOG = ProcessEngineLogger.CMNN_BEHAVIOR_LOGGER;
    }
}
