// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

import java.util.Map;

public interface CandidateDeployment
{
    String getName();
    
    Map<String, Resource> getResources();
}
