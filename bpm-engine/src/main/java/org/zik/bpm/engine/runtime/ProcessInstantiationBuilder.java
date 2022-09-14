// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

public interface ProcessInstantiationBuilder extends ActivityInstantiationBuilder<ProcessInstantiationBuilder>, InstantiationBuilder<ProcessInstantiationBuilder>
{
    ProcessInstantiationBuilder processDefinitionTenantId(final String p0);
    
    ProcessInstantiationBuilder processDefinitionWithoutTenantId();
    
    ProcessInstantiationBuilder businessKey(final String p0);
    
    ProcessInstantiationBuilder caseInstanceId(final String p0);
    
    ProcessInstance execute();
    
    ProcessInstance execute(final boolean p0, final boolean p1);
    
    ProcessInstanceWithVariables executeWithVariablesInReturn();
    
    ProcessInstanceWithVariables executeWithVariablesInReturn(final boolean p0, final boolean p1);
}
