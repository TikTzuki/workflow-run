// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn;

import org.zik.bpm.engine.runtime.CaseInstance;
import java.util.Map;
import org.zik.bpm.engine.impl.cmmn.cmd.GetCaseExecutionVariableTypedCmd;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.impl.cmmn.cmd.GetCaseExecutionVariableCmd;
import org.zik.bpm.engine.exception.cmmn.CaseExecutionNotFoundException;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.exception.NullValueException;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmmn.cmd.GetCaseExecutionVariablesCmd;
import java.util.Collection;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.runtime.CaseExecutionCommandBuilder;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionQueryImpl;
import org.zik.bpm.engine.runtime.CaseExecutionQuery;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseInstanceQueryImpl;
import org.zik.bpm.engine.runtime.CaseInstanceQuery;
import org.zik.bpm.engine.runtime.CaseInstanceBuilder;
import org.zik.bpm.engine.CaseService;
import org.zik.bpm.engine.impl.ServiceImpl;

public class CaseServiceImpl extends ServiceImpl implements CaseService
{
    @Override
    public CaseInstanceBuilder withCaseDefinitionByKey(final String caseDefinitionKey) {
        return new CaseInstanceBuilderImpl(this.commandExecutor, caseDefinitionKey, null);
    }
    
    @Override
    public CaseInstanceBuilder withCaseDefinition(final String caseDefinitionId) {
        return new CaseInstanceBuilderImpl(this.commandExecutor, null, caseDefinitionId);
    }
    
    @Override
    public CaseInstanceQuery createCaseInstanceQuery() {
        return new CaseInstanceQueryImpl(this.commandExecutor);
    }
    
    @Override
    public CaseExecutionQuery createCaseExecutionQuery() {
        return new CaseExecutionQueryImpl(this.commandExecutor);
    }
    
    @Override
    public CaseExecutionCommandBuilder withCaseExecution(final String caseExecutionId) {
        return new CaseExecutionCommandBuilderImpl(this.commandExecutor, caseExecutionId);
    }
    
    public VariableMap getVariables(final String caseExecutionId) {
        return this.getVariablesTyped(caseExecutionId);
    }
    
    @Override
    public VariableMap getVariablesTyped(final String caseExecutionId) {
        return this.getVariablesTyped(caseExecutionId, true);
    }
    
    @Override
    public VariableMap getVariablesTyped(final String caseExecutionId, final boolean deserializeValues) {
        return this.getCaseExecutionVariables(caseExecutionId, null, false, deserializeValues);
    }
    
    public VariableMap getVariablesLocal(final String caseExecutionId) {
        return this.getVariablesLocalTyped(caseExecutionId);
    }
    
    @Override
    public VariableMap getVariablesLocalTyped(final String caseExecutionId) {
        return this.getVariablesLocalTyped(caseExecutionId, true);
    }
    
    @Override
    public VariableMap getVariablesLocalTyped(final String caseExecutionId, final boolean deserializeValues) {
        return this.getCaseExecutionVariables(caseExecutionId, null, true, deserializeValues);
    }
    
    public VariableMap getVariables(final String caseExecutionId, final Collection<String> variableNames) {
        return this.getVariablesTyped(caseExecutionId, variableNames, true);
    }
    
    @Override
    public VariableMap getVariablesTyped(final String caseExecutionId, final Collection<String> variableNames, final boolean deserializeValues) {
        return this.getCaseExecutionVariables(caseExecutionId, variableNames, false, deserializeValues);
    }
    
    public VariableMap getVariablesLocal(final String caseExecutionId, final Collection<String> variableNames) {
        return this.getVariablesLocalTyped(caseExecutionId, variableNames, true);
    }
    
    @Override
    public VariableMap getVariablesLocalTyped(final String caseExecutionId, final Collection<String> variableNames, final boolean deserializeValues) {
        return this.getCaseExecutionVariables(caseExecutionId, variableNames, true, deserializeValues);
    }
    
