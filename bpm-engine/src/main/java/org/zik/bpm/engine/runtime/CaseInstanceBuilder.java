// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import java.util.Map;

public interface CaseInstanceBuilder
{
    CaseInstanceBuilder businessKey(final String p0);
    
    CaseInstanceBuilder caseDefinitionTenantId(final String p0);
    
    CaseInstanceBuilder caseDefinitionWithoutTenantId();
    
    CaseInstanceBuilder setVariable(final String p0, final Object p1);
    
    CaseInstanceBuilder setVariables(final Map<String, Object> p0);
    
    CaseInstance create();
}
