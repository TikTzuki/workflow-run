// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.el;

import org.camunda.bpm.engine.variable.Variables;
import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;
import java.util.Collection;
import java.util.Map;
import java.util.Collections;
import java.util.Set;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.delegate.VariableScope;

public class StartProcessVariableScope implements VariableScope
{
    private static final StartProcessVariableScope INSTANCE;
    private static VariableMap EMPTY_VARIABLE_MAP;
    
    public static StartProcessVariableScope getSharedInstance() {
        return StartProcessVariableScope.INSTANCE;
    }
    
    @Override
    public String getVariableScopeKey() {
        return "scope";
    }
    
    public VariableMap getVariables() {
        return StartProcessVariableScope.EMPTY_VARIABLE_MAP;
    }
    
    public VariableMap getVariablesLocal() {
        return StartProcessVariableScope.EMPTY_VARIABLE_MAP;
    }
    
    @Override
    public Object getVariable(final String variableName) {
        return null;
    }
    
    @Override
    public Object getVariableLocal(final String variableName) {
        return null;
    }
    
    @Override
    public VariableMap getVariablesTyped(final boolean deserializeObjectValues) {
        return this.getVariables();
    }
    
    @Override
    public VariableMap getVariablesLocalTyped() {
        return this.getVariablesLocalTyped(true);
    }
    
    @Override
    public VariableMap getVariablesTyped() {
        return this.getVariablesTyped(true);
    }
    
    @Override
    public VariableMap getVariablesLocalTyped(final boolean deserializeObjectValues) {
        return this.getVariablesLocal();
    }
    
    public Object getVariable(final String variableName, final boolean deserializeObjectValue) {
        return null;
    }
    
    public Object getVariableLocal(final String variableName, final boolean deserializeObjectValue) {
        return null;
    }
    
    @Override
    public <T extends TypedValue> T getVariableTyped(final String variableName) {
        return null;
    }
    
    @Override
    public <T extends TypedValue> T getVariableTyped(final String variableName, final boolean deserializeObjectValue) {
        return null;
    }
    
    @Override
    public <T extends TypedValue> T getVariableLocalTyped(final String variableName) {
        return null;
    }
    
    @Override
    public <T extends TypedValue> T getVariableLocalTyped(final String variableName, final boolean deserializeObjectValue) {
        return null;
    }
    
    @Override
    public Set<String> getVariableNames() {
        return (Set<String>)Collections.EMPTY_SET;
    }
    
    @Override
    public Set<String> getVariableNamesLocal() {
        return null;
    }
    
    @Override
    public void setVariable(final String variableName, final Object value) {
        throw new UnsupportedOperationException("No execution active, no variables can be set");
    }
    
    @Override
    public void setVariableLocal(final String variableName, final Object value) {
        throw new UnsupportedOperationException("No execution active, no variables can be set");
    }
    
    @Override
    public void setVariables(final Map<String, ?> variables) {
        throw new UnsupportedOperationException("No execution active, no variables can be set");
    }
    
    @Override
    public void setVariablesLocal(final Map<String, ?> variables) {
        throw new UnsupportedOperationException("No execution active, no variables can be set");
    }
    
    @Override
    public boolean hasVariables() {
        return false;
    }
    
    @Override
    public boolean hasVariablesLocal() {
        return false;
    }
    
    @Override
    public boolean hasVariable(final String variableName) {
        return false;
    }
    
    @Override
    public boolean hasVariableLocal(final String variableName) {
        return false;
    }
    
    @Override
    public void removeVariable(final String variableName) {
        throw new UnsupportedOperationException("No execution active, no variables can be removed");
    }
    
    @Override
    public void removeVariableLocal(final String variableName) {
        throw new UnsupportedOperationException("No execution active, no variables can be removed");
    }
    
    @Override
    public void removeVariables() {
        throw new UnsupportedOperationException("No execution active, no variables can be removed");
    }
    
    @Override
    public void removeVariablesLocal() {
        throw new UnsupportedOperationException("No execution active, no variables can be removed");
    }
    
    @Override
    public void removeVariables(final Collection<String> variableNames) {
        throw new UnsupportedOperationException("No execution active, no variables can be removed");
    }
    
    @Override
    public void removeVariablesLocal(final Collection<String> variableNames) {
        throw new UnsupportedOperationException("No execution active, no variables can be removed");
    }
    
    public Map<String, CoreVariableInstance> getVariableInstances() {
        return Collections.emptyMap();
    }
    
    public CoreVariableInstance getVariableInstance(final String name) {
        return null;
    }
    
    public Map<String, CoreVariableInstance> getVariableInstancesLocal() {
        return Collections.emptyMap();
    }
    
    public CoreVariableInstance getVariableInstanceLocal(final String name) {
        return null;
    }
    
    static {
        INSTANCE = new StartProcessVariableScope();
        StartProcessVariableScope.EMPTY_VARIABLE_MAP = Variables.fromMap((Map)Collections.emptyMap());
    }
}
