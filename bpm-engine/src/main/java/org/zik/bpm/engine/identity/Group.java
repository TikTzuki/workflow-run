// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.identity;

import java.io.Serializable;

public interface Group extends Serializable
{
    String getId();
    
    void setId(final String p0);
    
    String getName();
    
    void setName(final String p0);
    
    String getType();
    
    void setType(final String p0);
}
