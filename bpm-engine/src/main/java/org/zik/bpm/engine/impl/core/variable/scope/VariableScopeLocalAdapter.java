// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.variable.scope;

import java.util.Collection;
import java.util.Set;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.engine.variable.VariableMap;
import java.util.Map;
import org.zik.bpm.engine.delegate.VariableScope;

public class VariableScopeLocalAdapter implements VariableScope
{
    protected VariableScope wrappedScope;
    
    public VariableScopeLocalAdapter(final VariableScope wrappedScope) {
        this.wrappedScope = wrappedScope;
    }
    
    @Override
    public String getVariableScopeKey() {
        return this.wrappedScope.getVariableScopeKey();
    }
    
    @Override
    public Map<String, Object> getVariables() {
        return this.getVariablesLocal();
    }
    
    @Override
    public VariableMap getVariablesTyped() {
        return this.getVariablesLocalTyped();
    }
    
    @Override
    public VariableMap getVariablesTyped(final boolean deserializeValues) {
        return this.getVariablesLocalTyped(deserializeValues);
    }
    
    @Override
    public Map<String, Object> getVariablesLocal() {
        return this.wrappedScope.getVariablesLocal();
    }
    
    @Override
    public VariableMap getVariablesLocalTyped() {
        return this.wrappedScope.getVariablesLocalTyped();
    }
    
    @Override
    public VariableMap getVariablesLocalTyped(final boolean deserializeValues) {
        return this.wrappedScope.getVariablesLocalTyped(deserializeValues);
    }
    
    @Override
    public Object getVariable(final String variableName) {
        return this.getVariableLocal(variableName);
    }
    
    @Override
    public Object getVariableLocal(final String variableName) {
        return this.wrappedScope.getVariableLocal(variableName);
    }
    
    @Override
    public <T extends TypedValue> T getVariableTyped(final String variableName) {
        return (T)this.getVariableLocalTyped(variableName);
    }
    
    @Override
    public <T extends TypedValue> T getVariableTyped(final String variableName, final boolean deserializeValue) {
        return (T)this.getVariableLocalTyped(variableName, deserializeValue);
    }
    
    @Override
    public <T extends TypedValue> T getVariableLocalTyped(final String variableName) {
        return this.wrappedScope.getVariableLocalTyped(variableName);
    }
    
    @Override
    public <T extends TypedValue> T getVariableLocalTyped(final String variableName, final boolean deserializeValue) {
        return this.wrappedScope.getVariableLocalTyped(variableName, deserializeValue);
    }
    
    @Override
    public Set<String> getVariableNames() {
        return this.getVariableNamesLocal();
    }
    
    @Override
    public Set<String> getVariableNamesLocal() {
        return this.wrappedScope.getVariableNamesLocal();
    }
    
    @Override
    public void setVariable(final String variableName, final Object value) {
        this.setVariableLocal(variableName, value);
    }
    
    @Override
    public void setVariableLocal(final String variableName, final Object value) {
        this.wrappedScope.setVariableLocal(variableName, value);
    }
    
    @Override
    public void setVariables(final Map<String, ?> variables) {
        this.setVariablesLocal(variables);
    }
    
    @Override
    public void setVariablesLocal(final Map<String, ?> variables) {
        this.wrappedScope.setVariablesLocal(variables);
    }
    
    @Override
    public boolean hasVariables() {
        return this.hasVariablesLocal();
    }
    
    @Override
    public boolean hasVariablesLocal() {
        return this.wrappedScope.hasVariablesLocal();
    }
    
    @Override
    public boolean hasVariable(final String variableName) {
        return this.hasVariableLocal(variableName);
    }
    
    @Override
    public boolean hasVariableLocal(final String variableName) {
        return this.wrappedScope.hasVariableLocal(variableName);
    }
    
    @Override
    public void removeVariable(final String variableName) {
        this.removeVariableLocal(variableName);
    }
    
    @Override
    public void removeVariableLocal(final String variableName) {
        this.wrappedScope.removeVariableLocal(variableName);
    }
    
    @Override
    public void removeVariables(final Collection<String> variableNames) {
        this.removeVariablesLocal(variableNames);
    }
    
    @Override
    public void removeVariablesLocal(final Collection<String> variableNames) {
        this.wrappedScope.removeVariablesLocal(variableNames);
    }
    
    @Override
    public void removeVariables() {
        this.removeVariablesLocal();
    }
    
    @Override
    public void removeVariablesLocal() {
        this.wrappedScope.removeVariablesLocal();
    }
}
