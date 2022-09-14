// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.identity;

import java.util.Map;

public interface Account
{
    public static final String NAME_ALFRESCO = "Alfresco";
    public static final String NAME_GOOGLE = "Google";
    public static final String NAME_SKYPE = "Skype";
    public static final String NAME_MAIL = "Mail";
    
    String getName();
    
    String getUsername();
    
    String getPassword();
    
    Map<String, String> getDetails();
}
