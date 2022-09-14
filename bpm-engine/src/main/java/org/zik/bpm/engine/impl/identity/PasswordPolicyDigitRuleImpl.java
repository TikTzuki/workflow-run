// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.identity;

import java.util.HashMap;
import java.util.Map;
import org.zik.bpm.engine.identity.PasswordPolicyRule;

public class PasswordPolicyDigitRuleImpl implements PasswordPolicyRule
{
    public static final String PLACEHOLDER = "PASSWORD_POLICY_DIGIT";
    protected int minDigit;
    
    public PasswordPolicyDigitRuleImpl(final int minDigit) {
        this.minDigit = minDigit;
    }
    
    @Override
    public String getPlaceholder() {
        return "PASSWORD_POLICY_DIGIT";
    }
    
    @Override
    public Map<String, String> getParameters() {
        final Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("minDigit", "" + this.minDigit);
        return parameter;
    }
    
    @Override
    public boolean execute(final String password) {
        int digitCount = 0;
        for (final Character c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                ++digitCount;
            }
            if (digitCount >= this.minDigit) {
                return true;
            }
        }
        return false;
    }
}
