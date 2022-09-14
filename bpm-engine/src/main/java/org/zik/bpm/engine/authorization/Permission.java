// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.authorization;

public interface Permission
{
    String getName();
    
    int getValue();
    
    Resource[] getTypes();
}
