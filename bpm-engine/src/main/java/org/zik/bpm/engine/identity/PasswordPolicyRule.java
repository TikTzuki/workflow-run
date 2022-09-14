// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.identity;

import java.util.Map;

public interface PasswordPolicyRule
{
    String getPlaceholder();
    
    Map<String, String> getParameters();
    
    boolean execute(final String p0);
    
    default boolean execute(final String candidatePassword, final User user) {
        return this.execute(candidatePassword);
    }
}
