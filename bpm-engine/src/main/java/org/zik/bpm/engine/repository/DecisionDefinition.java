// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

public interface DecisionDefinition extends ResourceDefinition
{
    String getDecisionRequirementsDefinitionId();
    
    String getDecisionRequirementsDefinitionKey();
    
    String getVersionTag();
}
