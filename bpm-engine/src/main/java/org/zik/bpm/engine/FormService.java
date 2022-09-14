// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import java.io.InputStream;
import java.util.Collection;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.form.TaskFormData;
import org.zik.bpm.engine.runtime.ProcessInstance;
import java.util.Map;
import org.zik.bpm.engine.form.StartFormData;

public interface FormService
{
    StartFormData getStartFormData(final String p0);
    
    Object getRenderedStartForm(final String p0);
    
    Object getRenderedStartForm(final String p0, final String p1);
    
    @Deprecated
    ProcessInstance submitStartFormData(final String p0, final Map<String, String> p1);
    
    ProcessInstance submitStartForm(final String p0, final Map<String, Object> p1);
    
    @Deprecated
    ProcessInstance submitStartFormData(final String p0, final String p1, final Map<String, String> p2);
    
    ProcessInstance submitStartForm(final String p0, final String p1, final Map<String, Object> p2);
    
    TaskFormData getTaskFormData(final String p0);
    
    Object getRenderedTaskForm(final String p0);
    
    Object getRenderedTaskForm(final String p0, final String p1);
    
    @Deprecated
    void submitTaskFormData(final String p0, final Map<String, String> p1);
    
    void submitTaskForm(final String p0, final Map<String, Object> p1);
    
    VariableMap submitTaskFormWithVariablesInReturn(final String p0, final Map<String, Object> p1, final boolean p2);
    
    VariableMap getStartFormVariables(final String p0);
    
    VariableMap getStartFormVariables(final String p0, final Collection<String> p1, final boolean p2);
    
    VariableMap getTaskFormVariables(final String p0);
    
    VariableMap getTaskFormVariables(final String p0, final Collection<String> p1, final boolean p2);
    
    String getStartFormKey(final String p0);
    
    String getTaskFormKey(final String p0, final String p1);
    
    InputStream getDeployedStartForm(final String p0);
    
    InputStream getDeployedTaskForm(final String p0);
}
