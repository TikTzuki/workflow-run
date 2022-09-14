// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.identity;

import java.util.HashMap;
import java.util.Map;
import org.zik.bpm.engine.identity.PasswordPolicyRule;

public class PasswordPolicyLengthRuleImpl implements PasswordPolicyRule
{
    public static final String PLACEHOLDER = "PASSWORD_POLICY_LENGTH";
    protected int minLength;
    
    public PasswordPolicyLengthRuleImpl(final int minLength) {
        this.minLength = minLength;
    }
    
    @Override
    public String getPlaceholder() {
        return "PASSWORD_POLICY_LENGTH";
    }
    
    @Override
    public Map<String, String> getParameters() {
        final Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("minLength", "" + this.minLength);
        return parameter;
    }
    
    @Override
    public boolean execute(final String password) {
        return password.length() >= this.minLength;
    }
}
