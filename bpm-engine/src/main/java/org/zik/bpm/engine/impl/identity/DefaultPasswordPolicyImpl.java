// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.identity;

import java.util.ArrayList;
import org.zik.bpm.engine.identity.PasswordPolicyRule;
import java.util.List;
import org.zik.bpm.engine.identity.PasswordPolicy;

public class DefaultPasswordPolicyImpl implements PasswordPolicy
{
    protected static final String PLACEHOLDER_PREFIX = "PASSWORD_POLICY_";
    public static final int MIN_LENGTH = 10;
    public static final int MIN_LOWERCASE = 1;
    public static final int MIN_UPPERCASE = 1;
    public static final int MIN_DIGIT = 1;
    public static final int MIN_SPECIAL = 1;
    protected final List<PasswordPolicyRule> rules;
    
    public DefaultPasswordPolicyImpl() {
        (this.rules = new ArrayList<PasswordPolicyRule>()).add(new PasswordPolicyUserDataRuleImpl());
        this.rules.add(new PasswordPolicyLengthRuleImpl(10));
        this.rules.add(new PasswordPolicyLowerCaseRuleImpl(1));
        this.rules.add(new PasswordPolicyUpperCaseRuleImpl(1));
        this.rules.add(new PasswordPolicyDigitRuleImpl(1));
        this.rules.add(new PasswordPolicySpecialCharacterRuleImpl(1));
    }
    
    @Override
    public List<PasswordPolicyRule> getRules() {
        return this.rules;
    }
}
