// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

import java.util.List;

public interface CalledProcessDefinition extends ProcessDefinition
{
    String getCallingProcessDefinitionId();
    
    List<String> getCalledFromActivityIds();
}
