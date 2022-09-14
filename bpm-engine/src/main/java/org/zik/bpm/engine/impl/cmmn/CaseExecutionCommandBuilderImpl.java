// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn;

import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.exception.cmmn.CaseIllegalStateTransitionException;
import org.zik.bpm.engine.exception.NotAllowedException;
import org.zik.bpm.engine.exception.cmmn.CaseDefinitionNotFoundException;
import org.zik.bpm.engine.exception.cmmn.CaseExecutionNotFoundException;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.exception.NullValueException;
import org.zik.bpm.engine.impl.cmmn.cmd.TerminateCaseExecutionCmd;
import org.zik.bpm.engine.impl.cmmn.cmd.CloseCaseInstanceCmd;
import org.zik.bpm.engine.impl.cmmn.cmd.CompleteCaseExecutionCmd;
import org.zik.bpm.engine.impl.cmmn.cmd.ReenableCaseExecutionCmd;
import org.zik.bpm.engine.impl.cmmn.cmd.DisableCaseExecutionCmd;
import org.zik.bpm.engine.impl.cmmn.cmd.ManualStartCaseExecutionCmd;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmmn.cmd.CaseExecutionVariableCmd;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.util.Collection;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.runtime.CaseExecutionCommandBuilder;

public class CaseExecutionCommandBuilderImpl implements CaseExecutionCommandBuilder
{
    protected CommandExecutor commandExecutor;
    protected CommandContext commandContext;
    protected String caseExecutionId;
    protected VariableMapImpl variables;
    protected VariableMapImpl variablesLocal;
    protected Collection<String> variableDeletions;
    protected Collection<String> variableLocalDeletions;
    
    public CaseExecutionCommandBuilderImpl(final CommandExecutor commandExecutor, final String caseExecutionId) {
        this(caseExecutionId);
        EnsureUtil.ensureNotNull("commandExecutor", commandExecutor);
        this.commandExecutor = commandExecutor;
    }
    
    public CaseExecutionCommandBuilderImpl(final CommandContext commandContext, final String caseExecutionId) {
        this(caseExecutionId);
        EnsureUtil.ensureNotNull("commandContext", commandContext);
        this.commandContext = commandContext;
    }
    
    private CaseExecutionCommandBuilderImpl(final String caseExecutionId) {
        this.caseExecutionId = caseExecutionId;
    }
    
    @Override
    public CaseExecutionCommandBuilder setVariable(final String variableName, final Object variableValue) {
        EnsureUtil.ensureNotNull(NotValidException.class, "variableName", (Object)variableName);
        this.ensureVariableShouldNotBeRemoved(variableName);
        this.ensureVariablesInitialized();
        this.variables.put(variableName, variableValue);
        return this;
    }
    
    @Override
    public CaseExecutionCommandBuilder setVariables(final Map<String, Object> variables) {
        if (variables != null) {
            this.ensureVariablesShouldNotBeRemoved(variables.keySet());
            this.ensureVariablesInitialized();
            this.variables.putAll((Map)variables);
        }
        return this;
    }
    
    @Override
    public CaseExecutionCommandBuilder setVariableLocal(final String localVariableName, final Object localVariableValue) {
        EnsureUtil.ensureNotNull(NotValidException.class, "localVariableName", (Object)localVariableName);
        this.ensureVariableShouldNotBeRemoved(localVariableName);
        this.ensureVariablesLocalInitialized();
        this.variablesLocal.put(localVariableName, localVariableValue);
        return this;
    }
    
    @Override
    public CaseExecutionCommandBuilder setVariablesLocal(final Map<String, Object> variablesLocal) {
        if (variablesLocal != null) {
            this.ensureVariablesShouldNotBeRemoved(variablesLocal.keySet());
            this.ensureVariablesLocalInitialized();
            this.variablesLocal.putAll((Map)variablesLocal);
        }
        return this;
    }
    
    @Override
    public CaseExecutionCommandBuilder removeVariable(final String variableName) {
        EnsureUtil.ensureNotNull(NotValidException.class, "variableName", (Object)variableName);
        this.ensureVariableShouldNotBeSet(variableName);
        this.ensureVariableDeletionsInitialized();
        this.variableDeletions.add(variableName);
        return this;
    }
    
    @Override
    public CaseExecutionCommandBuilder removeVariables(final Collection<String> variableNames) {
        if (variableNames != null) {
            this.ensureVariablesShouldNotBeSet(variableNames);
            this.ensureVariableDeletionsInitialized();
            this.variableDeletions.addAll(variableNames);
        }
        return this;
    }
    
