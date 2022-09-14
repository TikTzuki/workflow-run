// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.identity;

import java.util.List;

public interface PasswordPolicyResult
{
    boolean isValid();
    
    List<PasswordPolicyRule> getViolatedRules();
    
    List<PasswordPolicyRule> getFulfilledRules();
}
