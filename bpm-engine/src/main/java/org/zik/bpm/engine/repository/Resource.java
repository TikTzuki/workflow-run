// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

public interface Resource
{
    String getId();
    
    String getName();
    
    String getDeploymentId();
    
    byte[] getBytes();
}
