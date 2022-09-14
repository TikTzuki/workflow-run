// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting;

import org.zik.bpm.engine.delegate.DelegateCaseExecution;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.delegate.DelegateExecution;
import javax.script.Bindings;
import org.zik.bpm.engine.delegate.VariableScope;
import javax.script.ScriptEngine;

public abstract class ExecutableScript
{
    protected final String language;
    
    protected ExecutableScript(final String language) {
        this.language = language;
    }
    
    public String getLanguage() {
        return this.language;
    }
    
    public Object execute(final ScriptEngine scriptEngine, final VariableScope variableScope, final Bindings bindings) {
        return this.evaluate(scriptEngine, variableScope, bindings);
    }
    
    protected String getActivityIdExceptionMessage(final VariableScope variableScope) {
        String activityId = null;
        String definitionIdMessage = "";
        if (variableScope instanceof DelegateExecution) {
            activityId = ((DelegateExecution)variableScope).getCurrentActivityId();
            definitionIdMessage = " in the process definition with id '" + ((DelegateExecution)variableScope).getProcessDefinitionId() + "'";
        }
        else if (variableScope instanceof TaskEntity) {
            final TaskEntity task = (TaskEntity)variableScope;
            if (task.getExecution() != null) {
                activityId = task.getExecution().getActivityId();
                definitionIdMessage = " in the process definition with id '" + task.getProcessDefinitionId() + "'";
            }
            if (task.getCaseExecution() != null) {
                activityId = task.getCaseExecution().getActivityId();
                definitionIdMessage = " in the case definition with id '" + task.getCaseDefinitionId() + "'";
            }
        }
        else if (variableScope instanceof DelegateCaseExecution) {
            activityId = ((DelegateCaseExecution)variableScope).getActivityId();
            definitionIdMessage = " in the case definition with id '" + ((DelegateCaseExecution)variableScope).getCaseDefinitionId() + "'";
        }
        if (activityId == null) {
            return "";
        }
        return " while executing activity '" + activityId + "'" + definitionIdMessage;
    }
    
    protected abstract Object evaluate(final ScriptEngine p0, final VariableScope p1, final Bindings p2);
}
