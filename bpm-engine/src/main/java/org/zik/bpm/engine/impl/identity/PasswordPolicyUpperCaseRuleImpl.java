// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.identity;

import java.util.HashMap;
import java.util.Map;
import org.zik.bpm.engine.identity.PasswordPolicyRule;

public class PasswordPolicyUpperCaseRuleImpl implements PasswordPolicyRule
{
    public static final String PLACEHOLDER = "PASSWORD_POLICY_UPPERCASE";
    protected int minUpperCase;
    
    public PasswordPolicyUpperCaseRuleImpl(final int minUpperCase) {
        this.minUpperCase = minUpperCase;
    }
    
    @Override
    public String getPlaceholder() {
        return "PASSWORD_POLICY_UPPERCASE";
    }
    
    @Override
    public Map<String, String> getParameters() {
        final Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("minUpperCase", "" + this.minUpperCase);
        return parameter;
    }
    
    @Override
    public boolean execute(final String password) {
        int upperCaseCount = 0;
        for (final Character c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                ++upperCaseCount;
            }
            if (upperCaseCount >= this.minUpperCase) {
                return true;
            }
        }
        return false;
    }
}
