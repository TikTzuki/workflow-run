// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import java.util.Collection;
import java.util.Map;

public interface CaseExecutionCommandBuilder
{
    CaseExecutionCommandBuilder setVariable(final String p0, final Object p1);
    
    CaseExecutionCommandBuilder setVariables(final Map<String, Object> p0);
    
    CaseExecutionCommandBuilder setVariableLocal(final String p0, final Object p1);
    
    CaseExecutionCommandBuilder setVariablesLocal(final Map<String, Object> p0);
    
    CaseExecutionCommandBuilder removeVariable(final String p0);
    
    CaseExecutionCommandBuilder removeVariables(final Collection<String> p0);
    
    CaseExecutionCommandBuilder removeVariableLocal(final String p0);
    
    CaseExecutionCommandBuilder removeVariablesLocal(final Collection<String> p0);
    
    void execute();
    
    void manualStart();
    
    void disable();
    
    void reenable();
    
    void complete();
    
    void terminate();
    
    void close();
}
