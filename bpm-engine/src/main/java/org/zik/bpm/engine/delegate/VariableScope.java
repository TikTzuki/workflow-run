// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.delegate;

import java.util.Collection;
import java.util.Set;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.engine.variable.VariableMap;
import java.util.Map;

public interface VariableScope
{
    String getVariableScopeKey();
    
    Map<String, Object> getVariables();
    
    VariableMap getVariablesTyped();
    
    VariableMap getVariablesTyped(final boolean p0);
    
    Map<String, Object> getVariablesLocal();
    
    VariableMap getVariablesLocalTyped();
    
    VariableMap getVariablesLocalTyped(final boolean p0);
    
    Object getVariable(final String p0);
    
    Object getVariableLocal(final String p0);
    
     <T extends TypedValue> T getVariableTyped(final String p0);
    
     <T extends TypedValue> T getVariableTyped(final String p0, final boolean p1);
    
     <T extends TypedValue> T getVariableLocalTyped(final String p0);
    
     <T extends TypedValue> T getVariableLocalTyped(final String p0, final boolean p1);
    
    Set<String> getVariableNames();
    
    Set<String> getVariableNamesLocal();
    
    void setVariable(final String p0, final Object p1);
    
    void setVariableLocal(final String p0, final Object p1);
    
    void setVariables(final Map<String, ?> p0);
    
    void setVariablesLocal(final Map<String, ?> p0);
    
    boolean hasVariables();
    
    boolean hasVariablesLocal();
    
    boolean hasVariable(final String p0);
    
    boolean hasVariableLocal(final String p0);
    
    void removeVariable(final String p0);
    
    void removeVariableLocal(final String p0);
    
    void removeVariables(final Collection<String> p0);
    
    void removeVariablesLocal(final Collection<String> p0);
    
    void removeVariables();
    
    void removeVariablesLocal();
}
