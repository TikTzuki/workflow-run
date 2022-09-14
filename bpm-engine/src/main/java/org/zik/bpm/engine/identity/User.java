// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.identity;

import java.io.Serializable;

public interface User extends Serializable
{
    String getId();
    
    void setId(final String p0);
    
    String getFirstName();
    
    void setFirstName(final String p0);
    
    void setLastName(final String p0);
    
    String getLastName();
    
    void setEmail(final String p0);
    
    String getEmail();
    
    String getPassword();
    
    void setPassword(final String p0);
}
