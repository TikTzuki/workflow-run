// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import org.camunda.bpm.engine.variable.value.TypedValue;
import java.util.Collection;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.runtime.CaseExecutionQuery;
import org.zik.bpm.engine.runtime.CaseInstanceQuery;
import org.zik.bpm.engine.runtime.CaseExecutionCommandBuilder;
import org.zik.bpm.engine.runtime.CaseInstanceBuilder;
import java.util.Map;
import org.zik.bpm.engine.runtime.CaseInstance;

public interface CaseService
{
    CaseInstance createCaseInstanceByKey(final String p0);
    
    CaseInstance createCaseInstanceByKey(final String p0, final String p1);
    
    CaseInstance createCaseInstanceByKey(final String p0, final Map<String, Object> p1);
    
    CaseInstance createCaseInstanceByKey(final String p0, final String p1, final Map<String, Object> p2);
    
    CaseInstance createCaseInstanceById(final String p0);
    
    CaseInstance createCaseInstanceById(final String p0, final String p1);
    
    CaseInstance createCaseInstanceById(final String p0, final Map<String, Object> p1);
    
    CaseInstance createCaseInstanceById(final String p0, final String p1, final Map<String, Object> p2);
    
    void manuallyStartCaseExecution(final String p0);
    
    void manuallyStartCaseExecution(final String p0, final Map<String, Object> p1);
    
    void disableCaseExecution(final String p0);
    
    void disableCaseExecution(final String p0, final Map<String, Object> p1);
    
    void reenableCaseExecution(final String p0);
    
    void reenableCaseExecution(final String p0, final Map<String, Object> p1);
    
    void completeCaseExecution(final String p0);
    
    void completeCaseExecution(final String p0, final Map<String, Object> p1);
    
    void closeCaseInstance(final String p0);
    
    void terminateCaseExecution(final String p0);
    
    void terminateCaseExecution(final String p0, final Map<String, Object> p1);
    
    CaseInstanceBuilder withCaseDefinitionByKey(final String p0);
    
    CaseInstanceBuilder withCaseDefinition(final String p0);
    
    CaseExecutionCommandBuilder withCaseExecution(final String p0);
    
    CaseInstanceQuery createCaseInstanceQuery();
    
    CaseExecutionQuery createCaseExecutionQuery();
    
    Map<String, Object> getVariables(final String p0);
    
    VariableMap getVariablesTyped(final String p0);
    
    VariableMap getVariablesTyped(final String p0, final boolean p1);
    
    Map<String, Object> getVariablesLocal(final String p0);
    
    VariableMap getVariablesLocalTyped(final String p0);
    
    VariableMap getVariablesLocalTyped(final String p0, final boolean p1);
    
    Map<String, Object> getVariables(final String p0, final Collection<String> p1);
    
    VariableMap getVariablesTyped(final String p0, final Collection<String> p1, final boolean p2);
    
    Map<String, Object> getVariablesLocal(final String p0, final Collection<String> p1);
    
    VariableMap getVariablesLocalTyped(final String p0, final Collection<String> p1, final boolean p2);
    
    Object getVariable(final String p0, final String p1);
    
     <T extends TypedValue> T getVariableTyped(final String p0, final String p1);
    
     <T extends TypedValue> T getVariableTyped(final String p0, final String p1, final boolean p2);
    
    Object getVariableLocal(final String p0, final String p1);
    
     <T extends TypedValue> T getVariableLocalTyped(final String p0, final String p1);
    
     <T extends TypedValue> T getVariableLocalTyped(final String p0, final String p1, final boolean p2);
    
    void setVariables(final String p0, final Map<String, Object> p1);
    
    void setVariablesLocal(final String p0, final Map<String, Object> p1);
    
    void setVariable(final String p0, final String p1, final Object p2);
    
    void setVariableLocal(final String p0, final String p1, final Object p2);
    
    void removeVariables(final String p0, final Collection<String> p1);
    
    void removeVariablesLocal(final String p0, final Collection<String> p1);
    
    void removeVariable(final String p0, final String p1);
    
    void removeVariableLocal(final String p0, final String p1);
}
