// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.identity;

import java.util.HashMap;
import java.util.Map;
import org.zik.bpm.engine.identity.PasswordPolicyRule;

public class PasswordPolicySpecialCharacterRuleImpl implements PasswordPolicyRule
{
    public static final String SPECIALCHARACTERS = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
    public static final String PLACEHOLDER = "PASSWORD_POLICY_SPECIAL";
    protected int minSpecial;
    
    public PasswordPolicySpecialCharacterRuleImpl(final int minSpecial) {
        this.minSpecial = minSpecial;
    }
    
    @Override
    public String getPlaceholder() {
        return "PASSWORD_POLICY_SPECIAL";
    }
    
    @Override
    public Map<String, String> getParameters() {
        final Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("minSpecial", "" + this.minSpecial);
        return parameter;
    }
    
    @Override
    public boolean execute(final String password) {
        int specialCount = 0;
        for (final Character c : password.toCharArray()) {
            if (" !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~".indexOf(c) != -1) {
                ++specialCount;
            }
            if (specialCount >= this.minSpecial) {
                return true;
            }
        }
        return false;
    }
}