    protected VariableMap getCaseExecutionVariables(final String caseExecutionId, final Collection<String> variableNames, final boolean isLocal, final boolean deserializeValues) {
        try {
            return this.commandExecutor.execute((Command<VariableMap>)new GetCaseExecutionVariablesCmd(caseExecutionId, variableNames, isLocal, deserializeValues));
        }
        catch (NullValueException e) {
            throw new NotValidException(e.getMessage(), e);
        }
        catch (CaseExecutionNotFoundException e2) {
            throw new NotFoundException(e2.getMessage(), e2);
        }
    }
    
    @Override
    public Object getVariable(final String caseExecutionId, final String variableName) {
        return this.getCaseExecutionVariable(caseExecutionId, variableName, false);
    }
    
    @Override
    public Object getVariableLocal(final String caseExecutionId, final String variableName) {
        return this.getCaseExecutionVariable(caseExecutionId, variableName, true);
    }
    
    protected Object getCaseExecutionVariable(final String caseExecutionId, final String variableName, final boolean isLocal) {
        try {
            return this.commandExecutor.execute((Command<Object>)new GetCaseExecutionVariableCmd(caseExecutionId, variableName, isLocal));
        }
        catch (NullValueException e) {
            throw new NotValidException(e.getMessage(), e);
        }
        catch (CaseExecutionNotFoundException e2) {
            throw new NotFoundException(e2.getMessage(), e2);
        }
    }
    
    @Override
    public <T extends TypedValue> T getVariableTyped(final String caseExecutionId, final String variableName) {
        return this.getVariableTyped(caseExecutionId, variableName, true);
    }
    
    @Override
    public <T extends TypedValue> T getVariableTyped(final String caseExecutionId, final String variableName, final boolean deserializeValue) {
        return this.getCaseExecutionVariableTyped(caseExecutionId, variableName, false, deserializeValue);
    }
    
    @Override
    public <T extends TypedValue> T getVariableLocalTyped(final String caseExecutionId, final String variableName) {
        return this.getVariableLocalTyped(caseExecutionId, variableName, true);
    }
    
    @Override
    public <T extends TypedValue> T getVariableLocalTyped(final String caseExecutionId, final String variableName, final boolean deserializeValue) {
        return this.getCaseExecutionVariableTyped(caseExecutionId, variableName, true, deserializeValue);
    }
    
    protected <T extends TypedValue> T getCaseExecutionVariableTyped(final String caseExecutionId, final String variableName, final boolean isLocal, final boolean deserializeValue) {
        try {
            return this.commandExecutor.execute((Command<T>)new GetCaseExecutionVariableTypedCmd(caseExecutionId, variableName, isLocal, deserializeValue));
        }
        catch (NullValueException e) {
            throw new NotValidException(e.getMessage(), e);
        }
        catch (CaseExecutionNotFoundException e2) {
            throw new NotFoundException(e2.getMessage(), e2);
        }
    }
    
    @Override
    public void setVariables(final String caseExecutionId, final Map<String, Object> variables) {
        this.withCaseExecution(caseExecutionId).setVariables(variables).execute();
    }
    
    @Override
    public void setVariablesLocal(final String caseExecutionId, final Map<String, Object> variables) {
        this.withCaseExecution(caseExecutionId).setVariablesLocal(variables).execute();
    }
    
    @Override
    public void setVariable(final String caseExecutionId, final String variableName, final Object value) {
        this.withCaseExecution(caseExecutionId).setVariable(variableName, value).execute();
    }
    
    @Override
    public void setVariableLocal(final String caseExecutionId, final String variableName, final Object value) {
        this.withCaseExecution(caseExecutionId).setVariableLocal(variableName, value).execute();
    }
    
    @Override
    public void removeVariables(final String caseExecutionId, final Collection<String> variableNames) {
        this.withCaseExecution(caseExecutionId).removeVariables(variableNames).execute();
    }
    
    @Override
    public void removeVariablesLocal(final String caseExecutionId, final Collection<String> variableNames) {
        this.withCaseExecution(caseExecutionId).removeVariablesLocal(variableNames).execute();
    }
    
