// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.runtime;

import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;

public class ConditionHandlerResult
{
    private ProcessDefinitionEntity processDefinition;
    private ActivityImpl activity;
    
    public ConditionHandlerResult(final ProcessDefinitionEntity processDefinition, final ActivityImpl activity) {
        this.setProcessDefinition(processDefinition);
        this.setActivity(activity);
    }
    
    public ProcessDefinitionEntity getProcessDefinition() {
        return this.processDefinition;
    }
    
    public void setProcessDefinition(final ProcessDefinitionEntity processDefinition) {
        this.processDefinition = processDefinition;
    }
    
    public ActivityImpl getActivity() {
        return this.activity;
    }
    
    public void setActivity(final ActivityImpl activity) {
        this.activity = activity;
    }
    
    public static ConditionHandlerResult matchedProcessDefinition(final ProcessDefinitionEntity processDefinition, final ActivityImpl startActivityId) {
        final ConditionHandlerResult conditionHandlerResult = new ConditionHandlerResult(processDefinition, startActivityId);
        return conditionHandlerResult;
    }
}
