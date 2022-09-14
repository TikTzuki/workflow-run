// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.identity;

import java.util.HashMap;
import java.util.Map;
import org.zik.bpm.engine.identity.PasswordPolicyRule;

public class PasswordPolicyLowerCaseRuleImpl implements PasswordPolicyRule
{
    public static final String PLACEHOLDER = "PASSWORD_POLICY_LOWERCASE";
    protected int minLowerCase;
    
    public PasswordPolicyLowerCaseRuleImpl(final int minLowerCase) {
        this.minLowerCase = minLowerCase;
    }
    
    @Override
    public String getPlaceholder() {
        return "PASSWORD_POLICY_LOWERCASE";
    }
    
    @Override
    public Map<String, String> getParameters() {
        final Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("minLowerCase", "" + this.minLowerCase);
        return parameter;
    }
    
    @Override
    public boolean execute(final String password) {
        int lowerCaseCount = 0;
        for (final Character c : password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                ++lowerCaseCount;
            }
            if (lowerCaseCount >= this.minLowerCase) {
                return true;
            }
        }
        return false;
    }
}