    @Override
    public void removeVariable(final String caseExecutionId, final String variableName) {
        this.withCaseExecution(caseExecutionId).removeVariable(variableName).execute();
    }
    
    @Override
    public void removeVariableLocal(final String caseExecutionId, final String variableName) {
        this.withCaseExecution(caseExecutionId).removeVariableLocal(variableName).execute();
    }
    
    @Override
    public CaseInstance createCaseInstanceByKey(final String caseDefinitionKey) {
        return this.withCaseDefinitionByKey(caseDefinitionKey).create();
    }
    
    @Override
    public CaseInstance createCaseInstanceByKey(final String caseDefinitionKey, final String businessKey) {
        return this.withCaseDefinitionByKey(caseDefinitionKey).businessKey(businessKey).create();
    }
    
    @Override
    public CaseInstance createCaseInstanceByKey(final String caseDefinitionKey, final Map<String, Object> variables) {
        return this.withCaseDefinitionByKey(caseDefinitionKey).setVariables(variables).create();
    }
    
    @Override
    public CaseInstance createCaseInstanceByKey(final String caseDefinitionKey, final String businessKey, final Map<String, Object> variables) {
        return this.withCaseDefinitionByKey(caseDefinitionKey).businessKey(businessKey).setVariables(variables).create();
    }
    
    @Override
    public CaseInstance createCaseInstanceById(final String caseDefinitionId) {
        return this.withCaseDefinition(caseDefinitionId).create();
    }
    
    @Override
    public CaseInstance createCaseInstanceById(final String caseDefinitionId, final String businessKey) {
        return this.withCaseDefinition(caseDefinitionId).businessKey(businessKey).create();
    }
    
    @Override
    public CaseInstance createCaseInstanceById(final String caseDefinitionId, final Map<String, Object> variables) {
        return this.withCaseDefinition(caseDefinitionId).setVariables(variables).create();
    }
    
    @Override
    public CaseInstance createCaseInstanceById(final String caseDefinitionId, final String businessKey, final Map<String, Object> variables) {
        return this.withCaseDefinition(caseDefinitionId).businessKey(businessKey).setVariables(variables).create();
    }
    
    @Override
    public void manuallyStartCaseExecution(final String caseExecutionId) {
        this.withCaseExecution(caseExecutionId).manualStart();
    }
    
    @Override
    public void manuallyStartCaseExecution(final String caseExecutionId, final Map<String, Object> variables) {
        this.withCaseExecution(caseExecutionId).setVariables(variables).manualStart();
    }
    
    @Override
    public void disableCaseExecution(final String caseExecutionId) {
        this.withCaseExecution(caseExecutionId).disable();
    }
    
    @Override
    public void disableCaseExecution(final String caseExecutionId, final Map<String, Object> variables) {
        this.withCaseExecution(caseExecutionId).setVariables(variables).disable();
    }
    
    @Override
    public void reenableCaseExecution(final String caseExecutionId) {
        this.withCaseExecution(caseExecutionId).reenable();
    }
    
    @Override
    public void reenableCaseExecution(final String caseExecutionId, final Map<String, Object> variables) {
        this.withCaseExecution(caseExecutionId).setVariables(variables).reenable();
    }
    
    @Override
    public void completeCaseExecution(final String caseExecutionId) {
        this.withCaseExecution(caseExecutionId).complete();
    }
    
    @Override
    public void completeCaseExecution(final String caseExecutionId, final Map<String, Object> variables) {
        this.withCaseExecution(caseExecutionId).setVariables(variables).complete();
    }
    
    @Override
    public void closeCaseInstance(final String caseInstanceId) {
        this.withCaseExecution(caseInstanceId).close();
    }
    
    @Override
    public void terminateCaseExecution(final String caseExecutionId) {
        this.withCaseExecution(caseExecutionId).terminate();
    }
    
    @Override
    public void terminateCaseExecution(final String caseExecutionId, final Map<String, Object> variables) {
        this.withCaseExecution(caseExecutionId).setVariables(variables).terminate();
    }
}