    @Override
    public CaseExecutionCommandBuilder removeVariableLocal(final String variableName) {
        EnsureUtil.ensureNotNull(NotValidException.class, "localVariableName", (Object)variableName);
        this.ensureVariableShouldNotBeSet(variableName);
        this.ensureVariableDeletionsLocalInitialized();
        this.variableLocalDeletions.add(variableName);
        return this;
    }
    
    @Override
    public CaseExecutionCommandBuilder removeVariablesLocal(final Collection<String> variableNames) {
        if (variableNames != null) {
            this.ensureVariablesShouldNotBeSet(variableNames);
            this.ensureVariableDeletionsLocalInitialized();
            this.variableLocalDeletions.addAll(variableNames);
        }
        return this;
    }
    
    protected void ensureVariablesShouldNotBeRemoved(final Collection<String> variableNames) {
        for (final String variableName : variableNames) {
            this.ensureVariableShouldNotBeRemoved(variableName);
        }
    }
    
    protected void ensureVariableShouldNotBeRemoved(final String variableName) {
        if ((this.variableDeletions != null && this.variableDeletions.contains(variableName)) || (this.variableLocalDeletions != null && this.variableLocalDeletions.contains(variableName))) {
            throw new NotValidException("Cannot set and remove a variable with the same variable name: '" + variableName + "' within a command.");
        }
    }
    
    protected void ensureVariablesShouldNotBeSet(final Collection<String> variableNames) {
        for (final String variableName : variableNames) {
            this.ensureVariableShouldNotBeSet(variableName);
        }
    }
    
    protected void ensureVariableShouldNotBeSet(final String variableName) {
        if ((this.variables != null && this.variables.keySet().contains(variableName)) || (this.variablesLocal != null && this.variablesLocal.keySet().contains(variableName))) {
            throw new NotValidException("Cannot set and remove a variable with the same variable name: '" + variableName + "' within a command.");
        }
    }
    
    protected void ensureVariablesInitialized() {
        if (this.variables == null) {
            this.variables = new VariableMapImpl();
        }
    }
    
    protected void ensureVariablesLocalInitialized() {
        if (this.variablesLocal == null) {
            this.variablesLocal = new VariableMapImpl();
        }
    }
    
    protected void ensureVariableDeletionsInitialized() {
        if (this.variableDeletions == null) {
            this.variableDeletions = new ArrayList<String>();
        }
    }
    
    protected void ensureVariableDeletionsLocalInitialized() {
        if (this.variableLocalDeletions == null) {
            this.variableLocalDeletions = new ArrayList<String>();
        }
    }
    
    @Override
    public void execute() {
        final CaseExecutionVariableCmd command = new CaseExecutionVariableCmd(this);
        this.executeCommand(command);
    }
    
    @Override
    public void manualStart() {
        final ManualStartCaseExecutionCmd command = new ManualStartCaseExecutionCmd(this);
        this.executeCommand(command);
    }
    
    @Override
    public void disable() {
        final DisableCaseExecutionCmd command = new DisableCaseExecutionCmd(this);
        this.executeCommand(command);
    }
    
    @Override
    public void reenable() {
        final ReenableCaseExecutionCmd command = new ReenableCaseExecutionCmd(this);
        this.executeCommand(command);
    }
    
    @Override
    public void complete() {
        final CompleteCaseExecutionCmd command = new CompleteCaseExecutionCmd(this);
        this.executeCommand(command);
    }
    
    @Override
    public void close() {
        final CloseCaseInstanceCmd command = new CloseCaseInstanceCmd(this);
        this.executeCommand(command);
    }
    
    @Override
    public void terminate() {
        final TerminateCaseExecutionCmd command = new TerminateCaseExecutionCmd(this);
        this.executeCommand(command);
    }
    
    protected void executeCommand(final Command<?> command) {
        try {
            if (this.commandExecutor != null) {
                this.commandExecutor.execute(command);
            }
            else {
                command.execute(this.commandContext);
            }
        }
        catch (NullValueException e) {
            throw new NotValidException(e.getMessage(), e);
        }
        catch (CaseExecutionNotFoundException e2) {
            throw new NotFoundException(e2.getMessage(), e2);
        }
        catch (CaseDefinitionNotFoundException e3) {
            throw new NotFoundException(e3.getMessage(), e3);
        }
        catch (CaseIllegalStateTransitionException e4) {
            throw new NotAllowedException(e4.getMessage(), e4);
        }
    }
    
    public String getCaseExecutionId() {
        return this.caseExecutionId;
    }
    
    public VariableMap getVariables() {
        return (VariableMap)this.variables;
    }
    
    public VariableMap getVariablesLocal() {
        return (VariableMap)this.variablesLocal;
    }
    
    public Collection<String> getVariableDeletions() {
        return this.variableDeletions;
    }
    
    public Collection<String> getVariableLocalDeletions() {
        return this.variableLocalDeletions;
    }
}
